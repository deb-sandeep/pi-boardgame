package com.deb.pi.boardgame.core.util;

import java.io.File ;
import java.io.UnsupportedEncodingException ;
import java.net.URL ;
import java.net.URLDecoder ;

import org.apache.log4j.Logger ;

import com.deb.pi.boardgame.core.ui.DialogManager ;

public class MediaFile {
    
    private static final Logger log = Logger.getLogger( MediaFile.class ) ;

    private static final String MEDIA_DIR = "/com/deb/pi/boardgame/media/" ;
    
    public static final MediaFile BELL  = new MediaFile( "bell.wav" ) ;
    public static final MediaFile INTRO = new MediaFile( "intro.wav" ) ;
    
    private File soundFile = null ;
    
    private MediaFile( String mediaName ) {
        this.soundFile = getFile( mediaName ) ;
    }
    
    public void play() {
        try {
            SoundPlayer.playFile( this.soundFile ) ;
        }
        catch( Exception e ) {
            log.error( "Error playing sound file " + soundFile, e ) ;
        }
    }
    
    private File getFile( String mediaName ) {
        
        File file = null ;
        URL url = DialogManager.class.getResource( MEDIA_DIR + mediaName ) ;
        try {
            file = new File( URLDecoder.decode( url.getPath(), "UTF-8" ) ) ;
        }
        catch( UnsupportedEncodingException e ) {
            e.printStackTrace();
        }
        return file ;
    }
}
