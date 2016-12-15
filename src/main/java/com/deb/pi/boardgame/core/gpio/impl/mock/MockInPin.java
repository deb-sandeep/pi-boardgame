package com.deb.pi.boardgame.core.gpio.impl.mock;

import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.impl.AbstractPinImpl ;

public class MockInPin extends AbstractPinImpl implements InPin {

    MockInPin( int pinNum ) {
        super( pinNum ) ;
    }
}
