package com.deb.pi.boardgame.junit.bus;

import static org.hamcrest.CoreMatchers.equalTo ;
import static org.junit.Assert.assertThat ;

import java.io.IOException ;
import java.math.BigInteger ;

import org.junit.Before ;
import org.junit.Test ;

import com.deb.pi.boardgame.core.bus.Bus ;
import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.impl.output.OutputBusBase ;
import com.deb.pi.boardgame.core.bus.impl.output.OutputSubBus ;
import com.tomgibara.bits.BitVector ;

public class OutputBusTest {

    private class SimpleOutputBus extends OutputBusBase {

        public SimpleOutputBus() {
            super( 8 ) ;
        }
        
        @Override
        public Bus getSubBus( int startWireNum, int numWires ) { 
            return new OutputSubBus( this, startWireNum, numWires ) ;
        }

        @Override
        public void write( BitVector wireStates, int startWire, int numWires ) {
            updateNewBusState( wireStates, startWire, numWires ) ;
        }
    }
    
    private SimpleOutputBus bus = null ;
    
    @Before
    public void beforeTest() {
        bus = new SimpleOutputBus() ;
    }
    
    @Test
    public void simpleOutput() throws IOException {
        
        assertThat( 0, equalTo( (int)bus.getStateAsLong() ) ) ;
        
        bus.write( 3 ) ;
        assertThat( 3, equalTo( (int)bus.getStateAsLong() ) ) ;
        
        bus.write( 2, true ) ;
        assertThat( 7, equalTo( (int)bus.getStateAsLong() ) ) ;
        
        bus.write( BitVector.fromBigInteger( BigInteger.valueOf( 8 ), 8 ) ) ;
        assertThat( 8, equalTo( (int)bus.getStateAsLong() ) ) ;
    }
    
    @Test
    public void subBus() throws Exception {
        
        OutputBus sb1 = (OutputBus)bus.getSubBus( 4, 4 ) ;
        sb1.write( 4 ) ;
        
        OutputBus sb2 = (OutputBus)bus.getSubBus( 0, 4 ) ;
        sb2.write( 3 ) ;
        
        assertThat( 0b01000011, equalTo( (int)bus.getStateAsLong() ) ) ;
    }
    
    @Test
    public void subSubBus() throws Exception {
        
        OutputBus sb1 = (OutputBus)bus.getSubBus( 4, 4 ) ;
        OutputBus sb2 = (OutputBus)sb1.getSubBus( 2, 2 ) ;
        OutputBus sb3 = (OutputBus)bus.getSubBus( 0, 2 ) ;
        
        sb2.write( 2 ) ;
        sb3.write( 3 ) ;
        
        assertThat( 0b10000011, equalTo( (int)bus.getStateAsLong() ) ) ;
        assertThat( 0b00001000, equalTo( (int)sb1.getStateAsLong() ) ) ;
        assertThat( 0b00000010, equalTo( (int)sb2.getStateAsLong() ) ) ;
        assertThat( 0b00000011, equalTo( (int)sb3.getStateAsLong() ) ) ;
    }
}
