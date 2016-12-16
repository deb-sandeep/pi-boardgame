package com.deb.pi.boardgame.core.bus;

import com.deb.pi.boardgame.core.gpio.AbstractPin ;

abstract class AbstractBus {
    
    private AbstractPin[] pins = null ;
    
    protected AbstractBus( AbstractPin[] pins ) {
        this.pins = pins ;
        if( pins == null || pins.length == 0 ) {
            throw new IllegalArgumentException( "Num pins for a bus can't be 0" ) ;
        }
    }
    
    protected void checkDataSize( int data ) {
        if( data > getMaxDataSize() ) {
            throw new IllegalArgumentException( 
                                 "Bus size is insufficient to carry " + data + 
                                 ". Max data size = " + getMaxDataSize() ) ;
        }
    }
    
    protected int[] convertToBin( int data ) {
        checkDataSize( data ) ;
        
        int[] binData = new int[getNumPins()] ;
        String binaryStr = Integer.toBinaryString( data ) ;
        
        for( int i=0; i<binaryStr.length(); i++ ) {
            char bit = binaryStr.charAt( i ) ;
            binData[i] = bit=='0' ? 0 : 1 ;
        }
        return binData ;
    }
    
    public int getNumPins() {
        return pins.length ;
    }
    
    public int getMaxDataSize() {
        return (int)Math.pow( 2, getNumPins() ) ;
    }
    
    public abstract int getCurrentDataAsDec() ;
    
    public abstract int[] getCurrentDataAsBin() ;
}
