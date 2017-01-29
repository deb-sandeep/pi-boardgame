package com.deb.pi.boardgame.core.device.lcd.impl.mock;

import org.apache.commons.lang.StringUtils ;
import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.device.lcd.impl.AbstractLCDBaseImpl ;
import com.pi4j.util.StringUtil ;

public class MockLCDImpl extends AbstractLCDBaseImpl {
    
    private static final Logger log = Logger.getLogger( MockLCDImpl.class ) ;

    @Override
    public void clearDisplay() {
        // NO OP
    }
    
    @Override
    public void showScreen( String[] lineContents ) {
        
        StringBuilder buffer = new StringBuilder() ;
        String lines[] = new String[4] ;
        
        if( lineContents.length == 4 ) {
            lines = lineContents ;
        }
        else if( lineContents.length < 4 ) {
            for( int i=0; i<lineContents.length; i++ ) {
                lines[i] = lineContents[i] ;
            }
        }
        else {
            throw new IllegalArgumentException( "LCD doesn't support more than 4 lines" ) ;
        }
        
        for( int i=0; i<lineContents.length; i++ ) {
            lines[i] = (lines[i] == null) ? "" : lines[i] ;
        }
        
        buffer.append( '+' )
              .append( StringUtil.repeat( '-', 20 ) )
              .append( '+' )
              .append( '\n' ) ;
        
        for( int i=0; i<4; i++ ) {
            buffer.append( '|' )
                  .append( StringUtils.rightPad( lines[i], 20 ) )
                  .append( '|' )
                  .append( '\n' ) ;
        }
        
        buffer.append( '+' )
              .append( StringUtil.repeat( '-', 20 ) )
              .append( '+' )
              .append( '\n' ) ;

        log.debug( buffer ) ;
    }
}
