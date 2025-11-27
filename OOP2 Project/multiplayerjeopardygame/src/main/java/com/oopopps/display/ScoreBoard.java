package com.oopopps.display;

import java.util.HashMap;
import java.util.Map;

import com.oopopps.Player;

/**
 * Displays and maintains current scores for all players in the game.
 * Implements the Observer pattern to receive score updates from players.
 * 
 * This class provides a real-time view of player scores and can
 * render them in a formatted string representation.
 */
public class ScoreBoard implements ScoreObserver {
    private final Map<String, Integer> scores = new HashMap<>();

    /**
     * Updates the score for a specific player.
     * Called automatically when observed players change their scores.
     * 
     * @param p the player whose score has changed
     */
    public void update(Player p) {
        scores.put(p.getName(), p.getScore());
    }

    /**
     * Initializes the scoreboard with all players at the start of the game.
     * 
     * @param players the list of players participating in the game
     */
    public void initPlayers(java.util.List<Player> players) {
        scores.clear();

        for (Player player : players) {
            scores.put(player.getName(), player.getScore());
        }
    }

    /**
     * Renders the current scores in a human-readable format.
     * 
     * @return a formatted string showing all players and their current scores
     */
    public String render() {
        StringBuilder sb = new StringBuilder();
        sb.append("ScoreBoard\n");

        for (Map.Entry<String, Integer> playerEntry : scores.entrySet()) {
            sb.append(playerEntry.getKey() + ": " + playerEntry.getValue() + "\n");
        }

        return sb.toString();
    }
}
