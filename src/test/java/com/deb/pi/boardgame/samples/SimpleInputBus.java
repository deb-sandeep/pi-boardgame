package com.deb.pi.boardgame.samples;

import com.deb.pi.boardgame.core.bus.AbstractBus ;
import com.deb.pi.boardgame.core.bus.BusListener ;
import com.deb.pi.boardgame.core.bus.ParallelInputBus ;
import com.deb.pi.boardgame.core.bus.ParallelOutputBus ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;

public class SimpleInputBus {

    public static void main( String[] args ) throws Exception {
        
        ParallelInputBus iBus = new ParallelInputBus( 1 ) ;
        iBus.addBusListener( new BusListener() {
            @Override
            public void newDataAvailable( AbstractBus bus ) {
                System.out.println( "Bus has new data - " + bus.getCurrentDataAsDec() ) ;
            }
        } ) ;
        
        ParallelOutputBus oBus = new ParallelOutputBus( 0 ) ;
        oBus.setData( 1 ) ;
        
        Thread.sleep( 5000 ) ;
        System.out.println( "Time out exit" ) ;
        ObjectFactory.instance().getGPIOManager().shutdown() ;
    }
}
