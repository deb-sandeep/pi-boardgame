package com.deb.pi.boardgame.core.gpio.impl.mock;

import com.deb.pi.boardgame.core.gpio.InPin ;

public class MockInPin implements InPin {

    private int pinNum = 0 ;
    
    MockInPin( int pinNum ) {
        this.pinNum = pinNum ;
    }
}
