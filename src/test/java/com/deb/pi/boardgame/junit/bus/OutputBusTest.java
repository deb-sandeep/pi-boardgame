package com.deb.pi.boardgame.junit.bus;

import static org.junit.Assert.* ;
import static org.hamcrest.CoreMatchers.* ;

import org.junit.Before ;
import org.junit.Test ;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.gpio.GPIOManager ;
import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;

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
        
        OutputBus bus = new OutputBus( p0, p1, p2 ) ;
        assertThat( bus.getCurrentDataAsDec(), equalTo( 0 ) ) ;
        
        bus.setData( 2 ) ;
        assertArrayEquals( new int[]{0, 1, 0}, bus.getCurrentDataAsBin() ) ;
        
        bus.setData( 3 ) ;
        assertArrayEquals( new int[]{1, 1, 0}, bus.getCurrentDataAsBin() ) ;
    }

    @Test( expected=IllegalArgumentException.class )
    public void dataOverflow() {
        OutPin p0 = pi.getOutputPin( 0 ) ;
        OutPin p1 = pi.getOutputPin( 1 ) ;
        OutPin p2 = pi.getOutputPin( 2 ) ;
        
        OutputBus bus = new OutputBus( p0, p1, p2 ) ;
        assertThat( bus.getCurrentDataAsDec(), equalTo( 0 ) ) ;

        bus.setData( 24 ) ;
    }
}
