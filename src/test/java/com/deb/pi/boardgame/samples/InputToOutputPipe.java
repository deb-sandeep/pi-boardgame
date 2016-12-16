package com.deb.pi.boardgame.samples;

import com.deb.pi.boardgame.core.bus.AbstractBus ;
import com.deb.pi.boardgame.core.bus.BusListener ;
import com.deb.pi.boardgame.core.bus.InputBus ;
import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;

public class InputToOutputPipe {

    public static void main( String[] args ) throws Exception {
        
        final OutputBus oBus = new OutputBus( 0, 1, 2, 3 ) ;
        final InputBus  iBus = new InputBus( 4, 5, 6, 7 ) ;
        
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
