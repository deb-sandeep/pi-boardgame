package com.deb.pi.boardgame.core.bus;

import java.io.IOException ;

import com.tomgibara.bits.BitVector ;

public interface OutputBus extends Bus {

    public void write( BitVector wireStates ) throws IOException ;
    
    public void write( long state ) throws IOException  ;
    
    public void write( int wireNum, boolean state ) throws IOException  ;
    
    public void write( BitVector wireStates, int startWire ) throws IOException ;
    
    public void write( BitVector wireStates, int startWire, int numWires ) throws IOException ;
    
    public void write( long state, int startWire, int numWires ) throws IOException ;
    
    public void setHigh() throws IOException ;
    
    public void clear() throws IOException ;
}
