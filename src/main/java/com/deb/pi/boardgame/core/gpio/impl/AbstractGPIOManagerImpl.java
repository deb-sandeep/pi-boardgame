package com.deb.pi.boardgame.core.gpio.impl;

import java.util.Collection ;
import java.util.HashMap ;
import java.util.Map ;
import java.util.Map.Entry ;

import com.deb.pi.boardgame.core.gpio.GPIOManager ;
import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.deb.pi.boardgame.core.gpio.AbstractPin.Type ;

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
    
    protected void unprovisionAllPins() {
        outputPins.clear() ; 
        inputPins.clear() ;
    }

    @Override
    public void provisionPins( Map<Integer, Type> provCfg ) {
        for( Entry<Integer, Type> entry : provCfg.entrySet() ) {
            provisionPin( entry.getKey(), entry.getValue() ) ;
        }
    }

    @Override
    public InPin getInputPin( int pinNum ) {

        if( !isPinProvisioned( pinNum ) ) {
            provisionPin( pinNum, Type.INPUT ) ;
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
            provisionPin( pinNum, Type.OUTPUT ) ;
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
