package com.deb.pi.boardgame.core.bus.impl ;

import java.util.Set ;
import java.util.TreeSet ;

import com.deb.pi.boardgame.core.bus.Bus ;
import com.deb.pi.boardgame.core.bus.BusListener ;
import com.tomgibara.bits.BitVector ;

public abstract class BusBase implements Bus {

    private Set<BusListener> listeners = new TreeSet<BusListener>() ;
    private BitVector wireState = null ;
    
    protected BusBase( int numWires ) {
        wireState = new BitVector( numWires ) ;
    }
    
    protected void updateNewBusState( BitVector newState, 
                                      int startWireNum, 
                                      int numWires ) {

        BitVector bv = wireState.mutableCopy() ;
        for( int i=0; i<numWires; i++ ) {
            bv.setBit( i+startWireNum, newState.getBit( i ) );
        }
        setNewBusState( bv ) ;
    }
    
    protected void setNewBusState( BitVector newState ) {
        
        if( newState.size() != this.wireState.size() ) {
            throw new IllegalArgumentException( "Wire state size mismatch." ) ;
        }
        
        wireState.readFrom( newState.openReader() ) ;
        for( BusListener l : listeners ) {
            l.stateChanged( this ) ;
        }
    }
    
    @Override
    public void addListener( BusListener listener ) {
        listeners.add( listener ) ;
    }

    @Override
    public void removeListener( BusListener listener ) {
        listeners.remove( listener ) ;
    }

    @Override
    public int size() {
        return wireState.size() ;
    }

    @Override
    public boolean isHigh( int wireNum ) {
        return wireState.getBit( wireNum ) ;
    }

    @Override
    public boolean isLow( int wireNum ) {
        return !isHigh( wireNum ) ;
    }

    @Override
    public boolean getState( int wireNum ) {
        return isHigh( wireNum ) ;
    }

    @Override
    public long getStateAsLong() {
        return wireState.getBits( 0, size() ) ;
    }

    @Override
    public BitVector getState() {
        return wireState.immutableCopy() ;
    }
}
