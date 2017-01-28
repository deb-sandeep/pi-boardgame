package com.deb.pi.boardgame.core.bus.impl.output;

import java.io.IOException ;
import java.util.ArrayList ;
import java.util.List ;
import java.util.concurrent.LinkedBlockingQueue ;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.bus.OutputBus ;
import com.tomgibara.bits.BitVector ;

public abstract class BaseHardwareOutputBus extends OutputBusBase 
    implements OutputBus {
    
    static final Logger log = Logger.getLogger( BaseHardwareOutputBus.class ) ;
    
    private WriterDaemon writerDaemon = null ;
    private LinkedBlockingQueue<WriteRequest> writeRequestQ = null ;
    
    protected BaseHardwareOutputBus( int numWires ) {
        super( numWires ) ;
        writeRequestQ = new LinkedBlockingQueue<WriteRequest>() ;
    }
    
    protected void startDaemon() {
        
        if( writerDaemon != null ) {
            if( writerDaemon.isAlive() ) {
                throw new IllegalStateException( "Writer daemon is alive." ) ;
            }
            else {
                writerDaemon = null ;
            }
        }
        writerDaemon = new WriterDaemon() ;
        writerDaemon.start() ;
    }
    
    @Override
    public void write( BitVector bv, int startWire, int numWires ) {
        
        WriteRequest req = new WriteRequest( bv, startWire, numWires ) ;
        writeRequestQ.add( req ) ;
        synchronized( req.lock ) {
            while( !req.processed ) {
                try {
                    req.lock.wait() ;
                }
                catch( InterruptedException e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected abstract void setHardwareBusState( BitVector newState ) 
        throws IOException ;
    
    // ------------------------------------------------------------------------
    private class WriteRequest {
        BitVector bv = null ;
        int startWire = -1 ;
        int numWires = -1 ;
        
        Object lock = new Object() ;
        boolean processed = false ;
        
        WriteRequest( BitVector bv, int startWire, int numWires ) {
            this.bv = bv ;
            this.startWire = startWire ;
            this.numWires = numWires ;
        }
    }
    
    private class WriterDaemon extends Thread {
        
        public WriterDaemon() {
            super.setDaemon( true ) ;
        }
        
        public void run() {
            List<WriteRequest> pendingRequests = new ArrayList<WriteRequest>() ;
            while( true ) {
                try {
                    pendingRequests.clear() ;
                    pendingRequests.add( writeRequestQ.take() ) ; 
                    writeRequestQ.drainTo( pendingRequests ) ;
                    flushWriteRequests( pendingRequests ) ;
                }
                catch( Exception e ) {
                    e.printStackTrace() ;
                }
            }
        }
        
        private void flushWriteRequests( List<WriteRequest> requests ) 
            throws IOException {
            
            BitVector bv = BaseHardwareOutputBus.this.getState().mutableCopy() ;
            for( WriteRequest req : requests ) {
                for( int i=0; i<req.numWires; i++ ) {
                    bv.setBit( req.startWire+i, req.bv.getBit( i ) ) ;
                }
            }
            
            setHardwareBusState( bv ) ;
            BaseHardwareOutputBus.this.setNewBusState( bv ) ;
            
            for( WriteRequest req : requests ) {
                synchronized( req.lock ) {
                    req.processed = true ;
                    req.lock.notify() ;
                }
            }
        }
    }
}
