package com.deb.pi.boardgame.core.util;

import java.util.BitSet ;

public class PiUtils {

    public static BitSet convertToBitSet( int value ) {
        
        BitSet bits = new BitSet() ;
        int index = 0 ;
        while( value != 0L ) {
            if( value % 2L != 0 ) {
                bits.set( index ) ;
            }
            ++index ;
            value = value >>> 1 ;
        }
        return bits ;
    }
    
    public static int convertToInt( BitSet bits ) {
        
        int value = 0 ;
        for( int i = 0 ; i < bits.length() ; ++i ) {
            value += bits.get( i ) ? ( 1L << i ) : 0L ;
        }
        return value ;
    }
}
