package com.deb.pi.boardgame.samples;

import java.util.Arrays ;

import com.deb.pi.boardgame.core.bus.OutputBus ;

public class OutputBusExample {

    public static void main( String[] args ) throws Exception {
        
        OutputBus bus = new OutputBus( 0, 1, 2, 3 ) ;
        
        int iterationCount = 0 ;
        int nextDataOnBus  = 0 ;
        do {
            System.out.println( "----------------------------------------" ) ;
            System.out.println( "Setting data = " + nextDataOnBus ) ;
            bus.setData( nextDataOnBus ) ;
            System.out.println( Arrays.toString( bus.getCurrentDataAsBin() ) ) ;
            
            Thread.sleep( 100 ) ;
            
            nextDataOnBus = (nextDataOnBus == 15) ? 0 : nextDataOnBus+1 ;
            iterationCount++ ;
        }
        while( iterationCount < 100 ) ;
    }
}
