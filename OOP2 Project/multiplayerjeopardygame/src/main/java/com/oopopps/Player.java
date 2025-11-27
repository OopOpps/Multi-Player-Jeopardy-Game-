package com.oopopps;

/**
 * Represents a player in the Jeopardy game.
 * Manages player identity, score tracking, and provides access to player data.
 */
public class Player {
    private final String name;
    private int score = 0;
    private final String id;

    /**
     * Constructs a new Player with the given ID and name.
     * 
     * @param id the unique identifier for this player
     * @param name the display name of the player
     */
    public Player(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     * Gets the player's display name.
     * 
     * @return the player's name
     */
    public String getName() { return name; }
    
    /**
     * Gets the player's current score.
     * 
     * @return the current score
     */
    public int getScore() { return score; }
    
    /**
     * Updates the player's score by adding the specified delta.
     * 
     * @param delta the points to add (can be negative)
     */
    public void updateScore(int delta) { score += delta; }
    
    /**
     * Gets the player's unique identifier.
     * 
     * @return the player ID
     */
    public String getId() { return id; }
}
