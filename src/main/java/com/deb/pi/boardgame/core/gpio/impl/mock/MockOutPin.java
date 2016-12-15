package com.deb.pi.boardgame.core.gpio.impl.mock;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.gpio.OutPin ;

public class MockOutPin implements OutPin {
    
    private static Logger log = Logger.getLogger( MockOutPin.class ) ;

    private int pinNum = 0 ;
    private State state = null ;
    
    MockOutPin( int pinNum, State state ) {
        this.pinNum = pinNum ;
        this.state = state ;
    }

    @Override
    public void setState( State state ) {
        log.debug( "Setting pin " + pinNum + " to " + state ) ;
        this.state = state ;
    }
    
    @Override
    public State getState() {
        return this.state ;
    }
}
