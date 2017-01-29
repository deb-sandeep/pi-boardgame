package com.deb.pi.boardgame.core.device.lcd.impl ;

import com.deb.pi.boardgame.core.device.lcd.LCD ;

public abstract class AbstractLCDBaseImpl implements LCD {

    @Override
    public void showLines( String l1, String l2, String l3, String l4 ) {
        String[] lines = new String[4] ;
        lines[0] = l1 ;
        lines[1] = l2 ;
        lines[2] = l3 ;
        lines[3] = l4 ;
        showScreen( lines ) ;
    }
}
