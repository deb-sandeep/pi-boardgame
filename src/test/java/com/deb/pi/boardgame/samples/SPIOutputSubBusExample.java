package com.deb.pi.boardgame.samples;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.impl.output.SPIOutputBus ;
import com.pi4j.io.spi.SpiChannel ;

public class SPIOutputSubBusExample {

    private OutputBus mainBus = null ;
    private OutputBus[] subBuses = new OutputBus[4] ;
    
    public SPIOutputSubBusExample() throws Exception {
        mainBus = new SPIOutputBus( SpiChannel.CS0, 40 ) ;
        System.out.println( "Bus created." ) ;
        for( int i=0; i<subBuses.length; i++ ) {
            int startWireNum = i*10 ;
            subBuses[i] = (OutputBus)mainBus.getSubBus( startWireNum, 10 ) ;
            System.out.println( "Creating sub bus " + i + "." ) ;
            System.out.println( "  Start wire = " + startWireNum + ", num wires 10." ) ;
        }
    }
    
    public void run() throws Exception {
        mainBus.clear() ;
        for( int i=0; i<1024; i++ ) {
            for( OutputBus b : subBuses ) {
                b.write( i ) ;
            }
            Thread.sleep( 20 ) ;
        }
        mainBus.clear() ;
    }
    
    public static void main( String[] args ) throws Exception {
        System.out.println( "Starting program." ) ;
        new SPIOutputSubBusExample().run() ;
    }
}
