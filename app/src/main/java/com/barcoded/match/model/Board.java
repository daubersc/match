package com.barcoded.match.model;

/**
 * This interface represents a board in the Match game. Two players remove alternately at least one
 * up to at most all sticks from a single row (= pile = heap) of sticks.
 *
 * @author Kai Dauberschmidt
 * @version 1.0
 * @since 2021-02-03
 */
public interface Board extends Cloneable {

    /** Gets the number of rows of the game. */
    int getRowCount();

    /**
     * Gets the amount of sticks of a specific row.
     *
     * @param index The index of the row.
     * @return The number of sticks in the row.
     */
    int getMatches(int index);

    /**
     * Executes a human move.
     *
     * @param row The number of the zero indexed row ascending top down.
     * @param sticks The number of sticks to remove from row {@code row}. Must be at least 1 and at
     *     most the number of sticks of the row.
     * @throws IllegalStateException It is not the human's turn.
     * @throws IllegalArgumentException The provided move is illegal.
     */
    void move(int row, int sticks);

    /**
     * Executes a machine move.
     *
     * @throws IllegalStateException It is not the machine's turn.
     */
    void move();

    /**
     * Gets the last move made on the game.
     *
     * @return The move, i.e. the move's row as well as the amount of removed sticks.
     */
    Move getLastMove();

    /**
     * Checks if the game is over, i.e. one player has won.
     *
     * @return {@code true} if the game is over, {@code false} else.
     */
    boolean isGameOver();

    int[] showBoard();

    /**
     * Checks who has won the game. Should only be called if {@link #isGameOver()} returns {@code
     * true}.
     *
     * @return The winner of the game.
     */
    Player getWinner();

    /** Creates and returns a deep copy of this board. */
    Board clone();

    /**
     * Gets the string representation of the current board with the numbers of sticks per row.
     *
     * @return The string representation of the current game status in lines with ascending row
     *     numbers.
     */
    @Override
    String toString();
}
