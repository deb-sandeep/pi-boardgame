package com.deb.pi.boardgame.core.bus;

import java.util.HashMap ;
import java.util.Map ;

import com.deb.pi.boardgame.core.gpio.AbstractPin.State ;
import com.deb.pi.boardgame.core.gpio.GPIOManager ;
import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.InPinListener ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;

public class InputBus extends AbstractBus implements InPinListener {

    private InPin[] pins = null ;
    private int[]   pinStates = null ;
    private Map<InPin, Integer> pinBitMap = new HashMap<InPin, Integer>() ;
    private int curVal = 0 ;
    
    public InputBus( InPin... pins ) {
        this.setPins( pins ) ;
    }
    
    public InputBus( int... pinNums ) {
        
        GPIOManager pi = ObjectFactory.instance().getGPIOManager() ;
        InPin[] pins = new InPin[pinNums.length] ;
        for( int i : pinNums ) {
            pins[i] = pi.getInputPin( i ) ;
        }
        this.setPins( pins ) ;
    }
    
    private void setPins( InPin[] pins ) {
        
        super.setPins( pins ) ;
        this.pins = pins ;
        this.pinStates = new int[ this.pins.length ] ;
        this.curVal = 0 ;
        
        for( int i=0; i<this.pins.length; i++ ) {
            InPin pin = this.pins[i] ;
            
            this.pinStates[i] = 0 ;
            this.pinBitMap.put( pin, i ) ;
            pin.addInputPinListener( this ) ;
        }
    }
    
    public int getCurrentDataAsDec() {
        return curVal ;
    }
    
    public int[] getCurrentDataAsBin() {
        return pinStates ;
    }

    @Override
    public void stateChanged( InPin pin ) {
        
        int bitNum = pinBitMap.get( pin ) ;
        pinStates[bitNum] = pin.getState() == State.HIGH ? 1 : 0 ;
        
        StringBuffer buffer = new StringBuffer() ;
        for( int i=0; i<pins.length; i++ ) {
            buffer.insert( 0, Integer.toString( pinStates[i] ) ) ;
        }
        this.curVal = Integer.parseInt( buffer.toString(), 2 ) ;
    }
}
