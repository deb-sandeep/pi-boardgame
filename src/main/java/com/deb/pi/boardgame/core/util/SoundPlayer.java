package com.deb.pi.boardgame.core.util;

import java.io.File ;
import java.util.List ;

import javax.sound.sampled.AudioFormat ;
import javax.sound.sampled.AudioInputStream ;
import javax.sound.sampled.AudioSystem ;
import javax.sound.sampled.DataLine ;
import javax.sound.sampled.SourceDataLine ;

import org.apache.commons.io.IOUtils ;

public class SoundPlayer {

    public static void playFileSequence( List<File> files, int gapInMillis ) 
        throws Exception {
        
        for( File file : files ) {
            playFile( file ) ;
            try {
                Thread.sleep( gapInMillis ) ;
            }
            catch( Exception e ) {
                // Gobble
            }
        }
    }

    public static void playFile( File file ) 
            throws Exception {
            
        AudioInputStream  audioStream = AudioSystem.getAudioInputStream( file ) ;
        AudioFormat       audioFormat = audioStream.getFormat() ; ;
        SourceDataLine    sourceLine  = null ;
        byte[]            abData      = IOUtils.toByteArray( audioStream ) ;
        
        DataLine.Info info = new DataLine.Info( SourceDataLine.class, audioFormat ) ;
        sourceLine = ( SourceDataLine )AudioSystem.getLine( info ) ;
        sourceLine.open( audioFormat ) ;
        sourceLine.start() ;
        audioStream.read( abData ) ;
        sourceLine.write( abData, 0, abData.length ) ;
        sourceLine.drain() ;
        sourceLine.close() ;
        
        audioStream.close() ;
    }
}
