package com.deb.pi.boardgame.samples.ipdriver;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.impl.output.SPIOutputBus ;
import com.deb.pi.boardgame.core.gpio.GPIOManager ;
import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;
import com.deb.pi.boardgame.samples.PiReadinessChecker ;
import com.pi4j.io.spi.SpiChannel ;

public class BoardReader {
    
    private static final Logger log = Logger.getLogger( BoardReader.class ) ;
    
    private static boolean DBG_ENABLE = false ;
    
    private static final int NUM_ROWS_GAMEBOARD = 3 ;
    private static final int NUM_COLS_GAMEBOARD = 3 ;
    private static final int NUM_SW_GAMEBOARD   = 3 ;
    
    private static final int SPI_BUS_SIZE   = 17 ;
    private static final int INPUT_PIN_NUM  = 0 ;
    private static final int OUTPUT_PIN_NUM = 1 ;
    
    private GPIOManager gpioManager = null ;
    private OutputBus   spiBus      = null ;
    private OutputBus   rowProbeBus = null ;
    private OutputBus   colProbeBus = null ;
    private InPin       inPin       = null ;
    private OutPin      vibPin      = null ;
    
    private static PiReadinessChecker CHECKER = new PiReadinessChecker() ;
    
    private BoardState boardState = new BoardState( NUM_ROWS_GAMEBOARD, 
                                                    NUM_COLS_GAMEBOARD,
                                                    NUM_SW_GAMEBOARD ) ;
    
    public BoardReader() throws Exception {
        
        spiBus      = new SPIOutputBus( SpiChannel.CS1, SPI_BUS_SIZE ) ;
        rowProbeBus = ( OutputBus )spiBus.getSubBus( 0, NUM_ROWS_GAMEBOARD + 1 ) ;
        colProbeBus = ( OutputBus )spiBus.getSubBus( 9, NUM_COLS_GAMEBOARD ) ;
        
        gpioManager = ObjectFactory.instance().getGPIOManager() ;
        inPin       = gpioManager.getInputPin( INPUT_PIN_NUM ) ;
        vibPin      = gpioManager.getOutputPin( OUTPUT_PIN_NUM ) ; 
    }
    
    public void readBoardState() throws Exception {
        
        log.debug( "Starting the read cycles.." ) ;
        
        BoardState tempState  = ( BoardState )boardState.clone() ;
        long startTime        = System.currentTimeMillis() ;
        long duration         = 0 ;
        int  numSecToRun      = 60*5 ;
        long lastDurationMark = 0 ;
        
        while( duration < numSecToRun*1000 ) {
            refreshBoardState( tempState ) ;
            if( !boardState.equals( tempState ) ) {
                boardState = ( BoardState )tempState.clone() ;
                log.debug( boardState ) ;
                vibPin.pulse( 75 ) ;
            }
            Thread.sleep( 100 ) ;
            duration = System.currentTimeMillis() - startTime ;
            
            if( duration - lastDurationMark > 60000 ) {
                if( !CHECKER.dialogInput() ) {
                    break ;
                }
                lastDurationMark = duration ;
            }
        }
    }
    
    public void singleRead() throws Exception {
        
        refreshBoardState( boardState ) ;
        System.out.println( boardState ) ;
    }
    
    private void refreshBoardState( BoardState state ) throws Exception {
        
        log( "Reading board state..." ) ;
        
        log( "Cleared the SPI bus" ) ;
        spiBus.clear() ;
        
        state.clear() ;
        
        log( "Reading cell states" ) ;
        readCellStates( state ) ;
        
        log( "Reading switch states" ) ;
        readSwitchStates( state ) ;

        spiBus.clear() ;
    }

    private void readCellStates( BoardState state ) throws Exception {
        
        for( int r=0; r<NUM_ROWS_GAMEBOARD; r++ ) {
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
                
                for( int c=0; c<NUM_COLS_GAMEBOARD; c++ ) {
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

    private void readSwitchStates( BoardState state ) throws Exception {
        
        log( "Setting row probe " + (NUM_ROWS_GAMEBOARD-1) + " to low." ) ;
        rowProbeBus.write( NUM_ROWS_GAMEBOARD-1, false ) ;
        
        log( "Setting row probe " + NUM_ROWS_GAMEBOARD + " to high." ) ;
        rowProbeBus.write( NUM_ROWS_GAMEBOARD, true ) ;
        
        log( "Setting col probe to high." ) ;
        colProbeBus.setHigh() ;
        
        if( inPin.isHigh() ) {
            log( "\tIn Pin is high" ) ;
            log( "\tSome switches are on." ) ;
            log( "\tTrying to determine on switches" ) ;
            
            colProbeBus.clear() ;
            log( "\tCol bus cleared." ) ;
            
            for( int c=0; c<NUM_COLS_GAMEBOARD; c++ ) {
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
        if( DBG_ENABLE ) {
            log.debug( message ) ;
        }
    }
    
    public void shutdown() {
        gpioManager.shutdown() ;
    }
    
    public static void main( String[] args ) throws Exception {
        
        log.debug( "Starting BoardReader." ) ;
        
        boolean ONE_SHOT = false ;
        BoardReader boardReader = new BoardReader() ;
        
        log.debug( "\n" ) ;
        
        try {
            if( ONE_SHOT ) {
                DBG_ENABLE = true ;
                CHECKER.runPreFlightCheck() ;
                boardReader.singleRead() ;
            }
            else {
                boardReader.readBoardState() ;
            }
        }
        finally {
            boardReader.shutdown() ;
        }
    
        log.debug( "Ending BoardReader." ) ;
    }
}
