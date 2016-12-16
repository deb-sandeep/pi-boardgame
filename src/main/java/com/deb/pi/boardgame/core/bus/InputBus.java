package com.deb.pi.boardgame.core.bus;

import java.util.HashMap ;
import java.util.Map ;
import java.util.Timer ;
import java.util.TimerTask ;

import com.deb.pi.boardgame.core.gpio.AbstractPin.State ;
import com.deb.pi.boardgame.core.gpio.GPIOManager ;
import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.InPinListener ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;

public class InputBus extends AbstractBus implements InPinListener {

    private enum ProcessingState{ IDLE, COLLECTING } ;
    
    private class CollectionMonitorTask extends TimerTask {
        
        private boolean isCancelled = false ;
        
        public boolean cancel() {
            isCancelled = true ;
            return super.cancel() ;
        }
        
        @Override public void run() {
            synchronized( collectionLock ) {
                if( !isCancelled ) {
                    broadcastNewDataOnBus( curVal ) ;
                    state = ProcessingState.IDLE ;
                }
            }
        }
    }
    
    private InPin[] pins = null ;
    private int[]   pinStates = null ;
    private Map<InPin, Integer> pinBitMap = new HashMap<InPin, Integer>() ;
    private int curVal = 0 ;
    private int debounceDelay = 30 ;
    private ProcessingState state = ProcessingState.IDLE ;
    private Timer collectionMonitorTimer = new Timer( true ) ;
    private CollectionMonitorTask currentMonitorTask = null ;
    
    private Object collectionLock = new Object() ;
    
    public InputBus( InPin... pins ) {
        this.setPins( pins ) ;
    }
    
    public InputBus( int... pinNums ) {
        
        GPIOManager pi = ObjectFactory.instance().getGPIOManager() ;
        InPin[] pins = new InPin[pinNums.length] ;
        for( int i=0; i<pinNums.length; i++ ) {
            pins[i] = pi.getInputPin( pinNums[i] ) ;
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
    
    public void setDebounceDelay( int millis ) {
        this.debounceDelay = millis ;
    }
    
    public int getDebounceDelay() {
        return this.debounceDelay ;
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
        
        // We are entering the collection phase. Start the timer.
        if( state == ProcessingState.IDLE ) {
            
            if( this.debounceDelay == 0 ) {
                broadcastNewDataOnBus( this.curVal ) ;
            }
            else {
                state = ProcessingState.COLLECTING ;
                currentMonitorTask = new CollectionMonitorTask() ;
                collectionMonitorTimer.schedule( currentMonitorTask, 
                        this.debounceDelay ) ;
            }
        }
        else {
            // We are in the collection phase. Acquire the lock such that 
            // if the collection timer is ending, we don't want it to broadcast
            // the value.
            synchronized( collectionLock ) {
                // Cancel the current task - we need to wait for a further
                // duration of debounce interval
                currentMonitorTask.cancel() ;
                collectionMonitorTimer.purge() ;
                
                // Create a new new monitor task which will attempt broadcasting
                // the current value after a delay of debounce interval if no
                // input has been received by then.
                currentMonitorTask = new CollectionMonitorTask() ;
                collectionMonitorTimer.schedule( currentMonitorTask, 
                                                 this.debounceDelay ) ;
            }
        }
    }
}
