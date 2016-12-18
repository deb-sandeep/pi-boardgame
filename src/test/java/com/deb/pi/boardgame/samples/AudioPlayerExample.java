package com.deb.pi.boardgame.samples;

import com.deb.pi.boardgame.core.audio.AudioPlayer;

public class AudioPlayerExample {

	public static void main(String[] args) throws Exception{
		AudioPlayer player = new AudioPlayer();
		player.playSound( "pawn C4 takes knight E5" );
	}

}
