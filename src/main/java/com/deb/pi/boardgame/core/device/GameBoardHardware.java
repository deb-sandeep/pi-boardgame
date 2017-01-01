package com.deb.pi.boardgame.core.device;

import java.util.ArrayList ;
import java.util.BitSet ;
import java.util.List ;

import org.apache.log4j.Logger ;
import org.springframework.util.Assert ;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.gpio.OutPin ;

public class GameBoardHardware {

    static final Logger log = Logger.getLogger( GameBoardHardware.class ) ;
    
    public static enum GlowState{ GREEN, RED, OFF } ;

    private class Cell {
        
        private GamePiece piece     = null ;
        private GlowState glowState = GlowState.OFF ;

        Cell() {
            this.glowState = GlowState.OFF ;
        }
        
        public void setPiece( GamePiece piece ) {
            if( this.piece != null ) {
                throw new IllegalStateException( "Cell already contains a piece" ) ;
            }
            this.piece = piece ;
            this.glowState = glowStateIfPiecePresent ;
        }
        
        public GamePiece removePiece() {
            GamePiece retVal = this.piece ;
            this.piece = null ;
            this.glowState = glowStateIfPieceAbsent ;
            return retVal ;
        }

        public GlowState getGlowState() {
            return this.glowState ;
        }
        
        public void setGlowState( GlowState glowState ) {
            this.glowState = glowState ;
        }
        
        public boolean isOccupied() {
            return this.piece != null ;
        }
    }
    
    private class Renderer extends Thread {
        
        private BitSet colBitStates = null ;
        
        Renderer() {
            super.setDaemon( true ) ;
            this.colBitStates = new BitSet( numCols*2 ) ;
        }
        
        public void run() {
            while( true ) {
                strobeCellGlowStates() ;
            }
        }
        
        private void strobeCellGlowStates() {
            // TODO
        }
        
        private void updateBitStatesForRow( int row ) {
            
            Cell[] rowCells = cells[row] ;
            this.colBitStates.clear() ;
            
            for( int col=0; col<numCols; col++ ) {
                Cell cell = rowCells[col] ;
                if( cell.getGlowState() == GlowState.RED ) {
                    this.colBitStates.set( col*2 ) ;
                }
                else if( cell.getGlowState() == GlowState.GREEN ) {
                    this.colBitStates.set( col*2 + 1 ) ;
                }
            }
        }
    }
    
    private int        numRows       = 0 ;
    private int        numCols       = 0 ;
    private OutputBus  colsOutputBus = null ;
    private OutPin     rowStrobePin  = null ;
    private Cell[][]   cells         = null ;
    private List<Cell> cellArray     = new ArrayList<GameBoardHardware.Cell>() ;
    private Renderer   renderer      = new Renderer() ;
    
    private GlowState glowStateIfPiecePresent = GlowState.GREEN ;
    private GlowState glowStateIfPieceAbsent  = GlowState.OFF ;
    
    
    public GameBoardHardware( int numRows, int numCols, 
                                         OutputBus colSignalBus,
                                         OutPin rowStobePin ) {
        
        this( numRows, numCols, colSignalBus, rowStobePin, 
              GlowState.GREEN, GlowState.OFF ) ;
    }
    
    public GameBoardHardware( int numRows, int numCols, 
                                 OutputBus colSignalBus,
                                 OutPin rowStrobePin,
                                 GlowState glowStateIfPiecePresent,
                                 GlowState glowStateIfPieceAbsent ) {
        
        Assert.isTrue( numRows > 0, "Num rows must be greater than 0" ) ;
        Assert.isTrue( numCols > 0, "Num cols must be greater than 0" ) ;
        
        this.numRows = numRows ;
        this.numCols = numCols ;
        this.colsOutputBus = colSignalBus ;
        this.rowStrobePin = rowStrobePin ;
        
        this.cells = new Cell[numRows][numCols] ;
        for( int row=0; row<numRows; row++ ) {
            for( int col=0; col<numCols; col++ ) {
                cells[row][col] = new Cell() ;
                cellArray.add( cells[row][col] ) ;
            }
        }
        
        renderer.start() ;
        
        setGlowCellAutoStates( glowStateIfPiecePresent, 
                               glowStateIfPieceAbsent ) ;
        
        
    }
    
    public void setGlowCellAutoStates( GlowState glowStateIfPiecePresent, 
                                       GlowState glowStateIfPieceAbsent  ) {
        
        this.glowStateIfPiecePresent = glowStateIfPiecePresent ;
        this.glowStateIfPieceAbsent  = glowStateIfPieceAbsent ;
        
        for( Cell c : cellArray ) {
            if( c.isOccupied() ) {
                c.setGlowState( glowStateIfPiecePresent ) ;
            }
            else {
                c.setGlowState( this.glowStateIfPieceAbsent ) ;
            }
        }
    }
    
    public void showPattern( GlowState[][] pattern, int millis ) {
        
        Assert.isTrue( pattern.length == numRows, 
                       "Pattern rows should equal board row size" ) ;
        Assert.isTrue( pattern[0].length == numCols, 
                       "Pattern cols should equal board col size" ) ;
        
        // Suppress the auto glow thingi
        GlowState oldGlowStateIfPiecePresent = glowStateIfPiecePresent ;
        GlowState oldGlowStateIfPieceAbsent  = glowStateIfPieceAbsent ;
        
        setGlowCellAutoStates( GlowState.OFF, GlowState.OFF ) ;
        
        for( int row=0; row<numRows; row++ ) {
            for( int col=0; col<numCols; col++ ) {
                // Just set the cell glow state, the daemon renderer will strobe
                // the cell states to the physical bus
                getCell( row, col ).setGlowState( pattern[row][col] );
            }
        }
        
        try {
            Thread.sleep( millis ) ;
        }
        catch( Exception e ) { /* Gobble */ }
        
        setGlowCellAutoStates( oldGlowStateIfPiecePresent, 
                               oldGlowStateIfPieceAbsent ) ;
    }
    
    public void superimposePattern( int [][] pattern, int millis ) {
        
    }
    
    private Cell getCell( int row, int col ) {
        return cells[row][col] ;
    }
    
    public void setPiece( int row, int col, GamePiece piece ) {
        log.debug( "Setting piece at " + row + ", " + col ) ;
        Cell cell = getCell( row, col ) ;
        cell.setPiece( piece ) ;
    }
    
    public GamePiece removePiece( int row, int col ) {
        return getCell( row, col ).removePiece() ;
    }
    
    public GamePiece replacePiece( int row, int col, 
                                           GamePiece newPiece ) {
        Cell cell = getCell( row, col ) ;
        GamePiece oldPiece = cell.removePiece() ;
        cell.setPiece( newPiece ) ;
        return oldPiece ;
    }
}
