package com.deb.pi.boardgame.samples;

import com.deb.pi.boardgame.core.bus.AbstractBus ;
import com.deb.pi.boardgame.core.bus.BusListener ;
import com.deb.pi.boardgame.core.bus.InputBus ;
import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;

public class SimpleInputBus {

    public static void main( String[] args ) throws Exception {
        
        InputBus iBus = new InputBus( 1 ) ;
        iBus.addBusListener( new BusListener() {
            @Override
            public void newDataAvailable( AbstractBus bus ) {
                System.out.println( "Bus has new data - " + bus.getCurrentDataAsDec() ) ;
            }
        } ) ;
        
        OutputBus oBus = new OutputBus( 0 ) ;
        oBus.setData( 1 ) ;
        
        Thread.sleep( 5000 ) ;
        System.out.println( "Time out exit" ) ;
        ObjectFactory.instance().getGPIOManager().shutdown() ;
    }
}
