package com.deb.pi.boardgame.samples.ipdriver;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.impl.output.SPIOutputBus ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;
import com.deb.pi.boardgame.samples.PiReadinessChecker ;
import com.pi4j.io.spi.SpiChannel ;

// Take the 595 driver circuit, connect all 17 to LEDs and run this test
// Observation
// For each column probe line, the LED will glow
//     Program will loop through all row probe lines glowing each LED in turn.
public class SPIProbeDriverTest {

    static Logger log = Logger.getLogger( SPIProbeDriverTest.class ) ;
    
    private OutputBus mainBus = null ;
    private OutputBus colProbeBus = null ;
    private OutputBus rowProbeBus = null ;
    
    public SPIProbeDriverTest() throws Exception {
        mainBus = new SPIOutputBus( SpiChannel.CS1, 17 ) ;
        log.debug( "Main bus created." ) ;
        
        colProbeBus = ( OutputBus )mainBus.getSubBus( 0, 9 ) ;
        rowProbeBus = ( OutputBus )mainBus.getSubBus( 9, 8 ) ;
        log.debug( "Row and col probe busses created" ) ;
    }
    
    public void run() throws Exception {
        mainBus.clear() ;

        colProbeBus.setHigh() ;
        log( "Set col probe bus to high" ) ;
        Thread.sleep( 1000 ) ;
        
        colProbeBus.clear() ;
        log( "Cleared col probe bus" ) ;
        Thread.sleep( 1000 ) ;
        
        rowProbeBus.setHigh() ;
        log( "Set row probe bus to high" ) ;
        Thread.sleep( 1000 ) ;
        
        rowProbeBus.clear() ;
        log( "Cleared row probe bus" ) ;
        Thread.sleep( 1000 ) ;
        
        for( int i=0; i<colProbeBus.size(); i++ ) {
            if( i>0 )colProbeBus.write( (i-1), false ) ;
            colProbeBus.write( i, true ) ;
            log( "Set col " + i + " to high." ) ;
            Thread.sleep( 50 ) ;
            
            for( int j=0; j<rowProbeBus.size(); j++ ) {
                if( j>0 )rowProbeBus.write( (j-1), false ) ;
                rowProbeBus.write( j, true ) ;
                log( "\tSet row " + j + " to high." ) ;
                Thread.sleep( 50 ) ;
            }
            
            rowProbeBus.clear() ;
            log( "Cleared row probe." ) ;
            Thread.sleep( 100 ) ;
        }
        mainBus.clear() ;
    }
    
    private void log( String msg ) throws Exception {
        log.debug( msg ) ;
    }
    
    public static void main( String[] args ) throws Exception {
//        new PiReadinessChecker().runPreFlightCheck() ;
        new SPIProbeDriverTest().run() ;
        ObjectFactory.instance().getGPIOManager().shutdown() ;
    }
}
