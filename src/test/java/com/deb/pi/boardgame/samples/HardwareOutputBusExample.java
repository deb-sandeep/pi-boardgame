package com.deb.pi.boardgame.samples;

import com.deb.pi.boardgame.core.bus.OutputBus ;

public class HardwareOutputBusExample {
    
    private class BusWriter extends Thread {
        
        OutputBus bus = null ;
        int delay = 0 ;
        int numIter = 0 ;
        String name = null ;
        
        BusWriter( String name, OutputBus bus, int delay, int numIter ) {
            this.name = name ;
            this.bus = bus ;
            this.delay = delay ;
            this.numIter = numIter ;
        }
        
        public void run() {
            for( int i=0; i<numIter; i++ ) {
                try {
                    System.out.println( name + " writing " + i ) ;
                    bus.write( i ) ;
                    Thread.sleep( delay ) ;
                }
                catch( Exception e ) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void run() throws Exception {
        
        TestHWOutputBus bus = new TestHWOutputBus( 8 ) ;
        OutputBus ob1 = (OutputBus)bus.getSubBus( 0, 4 ) ;
        OutputBus ob2 = (OutputBus)bus.getSubBus( 4, 4 ) ;
        
        BusWriter b1 = new BusWriter( "W1", ob1, 100, 5 ) ;
        BusWriter b2 = new BusWriter( "W2", ob2, 100, 5 ) ;
        
        b1.start() ;
        b2.start() ;
    }

    public static void main( String[] args ) throws Exception {
        new HardwareOutputBusExample().run() ;
    }
}
