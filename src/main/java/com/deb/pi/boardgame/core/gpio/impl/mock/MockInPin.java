package com.deb.pi.boardgame.core.gpio.impl.mock;

import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.impl.AbstractInPinImpl ;

public class MockInPin extends AbstractInPinImpl implements InPin {

    MockInPin( int pinNum ) {
        super( pinNum ) ;
    }

    public void setCurrentState( State state ) {
        super.setCurrentState( state ) ;
    }
}
