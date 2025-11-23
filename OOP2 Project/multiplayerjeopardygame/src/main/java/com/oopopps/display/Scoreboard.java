package com.oopopps.display;

import java.util.HashMap;
import java.util.Map;

import com.oopopps.Player;

public class Scoreboard implements ScoreObserver {
    private final Map<String, Integer> scores = new HashMap<>();

    public void update(Player p) {
        scores.put(p.getName(), p.getScore());
    }

    public void initPlayers(java.util.List<Player> players) {
        scores.clear();

        for (Player player : players) {
            scores.put(player.getName(), player.getScore());
        }
    }

    public String render() {
        StringBuilder sb = new StringBuilder();
        sb.append("ScoreBoard\n");

        for (Map.Entry<String, Integer> playerEntry : scores.entrySet()) {
            sb.append(playerEntry.getKey() + ": " + playerEntry.getValue() + "\n");
        }

        return sb.toString();
    }
}
