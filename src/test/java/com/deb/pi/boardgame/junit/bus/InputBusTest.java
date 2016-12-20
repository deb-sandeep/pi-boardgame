package com.deb.pi.boardgame.junit.bus;

import static org.hamcrest.CoreMatchers.equalTo ;
import static org.junit.Assert.assertThat ;

import org.junit.Before ;
import org.junit.Test ;

import com.deb.pi.boardgame.core.bus.ParallelInputBus ;
import com.deb.pi.boardgame.core.gpio.GPIOManager ;
import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.AbstractPin.State ;
import com.deb.pi.boardgame.core.gpio.impl.mock.MockInPin ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;

public class InputBusTest {
    
    private GPIOManager pi = null ;
    
    @Before
    public void beforeTest() {
        ObjectFactory.instance().reset() ;
        if( pi != null ) {
            pi.reset() ;
        }
        pi = ObjectFactory.instance().getGPIOManager() ;
    }

    @Test
    public void simpleInputFor1() {
        InPin p0 = pi.getInputPin( 0 ) ;
        InPin p1 = pi.getInputPin( 1 ) ;
        InPin p2 = pi.getInputPin( 2 ) ;
        
        ParallelInputBus bus = new ParallelInputBus( p0, p1, p2 ) ;
        assertThat( bus.getCurrentDataAsDec(), equalTo( 0 ) ) ;
        
        ((MockInPin)p0).setCurrentState( State.HIGH ) ;
        assertThat( bus.getCurrentDataAsDec(), equalTo( 1 ) ) ;
    }

    @Test
    public void simpleInputFor2() {
        InPin p0 = pi.getInputPin( 0 ) ;
        InPin p1 = pi.getInputPin( 1 ) ;
        InPin p2 = pi.getInputPin( 2 ) ;
        
        ParallelInputBus bus = new ParallelInputBus( p0, p1, p2 ) ;
        assertThat( bus.getCurrentDataAsDec(), equalTo( 0 ) ) ;
        
        ((MockInPin)p1).setCurrentState( State.HIGH ) ;
        assertThat( bus.getCurrentDataAsDec(), equalTo( 2 ) ) ;
    }
    
    @Test
    public void simpleInputFor3() {
        InPin p0 = pi.getInputPin( 0 ) ;
        InPin p1 = pi.getInputPin( 1 ) ;
        InPin p2 = pi.getInputPin( 2 ) ;
        
        ParallelInputBus bus = new ParallelInputBus( p0, p1, p2 ) ;
        assertThat( bus.getCurrentDataAsDec(), equalTo( 0 ) ) ;
        
        ((MockInPin)p0).setCurrentState( State.HIGH ) ;
        ((MockInPin)p1).setCurrentState( State.HIGH ) ;
        assertThat( bus.getCurrentDataAsDec(), equalTo( 3 ) ) ;
    }
}
