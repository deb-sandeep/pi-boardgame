package com.deb.pi.boardgame.core.ui;

import java.util.Stack ;

import org.apache.commons.lang.StringUtils ;
import org.apache.commons.lang.WordUtils ;

import com.deb.pi.boardgame.core.device.LCD ;
import com.deb.pi.boardgame.core.ui.Dialog.Option ;
import com.deb.pi.boardgame.core.util.ObjectFactory ;

public class DialogManager {

    private static DialogManager instance = null ;
    
    private LCD lcd = null ;
    private Stack<Dialog> dialogStack = new Stack<>() ;
    
    private DialogManager(){
        lcd = ObjectFactory.instance().getLCD() ;
    }
    
    public static DialogManager instance() {
        if( instance == null ) {
            instance = new DialogManager() ;
        }
        return instance ;
    }
    
    public void showDialog( Dialog d ) {
        dialogStack.push( d ) ;
        show( d ) ;
    }
    
    public void popDialog() {
        if( !dialogStack.isEmpty() ) {
            dialogStack.pop() ;
        }
        
        Dialog d = null ;
        if( !dialogStack.isEmpty() ) {
            d = dialogStack.peek() ;
        }
        show( d ) ;
    }
    
    private void show( Dialog d ) {
        lcd.clearDisplay() ;
        if( d != null ) {
            String[] lines = convertDialogToStringArray( d ) ;
            lcd.showScreen( lines ) ;
        }
    }
    
    private String[] convertDialogToStringArray( Dialog d ) {
        
        String msg = WordUtils.wrap( d.getMessage(), 20, "\n", false ) ;
        String msgLines[] = msg.split( "\n" ) ;
        
        if( d.getInputOptions() != Option.NO_INPUT && msgLines.length > 3 ) {
            throw new IllegalArgumentException( "Number of dialog lines can't be more than 3" ) ;
        }
        else if( msgLines.length > 4 ){
            throw new IllegalArgumentException( "Number of dialog lines can't be more than 4" ) ;
        }
        
        String lcdLines[] = new String[4] ;
        for( int i=0; i<msgLines.length; i++ ) {
            lcdLines[i] = msgLines[i] ;
        }
        
        if( d.getInputOptions() != Option.NO_INPUT ) {
            lcdLines[3] = getButtonDisplayLine( d.getInputOptions() ) ;
        }
        
        return lcdLines ;
    }

    @SuppressWarnings( "incomplete-switch" )
    private String getButtonDisplayLine( Option inputOptions ) {
        String str = null ;
        switch( inputOptions ) {
            case OK:
                str = "[Ok]" ;
                break ;
            case OK_CANCEL:
                str = "[Ok]  [Esc]" ;
                break ;
            case YES_NO:
                str = "[Yes]  [No]" ;
                break ;
            case YES_NO_CANCEL:
                str = "[Yes]  [No]  [Esc]" ;
                break ;
        }
        return StringUtils.center( str, 20 ) ;
    }
}
