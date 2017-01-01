package com.deb.pi.boardgame.samples;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.impl.output.SPIOutputBus ;
import com.pi4j.io.spi.SpiChannel ;
import com.tomgibara.bits.BitVector ;

public class TTTBoardPattern {
    
    private static final Logger log = Logger.getLogger( TTTBoardPattern.class ) ;
    
    private static final int OFF   = 0 ;
    private static final int RED   = 1 ;
    private static final int GREEN = 2 ;
    
    private OutputBus spiBus = null ;
    
    public TTTBoardPattern() throws Exception {
        
        spiBus = new SPIOutputBus( SpiChannel.CS0, 9 ) ;
//        spiBus = new TestHWOutputBus( 9 ) ;
        log.debug( "Created SPI bus" ) ;
    }
    
    public void run() throws Exception {
        
        spiBus.clear() ;
        for( int i=0; i<60u; i++ ) {
            renderPattern( getBoardPattern( i%2 ), 1 ) ;
        }
        spiBus.clear() ;
    }
    
    private void renderPattern( int[][] pattern, int seconds ) 
        throws Exception {
        
        long startTime = System.currentTimeMillis() ;
        long displayDuration = seconds*1000 ;
        long elapsedTime = 0 ;
        
        BitVector busData = new BitVector( 9 ) ;
        BitVector rowData = busData.range( 0, 3 ) ;
        BitVector colData = busData.rangeFrom( 3 ) ;
        
        int nextActiveRow = 0 ;
        while( elapsedTime < displayDuration ) {
            
            busData.clear() ;
            
//            log.debug( "Rendering row " + nextActiveRow );
            rowData.setBit( nextActiveRow, true ) ;
            populateColData( colData, pattern[nextActiveRow] ) ;
            
//            log.debug( "\tRow data " + rowData ) ;
//            log.debug( "\tCol data " + colData ) ;
//            log.debug( "\tBus data " + busData ) ;
            
            spiBus.write( busData ) ;
            
            Thread.sleep( 5 ) ;
            
            nextActiveRow = nextActiveRow == 2 ? 0 : nextActiveRow+1 ;
            elapsedTime = System.currentTimeMillis() - startTime ;
        }
    }
    
    private void populateColData( BitVector colData, int[] rowPattern ) {
        
        for( int i=0; i<rowPattern.length; i++ ) {
            if( rowPattern[i] == RED ) {
                colData.setBit( i*2, true ) ;
            }
            else if( rowPattern[i] == GREEN ) {
                colData.setBit( i*2+1, true ) ;
            }
        }
    }
    
    public int[][] getBoardPattern( int num ) {
        
        if( num == 0 ) {
            return new int[][] {
                    { GREEN, RED,   GREEN },
                    { RED,   OFF,   RED   },
                    { GREEN, RED,   GREEN }
            } ;
        }
        return new int[][] {
                { RED,     GREEN,   RED },
                { GREEN,   OFF,   GREEN   },
                { RED,     GREEN,   RED }
        } ;
    }

    public static void main( String[] args ) throws Exception {
        new TTTBoardPattern().run() ;
    }
}
