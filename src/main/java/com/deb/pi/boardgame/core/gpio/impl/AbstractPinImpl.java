package com.deb.pi.boardgame.core.gpio.impl;

import com.deb.pi.boardgame.core.gpio.AbstractPin ;
import com.pi4j.io.gpio.PinState ;

public abstract class AbstractPinImpl implements AbstractPin {
    
    private int pinNum = 0 ;
    
    protected AbstractPinImpl( int pinNum ) {
        this.pinNum = pinNum ;
    }
    
    public int getPinNum() {
        return this.pinNum ;
    }

    protected static PinState translateToPinState( State state ) {
        return state == State.HIGH ? PinState.HIGH : PinState.LOW ;
    }
    
    protected static State translateToState( PinState pinState ) {
        return pinState == PinState.HIGH ? State.HIGH : State.LOW ;
    }
}
