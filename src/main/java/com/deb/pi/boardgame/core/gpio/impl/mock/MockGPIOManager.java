package com.deb.pi.boardgame.core.gpio.impl.mock;

import java.util.HashMap ;
import java.util.Map ;

import com.deb.pi.boardgame.core.gpio.AbstractPin.State ;
import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.InPin.PullResistanceType ;
import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.deb.pi.boardgame.core.gpio.impl.AbstractGPIOManagerImpl ;

public class MockGPIOManager extends AbstractGPIOManagerImpl {
    
    private static final Map<Integer, Integer> PI_PIN_MAP = 
                                               new HashMap<Integer, Integer>() ;
    
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
        
        InPin inPin = new MockInPin( pinNum ) ;
        addInputPin( pinNum, inPin ) ;

        return inPin ;
    }

    @Override
    public OutPin provisionOutputPin( int pinNum ) {
        
        assertIfPinProvisioned( pinNum ) ;
        assertIfPinUnprovisionable( pinNum ) ;
        
        OutPin outPin = new MockOutPin( pinNum, State.LOW ) ;
        addOutputPin( pinNum, outPin ) ;
        
        return outPin ;
    }
    
    public void reset() {
        super.unprovisionAllPins() ;
    }
    
    public void shutdown() {
        reset() ;
    }
}
