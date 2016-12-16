package com.deb.pi.boardgame.core.bus;

import com.deb.pi.boardgame.core.gpio.AbstractPin.State ;
import com.deb.pi.boardgame.core.gpio.OutPin ;

public class OutputBus extends AbstractBus {

    private OutPin[] pins = null ;
    private int currentData = 0 ;
    
    public OutputBus( OutPin... pins ) {
        super( pins ) ;
        this.pins = pins ;
    }
    
    public void sendData( int data ) {
        
        checkDataSize( data ) ;        
        
        String binaryStr = Integer.toBinaryString( data ) ;
        for( int i=0; i<binaryStr.length(); i++ ) {
            char bit = binaryStr.charAt( i ) ;
            State state = bit == '0' ? State.LOW : State.HIGH ;
            pins[i].setState( state ) ;
        }
        
        this.currentData = data ;
    }

    public int getCurrentDataAsDec() {
        return this.currentData ;
    }
    
    public int[] getCurrentDataAsBin() {
        return super.convertToBin( this.currentData ) ;
    }
}
