package com.deb.pi.boardgame.core.device.board;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.impl.output.SPIOutputBus ;
import com.deb.pi.boardgame.core.gpio.GPIOManager ;
import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;
import com.pi4j.io.spi.SpiChannel ;

class StateReaderDaemon extends Thread {
    
    private static final Logger log = Logger.getLogger( StateReaderDaemon.class ) ;
    
    private static final int TIME_BETWEEN_REFRESH     = 100 ;
    private static final int SPI_BUS_SIZE             = 17 ;
    private static final int COL_PROBE_START_WIRE_NUM = 9 ;
    
    private static final boolean DBG_ENABLED = false ;

    private GameBoardHardware hardware    = null ;
    private boolean           keepRunning = true ;
    
    private GPIOManager gpioManager = null ;
    private OutputBus   spiBus      = null ;
    private OutputBus   rowProbeBus = null ;
    private OutputBus   colProbeBus = null ;
    private InPin       inPin       = null ;
    
    private int numCols     = 0 ;
    private int numRows     = 0 ;
    private int numSwitches = 0 ;
    
    public StateReaderDaemon( GameBoardHardware hardware,
                              SpiChannel spiChannel, int inPinNumber ) 
        throws Exception {
        
        super.setDaemon( true ) ;
        this.hardware = hardware ;
        
        this.numCols     = hardware.getNumCols() ;
        this.numRows     = hardware.getNumRows() ;
        this.numSwitches = hardware.getNumSwitches() ;

        spiBus      = new SPIOutputBus( spiChannel, SPI_BUS_SIZE ) ;
        rowProbeBus = ( OutputBus )spiBus.getSubBus( 0, numRows + 1 ) ;
        colProbeBus = ( OutputBus )spiBus.getSubBus( COL_PROBE_START_WIRE_NUM, 
                                                     numCols ) ;
        
        gpioManager = ObjectFactory.instance().getGPIOManager() ;
        inPin       = gpioManager.getInputPin( inPinNumber ) ;
    }
    
    public void die() throws Exception {
        this.keepRunning = false ;
        while( super.isAlive() ) {
            try {
                log.debug( "Waiting for reader daemon to die." ) ;
                Thread.sleep( 100 ) ;
            }
            catch( InterruptedException e ) {}
        }
        spiBus.clear() ;
    }
    
    public void run() {
        try {
            log.debug( "Starting game board state reader daemon." ) ;
            InstantaneousBoardState tempState = new InstantaneousBoardState() ;
            while( keepRunning ) {
                refreshBoardState( tempState ) ;
                handleAnyChangeInState( tempState ) ;
                try {
                    Thread.sleep( TIME_BETWEEN_REFRESH ) ;
                }
                catch( InterruptedException e ) {
                    // Gobble
                }
            }
        }
        catch( Exception e ) {
            log.error( "Error while refreshing board state.", e ) ;
        }
    }
    
    private void refreshBoardState( InstantaneousBoardState state ) throws Exception {
        
        log( "Reading board state..." ) ;
        
        log( "Cleared the SPI bus" ) ;
        spiBus.clear() ;
        
        state.clear() ;
        
        log( "Reading cell states" ) ;
        readCellStates( state ) ;
        
        log( "Reading switch states" ) ;
        readSwitchStates( state ) ;
    }

    private void readCellStates( InstantaneousBoardState state ) throws Exception {
        
        for( int r=0; r<numRows; r++ ) {
            log( "Reading cell states for row " + r ) ;
            
            if( r>0 ) {
                log( "\tSetting row " + (r-1) + " probe to low." );
                rowProbeBus.write( (r-1), false ) ;
            }
            log( "\tSetting row " + r + " probe to high." );
            rowProbeBus.write( r, true ) ;
            
            log( "\tSetting all col probes to high." );
            colProbeBus.setHigh() ;
            
            if( inPin.isHigh() ) {
                log( "\t\tIn Pin is high" ) ;
                log( "\t\tSome cells in row " + r + " are occupied." ) ;
                log( "\t\tTrying to determing occupied cells" ) ;
                
                colProbeBus.clear() ;
                log( "\t\t\tCol bus cleared" ) ;
                
                for( int c=0; c<numCols; c++ ) {
                    if( c>0 ) {
                        log( "\t\t\tSetting col " + (c-1) + " probe to low." );
                        colProbeBus.write( c-1, false ) ;
                    }
                    log( "\t\t\tSetting col " + c + " probe to high." );
                    colProbeBus.write( c, true ) ;
                    
                    if( inPin.isHigh() ) {
                        log( "\t\t\t\tIn Pin is high. [" + r + "][" + c + "] occupied" ) ;
                        state.setCellState( r, c, true ) ;
                    }
                    else {
                        log( "\t\t\t\tIn Pin is low. [" + r + "][" + c + "] empty" ) ;
                    }
                }
            }
            else {
                log( "\t\tIn Pin is low" ) ;
                log( "\t\tNo cells in row " + r + " are occupied." ) ;
            }
            
            log( "\tSetting col probe to low" ) ;
            colProbeBus.clear() ;
        }
    }

