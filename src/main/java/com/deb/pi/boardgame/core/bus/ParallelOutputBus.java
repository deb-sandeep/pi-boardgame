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
        setData( pinStates ) ;
    }
    
    public void setData( BitSet pinStates ) {
        for( int i=0; i<pins.length; i++ ) {
            State state = pinStates.get( i ) ? State.HIGH : State.LOW ;
            if( pins[i].getState() != state ) {
                pins[i].setState( state ) ;
            }
        }
        this.currentData = PiUtils.convertToInt( pinStates ) ;
        super.broadcastNewDataOnBus( this.currentData ) ;
    }
    
    public void setDataOR( BitSet pinStates ) {
        pinStates.or( getCurrentDataAsBin() ) ;
        setData( pinStates ) ;
    }
    
    public void setDataAND( BitSet pinStates ) {
        pinStates.and( getCurrentDataAsBin() ) ;
        setData( pinStates ) ;
    }
    
    public void setDataXOR( BitSet pinStates ) {
        pinStates.xor( getCurrentDataAsBin() ) ;
        setData( pinStates ) ;
    }
    
    public void shiftLeft() {
        shiftLeft( false ) ;
    }
    
    public void shiftLeft( boolean zeroBitState ) {
        BitSet nextState = getCurrentDataAsBin() ;
        for( int i=pins.length-2; i>=0; i-- ) {
            nextState.set( i+1, nextState.get( i ) ) ;
        }
        nextState.set( 0, zeroBitState ) ;
        setData( nextState ) ;
    }
    
    public void shiftRight() {
        shiftRight( false ) ;
    }
    
    public void shiftRight( boolean highBitState ) {
        BitSet nextState = getCurrentDataAsBin() ;
        for( int i=1; i<=pins.length-1; i++ ) {
            nextState.set( i-1, nextState.get( i ) ) ;
        }
        nextState.set( pins.length-1, highBitState ) ;
        setData( nextState ) ;
    }
    
    public void setLow( int pinNum ) {
        setPin( pinNum, false ) ;
    }
    
    public void setHigh( int pinNum ) {
        setPin( pinNum, true ) ;
    }
    
    public void setPin( int pinNum, boolean b ) {
        BitSet nextState = getCurrentDataAsBin() ;
        nextState.set( pinNum, b ) ;
        setData( nextState ) ;
    }

    public int getCurrentDataAsDec() {
        return this.currentData ;
    }
    
    public BitSet getCurrentDataAsBin() {
        return PiUtils.convertToBitSet( this.currentData ) ;
    }
}
