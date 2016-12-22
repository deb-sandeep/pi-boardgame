package com.deb.pi.boardgame.samples;

import java.util.BitSet ;

import com.deb.pi.boardgame.core.bus.ParallelOutputBus ;

public class TTTBoardLighting {

    private static enum CellState { RED, GREEN } ;

    private class Pattern {
    	
    	private BitSet[] bitSets = new BitSet[3] ;
    	
    	public Pattern( int[][] data ) {
            for( int row=0; row<3; row++ ) {
                BitSet bs = new BitSet(6) ;
                for( int col=0; col<3; col++ ) {
                    if( data[row][col] == 1 ) bs.set( col*2 ) ;
                }
                bitSets[row] = bs ;
            }
    	}
    	
    	public BitSet getBitSet( int row ) {
    		return bitSets[row] ;
    	}
    }
    
    private ParallelOutputBus colBus = null ;
    private ParallelOutputBus rowBus = null ;
    
    public TTTBoardLighting() {
        colBus = new ParallelOutputBus( 0, 1, 2, 3, 4, 5 ) ;
        rowBus = new ParallelOutputBus( 6 ) ;
    }
    
    public void runSimulation() throws Exception {
        
        for( int iter=0; iter<50; iter++ ) {
            for( int row=0; row<3; row++ ) {
                for( int col=0; col<3; col++ ) {
                    lightCell( row, col, CellState.RED ) ;
                    Thread.sleep( 1 ) ;
                }
            }
        }
    }
    
    public void strobePattern() throws Exception {

    	Pattern patterns[] = new Pattern[3] ;
        patterns[0] = new Pattern( new int[][] {
            {0, 1, 0},
            {1, 0, 1},
            {0, 1, 0}
        } ) ;
        
        patterns[1] = new Pattern( new int[][] {
            {1, 0, 1},
            {0, 1, 0},
            {1, 0, 1},
        } ) ;
        
        patterns[2] = new Pattern( new int[][] {
            {1, 0, 1},
            {0, 0, 0},
            {1, 0, 1},
        } ) ;
                
        for( int iter=0; iter<25; iter++ ) {
        	for( int i=0; i<patterns.length; i++ ) {
        		showPattern( patterns[i], 1000 ) ;
        	}
        }
    }
    
    private void showPattern( Pattern pattern, int displayTime ) throws Exception{
    	
    	long startTime = System.currentTimeMillis() ;
    	long elapsedTime = 0 ;
    	
    	while( elapsedTime < displayTime ) {
    		strobePattern( pattern ) ;
    		elapsedTime = System.currentTimeMillis() - startTime ;
    	}
    }
    
    private void strobePattern( Pattern pattern ) throws Exception{
        
        for( int row=0; row<3; row++ ) {
            BitSet bs = pattern.getBitSet(row) ;
            rowBus.setData( 0 ) ;
            colBus.setData( bs ) ;
            rowBus.setData( 1 );
            try{
                Thread.sleep( 5 ) ;
            }
            catch( Exception e ){}
        }
    }
    
    private int rowCache = -1;
    
    private void lightCell( int row, int col, CellState color ) throws InterruptedException {
        
        int colBusBitNum = col*2 ;
        
        if( color == CellState.GREEN ) colBusBitNum++ ;
        
        System.out.println( "Setting [" + row + "," + col + "] to " + color ) ;
        if( rowCache != row ) {
        	rowBus.setData( 1 );
        	rowCache = row;
        }
        colBus.setData( (int)Math.pow( 2, colBusBitNum ) ) ;
    	rowBus.setData( 0 );
    }
    
    public void testRows() throws Exception{
        rowBus.setData( 1 );
        colBus.setData( 21 );
        rowBus.setData( 0 );
        Thread.sleep( 1000 );
        rowBus.setData( 1 );
        colBus.setData( 21 );
        rowBus.setData( 0 );
        Thread.sleep( 1000 );
        rowBus.setData( 1 );
        colBus.setData( 21 );
        rowBus.setData( 0 );
        Thread.sleep( 1000 );
    }

    public static void main( String[] args ) throws Exception {
        TTTBoardLighting driver = new TTTBoardLighting() ;
        driver.testRows();;
    }
}
