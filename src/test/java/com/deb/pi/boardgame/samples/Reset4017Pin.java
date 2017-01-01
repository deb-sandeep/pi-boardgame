package com.deb.pi.boardgame.samples;

import com.deb.pi.boardgame.core.gpio.OutPin;
import com.deb.pi.boardgame.core.gpio.AbstractPin.State;
import com.deb.pi.boardgame.core.util.ObjectFactory;

public class Reset4017Pin {

	public static void main(String[] args) {

		OutPin reset = ObjectFactory.instance().getGPIOManager().getOutputPin( 7 );
		reset.setState( State.HIGH );
	}

}
