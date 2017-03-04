package com.deb.pi.boardgame.devicetester;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.device.board.GameBoardHardware ;
import com.deb.pi.boardgame.core.ui.Dialog ;
import com.deb.pi.boardgame.core.ui.DialogManager ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;
import com.deb.pi.boardgame.samples.PiReadinessChecker ;

/**
 * This is an executable class which does a broad spectrum sanity test of all
 * the gameboard components in sequence. 
 * 
 * 1. LCD Panel
 * 2. Audio amplifier
 * 3. Board interaction
 */
public class GameBoardDeviceTester extends Thread {
    
    static Logger log = Logger.getLogger( GameBoardDeviceTester.class ) ;
    
    static enum BoardType{ CHESS, TICTACTOE } ;
    static PiReadinessChecker CHECKER = new PiReadinessChecker() ;

    private GameBoardHardware hardware = null ;
    private boolean keepRunning = true ;
    private DialogManager dMgr = null ;
    
    public GameBoardDeviceTester( BoardType boardType ) throws Exception {
        if( boardType == BoardType.TICTACTOE ) {
            hardware = new GameBoardHardware( 3, 3, 3 ) ;
        }
        else {
            hardware = new GameBoardHardware( 8, 8, 3 ) ;
        }
        dMgr = DialogManager.instance() ;
    }
    
    public void runSimulation() throws Exception {
        
        showDialog( Dialog.createNoInputDialog( "Game Board\nDevice Diagnostic" ) ) ;
        
        // Hardware initialization starts the state reader and renderer daemons
        hardware.initialize() ;
        
        // Starts the killer watchdog which enables the program to be shut down
        start() ;
    }
    
    private void showDialog( Dialog dialog ) {
        dMgr.showDialog( dialog ) ;
    }
    
    public void run() {
        log.debug( "Starting killer watchdog" ) ;
        long startTime = System.currentTimeMillis() ;
        long duration = 0 ;
        
        while( keepRunning ) {
            try {
                Thread.sleep( 1000 ) ;
            }
            catch( Exception e ){}
            
            duration = System.currentTimeMillis() - startTime ;
            if( duration > 30*1000 ) {
                if( !CHECKER.dialogInput() ) {
                    log.debug( "Shutting down hardware." ) ;
                    try {
                        hardware.shutdown() ;
                    }
                    catch( Exception e ) {
                        log.error( "Hardware shutdown problem.", e ) ;
                    }
                    keepRunning = false ;
                }
            }
        }
        
        ObjectFactory.instance().getGPIOManager().shutdown() ;
    }
    
    public static void main( String[] args ) throws Exception {
        
        log.debug( "Starting game board hardware testing.." ) ;
        GameBoardDeviceTester example = new GameBoardDeviceTester( BoardType.TICTACTOE ) ;
        example.runSimulation() ;
    }
}
