package com.deb.pi.boardgame.core.gpio.impl.pi;

import java.util.HashMap ;
import java.util.Map ;

import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.deb.pi.boardgame.core.gpio.InPin.PullResistanceType ;
import com.deb.pi.boardgame.core.gpio.impl.AbstractGPIOManagerImpl ;
import com.pi4j.io.gpio.GpioController ;
import com.pi4j.io.gpio.GpioFactory ;
import com.pi4j.io.gpio.GpioPinDigitalInput ;
import com.pi4j.io.gpio.GpioPinDigitalOutput ;
import com.pi4j.io.gpio.Pin ;
import com.pi4j.io.gpio.PinPullResistance ;
import com.pi4j.io.gpio.PinState ;
import com.pi4j.io.gpio.RaspiPin ;

public class PiGPIOManager extends AbstractGPIOManagerImpl {
    
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
    
    public PiGPIOManager() {
        super() ;
        pi = GpioFactory.getInstance() ;
    }
    
    private void assertIfPinUnprovisionable( int pinNum ) {
        if( PI_PIN_MAP.get( pinNum ) == null ) {
            throw new IllegalArgumentException( 
                           "Pin " + pinNum + " can't be provisioned." ) ;
        }
    }
    
    @Override
    public InPin provisionInputPin( int pinNum, PullResistanceType prType ) {
        
        assertIfPinProvisioned( pinNum ) ;
        assertIfPinUnprovisionable( pinNum ) ;

        GpioPinDigitalInput piInPin        = null ;
        PinPullResistance   pullResistance = null ;
        Pin                 piPin          = PI_PIN_MAP.get( pinNum ) ;
        
        pullResistance = (prType == PullResistanceType.DOWN) ? 
                                   PinPullResistance.PULL_DOWN :
                                   PinPullResistance.PULL_UP ;
             
        piInPin = pi.provisionDigitalInputPin( piPin, pullResistance ) ;
        piInPin.setDebounce( 500 ) ;
        
        InPin pin = new PiInPin( pinNum, piInPin ) ;
        addInputPin( pinNum, pin ) ;
        return pin ;
    }

    @Override
    public OutPin provisionOutputPin( int pinNum ) {
        
        assertIfPinProvisioned( pinNum ) ;
        assertIfPinUnprovisionable( pinNum ) ;
        
        GpioPinDigitalOutput piOutPin = null ;
        
        Pin pin = PI_PIN_MAP.get( pinNum ) ;
        piOutPin = pi.provisionDigitalOutputPin( pin, PinState.LOW ) ;
        piOutPin.setShutdownOptions( true, PinState.LOW ) ;
        
        OutPin outPin = new PiOutPin( pinNum, piOutPin ) ;
        addOutputPin( pinNum, outPin ) ;
        return outPin ;
    }
    
    public void reset() {
        
        for( OutPin pin : getOutputPins() ) {
            pi.unprovisionPin( ((PiOutPin)pin).getPiPin() ) ;
        }
        for( InPin pin : getInputPins() ) {
            pi.unprovisionPin( ((PiInPin)pin).getPiPin() ) ;
        }

        super.unprovisionAllPins() ;
    }

    @Override
    public void shutdown() {
        pi.shutdown() ;
    }
}
