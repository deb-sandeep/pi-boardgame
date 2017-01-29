package com.deb.pi.boardgame.samples;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.device.board.GameBoardHardware ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;

public class GameBoardHardwareExample extends Thread {
    
    static Logger log = Logger.getLogger( GameBoardHardwareExample.class ) ;
    static PiReadinessChecker CHECKER = new PiReadinessChecker() ;

    private GameBoardHardware hardware = null ;
    private boolean keepRunning = true ;
    
    public GameBoardHardwareExample() throws Exception {
        hardware = new GameBoardHardware( 3, 3, 3 ) ;
    }
    
    public void runSimulation() throws Exception {
        start() ;
        hardware.initialize() ;
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
            if( duration > 60*1000 ) {
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
        
//        CHECKER.runPreFlightCheck() ;
        
        log.debug( "Starting game board hardware testing.." ) ;
        GameBoardHardwareExample example = new GameBoardHardwareExample() ;
        example.runSimulation() ;
    }
}
