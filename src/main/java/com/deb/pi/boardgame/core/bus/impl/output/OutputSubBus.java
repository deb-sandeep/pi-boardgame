package com.deb.pi.boardgame.core.bus.impl.output;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.tomgibara.bits.BitVector ;

public class OutputSubBus extends OutputBusBase implements OutputBus {

    private OutputBus parentBus             = null ;
    private int       parentBusStartWireNum = 0 ;
    
    public OutputSubBus( OutputBus parentBus, 
                         int parentBusStartWireNum, int numWires ) {
        super( numWires ) ;
        this.parentBus = parentBus ;
        this.parentBusStartWireNum = parentBusStartWireNum ;
    }

    @Override
    public void write( BitVector wireStates, int startWire, int numWires ) {
        parentBus.write( wireStates, parentBusStartWireNum+startWire, numWires ) ;
        updateNewBusState( wireStates, startWire, numWires ) ;
    }
}
