package com.deb.pi.boardgame.core.audio;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class AudioPlayer {
	
	private final int BUFFER_SIZE = 128000;
	private final String RES_PATH = "/com/deb/pi/boardgame/core/audio";
    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;
    
    private Map<String, File> database = null;
    
    public AudioPlayer() {
    	database = new HashMap<String, File>();
    	buildDatabase( RES_PATH );
    }

    public void playSound( String input ) throws Exception {

    	String[] buffer = input.split( " " );
    	
    	for( String s : buffer ) {
	        soundFile = database.get( s );
	
	        audioStream = AudioSystem.getAudioInputStream(soundFile);
	
	        audioFormat = audioStream.getFormat();
	
	        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
	        sourceLine = (SourceDataLine) AudioSystem.getLine(info);
	        sourceLine.open(audioFormat);
	
	        sourceLine.start();
	
	        int nBytesRead = 0;
	        byte[] abData = new byte[BUFFER_SIZE];
	        while (nBytesRead != -1) {
	                nBytesRead = audioStream.read(abData, 0, abData.length);
	            if (nBytesRead >= 0) {
	                @SuppressWarnings("unused")
	                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
	            }
	        }
	
	        sourceLine.drain();
    	}
	        sourceLine.close();
    }
    
    private void buildDatabase( String dir ) {
		File folder = new File( AudioPlayer.class.getResource( RES_PATH ).getPath() );;

		for( File file : folder.listFiles() ) {
			if( file.isDirectory() ) {
				buildDatabase( dir + "/" + file.getName() );
			}
			else {
				if( file.getName().endsWith( ".wav" ) ) {
					// remove the .wav extension
					StringBuilder str = new StringBuilder( file.getName() );
					str.delete( str.length()-4, str.length() );
					// put the file in the database
					database.put( str.toString(), file );
				}
			}
		}
	}
}
