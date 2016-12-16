package com.deb.pi.boardgame.core.gpio.impl;

import java.util.HashSet ;
import java.util.Set ;

import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.InPinListener ;

public abstract class AbstractInPinImpl extends AbstractPinImpl
    implements InPin {

    private State curState = null ;
    private Set<InPinListener> listeners = new HashSet<InPinListener>() ;

    protected AbstractInPinImpl( int pinNum ) {
        super( pinNum ) ;
    }

    protected void setCurrentState( State state ) {
        this.curState = state ;
        for( InPinListener l : listeners ) {
            l.stateChanged( this ) ;
        }
    }
    
    @Override
    public State getState() {
        return this.curState ;
    }
    
    @Override
    public void addInputPinListener( InPinListener listener ) {
        this.listeners.add( listener ) ;
    }
    
    @Override
    public void removeAllListeners() {
        this.listeners.clear() ;
    }
}
