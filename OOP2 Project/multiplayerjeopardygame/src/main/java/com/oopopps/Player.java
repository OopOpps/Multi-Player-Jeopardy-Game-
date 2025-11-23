package com.oopopps;

//this class represents a player in the game
public class Player {
    private final String name;
    private int score = 0;
    private final String id;

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public String getName() { return name; }
    public int getScore() { return score; }
    public void updateScore(int delta) { score += delta; }
    public String getId() { return id; }
}
