package com.deb.pi.boardgame.samples;

import java.util.Scanner ;

import com.pi4j.util.StringUtil ;

public class PiReadinessChecker {

    private Scanner keyboard = null ;
    
    public PiReadinessChecker() {
        keyboard = new Scanner( System.in ) ;
    }
    
    public void runPreFlightCheck() throws IllegalStateException {
        
        checkReadiness( "Is external power supply on?" ) ;
        checkReadiness( "Is breadboard powered on?" ) ;
        checkReadiness( "Are pi and external power source grounded together?" ) ;
        checkReadiness( "Is circuit powered by external source?" ) ;
        checkReadiness( "Are any input pins (if present), protected?" ) ;
    }
    
    private void checkReadiness( String question ) 
            throws IllegalStateException {
        
        System.out.print( question ) ;
        System.out.print( " ([Yes]/No) : " ) ;
        
        String input = keyboard.nextLine() ;
        
        if( StringUtil.isNotNullOrEmpty( input, true ) ) {
            input = input.trim().toUpperCase() ;
            if( input.equalsIgnoreCase( "Y" ) || 
                input.equalsIgnoreCase( "YES" ) ) {
                return ;
            }
        }
        throw new IllegalStateException( question ) ;
    }
}
