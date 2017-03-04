package com.deb.pi.boardgame.devicetester;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.device.board.GameBoardHardware ;
import com.deb.pi.boardgame.core.ui.Dialog ;
import com.deb.pi.boardgame.core.ui.DialogManager ;
import com.deb.pi.boardgame.core.util.MediaFile ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;
import com.deb.pi.boardgame.samples.PiReadinessChecker ;

/**
 * This is an executable class which does a broad spectrum sanity test of all
 * the two player hardware components in sequence. 
 * 
 * 1. LCD Panel
 * 2. Audio amplifier
 * 3. Board interaction
 */
public class TwoPlayerDeviceTester extends Thread {
    
    static Logger log = Logger.getLogger( TwoPlayerDeviceTester.class ) ;
    
    static enum BoardType{ CHESS, TICTACTOE } ;
    static PiReadinessChecker CHECKER = new PiReadinessChecker() ;

    private GameBoardHardware hardware = null ;
    private DialogManager dMgr = null ;
    
    private boolean keepRunning = true ;
    
    public TwoPlayerDeviceTester( BoardType boardType ) throws Exception {
        if( boardType == BoardType.TICTACTOE ) {
            hardware = new GameBoardHardware( 3, 3, 3 ) ;
        }
        else {
            hardware = new GameBoardHardware( 8, 8, 3 ) ;
        }
        dMgr = DialogManager.instance() ;
    }
    
    public void runSimulation() throws Exception {
        
        // Hardware initialization starts the state reader and renderer daemons
        hardware.initialize() ;

        // Note that till the time the gameboard is initialized, the extended
        // circuitry does not receive power. Hence the startup screen can only 
        // be shown after we have initialized the hardware.
        showStartupScreen() ;
        
        // Starts the killer watchdog which enables the program to be shut down
        //start() ;
        hardware.shutdown() ;
    }
    
    private void showStartupScreen() throws Exception {
        
        dMgr.showDialog( Dialog.createNoInputDialog( "Game Board\n" + 
                                                     "Device Diagnostic" )
                               .setCentered() ) ;
        MediaFile.INTRO.play() ;
        
        Thread.sleep( 5000 ) ;
        dMgr.popDialog() ;
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
        TwoPlayerDeviceTester example = new TwoPlayerDeviceTester( BoardType.TICTACTOE ) ;
        example.runSimulation() ;
    }
}
