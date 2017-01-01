package com.deb.pi.boardgame.core.bus.impl.output;

import java.io.IOException ;

import org.springframework.util.Assert ;

import com.deb.pi.boardgame.core.bus.Bus ;
import com.pi4j.io.spi.SpiChannel ;
import com.pi4j.io.spi.SpiDevice ;
import com.pi4j.io.spi.SpiFactory ;
import com.tomgibara.bits.BitVector ;

public class SPIOutputBus extends BaseHardwareOutputBus {
    
    private SpiChannel channel = null ;
    private SpiDevice bus = null ; 
    
    public SPIOutputBus( SpiChannel channel, int numWires ) 
        throws Exception {
        
        super( numWires ) ;
        
        Assert.notNull( channel, "SPI channel can't be null." ) ;
        this.channel = channel ;
        this.bus = SpiFactory.getInstance( channel ) ;
    }
    
    public SpiChannel getChannel() {
        return this.channel ;
    }

    @Override
    public Bus getSubBus( int startWireNum, int numWires ) {
        return null ;
    }

    @Override
    protected void setHardwareBusState( BitVector newState ) throws IOException {
        this.bus.write( newState.toByteArray() ) ;
    }
}
