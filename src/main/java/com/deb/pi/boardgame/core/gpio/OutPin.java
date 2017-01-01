package com.deb.pi.boardgame.core.gpio;

public interface OutPin extends AbstractPin {

    public void pulse() ;
    public void pulse( int pulseWidthInMillis ) ;
    public void setState( State state ) ;
    public State getState() ;
}
