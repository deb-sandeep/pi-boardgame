package com.deb.pi.boardgame.core.bus;

import com.deb.pi.boardgame.core.gpio.AbstractPin.State ;
import com.deb.pi.boardgame.core.gpio.GPIOManager ;
import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;

public class OutputBus extends AbstractBus {

    private OutPin[] pins = null ;
    private int currentData = 0 ;
    
    public OutputBus( OutPin... pins ) {
        super( pins ) ;
        this.pins = pins ;
    }
    
    public OutputBus( int... pinNums ) {
        GPIOManager pi = ObjectFactory.instance().getGPIOManager() ;
        OutPin[] pins = new OutPin[pinNums.length] ;
        for( int i=0; i<pinNums.length; i++ ) {
            pins[i] = pi.getOutputPin( pinNums[i] ) ;
        }
        super.setPins( pins ) ;
        this.pins = pins ;
    }
    
    public void setData( int data ) {
        
        checkDataSize( data ) ;        
        
        int[] pinStates = super.convertToBin( data ) ;
        for( int i=0; i<pinStates.length; i++ ) {
            State state = pinStates[i] == 0 ? State.LOW : State.HIGH ;
            pins[i].setState( state ) ;
        }
        this.currentData = data ;
        super.broadcastNewDataOnBus( this.currentData ) ;
    }

    public int getCurrentDataAsDec() {
        return this.currentData ;
    }
    
    public int[] getCurrentDataAsBin() {
        return super.convertToBin( this.currentData ) ;
    }
}
