package com.deb.pi.boardgame.core.gpio.impl.mock;

import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;
import java.util.Map.Entry ;

import com.deb.pi.boardgame.core.gpio.AbstractPin.State ;
import com.deb.pi.boardgame.core.gpio.AbstractPin.Type ;
import com.deb.pi.boardgame.core.gpio.impl.pi.PiOutPin ;
import com.deb.pi.boardgame.core.gpio.GPIOManager ;
import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.pi4j.io.gpio.GpioController ;
import com.pi4j.io.gpio.GpioFactory ;
import com.pi4j.io.gpio.GpioPinDigitalInput ;
import com.pi4j.io.gpio.GpioPinDigitalOutput ;
import com.pi4j.io.gpio.Pin ;
import com.pi4j.io.gpio.PinState ;

public class MockGPIOManager implements GPIOManager {
    
    private static final Map<Integer, Integer> PI_PIN_MAP = new HashMap<Integer, Integer>() ;
    static {
        PI_PIN_MAP.put( 0, 00 ) ;
        PI_PIN_MAP.put( 1, 01 ) ;
        PI_PIN_MAP.put( 2, 02 ) ;
        PI_PIN_MAP.put( 3, 03 ) ;
        PI_PIN_MAP.put( 4, 04 ) ;
        PI_PIN_MAP.put( 5, 05 ) ;
        PI_PIN_MAP.put( 6, 06 ) ;
        PI_PIN_MAP.put( 7, 07 ) ;
        // NOTE: Pin 8 and 9 are not being configured as direct pins but are
        // configured separately as I2C pins.
        //PI_PIN_MAP.put( 8, 08 ) ;
        //PI_PIN_MAP.put( 9, 09 ) ;
        PI_PIN_MAP.put( 10, 10 ) ;
        PI_PIN_MAP.put( 11, 11 ) ;
        PI_PIN_MAP.put( 12, 12 ) ;
        PI_PIN_MAP.put( 13, 13 ) ;
        PI_PIN_MAP.put( 14, 14 ) ;
        PI_PIN_MAP.put( 15, 15 ) ;
        PI_PIN_MAP.put( 16, 16 ) ;
        PI_PIN_MAP.put( 17, 17 ) ;
        PI_PIN_MAP.put( 18, 18 ) ;
        PI_PIN_MAP.put( 19, 19 ) ;
        PI_PIN_MAP.put( 20, 20 ) ;
        PI_PIN_MAP.put( 21, 21 ) ;
        PI_PIN_MAP.put( 22, 22 ) ;
        PI_PIN_MAP.put( 23, 23 ) ;
        PI_PIN_MAP.put( 24, 24 ) ;
    }
    
    private Map<Integer, MockOutPin> outputPins = null ;
    private Map<Integer, MockInPin>  inputPins = null ;
    
    public MockGPIOManager() {
        
        outputPins = new HashMap<Integer, MockOutPin>() ;
        inputPins  = new HashMap<Integer, MockInPin>() ;
    }

    private boolean isPinProvisioned( int pinNum ) {
        
        if( inputPins.containsKey( pinNum ) ) {
            return true ;
        }
        if( outputPins.containsKey( pinNum ) ) {
            return true ;
        }
        
        return false ;
    }
    
    @Override
    public void provisionPin( int pinNum, Type pinType ) {
        
        if( isPinProvisioned( pinNum ) ) {
            throw new IllegalArgumentException( 
                           "Pin " + pinNum + " is already provisioned." ) ;
        }
        
        if( PI_PIN_MAP.get( pinNum ) == null ) {
            throw new IllegalArgumentException( 
                           "Pin " + pinNum + " can't be provisioned." ) ;
        }
        
        if( pinType == Type.OUTPUT ) {
            MockOutPin outPin = new MockOutPin( pinNum, State.LOW ) ;
            outputPins.put( pinNum, outPin ) ;
        }
        else {
            MockInPin inPin = new MockInPin( pinNum ) ;
            inputPins.put( pinNum, inPin ) ;
        }
    }

    @Override
    public void provisionPins( Map<Integer, Type> provCfg ) {
        
        for( Entry<Integer, Type> entry : provCfg.entrySet() ) {
            provisionPin( entry.getKey(), entry.getValue() ) ;
        }
    }

    @Override
    public InPin getInputPin( int pinNum ) {
        if( !inputPins.containsKey( pinNum ) ) {
            throw new IllegalArgumentException( 
                 "Pin " + pinNum + " has not been provisioned as input pin." ) ;
        }
        return inputPins.get( pinNum ) ;
    }

    @Override
    public OutPin getOutputPin( int pinNum ) {
        if( !outputPins.containsKey( pinNum ) ) {
            throw new IllegalArgumentException( 
                 "Pin " + pinNum + " has not been provisioned as output pin." ) ;
        }
        return outputPins.get( pinNum ) ;
    }
}
