package com.deb.pi.boardgame.core.gpio;

public interface InPin extends AbstractPin {

    public static enum PullResistanceType { UP, DOWN } ;
    
    public void addInputPinListener( InPinListener listener ) ;
    
    public void removeAllListeners() ;
}
