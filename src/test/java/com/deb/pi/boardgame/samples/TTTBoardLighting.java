package com.deb.pi.boardgame.samples;

import java.util.BitSet ;

import com.deb.pi.boardgame.core.bus.ParallelOutputBus ;

public class TTTBoardLighting {

    private static enum CellState { RED, GREEN } ;
    
    private ParallelOutputBus colBus = null ;
    private ParallelOutputBus rowBus = null ;
    
    public TTTBoardLighting() {
        colBus = new ParallelOutputBus( 0, 1, 2, 3, 4, 5 ) ;
        rowBus = new ParallelOutputBus( 6, 7, 15 ) ;
    }
    
    public void runSimulation() throws Exception {
        
        for( int iter=0; iter<50; iter++ ) {
            for( int row=0; row<3; row++ ) {
                for( int col=0; col<3; col++ ) {
                    lightCell( row, col, CellState.RED ) ;
                    Thread.sleep( 100 ) ;
                }
            }
        }
    }
    
    public void strobePattern() throws Exception {

        int[][] xPattern = new int[][] {
            {0, 1, 0},
            {1, 0, 1},
            {0, 1, 0}
        } ;
        int[][] yPattern = new int[][] {
            {1, 0, 1},
            {0, 1, 0},
            {1, 0, 1},
        };
        
        BitSet[] rowBitSets1 = new BitSet[3] ;
        for( int row=0; row<3; row++ ) {
            BitSet bs = new BitSet(6) ;
            for( int col=0; col<3; col++ ) {
                if( xPattern[row][col] == 1 ) bs.set( col*2 ) ;
            }
            rowBitSets1[row] = bs ;
        }
        
        BitSet[] rowBitSets2 = new BitSet[3] ;
        for( int row=0; row<3; row++ ) {
            BitSet bs = new BitSet(6) ;
            for( int col=0; col<3; col++ ) {
                if( yPattern[row][col] == 1 ) bs.set( col*2 ) ;
            }
            rowBitSets2[row] = bs ;
        }
        
        for( int iter=0; iter<1000; iter++ ) {
            strobePattern( rowBitSets1 ) ;
        }
        for( int iter=0; iter<1000; iter++ ) {
            strobePattern( rowBitSets2 ) ;
        }
        for( int iter=0; iter<1000; iter++ ) {
            strobePattern( rowBitSets1 ) ;
        }
        for( int iter=0; iter<1000; iter++ ) {
            strobePattern( rowBitSets2 ) ;
        }
        for( int iter=0; iter<1000; iter++ ) {
            strobePattern( rowBitSets1 ) ;
        }
    }
    
    private void strobePattern( BitSet[] pattern ) {
        
        for( int row=0; row<3; row++ ) {
            BitSet bs = pattern[row] ;
            rowBus.setData( 0 ) ;
            colBus.setData( bs ) ;
            rowBus.setData( (int)Math.pow( 2, row ) );
            try{
                Thread.sleep( 5 ) ;
            }
            catch( Exception e ){}
        }
    }
    
    private void lightCell( int row, int col, CellState color ) {
        
        int colBusBitNum = col*2 ;
        int rowBusBitNum = row ;
        
        if( color == CellState.GREEN ) colBusBitNum++ ;
        
        System.out.println( "Setting [" + row + "," + col + "] to " + color ) ;
        colBus.setData( (int)Math.pow( 2, colBusBitNum ) ) ;
        rowBus.setData( (int)Math.pow( 2, rowBusBitNum ) ) ;
    }
    
    public void testRows() throws Exception{
        rowBus.setData( 1 );
        colBus.setData( 21 );
        Thread.sleep( 1000 );
        rowBus.setData( 2 );
        colBus.setData( 21 );
        Thread.sleep( 1000 );
        rowBus.setData( 4 );
        colBus.setData( 21 );
        Thread.sleep( 1000 );
    }

    public static void main( String[] args ) throws Exception {
        TTTBoardLighting driver = new TTTBoardLighting() ;
        driver.strobePattern();
    }
}
