package com.deb.pi.boardgame;

import com.deb.pi.boardgame.core.gpio.AbstractPin.State ;
import com.deb.pi.boardgame.core.gpio.GPIOManager ;
import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.deb.pi.boardgame.core.gpio.AbstractPin.Type ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;


public class App {

    public static void main( String[] args ) throws Exception {
        
        GPIOManager gpioManager = ObjectFactory.instance().getGPIOManager() ;
        gpioManager.provisionPin( 0, Type.OUTPUT ) ;
        
        OutPin pin = gpioManager.getOutputPin( 0 ) ;
        
        pin.setState( State.HIGH ) ;
        Thread.sleep( 2000 ) ;
        pin.setState( State.LOW ) ;
        Thread.sleep( 2000 ) ;
        pin.setState( State.HIGH ) ;
        Thread.sleep( 2000 ) ;
    }
}
