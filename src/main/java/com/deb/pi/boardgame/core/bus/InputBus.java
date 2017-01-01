package com.deb.pi.boardgame.core.bus;

import java.io.IOException ;

import com.tomgibara.bits.BitVector ;

public interface InputBus {

    public BitVector read() throws IOException ;
}
