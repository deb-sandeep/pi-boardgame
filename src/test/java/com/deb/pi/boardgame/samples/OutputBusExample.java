package com.deb.pi.boardgame.samples;

import com.deb.pi.boardgame.core.bus.impl.old.ParallelOutputBus ;

public class OutputBusExample {

    public static void main( String[] args ) throws Exception {
        
        ParallelOutputBus bus = new ParallelOutputBus( 0, 1, 2, 3 ) ;
        
        int iterationCount = 0 ;
        int nextDataOnBus  = 0 ;
        do {
            System.out.println( "----------------------------------------" ) ;
            System.out.println( "Setting data = " + nextDataOnBus ) ;
            bus.setData( nextDataOnBus ) ;
            System.out.println( bus.getCurrentDataAsDec() ) ;
            
            if( bus.getCurrentDataAsDec() != nextDataOnBus ) {
                throw new IllegalStateException( "Buurrajjjj" ) ;
            }
            
            nextDataOnBus = (nextDataOnBus == 15) ? 0 : nextDataOnBus+1 ;
            iterationCount++ ;
        }
        while( iterationCount < 100 ) ;
    }
}
