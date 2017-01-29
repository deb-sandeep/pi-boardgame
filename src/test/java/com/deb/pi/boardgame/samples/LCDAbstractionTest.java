package com.deb.pi.boardgame.samples;

import com.deb.pi.boardgame.core.device.lcd.LCD ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;

public class LCDAbstractionTest {

    public static void main( String[] args ) {
        
        LCD lcd = ObjectFactory.instance().getLCD() ;
        lcd.showLines( "01234567890123456789", "Aniruddha", null, "[OK]" );
    }
}
