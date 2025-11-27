package com.oopopps.display;
import com.oopopps.Player;

/**
 * Defines the contract for objects that need to be notified when player scores change.
 * Implements the Observer pattern for score tracking.
 */
public interface ScoreObserver {
    
    /**
     * Called when a player's score has been updated.
     * 
     * @param p the player whose score has changed
     */
    void update(Player p);
}
