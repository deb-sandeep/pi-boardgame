package com.deb.pi.boardgame.core.gpio.impl.pi;

import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.impl.AbstractPinImpl ;

public class PiInPin extends AbstractPinImpl implements InPin {

    PiInPin( int pinNum ) {
        super( pinNum ) ;
    }
}
