package com.barcoded.match.model;

/**
 * This {@code Reverse} class represents another variant of the {@link Board Match game}. The game
 * procedure is basically the same. The difference to a regular game is that the one who picks the
 * last stick lost.
 *
 * @version 1.0
 * @see Board
 * @see Match
 * @since 2021-01-27
 * @author Kai Dauberschmidt
 */
public class Misere extends Match {

    /** The invalid row index */
    private static final int INVALID = -1;

    /**
     * Constructs a <em>reverse</em> Match game. The board is the same as a regular game, therefore we
     * can use the super constructor.
     *
     * @param board The game board to play with.
     * @param isPCOpener {@code true} if player character should start.
     */
    public Misere(int[] board, boolean isPCOpener) {
        super(board, isPCOpener);
    }

    /** {@inheritDoc} */
    @Override
    public void move() throws IllegalStateException {

        // Check if the machine can legally perform a move.
        if (getCurrentPlayer() != Player.NPC || isGameOver()) {
            throw new IllegalStateException("Error! Illegal Move.");
        }

        int[] board = getBoard();
        int amtToRemove;
        int row = getPreferredRow();

        // If there exists no preferred row play regular.
        if (row == INVALID) {
            super.move();

            // Else create a board with an odd amount of rows with exactly a single stick left.
        } else {
            // If the board has an odd amount of single stick rows, remove a whole row, else create one.
            amtToRemove = (hasOddSingleStickRows()) ? board[row] : board[row] - 1;
            board[row] -= amtToRemove;

            // Set the move and swap players.
            setLastMove(new Move(Player.NPC, row, amtToRemove));
            System.out.println(getLastMove());
            setPCPlayer();
        }
    }

    /**
     * Determines the amount of rows that have > 1 stick left. If there is exactly one such row return
     * its index, else return the INVALID index.
     *
     * @return The index of the preferred row if there exists exactly one, or INVALID else.
     */
    private int getPreferredRow() {
        int rows = 0; // the amount of rows with > 1 stick.
        int preferredRow = 0; // the latest index of a preferred row.
        int[] board = getBoard(); // The current game board.

        // Traverse the board.
        for (int i = 0; i < board.length; i++) {

            // If the row has > 1 stick increment the row counter and save the latest index.
            if (board[i] > 1) {
                rows += 1;
                preferredRow = i;
            }
        }
        // If there is exactly one preferred row return its index, else return INVALID:
        return (rows == 1) ? preferredRow : INVALID;
    }

    /**
     * Returns a boolean that indicates whether an odd or even amount of rows with exactly one stick
     * exist on the board. This decides whether the NPC should take all sticks from the row or all but
     * one as the goal is to have an odd amount of rows with just a single stick left.
     *
     * @return {@code true} there is an odd amount of rows with exactly one stick, {@code false} else.
     */
    private boolean hasOddSingleStickRows() {
        int[] board = getBoard(); // The current board.

        // Count the amount of rows with exactly one stick left.
        int rows = 0;
        for (int sticks : board) {
            if (sticks == 1) {
                rows += 1;
            }
        }

        return !(rows % 2 == 0);  // return not even, therefore return odd.
    }
}
