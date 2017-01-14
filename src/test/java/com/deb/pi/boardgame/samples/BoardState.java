package com.deb.pi.boardgame.samples;

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
    
    public String toString() {
        
        StringBuilder buffer = new StringBuilder( "\n" ) ;
        
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
            buffer.append( "\tSwitch " + i + " = " + switchState[i] + "\n" ) ;
        }
        
        return buffer.toString() ;
    }
}
