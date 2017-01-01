package com.deb.pi.boardgame.samples;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.ParallelOutputBus ;
import com.deb.pi.boardgame.core.device.GameBoardHardware ;
import com.deb.pi.boardgame.core.device.GamePiece ;
import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;

public class GameBoardHardwareExample {
    
    static Logger log = Logger.getLogger( GameBoardHardwareExample.class ) ;

    private GameBoardHardware gameBoard      = null ;
    private OutputBus         colBus         = null ;
    private OutPin            strobePulsePin = null ;
    
    public GameBoardHardwareExample() {
        
        colBus = new ParallelOutputBus( 0, 1, 2, 3, 4, 5 ) ;
        strobePulsePin = ObjectFactory.instance().getGPIOManager().getOutputPin( 6 ) ;
        
        gameBoard = new GameBoardHardware( 3, 3, colBus, strobePulsePin ) ;
    }
    
    public void runSimulation() throws Exception {
        gameBoard.setPiece( 0, 0, new GamePiece() ) ;
        gameBoard.setPiece( 1, 1, new GamePiece() ) ;
        Thread.sleep( 5000 ) ;
//        for( int row=0; row<3; row++ ) {
//            for( int col=0; col<3; col++ ) {
//                log.debug( "Setting piece on [" + row + "," + col + "]" ) ;
//                Thread.sleep( 2000 ) ;
//            }
//        }
    }
    
    public static void main( String[] args ) throws Exception {
        
        GameBoardHardwareExample example = new GameBoardHardwareExample() ;
        example.runSimulation() ;
    }
}
