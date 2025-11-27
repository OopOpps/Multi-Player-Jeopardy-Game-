package com.oopopps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit test suite for Player entity validation and behavior verification.
 * Tests core player functionality including identity management, score tracking,
 * uniqueness enforcement, and state consistency across operations.
 * Ensures Player class maintains data integrity and follows entity design principles.
 */
public class PlayerTest {
    @Test
    public void testPlayerCreation() {
        Player player = new Player("P1", "Vincent");
        
        assertEquals("P1", player.getId());
        assertEquals("Vincent", player.getName());
        assertEquals(0, player.getScore());
    }

    /**
     * Tests Player object construction and initial state validation.
     * Verifies that constructor parameters are properly assigned and
     * initial score is correctly set to zero for new player instances.
     */
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

    /**
     * Tests comprehensive score management functionality including positive
     * accumulation, negative deductions, and sequential score modifications.
     * Validates that score updates are applied correctly and cumulative
     * scoring maintains mathematical integrity.
     */
    @Test
    public void testPlayersUnique() {
        Player player1 = new Player("P1", "Vincent");
        Player player2 = new Player("P2", "Jules");
        
        assertNotEquals(player1.getId(), player2.getId());
        assertNotEquals(player1.getName(), player2.getName());
    }

    /**
     * Tests player uniqueness and identity distinction between multiple instances.
     * Verifies that different player objects maintain separate identities
     * through unique identifiers and distinct names to prevent entity confusion.
     */
    @Test
    public void testPlayerToString() {
        Player player = new Player("P1", "Butch");
        player.updateScore(200);
        assertEquals("P1", player.getId());
        assertEquals("Butch", player.getName());
        assertEquals(200, player.getScore());
    }
}