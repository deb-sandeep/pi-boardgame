package com.deb.pi.boardgame.core.ui;

public class Dialog {

    public static enum Option {
        OK,
        OK_CANCEL,
        YES_NO,
        YES_NO_CANCEL,
        NO_INPUT
    } ;
    
    private String message = null ;
    private Option inputOptions = null ;
    
    private Dialog( String message, Option inputOptions ) {
        this.message = message ;
        this.inputOptions = inputOptions ;
    }
    
    public String getMessage() {
        return this.message ;
    }
    
    public Option getInputOptions() {
        return this.inputOptions ;
    }
    
    public static Dialog createOkDialog( String message ) {
        return new Dialog( message, Option.OK ) ;
    }
    
    public static Dialog createOkCancelDialog( String message ) {
        return new Dialog( message, Option.OK_CANCEL ) ;
    }
    
    public static Dialog createYesNoDialog( String message ) {
        return new Dialog( message, Option.YES_NO ) ;
    }
    
    public static Dialog createYesNoCancelDialog( String message ) {
        return new Dialog( message, Option.YES_NO_CANCEL ) ;
    }
    
    public static Dialog createNoInputDialog( String message ) {
        return new Dialog( message, Option.NO_INPUT ) ;
    }
}
