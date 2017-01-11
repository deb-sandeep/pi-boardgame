package com.deb.pi.boardgame.samples;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.deb.pi.boardgame.core.bus.impl.output.SPIOutputBus ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;
import com.pi4j.io.spi.SpiChannel ;

public class RowProbeUnit {

    private OutputBus mainBus      = null ;
    private OutputBus rowSourceBus = null ;
    private OutputBus rowSinkBus   = null ;
    
    public RowProbeUnit() throws Exception {
        
        mainBus = new SPIOutputBus( SpiChannel.CS0, 24 ) ;
        rowSourceBus = ( OutputBus )mainBus.getSubBus( 0, 16 ) ;
        rowSinkBus   = ( OutputBus )mainBus.getSubBus( 16, 8 ) ;
        
        System.out.println( "Bus created." ) ;
    }
    
    public void runSinkSequence() throws Exception {
        
        mainBus.clear() ;
        System.out.println( "Running sink sequence." ) ;
        rowSourceBus.setHigh() ;
        for( int i=0; i<8; i++ ) {
            if( i>0 )rowSinkBus.write( i-1, false );
            rowSinkBus.write( i, true ) ;
            Thread.sleep( 500 ) ;
        }
        System.out.println( "Shutting down everything." ) ;
        mainBus.clear() ;
    }
    
    public void runSourceSequence() throws Exception {
        
        mainBus.clear() ;
        System.out.println( "Setting source sequence." ) ;
        rowSinkBus.setHigh() ;
        for( int i=0; i<16; i++ ) {
            if( i>0 )rowSourceBus.write( i-1, false );
            rowSourceBus.write( i, true ) ;
            Thread.sleep( 500 ) ;
        }
        System.out.println( "Shutting down everything." ) ;
        mainBus.clear() ;
    }
    
    public static void main( String[] args ) throws Exception {
        System.out.println( "Starting program." ) ;
        RowProbeUnit unit = new RowProbeUnit() ;
        unit.runSourceSequence() ;
        unit.runSinkSequence() ;
        ObjectFactory.instance().getGPIOManager().shutdown() ;
    }
    
}
