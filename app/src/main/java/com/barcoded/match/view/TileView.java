package com.barcoded.match.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.annotation.Nullable;

/**
 * This TileView class is used to represent game tiles within the match game. Tiles are implemented
 * as a {@link androidx.appcompat.widget.AppCompatImageView ImageView} with additional information
 * about the location in the grid. <br/>
 * Tiles also have a state that is usable, not usable and empty. This state is represented by the
 * ImageView's boolean {@code clickable} and whether a drawable is present or not.
 *
 * @author Kai Dauberschmidt
 * @version 1.0
 * @since 2021-02-10
 */
public class TileView extends androidx.appcompat.widget.AppCompatImageView {

  /** The column index of the tile. Columns are 1-indexed. */
  private int col;

  /** The row index of the tile. Rows are 0-indexed. */
  private int row;

  public TileView(Context context, int row, int col) {
    super(context);
    this.row = row;
    this.col = col;
  }

  /** Gets the 1-indexed column index of this tile. */
  public int getCol() {
    return col;
  }

  /** Sets the 1-indexed column index of this tile. */
  public void setCol(int col) {
    this.col = col;
  }

  /** Gets the 1-indexed row index of this tile. */
  public int getRow() {
    return row;
  }

  /** Sets the 1-indexed row index of this tile. */
  public void setRow(int row) {
    this.row = row;
  }
}
