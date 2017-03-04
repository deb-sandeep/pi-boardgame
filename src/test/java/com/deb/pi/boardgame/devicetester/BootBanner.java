package com.deb.pi.boardgame.devicetester;

import java.io.InputStream ;
import java.nio.charset.Charset ;
import java.util.ArrayList ;
import java.util.List ;

import org.apache.commons.io.IOUtils ;

import com.deb.pi.boardgame.core.device.board.GameBoardHardware ;
import com.deb.pi.boardgame.core.device.board.GameBoardHardware.GlowState ;
import com.pi4j.util.StringUtil ;

public class BootBanner extends Thread {
    
    private static final String BANNER_FRAMES_PATH = 
                        "/com/deb/pi/boardgame/devicetester/banner_frames.txt" ;
    
    private List<GlowState[][]> frames = new ArrayList<GlowState[][]>() ;
    private GameBoardHardware hardware = null ;
    
    BootBanner( GameBoardHardware hardware ) throws Exception {
        
        this.hardware = hardware ;
        
        InputStream is = BootBanner.class.getResourceAsStream( BANNER_FRAMES_PATH ) ;
        List<String> lines = IOUtils.readLines( is, Charset.defaultCharset() ) ;
        readFrames( lines ) ;
    }
    
    private void readFrames( List<String> lines ) {
        
        List<String> frameData = new ArrayList<String>() ;

        for( String line : lines ) {
            if( StringUtil.isNotNullOrEmpty( line ) ) {
                frameData.add( line ) ;
                if( frameData.size() == 3 ) {
                    frames.add( createFrame( frameData ) ) ;
                    frameData.clear() ;
                }
            }
        }
    }
    
    private GlowState[][] createFrame( List<String> frameData ) {
        GlowState[][] frame = {
                { GlowState.OFF, GlowState.OFF, GlowState.OFF },
                { GlowState.OFF, GlowState.OFF, GlowState.OFF },
                { GlowState.OFF, GlowState.OFF, GlowState.OFF }
        } ;
        
        for( int r=0; r<frameData.size(); r++ ) {
            String dataLine = frameData.get( r ) ;
            for( int c=0; c<3; c++ ) {
                char state = dataLine.charAt( c ) ;
                if( state == 'R' ) {
                    frame[r][c] = GlowState.RED ;
                }
                else if( state == 'G' ) {
                    frame[r][c] = GlowState.GREEN ;
                }
            }
        }
        return frame ;
    }
    
    public void run() {
        for( int i=0; i<frames.size(); i++ ) {
            hardware.showPattern( frames.get( i ), 250 ) ;
        }
    }
}
