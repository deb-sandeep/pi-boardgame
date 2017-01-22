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
        spiBus = new SPIOutputBus( SpiChannel.CS1, 1 ) ;
        inPin = ObjectFactory.instance().getGPIOManager().getInputPin( 0 ) ;
        
        spiBus.clear() ;
    }
    
    public void run() throws Exception {
        
        int numIterationsPerCycle = 10000 ;
        long delay = 1 ;
        
        spiBus.clear() ;
        
        log.debug( "Starting test with switch delay = " + delay ) ;
        
        for( int i=0; i<numIterationsPerCycle; i++ ) {
            spiBus.setHigh() ;
            Assert.state( inPin.getState() == State.HIGH, 
                          "In pin should be high for a high input" ) ;
            
            Thread.sleep( delay ) ;
            
            spiBus.clear() ;
            Assert.state( inPin.getState() == State.LOW, 
                          "In pin should be high for a low input" ) ;
            
            Thread.sleep( delay ) ;
            if( i % 100 == 0 ) {
                System.out.println( i ) ;
            }
        }
        
        spiBus.clear() ;
    }
    
    public void test1() throws Exception {
        
        spiBus.clear() ;
        for( int i=0; i<5; i++ ) {
            System.out.println( "-------------------------" ) ;
            spiBus.write( 0, true ) ;
            log.debug( "High" ) ;
            Thread.sleep( 2000 ) ;
            spiBus.write( 0, false ) ;
            log.debug( "Low" ) ;
            Thread.sleep( 2000 ) ;
        }
        spiBus.clear() ;
    }
    
    public static void main( String[] args ) throws Exception {
        
        try {
            checker.runPreFlightCheck() ;
            new InPinReadSpeedTest().run() ;
            ObjectFactory.instance().getGPIOManager().shutdown() ;
            System.out.println( "Program can be terminated now." ) ;
        }
        catch( IllegalStateException e ) {
            log.error( "\n\nPreflight check failed. Check - " + e.getMessage() ) ;
            log.error( "\nTerminating program." ) ;
        }
    }
}
