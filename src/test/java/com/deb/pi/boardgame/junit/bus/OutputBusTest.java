package com.deb.pi.boardgame.junit.bus;

import static org.hamcrest.CoreMatchers.equalTo ;
import static org.junit.Assert.assertEquals ;
import static org.junit.Assert.assertThat ;

import org.junit.Before ;
import org.junit.Test ;

import com.deb.pi.boardgame.core.bus.ParallelOutputBus ;
import com.deb.pi.boardgame.core.gpio.GPIOManager ;
import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;
import com.deb.pi.boardgame.core.util.PiUtils ;

public class OutputBusTest {
    
    private GPIOManager pi = null ;
    
    @Before
    public void beforeTest() {
        if( pi != null ) {
            pi.reset() ;
        }
        pi = ObjectFactory.instance().getGPIOManager() ;
    }

    @Test
    public void simpleOutput() {
        OutPin p0 = pi.getOutputPin( 0 ) ;
        OutPin p1 = pi.getOutputPin( 1 ) ;
        OutPin p2 = pi.getOutputPin( 2 ) ;
        
        ParallelOutputBus bus = new ParallelOutputBus( p0, p1, p2 ) ;
        assertThat( bus.getCurrentDataAsDec(), equalTo( 0 ) ) ;
        
        bus.setData( 2 ) ;
        assertEquals( PiUtils.convertToBitSet( 2 ), bus.getCurrentDataAsBin() ) ;
        
        bus.setData( 3 ) ;
        assertEquals( PiUtils.convertToBitSet( 3 ), bus.getCurrentDataAsBin() ) ;
    }

    @Test( expected=IllegalArgumentException.class )
    public void dataOverflow() {
        OutPin p0 = pi.getOutputPin( 0 ) ;
        OutPin p1 = pi.getOutputPin( 1 ) ;
        OutPin p2 = pi.getOutputPin( 2 ) ;
        
        ParallelOutputBus bus = new ParallelOutputBus( p0, p1, p2 ) ;
        assertThat( bus.getCurrentDataAsDec(), equalTo( 0 ) ) ;

        bus.setData( 24 ) ;
    }
}
