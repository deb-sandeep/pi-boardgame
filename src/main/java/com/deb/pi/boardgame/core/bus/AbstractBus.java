package com.deb.pi.boardgame.core.bus;

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
    
    protected int[] convertToBin( int data ) {
        
        checkDataSize( data ) ;
        
        int[] binData = new int[getNumPins()] ;
        String binaryStr = Integer.toBinaryString( data ) ;
        
        for( int i=0; i<binaryStr.length(); i++ ) {
            char bit = binaryStr.charAt( binaryStr.length()-1-i ) ;
            binData[i] = bit=='0' ? 0 : 1 ;
        }
        
        return binData ;
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
    
    public abstract int[] getCurrentDataAsBin() ;
}
