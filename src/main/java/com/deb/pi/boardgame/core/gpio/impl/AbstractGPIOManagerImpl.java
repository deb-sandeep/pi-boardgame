package com.deb.pi.boardgame.core.gpio.impl;

import java.util.Collection ;
import java.util.HashMap ;
import java.util.Map ;

import com.deb.pi.boardgame.core.gpio.GPIOManager ;
import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.InPin.PullResistanceType ;
import com.deb.pi.boardgame.core.gpio.OutPin ;

public abstract class AbstractGPIOManagerImpl implements GPIOManager {

    private Map<Integer, OutPin> outputPins = null ;
    private Map<Integer, InPin>  inputPins = null ;
    
    protected AbstractGPIOManagerImpl() {
        
        outputPins = new HashMap<Integer, OutPin>() ;
        inputPins = new HashMap<Integer, InPin>() ;
    }
    
    protected void addOutputPin( int pinNum, OutPin pin ) {
        outputPins.put( pinNum, pin ) ;
    }
    
    protected void addInputPin( int pinNum, InPin pin ) {
        inputPins.put( pinNum, pin ) ;
    }

    protected boolean isPinProvisioned( int pinNum ) {
        
        if( inputPins.containsKey( pinNum ) || 
            outputPins.containsKey( pinNum ) ) {
            return true ;
        }
        return false ;
    }
    
    protected void assertIfPinProvisioned( int pinNum ) {
        if( isPinProvisioned( pinNum ) ) {
            throw new IllegalArgumentException( 
                           "Pin " + pinNum + " is already provisioned." ) ;
        }
    }
    
    protected void unprovisionAllPins() {
        outputPins.clear() ; 
        for( InPin inPin : inputPins.values() ) {
            inPin.removeAllListeners() ;
        }
        inputPins.clear() ;
    }

    @Override
    public InPin getInputPin( int pinNum ) {

        if( !isPinProvisioned( pinNum ) ) {
            provisionInputPin( pinNum, PullResistanceType.DOWN  ) ;
        }
        if( !inputPins.containsKey( pinNum ) ) {
            throw new IllegalArgumentException( 
                 "Pin " + pinNum + " has not been provisioned as input pin." ) ;
        }
        return inputPins.get( pinNum ) ;
    }

    @Override
    public OutPin getOutputPin( int pinNum ) {
        
        if( !isPinProvisioned( pinNum ) ) {
            provisionOutputPin( pinNum) ;
        }
        
        if( !outputPins.containsKey( pinNum ) ) {
            throw new IllegalArgumentException( 
                 "Pin " + pinNum + " has not been provisioned as output pin." ) ;
        }
        
        return outputPins.get( pinNum ) ;
    }
    
    protected Collection<OutPin> getOutputPins() {
        return outputPins.values() ;
    }
    
    protected Collection<InPin> getInputPins() {
        return inputPins.values() ;
    }
}
