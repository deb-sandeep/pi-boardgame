package com.deb.pi.boardgame.core.gpio.impl.pi;

import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.impl.AbstractInPinImpl ;
import com.deb.pi.boardgame.core.gpio.impl.AbstractPinImpl ;
import com.pi4j.io.gpio.GpioPinDigitalInput ;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent ;
import com.pi4j.io.gpio.event.GpioPinListenerDigital ;

public class PiInPin extends AbstractInPinImpl 
    implements InPin, GpioPinListenerDigital {
    
    private GpioPinDigitalInput piInPin = null ;
    
    PiInPin( int pinNum, GpioPinDigitalInput piInPin ) {
        super( pinNum ) ;
        this.piInPin = piInPin ;
        //this.piInPin.addListener( this ) ;
        super.setCurrentState( AbstractPinImpl.translateToState( piInPin.getState() ) );
    }

    public GpioPinDigitalInput getPiPin() {
        return this.piInPin ;
    }
    
    @Override
    public void handleGpioPinDigitalStateChangeEvent(
                                    GpioPinDigitalStateChangeEvent event ) {
        
        State newState = AbstractPinImpl.translateToState( event.getState() ) ;
        super.setCurrentState( newState ) ;
    }
    
    @Override
    public State getState() {
        System.out.println( "In pin state = " + piInPin.getState() ) ; 
        super.setCurrentState( AbstractPinImpl.translateToState( piInPin.getState() ) ) ;
        return super.getState() ;
    }
}
