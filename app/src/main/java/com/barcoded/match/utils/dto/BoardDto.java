package com.barcoded.match.utils.dto;

import com.barcoded.match.model.Board;

/**
 * This class is a data transfer object (DTO) that contains both a game board as well as two
 * integers that represent the columns and rows of the board. Even though the rows and columns are
 * within the {@link com.barcoded.match.model.Board} implementations, they have to be calculated.
 * They however need to be accessed often in the GUI. therefore we store them separately.
 *
 * @author Kai Dauberschmidt
 * @version 1.0
 * @since 2021-02-03
 */
public class BoardDto {

  // Todo: Check doc and attrs.

  /** The complete game-board of the match game. */
  Board game;

  /** The actual board of the game. */
  int[] board;

  /** The amount of rows. This is dependent on the difficulty. */
  int rows;

  /** The amount of columns. This is dependent on the difficulty. */
  int cols;

  /** Constructs a DTO with a given board, rows and cols. */
  public BoardDto(Board game, int[] board, int rows, int cols) {
    this.game = game;
    this.board = board;
    this.rows = rows;
    this.cols = cols;
  }

  /** Gets the DTO's board. */
  public Board getGame() {
    return game;
  }

  /** Sets the DTO's board. */
  public void setGame(Board game) {
    this.game = game;
  }

  /** Gets the DTO's rows. */
  public int getRows() {
    return rows;
  }

  /** Gets the DTO's board.. */
  public int[] getBoard() {
    return board;
  }

  /** Sets the DTO's rows. */
  public void setRows(int rows) {
    this.rows = rows;
  }

  /** Gets the DTO's columns. */
  public int getCols() {
    return cols;
  }

  /** Sets the DTO's columns. */
  public void setCols(int cols) {
    this.cols = cols;
  }
}
