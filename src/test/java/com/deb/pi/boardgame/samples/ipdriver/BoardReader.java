package com.deb.pi.boardgame.samples.ipdriver;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.impl.output.SPIOutputBus ;
import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;
import com.pi4j.io.spi.SpiChannel ;

public class BoardReader {
    
    private static final Logger log = Logger.getLogger( BoardReader.class ) ;
    
    private static final int NUM_ROWS_GAMEBOARD = 3 ;
    private static final int NUM_COLS_GAMEBOARD = 3 ;
    private static final int SPI_BUS_SIZE       = 17 ;
    
    private OutputBus spiBus      = null ;
    private OutputBus rowProbeBus = null ;
    private OutputBus colProbeBus = null ;
    private InPin     inPin       = null ;
    
    private BoardState boardState = new BoardState( NUM_ROWS_GAMEBOARD, 
                                                    NUM_COLS_GAMEBOARD,
                                                    NUM_ROWS_GAMEBOARD ) ;
    
    public BoardReader() throws Exception {
        spiBus = new SPIOutputBus( SpiChannel.CS1, SPI_BUS_SIZE ) ;
        colProbeBus = ( OutputBus )spiBus.getSubBus( 0, NUM_COLS_GAMEBOARD + 1 ) ;
        rowProbeBus = ( OutputBus )spiBus.getSubBus( 4, NUM_ROWS_GAMEBOARD ) ;
        
        inPin = ObjectFactory.instance().getGPIOManager().getInputPin( 0 ) ;
    }
    
    public void readBoardState() throws Exception {
        
        BoardState tempState = ( BoardState )boardState.clone() ;
        long startTime   = System.currentTimeMillis() ;
        long duration    = 0 ;
        int  numSecToRun = 60*5 ;
        
        while( duration < numSecToRun*1000 ) {
            refreshBoardState( tempState ) ;
            if( !boardState.equals( tempState ) ) {
                boardState = ( BoardState )tempState.clone() ;
                log.debug( boardState ) ;
            }
            Thread.sleep( 100 ) ;
            duration = System.currentTimeMillis() - startTime ;
        }
    }
    
    /**
     * This function is used to test only the 595 probe circuit without the 
     * associated 4066 board. In place of the 4066 board, we connect 17 LEDs
     * to test the row and column probe status.
     */
    public void testProbeBusOutput() throws Exception {
        
        log( "Testing probe busses..." ) ;
        
        spiBus.setHigh() ;
        log( "Set SPI bus to high." ) ;
        
        spiBus.clear() ;
        log( "Cleared the SPI bus" ) ;

        // Read the grid states
        for( int c=0; c<NUM_COLS_GAMEBOARD; c++ ) {
            log( "Reading cell states for column " + c ) ;
            
            if( c>0 ) {
                log( "\tSetting col " + (c-1) + " probe to low." );
                colProbeBus.write( (c-1), false ) ;
            }
            log( "\tSetting col " + c + " probe to high." );
            colProbeBus.write( c, true ) ;
            
            log( "\tSetting all row probes to high." );
            rowProbeBus.setHigh() ;
            
            log( "\tClearing row bus" ) ;
            rowProbeBus.clear() ;
            
            for( int r=0; r<NUM_ROWS_GAMEBOARD; r++ ) {
                if( r>0 ) {
                    log( "Setting row " + (r-1) + " probe to low." );
                    rowProbeBus.write( r-1, false ) ;
                }
                log( "Setting row " + r + " probe to high." );
                rowProbeBus.write( r, true ) ;
            }
        }
        
        log( "Reading switch states." ) ;
        log( "Setting col probe " + (NUM_COLS_GAMEBOARD-1) + " to low." ) ;
        colProbeBus.write( NUM_COLS_GAMEBOARD-1, false ) ;
        
        log( "Setting col probe " + NUM_COLS_GAMEBOARD + " to low." ) ;
        colProbeBus.write( NUM_COLS_GAMEBOARD, true ) ;
        
        log( "Setting row probe to high." ) ;
        rowProbeBus.setHigh() ;
        
        log( "\tClearing row bus." ) ;
        rowProbeBus.clear() ;
        
        for( int r=0; r<NUM_ROWS_GAMEBOARD; r++ ) {
            if( r>0 ) {
                log( "Setting row " + (r-1) + " probe to low." ) ;
                rowProbeBus.write( r-1, false ) ;
            }
            
            log( "Setting row " + r + " probe to high." ) ;
            rowProbeBus.write( r, true ) ;
        }
        
        spiBus.clear() ;
    }
    