    private void readSwitchStates( InstantaneousBoardState state ) throws Exception {
        
        log( "Setting row probe " + (numRows-1) + " to low." ) ;
        rowProbeBus.write( numRows-1, false ) ;
        
        log( "Setting row probe " + numRows + " to high." ) ;
        rowProbeBus.write( numRows, true ) ;
        
        log( "Setting col probe to high." ) ;
        colProbeBus.setHigh() ;
        
        if( inPin.isHigh() ) {
            log( "\tIn Pin is high" ) ;
            log( "\tSome switches are on." ) ;
            log( "\tTrying to determine on switches" ) ;
            
            colProbeBus.clear() ;
            log( "\tCol bus cleared." ) ;
            
            for( int c=0; c<numCols; c++ ) {
                if( c>0 ) {
                    log( "\t\tSetting col " + (c-1) + " probe to low." ) ;
                    colProbeBus.write( c-1, false ) ;
                }
                
                log( "\t\tSetting col " + c + " probe to high." ) ;
                colProbeBus.write( c, true ) ;
                
                if( inPin.isHigh() ) {
                    log( "\t\tIn Pin is high. Switch [" + c + "] is on" ) ;
                    state.setSwitchState( c, true ) ;
                }
                else {
                    log( "\t\t\tIn Pin is low. Switch [" + c + "] is off" ) ;
                }
            }
        }
    }
    
    private void log( String message ) throws Exception {
        if( DBG_ENABLED ) {
            log.debug( message ) ;
        }
    }
    
    private void handleAnyChangeInState( InstantaneousBoardState state ) {
        
        boolean stateChanged = false ;
        
        for( int r=0; r<numRows; r++ ) {
            for( int c=0; c<numCols; c++ ) {
                if( state.getCellState( r, c ) != 
                    hardware.getCellState( r, c ) ) {
                    
                    hardware.setCellState( r, c, state.getCellState( r, c ) ) ;
                    stateChanged = true ;
                }
            }
        }
        
        for( int s=0; s<numSwitches; s++ ) {
            if( state.getSwitchState( s ) != hardware.getSwitchState( s ) ) {
                hardware.setSwitchState( s, state.getSwitchState( s ) ) ;
                stateChanged = true ;
            }
        }
        
        if( stateChanged ) {
            log.debug( state ) ;
        }
    }

    // ========================================================================
    
    public class InstantaneousBoardState {

        private boolean gridState[][] = null ;
        private boolean switchState[] = null ;
        
        public InstantaneousBoardState() {
            this.gridState = new boolean[numRows][numCols] ;
            this.switchState = new boolean[numRows] ;
        }
        
        public void clear() {
            for( int r=0; r<numRows; r++ ) {
                switchState[r] = false ;
                for( int c=0; c<numCols; c++ ) {
                    gridState[r][c] = false ;
                }
            }
        }
        
        public void setCellState( int row, int col, boolean state ) {
            gridState[row][col] = state ;
        }
        
        public void setSwitchState( int switchNum, boolean state ) {
            switchState[switchNum] = state ;
        }
        
        public boolean getCellState( int row, int col ) {
            return gridState[row][col] ;
        }
        
        public boolean getSwitchState( int switchNum ) {
            return switchState[switchNum] ;
        }
        
        public String toString() {
            
            StringBuilder buffer = new StringBuilder( "------------\n" ) ;
            
            for( int r=0; r<numRows; r++ ) {
                for( int c=0; c<numCols; c++ ) {
                    if( gridState[r][c] ) {
                        buffer.append( " X " ) ;
                    }
                    else {
                        buffer.append( " . " ) ;
                    }
                }
                buffer.append( "\n" ) ;
            }
            
            buffer.append( "\n" ) ;
            for( int i=0; i<numSwitches; i++ ) {
                buffer.append( switchState[i] ? " + " : " . " ) ;
            }
            buffer.append( "\n" ) ;
            
            return buffer.toString() ;
        }
    }
}
