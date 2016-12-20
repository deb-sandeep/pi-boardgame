package com.deb.pi.boardgame.core.bus;

import java.util.BitSet ;
import java.util.HashSet ;
import java.util.Set ;

import com.deb.pi.boardgame.core.gpio.AbstractPin ;

public abstract class AbstractBus {
    
    private AbstractPin[] pins = null ;
    private Set<BusListener> listeners = new HashSet<BusListener>() ;
    
    protected AbstractBus() {
    }
    
    protected AbstractBus( AbstractPin[] pins ) {
        setPins( pins ) ;
    }
    
    protected void setPins( AbstractPin[] pins ) {
        this.pins = pins ;
        if( pins == null || pins.length == 0 ) {
            throw new IllegalArgumentException( "Num pins for a bus can't be 0" ) ;
        }
    }
    
    public AbstractPin[] getPins() {
        return this.pins ;
    }
    
    protected void checkDataSize( int data ) {
        if( data > getMaxDataSize() ) {
            throw new IllegalArgumentException( 
                                 "Bus size is insufficient to carry " + data + 
                                 ". Max data size = " + getMaxDataSize() ) ;
        }
    }
    
    protected void broadcastNewDataOnBus( int data ) {
        for( BusListener l : listeners ) {
            l.newDataAvailable( this ) ;
        }        
    }
    
    public void addBusListener( BusListener listener ) {
        this.listeners.add( listener ) ;
    }
    
    public void removeBusListener( BusListener listner ) {
        this.listeners.remove( listner ) ;
    }
    
    public int getNumPins() {
        return pins.length ;
    }
    
    public int getMaxDataSize() {
        return (int)Math.pow( 2, getNumPins() ) ;
    }
    
    public abstract int getCurrentDataAsDec() ;
    
    public abstract BitSet getCurrentDataAsBin() ;
}
