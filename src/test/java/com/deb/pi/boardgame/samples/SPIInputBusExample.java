package com.deb.pi.boardgame.samples;

import com.deb.pi.boardgame.core.bus.InputBus ;
import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.impl.input.IC595BasedInputBus ;
import com.deb.pi.boardgame.core.bus.impl.output.SPIOutputBus ;
import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;
import com.pi4j.io.spi.SpiChannel ;
import com.tomgibara.bits.BitVector ;

public class SPIInputBusExample {

    private OutputBus spiOBus = null ;
    private InputBus iBus = null ;
    private OutputBus inputProbeBus = null ;
    private OutputBus reflectionBus = null ;
    
    public SPIInputBusExample() throws Exception {
        
        spiOBus = new SPIOutputBus( SpiChannel.CS0, 6 ) ;
        inputProbeBus = (OutputBus)spiOBus.getSubBus( 0, 3 ) ;
        reflectionBus = (OutputBus)spiOBus.getSubBus( 3, 3 ) ;
        
        InPin inPin = ObjectFactory.instance().getGPIOManager().getInputPin( 0 ) ;
        iBus = new IC595BasedInputBus( inputProbeBus, inPin ) ;
    }
    
    public void run() throws Exception {
        
        long runTimeInSec = 30 ;
        
        long startTime = System.currentTimeMillis() ;
        long elapsedTime = 0 ;
        
        BitVector lastBusState = iBus.read() ;
        reflectionBus.write( lastBusState ) ;
        
        while( elapsedTime < runTimeInSec*1000 ) {
            
            BitVector iBusState = iBus.read() ;
            if( !iBusState.equals( lastBusState ) ) {
                reflectionBus.write( iBusState ) ;
                lastBusState = iBusState ;
            }
            
            Thread.sleep( 100 ) ;
            elapsedTime = System.currentTimeMillis() - startTime ;
        }
        
        spiOBus.clear() ;
    }
    
    public static void main( String[] args ) throws Exception {
        new SPIInputBusExample().run() ;
        ObjectFactory.instance().getGPIOManager().shutdown() ;
    }
}
