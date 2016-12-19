package com.deb.pi.boardgame.samples;

import com.deb.pi.boardgame.core.util.AudioPlayer ;

public class AudioPlayerExample {

	public static void main(String[] args) throws Exception{
		AudioPlayer player = new AudioPlayer();
		player.playSound( "pawn c4 Takes knight E5" );
	}

}
