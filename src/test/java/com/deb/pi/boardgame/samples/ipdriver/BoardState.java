package com.deb.pi.boardgame.samples.ipdriver;

import java.util.Arrays ;

public class BoardState {

    private int numRows = 0 ;
    private int numCols = 0 ;
    private int numSwitches = 0 ;
    
    private boolean gridState[][] = null ;
    private boolean switchState[] = null ;
    
    public BoardState( int numRows, int numCols, int numSwitches ) {
        this.numCols = numCols ;
        this.numRows = numRows ;
        this.numSwitches = numSwitches ;
        
        this.gridState = new boolean[numRows][numCols] ;
        this.switchState = new boolean[numRows] ;
    }
    
    public Object clone() {
        BoardState clone = new BoardState( numRows, numCols, numSwitches ) ;
        for( int r=0; r<numRows; r++ ) {
            for( int c=0; c<numCols; c++ ) {
                clone.setCellState( r, c, gridState[r][c] );
            }
        }
        
        for( int s=0; s<numSwitches; s++ ) {
            clone.setSwitchState( s, switchState[s] ) ;
        }
        return clone ;
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
    
    @Override
    public boolean equals( Object obj ) {
        
        if( this == obj ) { 
            return true ; 
        }
        
        if( obj == null ) { 
            return false ; 
        }
        
        if( !( obj instanceof BoardState ) ) { 
            return false ; 
        }
        
        BoardState other = (BoardState) obj ;
        if( !Arrays.deepEquals( gridState, other.gridState ) ) { 
            return false ; 
        }
        
        if( numCols != other.numCols ) { 
            return false ; 
        }
        
        if( numRows != other.numRows ) { 
            return false ; 
        }
        
        if( numSwitches != other.numSwitches ) { 
            return false ; 
        }
        
        if( !Arrays.equals( switchState, other.switchState ) ) { 
            return false ; 
        }
        
        return true ;
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
