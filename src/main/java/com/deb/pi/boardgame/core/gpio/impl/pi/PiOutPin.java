package com.deb.pi.boardgame.core.gpio.impl.pi;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.deb.pi.boardgame.core.gpio.impl.AbstractPinImpl ;
import com.pi4j.io.gpio.GpioPinDigitalOutput ;

public class PiOutPin extends AbstractPinImpl implements OutPin {

    static Logger log = Logger.getLogger( PiOutPin.class ) ;
    
    private GpioPinDigitalOutput piOutPin = null ;
    
    PiOutPin( int pinNum, GpioPinDigitalOutput piOutPin ) {
        super( pinNum ) ;
        this.piOutPin = piOutPin ;
    }

    @Override
    public void setState( State state ) {
        piOutPin.setState( translateToPinState( state ) ) ;
    }
    
    @Override
    public State getState() {
        return translateToState( piOutPin.getState() ) ;
    }
    
    public GpioPinDigitalOutput getPiPin() {
        return this.piOutPin ;
    }
}
