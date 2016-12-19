package com.deb.pi.boardgame.core.util ;

import java.io.File ;
import java.io.UnsupportedEncodingException ;
import java.net.URL ;
import java.net.URLDecoder ;
import java.util.HashMap ;
import java.util.Map ;

import javax.sound.sampled.AudioFormat ;
import javax.sound.sampled.AudioInputStream ;
import javax.sound.sampled.AudioSystem ;
import javax.sound.sampled.DataLine ;
import javax.sound.sampled.SourceDataLine ;

public class AudioPlayer {

    private final String RES_PATH = "/com/deb/pi/boardgame/game/chess/audio" ;
    private final URL    RES_URL  = AudioPlayer.class.getResource( RES_PATH ) ;
    
    private Map<String, File> database    = null ;

    public AudioPlayer() {
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

    public void playSound( String clipSequence ) throws Exception {

        File              soundFile   = null ;
        AudioInputStream  audioStream = null ;
        AudioFormat       audioFormat = null ;
        SourceDataLine    sourceLine  = null ;

        String[] clipNames = clipSequence.split( "\\s+" ) ;

        for( String clipName : clipNames ) {
            
            soundFile   = database.get( clipName.toUpperCase() ) ;
            audioStream = AudioSystem.getAudioInputStream( soundFile ) ;
            audioFormat = audioStream.getFormat() ;

            DataLine.Info info = new DataLine.Info( SourceDataLine.class, audioFormat ) ;
            sourceLine = (SourceDataLine) AudioSystem.getLine( info ) ;
            sourceLine.open( audioFormat ) ;

            sourceLine.start() ;
            byte[] abData = new byte[(int)soundFile.length()] ;
            audioStream.read( abData ) ;
            sourceLine.write( abData, 0, abData.length ) ;
            sourceLine.drain() ;
            sourceLine.close() ;
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
}
