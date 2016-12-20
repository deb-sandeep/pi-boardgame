package com.deb.pi.boardgame.core.device.impl.pi;

import com.deb.pi.boardgame.core.device.impl.AbstractLCDBaseImpl ;
import com.pi4j.component.lcd.impl.I2CLcdDisplay ;
import com.pi4j.io.i2c.I2CBus ;
import com.pi4j.util.StringUtil ;

public class LCDImpl extends AbstractLCDBaseImpl {

    private I2CLcdDisplay lcd = null ;
    
    public LCDImpl() throws Exception {
        lcd = new I2CLcdDisplay( 4, 20, I2CBus.BUS_1, 0x27, 
                                 3, 0, 1, 2, 7, 6, 5, 4 ) ;
    }
    
    @Override
    public void clearDisplay() {
        lcd.clear() ;
        lcd.setCursorHome() ;
    }

    @Override
    public void showScreen( String[] lineContents ) {

        lcd.clear() ;
        
        if( lineContents.length > 4 ) {
            throw new IllegalArgumentException( "LCD doesn't support more than 4 lines" ) ;
        }
        
        for( int i=0; i<lineContents.length; i++ ) {
            if( StringUtil.isNotNullOrEmpty( lineContents[i] ) ) {
                lcd.write( i, lineContents[i] );
            }
        }
    }
}
