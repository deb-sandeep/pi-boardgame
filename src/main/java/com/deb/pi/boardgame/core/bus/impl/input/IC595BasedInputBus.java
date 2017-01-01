package com.deb.pi.boardgame.core.bus.impl.input;

import java.io.IOException ;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.bus.Bus ;
import com.deb.pi.boardgame.core.bus.InputBus ;
import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.impl.BusBase ;
import com.deb.pi.boardgame.core.gpio.InPin ;
import com.deb.pi.boardgame.core.gpio.AbstractPin.State ;
import com.tomgibara.bits.BitVector ;

public class IC595BasedInputBus extends BusBase 
    implements InputBus {
    
    private static final Logger log = Logger.getLogger( IC595BasedInputBus.class ) ;

    private OutputBus probeBus = null ;
    private InPin readPin = null ;
    
    public IC595BasedInputBus( OutputBus probeBus, InPin readPin ) {
        super( probeBus.size() ) ;
        this.probeBus = probeBus ;
        this.readPin = readPin ;
    }

    @Override
    public Bus getSubBus( int startWireNum, int numWires ) {
        return null ;
    }

    @Override
    public BitVector read() throws IOException {
        
        log.debug( "Trying to read from IC595 based input bus." ) ;
        
        BitVector newState = new BitVector( probeBus.size() ) ;
        
        // Set all pins to 1 and check if the readPin shows a HIGH input
        // If the readPin is not HIGH, all the buttons are on LOW state
        log.debug( "Setting probe bus to high." ) ;
        probeBus.setHigh() ;
        
        if( readPin.isLow() ) {
            log.debug( "Read pin is LOW. None of the switches are HIGH" ) ;
            super.setNewBusState( newState ) ;
        }
        else {
            log.debug( "Read pin is HIGH. At least one wire is HIGH" ) ;
            log.debug( "Probing to determing HIGH switches." ) ;
            
            // If the readPin is HIGH, it implies at least one of the switches
            // are on a HIGH state. We need to determine which ones by probing
            BitVector probeVector = new BitVector( probeBus.size() ) ;
            
            for( int index=0; index<probeBus.size(); index++ ) {
                
                log.debug( "Setting wire " + index + " to HIGH" ) ;
                probeVector.clear() ;
                probeVector.setBit( index, true ) ;
                probeBus.write( probeVector ) ;
                
                if( readPin.getState() == State.HIGH ) {
                    log.debug( "Switch " + index + " is HIGH." ) ;
                    newState.setBit( index, true ) ;
                }
            }
        }
        log.debug( "--------------------------------------------\n" ) ;
        return newState ;
    }
}
