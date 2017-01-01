package com.deb.pi.boardgame.samples;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.impl.output.SPIOutputBus ;
import com.pi4j.io.spi.SpiChannel ;

public class SPIOutputBusExample {

    private OutputBus bus = null ;
    
    public SPIOutputBusExample() throws Exception {
        bus = new SPIOutputBus( SpiChannel.CS0, 48 ) ;
        System.out.println( "Bus created." ) ;
    }
    
    public void run() throws Exception {
        System.out.println( "Running." ) ;
        System.out.println( "Bus cleared." ) ;
        bus.clear() ;
        for( int i=0; i<bus.size(); i++ ) {
            System.out.println( "Writing wire " + i + " to HIGH." ) ;
            bus.write( i, true ) ;
            Thread.sleep( 200 ) ;
        }
        bus.clear() ;
    }
    
    public static void main( String[] args ) throws Exception {
        System.out.println( "Starting program." ) ;
        new SPIOutputBusExample().run() ;
    }
}
