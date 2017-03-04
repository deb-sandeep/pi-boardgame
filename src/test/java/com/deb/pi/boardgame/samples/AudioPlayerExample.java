package com.deb.pi.boardgame.samples;

import com.deb.pi.boardgame.game.chess.MoveAudioPlayer ;

public class AudioPlayerExample {

	public static void main(String[] args) throws Exception{
		MoveAudioPlayer player = new MoveAudioPlayer();
		player.playSound( "pawn c4 Takes knight E5" );
	}

}
