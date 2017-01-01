package com.deb.pi.boardgame.core.bus;

import com.tomgibara.bits.BitVector ;

public interface Bus {

    public int getSize() ;
    
    public Bus getSubBus( int startWireNum, int numWires ) ;
    
    public void addListener( BusListener listener ) ;
    
    public void removeListener( BusListener listener ) ;
    
    public boolean isHigh( int wireNum ) ;
    
    public boolean isLow( int wireNum ) ;
    
    public boolean getState( int wireNum ) ;
    
    public long getStateAsLong() ;

    public BitVector getState() ;
}
