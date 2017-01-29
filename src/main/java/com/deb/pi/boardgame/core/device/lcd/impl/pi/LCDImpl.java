package com.deb.pi.boardgame.core.device.lcd.impl.pi;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.device.lcd.impl.AbstractLCDBaseImpl ;
import com.pi4j.component.lcd.impl.I2CLcdDisplay ;
import com.pi4j.io.i2c.I2CBus ;
import com.pi4j.util.StringUtil ;

public class LCDImpl extends AbstractLCDBaseImpl {

    private static final Logger log = Logger.getLogger( LCDImpl.class ) ;
    
    private I2CLcdDisplay lcd = null ;
    
    public LCDImpl() throws Exception {}
    
    private void checkInitialization() throws Exception {
        if( lcd == null ) {
            lcd = new I2CLcdDisplay( 4, 20, I2CBus.BUS_1, 0x27, 
                                     3, 0, 1, 2, 7, 6, 5, 4 ) ;
        }
    }
    
    @Override
    public void clearDisplay() {
        
        try {
            checkInitialization() ;
            lcd.clear() ;
            lcd.setCursorHome() ;
        }
        catch( Exception e ) {
            log.error( "Could not initialize LCD", e ) ;
        }
    }

    @Override
    public void showScreen( String[] lineContents ) {

        try {
            checkInitialization() ;
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
        catch( Exception e ) {
            log.error( "Could not initialize LCD", e ) ;
        }
    }
}
