package com.deb.pi.boardgame.core.gpio;

import java.util.Map ;

import com.deb.pi.boardgame.core.gpio.AbstractPin.Type ;

public interface GPIOManager {

    public void provisionPins( Map<Integer, Type> provisioningConfig ) ;
    
    public void provisionPin( int pinNum, Type pinType ) ;
    
    public InPin getInputPin( int pinNum ) ;
    
    public OutPin getOutputPin( int pinNum ) ;
}
