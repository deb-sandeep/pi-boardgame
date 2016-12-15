package com.deb.pi.boardgame.core.gpio.impl.pi;

import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;
import java.util.Map.Entry ;

import com.deb.pi.boardgame.core.gpio.GPIOManager ;
import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.deb.pi.boardgame.core.gpio.AbstractPin.Type ;
import com.pi4j.io.gpio.GpioController ;
import com.pi4j.io.gpio.GpioFactory ;
import com.pi4j.io.gpio.GpioPinDigitalInput ;
import com.pi4j.io.gpio.GpioPinDigitalOutput ;
import com.pi4j.io.gpio.Pin ;
import com.pi4j.io.gpio.PinState ;
import com.pi4j.io.gpio.RaspiPin ;

public class PiGPIOManager implements GPIOManager {
    
    private static final Map<Integer, Pin> PI_PIN_MAP = new HashMap<Integer, Pin>() ;
    static {
        PI_PIN_MAP.put( 0, RaspiPin.GPIO_00 ) ;
        PI_PIN_MAP.put( 1, RaspiPin.GPIO_01 ) ;
        PI_PIN_MAP.put( 2, RaspiPin.GPIO_02 ) ;
        PI_PIN_MAP.put( 3, RaspiPin.GPIO_03 ) ;
        PI_PIN_MAP.put( 4, RaspiPin.GPIO_04 ) ;
        PI_PIN_MAP.put( 5, RaspiPin.GPIO_05 ) ;
        PI_PIN_MAP.put( 6, RaspiPin.GPIO_06 ) ;
        PI_PIN_MAP.put( 7, RaspiPin.GPIO_07 ) ;
        // NOTE: Pin 8 and 9 are not being configured as direct pins but are
        // configured separately as I2C pins.
        //PI_PIN_MAP.put( 8, RaspiPin.GPIO_08 ) ;
        //PI_PIN_MAP.put( 9, RaspiPin.GPIO_09 ) ;
        PI_PIN_MAP.put( 10, RaspiPin.GPIO_10 ) ;
        PI_PIN_MAP.put( 11, RaspiPin.GPIO_11 ) ;
        PI_PIN_MAP.put( 12, RaspiPin.GPIO_12 ) ;
        PI_PIN_MAP.put( 13, RaspiPin.GPIO_13 ) ;
        PI_PIN_MAP.put( 14, RaspiPin.GPIO_14 ) ;
        PI_PIN_MAP.put( 15, RaspiPin.GPIO_15 ) ;
        PI_PIN_MAP.put( 16, RaspiPin.GPIO_16 ) ;
        PI_PIN_MAP.put( 17, RaspiPin.GPIO_17 ) ;
        PI_PIN_MAP.put( 18, RaspiPin.GPIO_18 ) ;
        PI_PIN_MAP.put( 19, RaspiPin.GPIO_19 ) ;
        PI_PIN_MAP.put( 20, RaspiPin.GPIO_20 ) ;
        PI_PIN_MAP.put( 21, RaspiPin.GPIO_21 ) ;
        PI_PIN_MAP.put( 22, RaspiPin.GPIO_22 ) ;
        PI_PIN_MAP.put( 23, RaspiPin.GPIO_23 ) ;
        PI_PIN_MAP.put( 24, RaspiPin.GPIO_24 ) ;
    }
    
    private GpioController pi = null ;
    private Map<Integer, PiOutPin> outputPins = null ;
    private Map<Integer, PiInPin>     inputPins = null ;
    
    public PiGPIOManager() {
        
        pi = GpioFactory.getInstance() ;
        outputPins = new HashMap<Integer, PiOutPin>() ;
        inputPins = new HashMap<Integer, PiInPin>() ;
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
        
        Pin pin = PI_PIN_MAP.get( pinNum ) ;
        if( pin == null ) {
            throw new IllegalArgumentException( 
                           "Pin " + pinNum + " can't be provisioned." ) ;
        }
        
        GpioPinDigitalOutput outPin = null ;
        GpioPinDigitalInput  inPin  = null ;
        
        if( pinType == Type.OUTPUT ) {
            outPin = pi.provisionDigitalOutputPin( pin, PinState.LOW ) ;
            outPin.setShutdownOptions( true, PinState.LOW ) ;
            
            outputPins.put( pinNum, new PiOutPin( outPin ) ) ;
        }
        else {
            // TODO: Understand how to configure a digital input.
            throw new IllegalStateException( "To be implemented" ) ;
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
