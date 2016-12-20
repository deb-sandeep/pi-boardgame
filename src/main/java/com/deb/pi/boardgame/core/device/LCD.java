package com.deb.pi.boardgame.core.device;

public interface LCD {

    public void clearDisplay();
    
    public void showLines( String l1, String l2, String l3, String l4 ) ;
    
    public void showScreen( String[] lineContents ) ;
}
