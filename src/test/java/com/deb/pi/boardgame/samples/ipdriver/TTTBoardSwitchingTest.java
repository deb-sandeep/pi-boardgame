package com.deb.pi.boardgame.samples.ipdriver;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.impl.output.SPIOutputBus ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;
import com.deb.pi.boardgame.samples.PiReadinessChecker ;
import com.pi4j.io.spi.SpiChannel ;

public class TTTBoardSwitchingTest {

    static Logger log = Logger.getLogger( TTTBoardSwitchingTest.class ) ;
    private OutputBus colProbeBus = null ;
    
    public TTTBoardSwitchingTest() throws Exception {
        colProbeBus = new SPIOutputBus( SpiChannel.CS1, 4 ) ;
        log.debug( "Column probe bus created." ) ;
    }
    
    public void run() throws Exception {
        
        long startTime = System.currentTimeMillis() ;
        long elapsedTime = 0 ;
        
        colProbeBus.clear() ;
        
        while( elapsedTime < 120*1000 ) {
            for( int probe=0; probe<4; probe++ ) {
                if( probe > 0 )colProbeBus.write( (probe-1), false ) ;
                colProbeBus.write( probe, true ) ;
                Thread.sleep( 5 ) ;
            }
            colProbeBus.clear() ;
            elapsedTime = System.currentTimeMillis() - startTime ;
        }
        
        colProbeBus.clear() ;
    }
    
    public static void main( String[] args ) throws Exception {
        
        try {
            new PiReadinessChecker().runPreFlightCheck() ;
            new TTTBoardSwitchingTest().run() ;
            ObjectFactory.instance().getGPIOManager().shutdown() ;
        }
        catch( IllegalStateException e ) {
            log.error( "\n\nPreflight check failed. Check - " + e.getMessage() ) ;
            log.error( "\nTerminating program." ) ;
        }
    }
}
