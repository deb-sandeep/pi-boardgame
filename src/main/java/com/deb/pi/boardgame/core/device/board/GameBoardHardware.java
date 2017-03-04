package com.deb.pi.boardgame.core.device.board;

import java.util.ArrayList ;
import java.util.List ;

import org.apache.log4j.Logger ;
import org.springframework.util.Assert ;

import com.deb.pi.boardgame.core.gpio.GPIOManager ;
import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.deb.pi.boardgame.core.gpio.AbstractPin.State ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;
import com.pi4j.io.spi.SpiChannel ;

public class GameBoardHardware {

    static final Logger log = Logger.getLogger( GameBoardHardware.class ) ;
    
    public static enum GlowState{ GREEN, RED, OFF } ;

    private class Cell {
        
        private boolean   occupied  = false ;
        private GlowState glowState = GlowState.OFF ;

        Cell() {
            this.glowState = GlowState.OFF ;
        }
        
        public void setOccupied( boolean newState ) {
            if( this.occupied && newState ) {
                throw new IllegalStateException( "Cell is occupied." ) ;
            }
            else {
                this.occupied = newState ;
                this.glowState = ( this.occupied ) ? glowStateIfCellOccupied :
                                                     glowStateIfCellEmpty ;
            }
        }
        
        public boolean isOccupied() {
            return this.occupied ;
        }
        
        public GlowState getGlowState() {
            return this.glowState ;
        }
        
        public void setGlowState( GlowState glowState ) {
            this.glowState = glowState ;
        }
    }
    
    private class Switch {
        
        private boolean state = false ;
        
        Switch() {
            this.state = false ;
        }
        
        public boolean getState() {
            return this.state ;
        }
        
        public void setState( boolean state ) {
            this.state = state ;
        }
    }
    
    private static final int READER_IN_PIN = 0 ;
    private static final int VIBRATOR_OUT_PIN = 1 ;
    private static final int EXT_CIRCUIT_POWER_PIN = 7 ;
    
    private int numRows     = 0 ;
    private int numCols     = 0 ;
    private int numSwitches = 0 ;
    
    private GlowState glowStateIfCellOccupied = GlowState.GREEN ;
    private GlowState glowStateIfCellEmpty    = GlowState.OFF ;
    
    private Cell[][]   cells     = null ;
    private Switch[]   switches  = null ;
    private List<Cell> cellArray = new ArrayList<GameBoardHardware.Cell>() ;
    
    private StateReaderDaemon stateReaderDaemon = null ;
    private RendererDaemon    rendererDaemon    = null ;
    
    private OutPin vibratorPin = null ;
    private OutPin extCktPowerPin = null ;
    
    public GameBoardHardware( int numRows, int numCols, int numSwitches ) 
        throws Exception {
        
        Assert.isTrue( numRows     > 0, "Num rows must be > 0" ) ;
        Assert.isTrue( numCols     > 0, "Num cols must be > 0" ) ;
        Assert.isTrue( numSwitches > 0, "Num switches must be > 0" ) ;
        
        this.numRows       = numRows ;
        this.numCols       = numCols ;
        this.numSwitches   = numSwitches ;
        
        this.cells = new Cell[numRows][numCols] ;
        for( int row=0; row<numRows; row++ ) {
            for( int col=0; col<numCols; col++ ) {
                cells[row][col] = new Cell() ;
                cellArray.add( cells[row][col] ) ;
            }
        }
        
        this.switches = new Switch[numSwitches] ;
        for( int sw=0; sw<numSwitches; sw++ ) {
            this.switches[sw] = new Switch() ;
        }
        
        disableCellTenantHighlight() ;
        
        rendererDaemon = new RendererDaemon( this, SpiChannel.CS0 ) ;
        stateReaderDaemon = new StateReaderDaemon( this, SpiChannel.CS1, 
                                                   READER_IN_PIN ) ; 
        
        GPIOManager gpioMgr = ObjectFactory.instance().getGPIOManager() ;
        vibratorPin = gpioMgr.getOutputPin( VIBRATOR_OUT_PIN ) ;
        extCktPowerPin = gpioMgr.getOutputPin( EXT_CIRCUIT_POWER_PIN ) ;
    }
    
    public void initialize() {
        
        log.debug( "Powering on extended circuitry" ) ;
        extCktPowerPin.setState( State.HIGH ) ;
        
        if( stateReaderDaemon != null ) {
            stateReaderDaemon.start() ;
        }
        else {
            throw new IllegalStateException( "Software clone of game board " + 
                    "hardware has been destoryed" ) ;
        }
        
        if( rendererDaemon != null ) {
            rendererDaemon.start() ;
        }
        else {
            throw new IllegalStateException( "Software clone of game board " + 
                                             "hardware has been destoryed" ) ;
        }
    }
    
    public void shutdown() throws Exception {
        stateReaderDaemon.die() ;
        stateReaderDaemon = null ;
        
        rendererDaemon.die() ;
        rendererDaemon = null ;
        
        extCktPowerPin.setState( State.LOW ) ;
    }
    
    public void disableCellTenantHighlight() {
        setTenantGlowStates( GlowState.OFF, GlowState.OFF ) ;
    }
    
    public void enableCellTenantHighlight() {
        setTenantGlowStates( GlowState.GREEN, GlowState.OFF ) ;
    }
    
    private void setTenantGlowStates( GlowState glowStateIfPiecePresent,
                                      GlowState glowStateIfPieceAbsent ) {
        
        this.glowStateIfCellOccupied = glowStateIfPiecePresent ;
        this.glowStateIfCellEmpty    = glowStateIfPieceAbsent ;
        
        for( Cell c : cellArray ) {
            if( c.isOccupied() ) {
                c.setGlowState( this.glowStateIfCellOccupied ) ;
            }
            else {
                c.setGlowState( this.glowStateIfCellEmpty ) ;
            }
        }
    }
    
    public void showPattern( GlowState[][] pattern, int millis ) {
        
        Assert.isTrue( pattern.length == numRows, 
                       "Pattern rows should equal board row size" ) ;
        
        Assert.isTrue( pattern[0].length == numCols, 
                       "Pattern cols should equal board col size" ) ;
        
        for( int row=0; row<numRows; row++ ) {
            for( int col=0; col<numCols; col++ ) {
                getCell( row, col ).setGlowState( pattern[row][col] );
            }
        }
        
        try {
            Thread.sleep( millis ) ;
        }
        catch( Exception e ) { /* Gobble */ }
    }
    
    private Cell getCell( int row, int col ) {
        return cells[row][col] ;
    }
    
    void setCellState( int row, int col, boolean state ) {
        
        log.debug( "Setting cell[" + row + "][" + col + "] as " + 
                   ( state ? "occupied" : "empty" ) ) ;
        
        Cell cell = getCell( row, col ) ;
        cell.setOccupied( state ) ;
        vibratorPin.pulse( 75 ) ;
    }

    boolean getCellState( int row, int col ) {
        return getCell( row, col ).isOccupied() ;
    }
    
    GlowState getCellGlowState( int row, int col ) {
        return getCell( row, col ).getGlowState() ;
    }
    
    void setSwitchState( int switchNum, boolean state ) {
        switches[switchNum].setState( state ) ;
        vibratorPin.pulse( 75 ) ;
    }
    
    boolean getSwitchState( int switchNum ) {
        return switches[switchNum].getState() ; 
    }
    
    public int getNumRows() {
        return this.numRows ;
    }
    
    public int getNumCols() {
        return this.numCols ;
    }
    
    public int getNumSwitches() {
        return this.numSwitches ;
    }
}
