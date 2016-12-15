package com.deb.pi.boardgame.core.gpio;

public interface AbstractPin {

    public static enum Type{ INPUT, OUTPUT } ;
    public static enum State{ HIGH, LOW } ;
    
    public int getPinNum() ;
}
