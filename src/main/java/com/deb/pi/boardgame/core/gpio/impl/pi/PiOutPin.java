package com.deb.pi.boardgame.core.gpio.impl.pi;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.pi4j.io.gpio.GpioPinDigitalOutput ;
import com.pi4j.io.gpio.PinState ;

public class PiOutPin implements OutPin {

    private static Logger log = Logger.getLogger( PiOutPin.class ) ;
    
    private GpioPinDigitalOutput piOutPin = null ;
    
    PiOutPin( GpioPinDigitalOutput piOutPin ) {
        this.piOutPin = piOutPin ;
    }

    @Override
    public void setState( State state ) {
        log.debug( "Setting pin " + piOutPin.getName() + " to " + state ) ;
        piOutPin.setState( state == State.HIGH ? PinState.HIGH : PinState.LOW ) ;
    }
    
    @Override
    public State getState() {
        return piOutPin.getState() == PinState.HIGH ? State.HIGH : State.LOW ;
    }
}
