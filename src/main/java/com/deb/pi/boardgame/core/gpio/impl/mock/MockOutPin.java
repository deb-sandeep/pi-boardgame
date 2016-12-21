package com.deb.pi.boardgame.core.gpio.impl.mock;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.deb.pi.boardgame.core.gpio.impl.AbstractPinImpl ;

public class MockOutPin extends AbstractPinImpl implements OutPin {
    
    static Logger log = Logger.getLogger( MockOutPin.class ) ;

    private State state = null ;
    
    MockOutPin( int pinNum, State state ) {
        super( pinNum ) ;
        this.state = state ;
    }

    @Override
    public void setState( State state ) {
        this.state = state ;
    }
    
    @Override
    public State getState() {
        return this.state ;
    }
}
