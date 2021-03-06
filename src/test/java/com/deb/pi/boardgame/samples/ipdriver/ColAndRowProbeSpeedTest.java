package com.deb.pi.boardgame.samples.ipdriver;

import java.math.BigInteger ;

import org.apache.log4j.Logger ;
import org.springframework.util.Assert ;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.impl.output.SPIOutputBus ;
import com.deb.pi.boardgame.core.gpio.AbstractPin.State ;
import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;
import com.deb.pi.boardgame.samples.PiReadinessChecker ;
import com.pi4j.io.spi.SpiChannel ;
import com.tomgibara.bits.BitVector ;

public class ColAndRowProbeSpeedTest {

    static Logger log = Logger.getLogger( ColAndRowProbeSpeedTest.class ) ;
    
    static PiReadinessChecker checker = new PiReadinessChecker() ;
    
    private OutputBus spiBus = null ;
    private OutputBus colProbeBus = null ;
    private OutputBus rowProbeBus = null ;
    private InPin     inPin = null ;
    
    public ColAndRowProbeSpeedTest() throws Exception {
        
        spiBus = new SPIOutputBus( SpiChannel.CS1, 17 ) ;
        colProbeBus = ( OutputBus )spiBus.getSubBus( 0, 8 ) ;
        rowProbeBus = ( OutputBus )spiBus.getSubBus( 9, 8 ) ;
        inPin = ObjectFactory.instance().getGPIOManager().getInputPin( 0 ) ;
        
        log.debug( "Busses and input pin created." ) ;
    }
    
    public void run() throws Exception {
        
        spiBus.clear() ;

        try {
            runTest1() ;
            log.debug( "Test 1 passed." );
            
            runTest2() ;
            log.debug( "Test 2 passed." );
        }
        catch( IllegalStateException e ) {
            log.error( "Test failed. " + e.getMessage() ) ;
        }
        
        spiBus.clear() ;
    }
    
    // Doesn't matter what the state of the column probes, if the row probes are
    // in a low state, the in pin is always low.
    private void runTest1() throws Exception {
        
        log.debug( "\nRunning test 1." );
        log.debug( "If row probe is low, doesn't matter what the state of " ) ;
        log.debug( "column probe, the in pin is always low." ) ;
        log.debug( "\n" ) ;
        
        spiBus.clear() ;
//        checker.dialogInput( "LED low" ) ;
        Assert.state( inPin.getState() == State.LOW,
                      "1. In pin should be low when row probe bus is at low." ) ;
        
        for( int c=0; c<colProbeBus.size(); c++ ) {
            if( c > 0 )colProbeBus.write( (c-1), false ) ;
            colProbeBus.write( c, true ) ;
            
//            checker.dialogInput( "LED low" ) ;
            Assert.state( inPin.getState() == State.LOW,
                          "2. In pin should be low when row probe bus is at low." ) ;
        }
        
        colProbeBus.clear() ;
        for( int i=0; i<Math.pow( 2, colProbeBus.size() ); i++ ) {
            colProbeBus.write( i ) ;
            Assert.state( inPin.getState() == State.LOW,
                          "3. In pin should be low when row probe bus is at low." ) ;
        }
        
        spiBus.clear() ;
    }
    
    // If a particular row probe line is on HIGH, every time a corresponding
    // column probe line is high, the in pin is HIGH, else LOW
    private void runTest2() throws Exception {
        
        log.debug( "\nRunning test 2." );
        log.debug( "If a particular row probe is high, doesn't matter what the" ) ;
        log.debug( "col probe value is, the in pin will be high if and only if" ) ;
        log.debug( "the corresponding col probe wire is high." ) ;
        log.debug( "\n" ) ;
        
        spiBus.clear() ;
        
        BitVector bv = null ;
        int numTests = 0 ;
        int maxColVal = (int)Math.pow( 2, colProbeBus.size() ) ;
        
        for( int r=0; r<rowProbeBus.size(); r++ ) {
            numTests++ ;
            
            if( r > 0 )rowProbeBus.write( r-1, false ) ;
            rowProbeBus.write( r, true ) ;
            
            for( int i=0; i<maxColVal; i++ ) {
                bv = BitVector.fromBigInteger( BigInteger.valueOf( maxColVal ), 
                                               colProbeBus.size() ) ;
                colProbeBus.write( bv ) ;

                if( bv.getBit( r ) ) {
//                    checker.dialogInput( "LED high" ) ;
                    Assert.state( inPin.getState() == State.HIGH,
                                  "In pin should be HIGH if corresponding" + 
                                  " column and row probes are HIGH." ) ;
                }
                
                if( numTests % 10 == 0 ) {
                    System.out.println() ;
                }
            }
        }
        
        spiBus.clear() ;
    }
    
    public static void main( String[] args ) throws Exception {
        
        try {
//            checker.runPreFlightCheck() ;
            new ColAndRowProbeSpeedTest().run() ;
            ObjectFactory.instance().getGPIOManager().shutdown() ;
        }
        catch( IllegalStateException e ) {
            log.error( "\n\nPreflight check failed. Check - " + e.getMessage() ) ;
            log.error( "\nTerminating program." ) ;
        }
    }
}
