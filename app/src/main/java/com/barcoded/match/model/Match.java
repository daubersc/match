package com.barcoded.match.model;

/**
 * This {@code Match} class manages the game board and the game logic for a <em>regular</em> Match
 * game.
 *
 * @version 1.0
 * @see Board
 * @since 2021-01-27
 */
public class Match implements Board, Cloneable {

    /** The current {@link Player}. */
    private Player currentPlayer;

    /** The game board. */
    private final int[] BOARD;

    /** the last Move that was made. */
    private Move lastMove;

    /**
     * The constructor of the {@code Match} class. It is used to create game objects with a given
     * board and a given opener.
     *
     * @param board The game board.
     * @param isPCOpener {@code true} if the player should start the game, {@code false} else.
     */
    public Match(int[] board, boolean isPCOpener) {
        this.BOARD = board;
        currentPlayer = (isPCOpener) ? Player.PC : Player.NPC;
    }

    /** {@inheritDoc} */
    @Override
    public int getRowCount() {
        return BOARD.length;
    }

    /** {@inheritDoc} */
    @Override
    public int getMatches(int row) {
        return BOARD[row];
    }

    /** Returns the current player. */
    Player getCurrentPlayer() {
        return currentPlayer;
    }

    /** Returns the game board. */
    int[] getBoard() {
        return BOARD;
    }

    /** Sets the current player to the PC. */
    void setPCPlayer() {
        this.currentPlayer = Player.PC;
    }

    /** Sets the last move. */
    void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }

    /** {@inheritDoc} */
    @Override
    public void move(int row, int sticks) {
        int currentSticks = getMatches(row);
        boolean isIllegal = (currentSticks < sticks) || isGameOver();

        // Throw exceptions if its not the players turn or if the move is illegal.
        if (currentPlayer != Player.PC) {
            throw new IllegalStateException("Not the player's turn.");
        } else if (isIllegal) {
            throw new IllegalArgumentException("Illegal move.");
        } else { // Else make the move.
            BOARD[row] = currentSticks - sticks;
            lastMove = new Move(Player.PC, row, sticks);
            currentPlayer = Player.NPC;
        }
    }

    public int[] showBoard() {
        return BOARD;
    }

    /** {@inheritDoc} */
    @Override
    public void move() {

        // defensive programming: If the game is over or no npc turn throw exception.
        if (currentPlayer != Player.NPC || isGameOver()) {
            throw new IllegalStateException("Illegal Move.");
        }

        int matchValue = getMatchValue();
        int amountToRemove = 0;
        int row = 0;

        if (matchValue == 0) { // Safe move

            // find the first non-empty row.
            while (row < BOARD.length && BOARD[row] == 0) {
                row++;
            }
            amountToRemove = (int) Math.ceil((double) BOARD[row] / 2); // remove half of it.

        } else { // play optimal.
            row = getMatchIndex(matchValue);
            amountToRemove = getAmountToRemove(BOARD[row], matchValue);
        }

        BOARD[row] -= amountToRemove;
        lastMove = new Move(Player.NPC, row, amountToRemove);
        currentPlayer = Player.PC;
    }

    /** {@inheritDoc} */
    @Override
    public Move getLastMove() {
        return lastMove;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isGameOver() {
        int isZero = 0;

        for (int stick : BOARD) {
            isZero += stick;
        }

        return isZero == 0;
    }

    /** {@inheritDoc} */
    @Override
    public Player getWinner() {
        String className = getClass().getSimpleName();
        Player winner;

        if (!isGameOver()) {
            throw new IllegalStateException("Game is not over yet.");
        } else if (className.equals("Match")) {
            winner = lastMove.getPlayer();
        } else {
            winner = (lastMove.getPlayer() == Player.PC) ? Player.NPC : Player.PC;
        }

        return winner;
    }

    /** {@inheritDoc} */
    @Override
    public Board clone() {
        Board copy;
        try {
            copy = (Board) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
        return copy;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        // More efficient than concat in loops (for lots of repetition).
        StringBuilder sb = new StringBuilder();
        int maxRows = BOARD.length;

        for (int row = 0; row < maxRows; row++) {
            // Used for PRINT: PC representation is 1-indexed.
            sb.append(row + 1).append(": ").append(BOARD[row]);
            if (row < maxRows - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Determines the optimal amount to remove. It takes the current value of the board and inverts
     * those bits that are 1-valued within the match value and determines the difference.
     *
     * @param currentValue The current value of the row.
     * @param matchValue The calculated match value.
     * @return The optimal amount to remove.
     */
    private static int getAmountToRemove(int currentValue, int matchValue) {
        int invertedValue = currentValue;

        while (matchValue != 0) {
            int msb = Integer.highestOneBit(matchValue); // Determine the most significant bit.
            matchValue ^= msb; // Call by value!
            invertedValue ^= msb; // continuously invert the current value at msb positions.
        }

        return currentValue - invertedValue;
    }

    /** Calculates the value of a given game board based on bitwise XOR. */
    private int getMatchValue() {
        int match = 0;

        for (int stick : BOARD) {
            match ^= stick;
        }
        return match;
    }

    /**
     * Calculates the lowest row index hosting a significant bit.
     *
     * @param matchValue - the match value that determines the index.
     * @return The zero-indexed row index.
     */
    private int getMatchIndex(int matchValue) {
        int index = 0; // The index of the board.
        int msb = Integer.highestOneBit(matchValue); // determine the msb value of the match at first.

        // Find the index that matches a least or most significant bit.
        while (index < BOARD.length && !isSignificantBit(msb, BOARD[index])) {
            index++;
        }

        return index;
    }

    /**
     * Checks whether a given most significant bit is equal to the most or least significant bit of a
     * position.
     *
     * @param msb The most significant bit to check.
     * @param position The value of this position within the board.
     * @return {@code true} if the msb is the boards msb or lsb.
     */
    private boolean isSignificantBit(int msb, int position) {
        return msb == Integer.highestOneBit(position) || msb == Integer.lowestOneBit(position);
    }
}
