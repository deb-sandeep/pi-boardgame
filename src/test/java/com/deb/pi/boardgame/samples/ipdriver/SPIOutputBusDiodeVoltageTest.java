package com.deb.pi.boardgame.samples.ipdriver;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.impl.output.SPIOutputBus ;
import com.deb.pi.boardgame.samples.PiReadinessChecker ;
import com.pi4j.io.spi.SpiChannel ;

public class SPIOutputBusDiodeVoltageTest {
    
    static Logger log = Logger.getLogger( SPIOutputBusDiodeVoltageTest.class ) ;

    private OutputBus bus = null ;
    private OutputBus rowProbeBus = null ;
    private OutputBus colProbeBus = null ;
    
    private PiReadinessChecker checker = new PiReadinessChecker() ;
    
    public SPIOutputBusDiodeVoltageTest() throws Exception {
        bus         = new SPIOutputBus( SpiChannel.CS1, 17 ) ;
        colProbeBus = ( OutputBus )bus.getSubBus( 0, 8 ) ;
        rowProbeBus = ( OutputBus )bus.getSubBus( 9, 8 ) ;
        
        log.debug( "Bus created." ) ;
    }
    
    public void run() throws Exception {
        log.debug( "Running." ) ;
        
        log.debug( "Bus cleared." ) ;
        bus.clear() ;
        
        try {
            for( int c=0; c<colProbeBus.size(); c++ ) {
                if( c > 0 )colProbeBus.write( (c-1), false ) ;
                colProbeBus.write( c, true ) ;
                rowProbeBus.clear() ;
                
                for( int r=0; r<rowProbeBus.size(); r++ ) {
                    if( r > 0 )rowProbeBus.write( (r-1), false ) ;
                    rowProbeBus.write( r, true ) ;
                   
                    String inPinState = c == r ? "HIGH" : "LOW" ;
                    log.debug( "C[" + c + "] R[" + r + "] => InPin = " + inPinState ) ;
                    checker.dialogInput() ;
                    log.debug( "" ) ;
                }
            }
        }
        catch( IllegalStateException e ) {
            log.debug( "\n\nUser abort." ) ;
        }
        
        log.debug( "Done." ) ;
        bus.clear() ;
    }
    
    public static void main( String[] args ) throws Exception {
        System.out.println( "Starting program." ) ;
        new SPIOutputBusDiodeVoltageTest().run() ;
    }
}
