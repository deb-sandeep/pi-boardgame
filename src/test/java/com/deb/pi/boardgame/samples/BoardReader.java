package com.deb.pi.boardgame.samples;

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
    private static final int NUM_COLS_FOR_PROBE = NUM_COLS_GAMEBOARD + 1 ;
    private static final int SPI_BUS_SIZE       = NUM_COLS_FOR_PROBE + 
                                                  NUM_ROWS_GAMEBOARD ;
    private OutputBus spiBus      = null ;
    private OutputBus colProbeBus = null ;
    private OutputBus rowProbeBus = null ;
    private InPin     inPin       = null ;
    
    private BoardState boardState = new BoardState( NUM_ROWS_GAMEBOARD, 
                                                    NUM_COLS_GAMEBOARD,
                                                    NUM_ROWS_GAMEBOARD ) ;
    
    public BoardReader() throws Exception {
        spiBus = new SPIOutputBus( SpiChannel.CS1, SPI_BUS_SIZE ) ;
        colProbeBus = ( OutputBus )spiBus.getSubBus( 0, NUM_COLS_FOR_PROBE ) ;
        rowProbeBus = ( OutputBus )spiBus.getSubBus( NUM_COLS_FOR_PROBE, 
                                                     NUM_ROWS_GAMEBOARD ) ;
        
        inPin = ObjectFactory.instance().getGPIOManager().getInputPin( 0 ) ;
    }
    
    public void run() throws Exception {
        refreshBoardState( boardState ) ;
        log.debug( "Board state = " + boardState ) ;
    }
    
    private void refreshBoardState( BoardState state ) throws Exception {
        
        spiBus.clear() ;

        // Read the grid states
        for( int c=0; c<NUM_COLS_GAMEBOARD; c++ ) {
            if( c>0 ) {
                colProbeBus.write( c-1, false ) ;
            }
            colProbeBus.write( c, true ) ;
            
            rowProbeBus.setHigh() ;
            if( inPin.isHigh() ) {
                rowProbeBus.clear() ;
                for( int r=0; r<NUM_ROWS_GAMEBOARD; r++ ) {
                    if( r>0 ) {
                        rowProbeBus.write( r-1, false ) ;
                    }
                    rowProbeBus.write( r, true ) ;
                    if( inPin.isHigh() ) {
                        state.setCellState( r, c, true ) ;
                    }
                }
            }
        }
        
        // Read the switch states
        colProbeBus.write( NUM_COLS_GAMEBOARD-1, false ) ;
        colProbeBus.write( NUM_COLS_GAMEBOARD, true ) ;
        rowProbeBus.setHigh() ;
        if( inPin.isHigh() ) {
            rowProbeBus.clear() ;
            for( int r=0; r<NUM_ROWS_GAMEBOARD; r++ ) {
                if( r>0 ) {
                    rowProbeBus.write( r-1, false ) ;
                }
                rowProbeBus.write( r, true ) ;
                if( inPin.isHigh() ) {
                    state.setSwitchState( r, true ) ;
                }
            }
        }
    }
    
    

    public static void main( String[] args ) throws Exception {
        
        log.debug( "Starting BoardReader sample program." ) ;
        
        new BoardReader().run() ;
        ObjectFactory.instance().getGPIOManager().shutdown() ;
        
        log.debug( "Ending BoardReader sample program" ) ;
    }
}
