package com.barcoded.match.model;

import androidx.annotation.NonNull;

/**
 * This {@code Move} class is a <em>wrapper class</em> to represent game moves in the {@link Board
 * Match} game. It wraps the {@code row} number and the amount of {@code matches} a {@code player}
 * chose to remove on their last move.
 *
 * <p>The <em>attributes</em> have {@code getters} and {@code setters}, whenever necessary.  
 *
 * @version 1.0
 * @see Board
 * @see Match
 * @see Misere
 * @see Player
 * @since 2021-01-27
 * @author Kai Dauberschmidt
 */
public class Move {

    /** The player that executes the move */
    private Player player;

    /** The row (index), the move was performed on */
    private final int row;

    /** The amount of matches that were removed */
    private final int removedMatches;

    /**
     * Constructs a {@code Move} for either <em>regular</em> or <em>reverse</em> Match games.
     *
     * @param player The player who executes the move.
     * @param row The row to remove matches from.
     * @param removedMatches The amount of matches removed.
     */
    public Move(Player player, int row, int removedMatches) {
        this.player = player;
        this.row = row;
        this.removedMatches = removedMatches;
    }

    /** Gets the Player of the move. */
    public Player getPlayer() {
        return player;
    }
    
    /** Sets the Player of the move. */
    public void setPlayer(Player player) {
        this.player = player; 
    }

    /** Gets the row of the move. */
    public int getRow() {
        return row;
    }

    /** Gets the amount of removed matches of the move. */
    public int getRemovedMatches() {
        return removedMatches;
    }

    /**
     * String representation of a move. This is not necessary for the actual app, but the game logic
     * is being tested within a shell and this method increases readability.
     *
     * @return A string that represents the move.
     */
    @NonNull
    @Override
    public String toString() {
        String plyr = (player == Player.PC) ? "You " : "Player machine ";
        return plyr + "removed " + removedMatches + " stick(s) from row " + (row + 1) + ".";
    }
}