    private void refreshBoardState( BoardState state ) throws Exception {
        
        log( "Reading board state..." ) ;
        
        spiBus.clear() ;
        state.clear() ;
        log( "Cleared the SPI bus" ) ;

        // Read the grid states
        for( int c=0; c<NUM_COLS_GAMEBOARD; c++ ) {
            log( "Reading cell states for column " + c ) ;
            
            if( c>0 ) {
                log( "\tSetting col " + (c-1) + " probe to low." );
                colProbeBus.write( (c-1), false ) ;
            }
            log( "\tSetting col " + c + " probe to high." );
            colProbeBus.write( c, true ) ;
            
            log( "\tSetting all row probes to high." );
            rowProbeBus.setHigh() ;
            
            if( inHigh() ) {
                log( "\t\tIn Pin is high" ) ;
                log( "\t\tSome cells in column " + c + " are occupied." ) ;
                log( "\t\tTrying to determing occupied cells" ) ;
                
                rowProbeBus.clear() ;
                log( "\t\t\tRow bus cleared" ) ;
                
                for( int r=0; r<NUM_ROWS_GAMEBOARD; r++ ) {
                    if( r>0 ) {
                        log( "\t\t\tSetting row " + (r-1) + " probe to low." );
                        rowProbeBus.write( r-1, false ) ;
                    }
                    log( "\t\t\tSetting row " + r + " probe to high." );
                    rowProbeBus.write( r, true ) ;
                    
                    if( inHigh() ) {
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
                log( "\t\tNo cells in column " + c + " are occupied." ) ;
            }
            log( "\tSetting row probe to low" ) ;
            rowProbeBus.clear() ;
        }
        
        log( "Reading switch states." ) ;
        log( "Setting col probe " + (NUM_COLS_GAMEBOARD-1) + " to low." ) ;
        colProbeBus.write( NUM_COLS_GAMEBOARD-1, false ) ;
        
        log( "Setting col probe " + NUM_COLS_GAMEBOARD + " to low." ) ;
        colProbeBus.write( NUM_COLS_GAMEBOARD, true ) ;
        
        log( "Setting row probe to high." ) ;
        rowProbeBus.setHigh() ;
        
        if( inHigh() ) {
            log( "\tIn Pin is high" ) ;
            log( "\tSome switches are on." ) ;
            log( "\tTrying to determine on switches" ) ;
            
            rowProbeBus.clear() ;
            log( "\tRow bus cleared." ) ;
            
            for( int r=0; r<NUM_ROWS_GAMEBOARD; r++ ) {
                if( r>0 ) {
                    log( "\t\tSetting row " + (r-1) + " probe to low." ) ;
                    rowProbeBus.write( r-1, false ) ;
                }
                
                log( "\t\tSetting row " + r + " probe to high." ) ;
                rowProbeBus.write( r, true ) ;
                
                if( inHigh() ) {
                    log( "\t\tIn Pin is high. Switch [" + r + "] is on" ) ;
                    state.setSwitchState( r, true ) ;
                }
                else {
                    log( "\t\t\tIn Pin is low. Switch [" + r + "] is off" ) ;
                }
            }
        }

        spiBus.clear() ;
    }
    
    private void log( String message ) {
//        log.debug( message ) ;

    }
    
    private boolean inHigh() throws Exception {
        return inPin.isHigh() ;
    }

    public static void main( String[] args ) throws Exception {
        
        log.debug( "Starting BoardReader sample program." ) ;
        
//        new PiReadinessChecker().runPreFlightCheck() ;
        new BoardReader().readBoardState() ;
        ObjectFactory.instance().getGPIOManager().shutdown() ;
        
        log.debug( "Ending BoardReader sample program" ) ;
    }
}
