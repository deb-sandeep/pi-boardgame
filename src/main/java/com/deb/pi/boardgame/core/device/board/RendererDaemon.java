package com.deb.pi.boardgame.core.device.board;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.impl.output.SPIOutputBus ;
import com.deb.pi.boardgame.core.device.board.GameBoardHardware.GlowState ;
import com.pi4j.io.spi.SpiChannel ;
import com.tomgibara.bits.BitVector ;

class RendererDaemon extends Thread {
    
    private static final Logger log = Logger.getLogger( RendererDaemon.class ) ;
    
    private static final int TIME_BETWEEN_REFRESH     = 5 ;
    private static final int SPI_BUS_SIZE             = 24 ;
    private static final int ROW_SINK_START_WIRE_NUM  = 16 ;
    
    private GameBoardHardware hardware    = null ;
    private boolean           keepRunning = true ;
    
    private OutputBus spiBus     = null ;
    private OutputBus colDataBus = null ;
    private OutputBus rowSinkBus = null ;
    
    private int numCols = 0 ;
    private int numRows = 0 ;
    
    private int nextRowToStrobe = 0 ;
    private BitVector colBitVector = null ;
    private BitVector rowBitVector = null ;
    
    public RendererDaemon( GameBoardHardware hardware, SpiChannel spiChannel )
        throws Exception {
        
        super.setDaemon( true ) ;
        this.hardware = hardware ;
        
        numCols = hardware.getNumCols() ;
        numRows = hardware.getNumRows() ;

        spiBus      = new SPIOutputBus( spiChannel, SPI_BUS_SIZE ) ;
        colDataBus  = ( OutputBus )spiBus.getSubBus( 0, numCols*2 ) ;
        rowSinkBus  = ( OutputBus )spiBus.getSubBus( ROW_SINK_START_WIRE_NUM, numRows ) ;
        
        colBitVector = new BitVector( 2*numCols ) ;
        rowBitVector = new BitVector( numRows ) ;
    }
    
    public void die() throws Exception {
        this.keepRunning = false ;
        while( super.isAlive() ) {
            try {
                log.debug( "Waiting for renderer daemon to die." ) ;
                Thread.sleep( 100 ) ;
            }
            catch( InterruptedException e ) {}
        }
        spiBus.clear() ;
    }
    
    public void run() {
        try {
            log.debug( "Starting game board rendering daemon." ) ;
            spiBus.clear() ;
            while( keepRunning ) {
                strobeNextRow() ;
                try {
                    Thread.sleep( TIME_BETWEEN_REFRESH ) ;
                }
                catch( InterruptedException e ) {
                    // Gobble
                }
            }
        }
        catch( Exception e ) {
            log.error( "Error while rendering board state.", e ) ;
        }
    }
    
    private void strobeNextRow() throws Exception {
        
        rowBitVector.clear() ;
        rowBitVector.setBit( nextRowToStrobe, true ) ;
        
        colBitVector.clear() ;
        for( int c=0; c<numCols; c++ ) {
            GlowState color = hardware.getCellGlowState( nextRowToStrobe, c ) ;
            if( color == GlowState.RED ) {
                colBitVector.setBit( 2*c, true ) ;
            }
            else if( color == GlowState.GREEN ) {
                colBitVector.setBit( 2*c+1, true ) ;
            }
        }
        
        rowSinkBus.clear() ;
        colDataBus.write( colBitVector ) ;
        rowSinkBus.write( rowBitVector ) ;
        
        nextRowToStrobe++ ;
        if( nextRowToStrobe == numRows ) {
            nextRowToStrobe = 0 ;
        }
    }
}
