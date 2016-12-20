package com.deb.pi.boardgame.samples;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.ui.Dialog ;
import com.deb.pi.boardgame.core.ui.DialogManager ;

public class DialogManagerExample {

    private static Logger log = Logger.getLogger( DialogManagerExample.class ) ;
    
    public static void main( String[] args ) throws Exception {
        
        DialogManager dMgr = DialogManager.instance() ;
        dMgr.showDialog( Dialog.createOkDialog( "Hello World!" ) ) ;
        dMgr.showDialog( Dialog.createOkCancelDialog( "Ok Cancel" ) ) ;
        dMgr.showDialog( Dialog.createYesNoCancelDialog( "Yes No Cancel" ) ) ;
        dMgr.showDialog( Dialog.createYesNoCancelDialog( "This long message should be broken down into lines" ) ) ;
        dMgr.showDialog( Dialog.createNoInputDialog( "This is a no input dialog. It will not have any buttons." ) ) ;
        
        log.debug( "-----------------------------------------------" ) ;
        
        dMgr.popDialog() ;
        dMgr.popDialog() ;
        dMgr.popDialog() ;
        dMgr.popDialog() ;
        dMgr.popDialog() ;
    }
}
