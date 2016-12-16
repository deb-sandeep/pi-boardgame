package com.deb.pi.boardgame.samples;

import com.deb.pi.boardgame.core.gpio.AbstractPin.State ;
import com.deb.pi.boardgame.core.gpio.GPIOManager ;
import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;


public class SinglePinOutput {

    public static void main( String[] args ) throws Exception {
        
        GPIOManager pi = ObjectFactory.instance().getGPIOManager() ;
        OutPin pin = pi.getOutputPin( 0 ) ;
        
        pin.setState( State.HIGH ) ;
        Thread.sleep( 2000 ) ;
        pin.setState( State.LOW ) ;
        Thread.sleep( 2000 ) ;
        pin.setState( State.HIGH ) ;
        Thread.sleep( 2000 ) ;
    }
}
