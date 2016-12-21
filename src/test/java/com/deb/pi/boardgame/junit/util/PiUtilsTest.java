package com.deb.pi.boardgame.junit.util;

import static org.hamcrest.CoreMatchers.equalTo ;
import static org.junit.Assert.assertThat ;

import java.util.BitSet ;

import org.junit.Test ;

import com.deb.pi.boardgame.core.util.PiUtils ;

public class PiUtilsTest {

    @Test
    public void convertToBitSet() {
        
        BitSet b = PiUtils.convertToBitSet( 8 ) ;
        assertThat( false, equalTo( b.get( 0 ) ) ) ;
        assertThat( false, equalTo( b.get( 1 ) ) ) ;
        assertThat( false, equalTo( b.get( 2 ) ) ) ;
        assertThat( true,  equalTo( b.get( 3 ) ) ) ;
    }
}
