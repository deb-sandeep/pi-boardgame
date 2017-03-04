package com.deb.pi.boardgame.game.chess ;

import java.io.File ;
import java.io.UnsupportedEncodingException ;
import java.net.URL ;
import java.net.URLDecoder ;
import java.util.HashMap ;
import java.util.Map ;

import com.deb.pi.boardgame.core.util.SoundPlayer ;

public class MoveAudioPlayer {

    private final String RES_PATH = "/com/deb/pi/boardgame/game/chess/audio" ;
    private final URL    RES_URL  = MoveAudioPlayer.class.getResource( RES_PATH ) ;
    
    private Map<String, File> database    = null ;

    public MoveAudioPlayer() {
        database = new HashMap<String, File>() ;
        try {
            buildDatabase( new File( URLDecoder.decode( RES_URL.getPath(), "UTF-8" ) ) ) ;
        }
        catch( UnsupportedEncodingException e ) {
            // Just log. This exception should never happen because the code
            // is tested for the hardcoded encoding type.
            e.printStackTrace() ;
        }
    }

    private void buildDatabase( File dir ) {
        
        for( File file : dir.listFiles() ) {
            if( file.isDirectory() ) {
                buildDatabase( file ) ;
            }
            else {
                String fileName = file.getName() ;
                if( fileName.endsWith( ".wav" ) ) {
                    String name = fileName.substring( 0, fileName.length()-4 ) ;
                    database.put( name.toUpperCase(), file ) ;
                }
            }
        }
    }

    public void playSound( String clipSequence ) throws Exception {

        File     soundFile = null ;
        String[] clipNames = clipSequence.split( "\\s+" ) ;

        for( String clipName : clipNames ) {
            soundFile = database.get( clipName.toUpperCase() ) ;
            SoundPlayer.playFile( soundFile ) ;
        }
    }
}
