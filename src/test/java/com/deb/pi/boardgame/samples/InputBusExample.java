package com.deb.pi.boardgame.samples;

import java.util.concurrent.ThreadLocalRandom ;

import com.deb.pi.boardgame.core.bus.AbstractBus ;
import com.deb.pi.boardgame.core.bus.BusListener ;
import com.deb.pi.boardgame.core.bus.InputBus ;
import com.deb.pi.boardgame.core.gpio.AbstractPin ;
import com.deb.pi.boardgame.core.gpio.AbstractPin.State ;
import com.deb.pi.boardgame.core.gpio.impl.mock.MockInPin ;

public class InputBusExample {

    public static void main( String[] args ) throws Exception {
        
        InputBus iBus = new InputBus( 0, 1, 2 ) ;
        
        iBus.addBusListener( new BusListener() {
            @Override
            public void newDataAvailable( AbstractBus bus ) {
                System.out.println( "Bus has new data - " + bus.getCurrentDataAsDec() ) ;
            }
        } ) ;
        
        for( int i=0; i<10; i++ ) {
            int data = ThreadLocalRandom.current().nextInt( 0, iBus.getMaxDataSize() ) ;
            System.out.println( "Setting new data in bus. Data " + data ) ;
            setDataInInputBus( data, iBus ) ;
            Thread.sleep( 500 ) ;
        }
    }
    
    // This is a mocking method - do not use this logic against Pi
    private static void setDataInInputBus( int data, InputBus bus ) {
        
        AbstractPin[] pins = bus.getPins() ;
        int[] pinValues = convertToBin( data, bus.getNumPins() ) ;
        
        for( int i=0; i<bus.getNumPins(); i++ ) {
            MockInPin inPin = ( MockInPin )pins[i] ;
            inPin.setCurrentState( pinValues[i] == 0 ? State.LOW : State.HIGH ) ;
        }
    }

    private static int[] convertToBin( int data, int numBits ) {
        
        int[] binData = new int[numBits] ;
        String binaryStr = Integer.toBinaryString( data ) ;
        
        for( int i=0; i<binaryStr.length(); i++ ) {
            char bit = binaryStr.charAt( binaryStr.length()-1-i ) ;
            binData[i] = bit=='0' ? 0 : 1 ;
        }
        
        return binData ;
    }
    
}
