package com.deb.pi.boardgame.samples;

import com.deb.pi.boardgame.core.bus.Bus ;
import com.deb.pi.boardgame.core.bus.BusListener ;
import com.deb.pi.boardgame.core.bus.impl.old.ParallelInputBus ;
import com.deb.pi.boardgame.core.bus.impl.old.ParallelOutputBus ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;

public class SimpleInputBus {

    public static void main( String[] args ) throws Exception {
        
        ParallelInputBus iBus = new ParallelInputBus( 1 ) ;
        iBus.addBusListener( new BusListener() {
            @Override
            public void stateChanged( Bus bus ) {
                System.out.println( "Bus has new data - " + bus.getStateAsLong() ) ;
            }
        } ) ;
        
        ParallelOutputBus oBus = new ParallelOutputBus( 0 ) ;
        oBus.setData( 1 ) ;
        
        Thread.sleep( 5000 ) ;
        System.out.println( "Time out exit" ) ;
        ObjectFactory.instance().getGPIOManager().shutdown() ;
    }
}
