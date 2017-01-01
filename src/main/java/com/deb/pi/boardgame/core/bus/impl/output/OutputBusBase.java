package com.deb.pi.boardgame.core.bus.impl.output;

import java.io.IOException ;
import java.math.BigInteger ;

import com.deb.pi.boardgame.core.bus.Bus ;
import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.impl.BusBase ;
import com.tomgibara.bits.BitVector ;

public abstract class OutputBusBase extends BusBase implements OutputBus {
    
    protected OutputBusBase( int numWires ) {
        super( numWires ) ;
    }
    
    @Override
    public Bus getSubBus( int startWireNum, int numWires ) {
        return new OutputSubBus( this, startWireNum, numWires ) ;
    }

    @Override
    public void write( BitVector wireStates ) throws IOException {
        write( wireStates, 0, super.getSize() ) ;
    }

    @Override
    public void write( long state ) throws IOException {
        write( BitVector.fromBigInteger( BigInteger.valueOf( state ), 
               super.getSize() ) ) ;
    }

    @Override
    public void write( int wireNum, boolean state ) throws IOException {
        write( state ? 1 : 0, wireNum, 1 ) ;
    }

    @Override
    public void write( long state, int startWire, int numWires ) {
        BitVector bv = BitVector.fromBigInteger( BigInteger.valueOf( state ), 
                                                 numWires ) ;
        write( bv, startWire, numWires ) ;
    }
    
    @Override
    public void write( BitVector wireStates, int startWire ) {
        write( wireStates, startWire, super.getSize()-startWire ) ;
    }
}
