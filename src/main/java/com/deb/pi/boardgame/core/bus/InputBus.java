package com.deb.pi.boardgame.core.bus;

import com.deb.pi.boardgame.core.gpio.InPin ;

public class InputBus extends AbstractBus {

    private InPin[] pins = null ;
    
    public InputBus( InPin... pins ) {
        super( pins ) ;
        this.pins = pins ;
    }
    
    public int getCurrentDataAsDec() {
        return 0 ;
    }
    
    public int[] getCurrentDataAsBin() {
        return null ;
    }
}
