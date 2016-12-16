package com.deb.pi.boardgame.core.gpio.impl.pi;

import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.impl.AbstractPinImpl ;
import com.pi4j.io.gpio.GpioPinDigitalInput ;
import com.pi4j.io.gpio.GpioPinDigitalOutput ;

public class PiInPin extends AbstractPinImpl implements InPin {

    PiInPin( int pinNum ) {
        super( pinNum ) ;
    }

    public GpioPinDigitalInput getPiPin() {
        return null ;
    }
}
