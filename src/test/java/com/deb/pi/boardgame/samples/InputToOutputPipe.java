package com.deb.pi.boardgame.samples;

import com.deb.pi.boardgame.core.bus.AbstractBus ;
import com.deb.pi.boardgame.core.bus.BusListener ;
import com.deb.pi.boardgame.core.bus.ParallelInputBus ;
import com.deb.pi.boardgame.core.bus.ParallelOutputBus ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;

public class InputToOutputPipe {

    public static void main( String[] args ) throws Exception {
        
        final ParallelOutputBus oBus = new ParallelOutputBus( 0, 1, 2, 3 ) ;
        final ParallelInputBus  iBus = new ParallelInputBus( 4, 5, 6, 7 ) ;
        
        //iBus.setDebounceDelay( 0 ) ;
        iBus.addBusListener( new BusListener() {

            @Override
            public void newDataAvailable( AbstractBus bus ) {
            
                System.out.println( "InputBus has new data - " + 
                                    bus.getCurrentDataAsDec() ) ;
                
                oBus.setData( bus.getCurrentDataAsDec() ) ;
            }
        } ) ;
        
        System.out.println( "Start test now..." ) ;
        Thread.sleep( 60000 ) ;
        ObjectFactory.instance().getGPIOManager().shutdown() ;
    }
}
