package com.deb.pi.boardgame.samples;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.ui.Dialog ;
import com.deb.pi.boardgame.core.ui.DialogManager ;

public class DialogManagerExample {

    private static Logger log = Logger.getLogger( DialogManagerExample.class ) ;
    
    public static void main( String[] args ) throws Exception {
        
        DialogManager dMgr = DialogManager.instance() ;
        dMgr.showDialog( Dialog.createOkDialog( "Hello World!" ) ) ;
        Thread.sleep( 2000 ) ;
        dMgr.showDialog( Dialog.createOkCancelDialog( "Ok Cancel" ) ) ;
        Thread.sleep( 2000 ) ;
        dMgr.showDialog( Dialog.createYesNoCancelDialog( "Yes No Cancel" ) ) ;
        Thread.sleep( 2000 ) ;
        dMgr.showDialog( Dialog.createYesNoCancelDialog( "This long message should be broken down into lines" ) ) ;
        Thread.sleep( 2000 ) ;
        dMgr.showDialog( Dialog.createNoInputDialog( "This is a no input dialog. It will not have any buttons." ) ) ;
        Thread.sleep( 2000 ) ;
        
        log.debug( "-----------------------------------------------" ) ;
        
        dMgr.popDialog() ;
        Thread.sleep( 2000 ) ;
        dMgr.popDialog() ;
        Thread.sleep( 2000 ) ;
        dMgr.popDialog() ;
        Thread.sleep( 2000 ) ;
        dMgr.popDialog() ;
        Thread.sleep( 2000 ) ;
        dMgr.popDialog() ;
        Thread.sleep( 2000 ) ;
    }
}
