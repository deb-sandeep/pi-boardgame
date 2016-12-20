package com.deb.pi.boardgame.core.bus;

import java.util.BitSet ;

import com.deb.pi.boardgame.core.gpio.AbstractPin.State ;
import com.deb.pi.boardgame.core.gpio.GPIOManager ;
import com.deb.pi.boardgame.core.gpio.OutPin ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;
import com.deb.pi.boardgame.core.util.PiUtils ;

public class ParallelOutputBus extends AbstractBus {

    private OutPin[] pins = null ;
    private int currentData = 0 ;
    
    public ParallelOutputBus( OutPin... pins ) {
        super( pins ) ;
        this.pins = pins ;
    }
    
    public ParallelOutputBus( int... pinNums ) {
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
        
        BitSet pinStates = PiUtils.convertToBitSet( data ) ;
        for( int i=0; i<pins.length; i++ ) {
            State state = pinStates.get( i ) ? State.HIGH : State.LOW ;
            pins[i].setState( state ) ;
        }
        this.currentData = data ;
        super.broadcastNewDataOnBus( this.currentData ) ;
    }

    public int getCurrentDataAsDec() {
        return this.currentData ;
    }
    
    public BitSet getCurrentDataAsBin() {
        return PiUtils.convertToBitSet( this.currentData ) ;
    }
}
