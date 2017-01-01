package com.deb.pi.boardgame.samples;

import java.io.IOException ;

import com.deb.pi.boardgame.core.bus.impl.output.BaseHardwareOutputBus ;
import com.tomgibara.bits.BitVector ;

class TestHWOutputBus extends BaseHardwareOutputBus {

    protected TestHWOutputBus( int numWires ) {
        super( numWires ) ;
        startDaemon() ;
    }

    @Override
    protected void setHardwareBusState( BitVector newState )
            throws IOException {
        System.out.println( "Writing to HW " + newState + "\n" ) ;
    }
}
