package com.oopopps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

public class PlayerTest {
    @Test
    public void testPlayerCreation() {
        Player player = new Player("P1", "Vincent");
        
        assertEquals("P1", player.getId());
        assertEquals("Vincent", player.getName());
        assertEquals(0, player.getScore());
    }

    @Test
    public void testPlayerScoring() {
        Player player = new Player("P2", "Jules");
        
        player.updateScore(100);
        assertEquals(100, player.getScore());
        
        player.updateScore(-100);
        assertEquals(0, player.getScore());
        
        player.updateScore(200);
        assertEquals(200, player.getScore());
    }

    @Test
    public void testPlayersUnique() {
        Player player1 = new Player("P1", "Vincent");
        Player player2 = new Player("P2", "Jules");
        
        assertNotEquals(player1.getId(), player2.getId());
        assertNotEquals(player1.getName(), player2.getName());
    }

    @Test
    public void testPlayerToString() {
        Player player = new Player("P1", "Butch");
        player.updateScore(200);
        assertEquals("P1", player.getId());
        assertEquals("Butch", player.getName());
        assertEquals(200, player.getScore());
    }
}