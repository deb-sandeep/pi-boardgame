package com.deb.pi.boardgame.samples.ipdriver;

import org.apache.log4j.Logger ;
import org.springframework.util.Assert ;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.impl.output.SPIOutputBus ;
import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.AbstractPin.State ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;
import com.deb.pi.boardgame.samples.PiReadinessChecker ;
import com.pi4j.io.spi.SpiChannel ;

public class InPinReadSpeedTest {

    static Logger log = Logger.getLogger( InPinReadSpeedTest.class ) ;
    
    private static PiReadinessChecker checker = new PiReadinessChecker() ;
    
    private OutputBus spiBus = null ;
    private InPin inPin = null ;
    
    public InPinReadSpeedTest() throws Exception {
        spiBus = new SPIOutputBus( SpiChannel.CS1, 8 ) ;
        inPin = ObjectFactory.instance().getGPIOManager().getInputPin( 0 ) ;
        
        spiBus.clear() ;
    }
    
    public void run() throws Exception {
        
        int numIterationsPerCycle = 1 ;
        long delay = 1000 ;
        
        spiBus.clear() ;
        
        while( delay > 0 ) {
            
            log.debug( "Testing with delay " + delay ) ;
            for( int i=0; i<numIterationsPerCycle; i++ ) {
                log.debug( "Switching on" ) ;
                spiBus.setHigh() ;
//                Assert.state( inPin.getState() == State.HIGH, 
//                              "In pin should be high for a high input" ) ;
                
                Thread.sleep( delay ) ;
                
                log.debug( "Switching off" ) ;
                spiBus.clear() ;
//                Assert.state( inPin.getState() == State.LOW, 
//                              "In pin should be high for a low input" ) ;
                
                Thread.sleep( delay ) ;
            }
            if     ( delay > 300 ) delay -= 100 ;
            else if( delay > 150 ) delay -= 50 ;
            else if( delay > 100 ) delay -= 30 ;
            else                   delay -= 1 ;
        }
        
        spiBus.clear() ;
    }
    
    public void test1() throws Exception {
        
        spiBus.clear() ;
        for( int i=0; i<5; i++ ) {
            System.out.println( "-------------------------" ) ;
            spiBus.write( 3, true ) ;
            log.debug( "High" ) ;
            Thread.sleep( 2000 ) ;
            spiBus.write( 3, false ) ;
            log.debug( "Low" ) ;
            Thread.sleep( 2000 ) ;
        }
        spiBus.clear() ;
    }
    
    public static void main( String[] args ) throws Exception {
        
        try {
//            checker.runPreFlightCheck() ;
            new InPinReadSpeedTest().test1() ;
            ObjectFactory.instance().getGPIOManager().shutdown() ;
        }
        catch( IllegalStateException e ) {
            log.error( "\n\nPreflight check failed. Check - " + e.getMessage() ) ;
            log.error( "\nTerminating program." ) ;
        }
    }
}
