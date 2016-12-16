package com.deb.pi.boardgame.core.gpio;

import com.deb.pi.boardgame.core.gpio.InPin.PullResistanceType ;

public interface GPIOManager {

    public InPin provisionInputPin( int pinNum, PullResistanceType prType ) ;
    
    public OutPin provisionOutputPin( int pinNum ) ;
    
    /**
     * Returns an input pin which corresponds to the given pin number on the 
     * microcontroller. Note that if the pin has not been provisioned beforehand,
     * this method will attempt to provision the pin as an input pin before
     * returning to the user.
     * 
     * Please note that in case this method does the provisioning, the default
     * pin pull resistance is considered to be low. If a high in pull resistance
     * is required, you should be provisioning the pin beforehand using the
     * {@link #provisionInputPin(int, PullResistanceType)} method.
     * 
     * @param pinNum The pin number of the microcontroller or the abstraction
     *        layer that this code deals with.
     *        
     * @return A provisioned input pin.
     * 
     * @throws IllegalArgumentException An exception is raised under the 
     *         following situations:
     *         
     *         1. The pin can't be provisioned
     *         2. The pin has already been provisioned
     */
    public InPin getInputPin( int pinNum ) ;
    
    public OutPin getOutputPin( int pinNum ) ;
    
    public void reset() ;
    
    public void shutdown() ;
}
