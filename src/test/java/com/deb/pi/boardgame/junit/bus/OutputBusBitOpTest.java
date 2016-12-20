package com.deb.pi.boardgame.junit.bus;

import static org.junit.Assert.assertEquals ;

import org.junit.Before ;
import org.junit.Test ;

import com.deb.pi.boardgame.core.bus.ParallelOutputBus ;
import com.deb.pi.boardgame.core.gpio.GPIOManager ;
import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;
import com.deb.pi.boardgame.core.util.PiUtils ;

public class OutputBusBitOpTest {
    
    private GPIOManager pi = null ;
    
    @Before
    public void beforeTest() {
        if( pi != null ) {
            pi.reset() ;
        }
        pi = ObjectFactory.instance().getGPIOManager() ;
    }

    @Test
    public void leftShift() {
        OutPin p0 = pi.getOutputPin( 0 ) ;
        OutPin p1 = pi.getOutputPin( 1 ) ;
        OutPin p2 = pi.getOutputPin( 2 ) ;
        OutPin p3 = pi.getOutputPin( 3 ) ;
        OutPin p4 = pi.getOutputPin( 4 ) ;
        
        ParallelOutputBus bus = new ParallelOutputBus( p0, p1, p2, p3, p4 ) ;
        bus.setData( 1 ) ;
        
        bus.shiftLeft() ;
        assertEquals( PiUtils.convertToBitSet( 2 ), bus.getCurrentDataAsBin() ) ;
        
        bus.shiftLeft() ;
        assertEquals( PiUtils.convertToBitSet( 4 ), bus.getCurrentDataAsBin() ) ;
        
        bus.shiftLeft() ;
        assertEquals( PiUtils.convertToBitSet( 8 ), bus.getCurrentDataAsBin() ) ;
        
        bus.shiftLeft() ;
        assertEquals( PiUtils.convertToBitSet( 16 ), bus.getCurrentDataAsBin() ) ;
    }
    
    @Test
    public void rightShift() {
        OutPin p0 = pi.getOutputPin( 0 ) ;
        OutPin p1 = pi.getOutputPin( 1 ) ;
        OutPin p2 = pi.getOutputPin( 2 ) ;
        OutPin p3 = pi.getOutputPin( 3 ) ;
        OutPin p4 = pi.getOutputPin( 4 ) ;
        
        ParallelOutputBus bus = new ParallelOutputBus( p0, p1, p2, p3, p4 ) ;
        bus.setPin( 4, true ) ;
        assertEquals( PiUtils.convertToBitSet( 16 ), bus.getCurrentDataAsBin() ) ;
        
        bus.shiftRight() ;
        assertEquals( PiUtils.convertToBitSet( 8 ), bus.getCurrentDataAsBin() ) ;
        
        bus.shiftRight() ;
        assertEquals( PiUtils.convertToBitSet( 4 ), bus.getCurrentDataAsBin() ) ;
        
        bus.shiftRight() ;
        assertEquals( PiUtils.convertToBitSet( 2 ), bus.getCurrentDataAsBin() ) ;
        
        bus.shiftRight() ;
        assertEquals( PiUtils.convertToBitSet( 1 ), bus.getCurrentDataAsBin() ) ;
        
        bus.shiftRight() ;
        assertEquals( PiUtils.convertToBitSet( 0 ), bus.getCurrentDataAsBin() ) ;
    }
}
