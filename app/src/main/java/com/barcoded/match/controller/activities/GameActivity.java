package com.barcoded.match.controller.activities;

import android.content.SharedPreferences;
import android.media.MediaParser;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.core.widget.TextViewCompat;
import androidx.preference.PreferenceManager;
import com.barcoded.match.R;
import com.barcoded.match.model.*;
import com.barcoded.match.view.TileView;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameActivity extends BaseActivity {

  /** The concrete instance of the game. */
  private Board game;

  /** The amount of rows present on the gui. */
  private int maxRows;

  /** The amount of columns present on the gui. */
  private int maxCols;

  /** The binary sum */
  private int binarySum;

  /** Maps the coordinates given as a String to the id. */
  private Map<String, Integer> mapCoordsToId;

  /** Maps the row to an id. */
  private Map<Integer, Integer> mapRowToId;

  private boolean isVerbose;

  /** {@inheritDoc} */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // create the maps.
    mapCoordsToId = new HashMap<>();
    mapRowToId = new HashMap<>();


    SharedPreferences prefs =
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    setContentView(R.layout.activity_game);

    // todo: verbose within grid.
    isVerbose = prefs.getBoolean("sw_verbose", false);

    // Create both the game and its visual representation.
    createBoard();

    // Check whether the NPC is the opener and let the npc execute the move in that case.
    boolean isPlayerOpener = prefs.getBoolean("sw_player_opens", true);
    if (!isPlayerOpener) npcMove();
  }

  /**
   * Creates both the actual game as well as the graphical user interface for the game within the
   * {@link GridLayout}.
   */
  private void createBoard() {

    // Create the game logic and return its internal representation.
    int[] board = createGame();

    // Get the GridLayout and clear it first.
    GridLayout grid = findViewById(R.id.board);
    grid.removeAllViews();
    grid.setColumnCount(maxCols);
    grid.setRowCount(maxRows);

    // check for verbose.
    if (isVerbose) {
      maxCols -= 1; // Decrement because first col is verbose.
    }

    // Fill the GridLayout dynamically dependent on the board.
    for (int row = 0; row < board.length; row++) {
      int col = board[row];

      // add binary text if verbose is toggled on.
      if (isVerbose) {
        grid.addView(createVerboseText(row, col));
      }
      int i = 1;

      // Fill new match tiles as long as there are matches expected.
      while (i <= col) {
        grid.addView(createTile(true, row, i));
        i++;
      }

      // Fill empty tiles when there are no matches expected.
      while (i <= maxCols) {
        grid.addView(createTile(null, row, i));
        i++;
      }
    }

    // if verbose add sum.
    if (isVerbose) {
      grid.addView(createVerboseText(maxRows, calculateSum()));
    }
  }

  /**
   * Calculates the binary sum of a board.
   */
  private int calculateSum() {
    int rows = game.getRowCount();
    int sum = 0;

    for (int row = 0; row < rows; row++) {
      int rowValue = game.getMatches(row);
      sum ^= rowValue;
    }

    return sum;
  }

  /**
   * Creates a {@link TextView} that shows the {@code rowValue} as a binary string.
   *
   * @param rowValue The int value to convert to binary string.
   * @param rowIndex The index of that row.
   * @return the actual {@link TextView}.
   */
  private TextView createVerboseText(int rowIndex, int rowValue) {
    TextView textView = new TextView(this);

    textView.setText(toBinaryString(rowValue));

    // Manage id.
    int id = View.generateViewId();
    textView.setId(id);
    mapRowToId.put(rowIndex, id);

    textView.setTextAppearance(R.style.TextAppearance_AppCompat_Small);
    textView.setLayoutParams(createLayoutParams(GridLayout.FILL));
    textView.setGravity(Gravity.CENTER);

    return textView;
  }

  /**
   * Updates the {@link TextView} with a given id to a given value.
   *
   * @param id the TextView's id.
   * @param value the new value as int.
   */
  private void updateValue(int id, int value) {
    TextView textView = findViewById(id);
    textView.setText(toBinaryString(value));
  }

  /**
   * Creates and returns {@link GridLayout.LayoutParams} with an undefined start, a dynamic gravity
   * and a weight of 1.
   *
   * @param gravity The dynamic Gravity
   * @return the LayoutParams.
   */
  private GridLayout.LayoutParams createLayoutParams(GridLayout.Alignment gravity) {
    // Define the spec, that is layout_weight="1" for both row and col.
    GridLayout.Spec spec = GridLayout.spec(GridLayout.UNDEFINED, gravity, 1f);
    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(spec, spec);

    // Set the width and height to both 0dp, so that it scales.
    layoutParams.width = 0;
    layoutParams.height = 0;

    return layoutParams;
  }

  /**
   * Creates a Match or Misere game based on the {@link SharedPreferences} and returns concrete
   * board.
   *
   * @return Returns the concrete board as a int[] array.
   */
  private int[] createGame() {
    SharedPreferences prefs =
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    // Extract the game settings.
    boolean isMisere = prefs.getBoolean("sw_misere", false);
    boolean isPlayer = prefs.getBoolean("sw_player_opens", true);
    String difficulty = prefs.getString("list_difficulty", "df_moderate");

    switch (difficulty) {
        // Easy game: Up to 3 rows and 5 columns.
      case "df_easy":
        maxRows = 3;
        maxCols = 5;
        break;

        // Hard game: Up to 7 rows and 9 columns.
      case "df_hard":
        maxRows = 7;
        maxCols = 9;
        break;

        // Moderate game: Up to 5 rows and 7 columns.
      case "df_moderate":
      default:
        maxRows = 5;
        maxCols = 7;
        break;
    }

    // Create the actual board: Insert random amounts of matches.
    int[] board = new int[maxRows];
    Random rnd = new Random();
    for (int i = 0; i < board.length; i++) {
      // each row has between [0, cols] sticks.
      board[i] = rnd.nextInt(maxCols + 1);
    }

    // Increment to add space for verbose.
    if (isVerbose) {
      maxCols += 1;
      maxRows += 1;
    }

    // Create a game based on the game mode, the opener and the previous board.
    game = isMisere ? new Misere(board, isPlayer) : new Match(board, isPlayer);

    // Wrap and return the information.
    return board;
  }

  /**
   * Creates a tile represented as an {@link TileView}. Each tile can have 3 states that is
   * represented by a nullable {@link Boolean}:
   *
   * <ul>
   *   <li><em>empty</em>: no image present and not clickable. The boolean is null.
   *   <li><em>isClickable</em>: regular image present and clickable. The boolean is true.
   *   <li><em>notClickable</em>: burned image present and not clickable. The boolean is false.
   * </ul>
   *
   * @param isClickable The nullable {@link Boolean} that decides the state as mentioned in the
   *     description.
   * @return The tile as an TileView.
   */
  private TileView createTile(Boolean isClickable, int row, int col) {
    TileView tile = new TileView(this, row, col);

    // Generate, set and map the id.
    int id = View.generateViewId();
    tile.setId(id);
    mapCoordsToId.put(getCoordinatesAsKey(row, col), id);

    tile.setLayoutParams(createLayoutParams(GridLayout.FILL));

    // Global Params of the tiles.
    String alt; // The alternative text to the image.

    // State: empty - no image, no id, not clickable.
    if (isClickable == null) {
      alt = "empty";
      tile.setClickable(false);

      /*
       * States:
       * - isClickable:clickable, regular image.
       * - notClickable: not clickable, used image.
       */
    } else {

      // Set clickable and action.
      tile.setClickable(isClickable);
      if (isClickable) {
        tile.setOnClickListener(onClickMove());
      }

      // Determine the image and alt text.
      int imgId = isClickable ? R.drawable.ic_match : R.drawable.ic_match_b;
      alt = isClickable ? "usable match" : "not usable match";
      tile.setImageDrawable(getDrawable(imgId));
    }

    tile.setContentDescription(alt);

    return tile;
  }

  /**
   * Converts a given {@code value} to a binary string with leading 0 equal to the highest number
   * to possibly represent in the board.
   *
   * @param value The value to represent.
   * @return The {@code value} as binary string.
   */
  private String toBinaryString(int value) {

    // Determine the highest amount of digits of the *game* and not the *board* using base 2 log.
    int digits = (int) (Math.floor(Math.log(maxCols) /Math.log(2)) + 1);

    // Create a format regex.
    String regex = "%" + digits + "s";

    // Binary representation according to the regex.
    String binaryString = Integer.toBinaryString(value);
    binaryString = String.format(regex, binaryString).replaceAll(" ", "0");
    return binaryString;
  }

  /**
   * Updates the tiles visually dependent on executed move. If the last move was executed by the
   * Player, then the tiles will be wiped, if it was executed by the NPC they will get a different
   * image.
   *
   * @param move The move that was made.
   */
  private void updateTiles(Move move) {

    // Determine whether the tiles should be updated to empty or burned.
    boolean isEmpty = move.getPlayer().equals(Player.PC);

    // Get the stats of the last move.
    int row = move.getRow();
    int toRemove = move.getRemovedMatches();
    int matchesLeft = game.getMatches(row);

    // If verbose update row value and binary sum.
    if (isVerbose) {
      // Update row.
      Integer id = mapRowToId.get(row);
      if (id != null) {
        updateValue(id, matchesLeft);
      }

      id = mapRowToId.get(maxRows);
      if (id != null) {
        updateValue(id, calculateSum());
      }
    }

    // The column index of the first tile to update.
    int col = matchesLeft + 1;

    // if to clear, clear whole row.
    if (isEmpty) {
      while (col <= maxCols) {
        Integer tileId = mapCoordsToId.get(getCoordinatesAsKey(row, col));
        if (tileId != null) {
          updateTile(tileId, isEmpty); // Update the tile to show a burned match.
        } else {
          throw new IllegalStateException("no tile at coordinates (" + row + "," + col + ") found");
        }
        col++;
      }

    } else {
      for (int i = 0; i < toRemove; i++) {
        Integer tileId = mapCoordsToId.get(getCoordinatesAsKey(row, col));
        if (tileId != null) {
          updateTile(tileId, isEmpty); // Update the tile to show a burned match.
        } else {
          throw new IllegalStateException("no tile at coordinates (" + row + "," + col + ") found");
        }
        col++;
      }
    }
  }

  /** Helper method to get the String coordinate representation given a row and column. */
  private String getCoordinatesAsKey(int row, int col) {
    return "( " + row + "," + col + " )";
  }

  /**
   * Helper Method to visually update a tile based on its id. It changes its state according to who
   * made the move and so on:
   *
   * <ul>
   *   <li>isClickable -> notClickable
   *   <li>isClickable, notClickable -> empty
   * </ul>
   *
   * The states again are:
   *
   * <ul>
   *   <li><em>isClickable</em>: clickable, regular image.
   *   <li><em>notClickable</em>: not clickable, burned image.
   *   <li><em>empty</em>: not clickable, no image.
   * </ul>
   *
   * @param id The id of the non-empty tile.
   * @param isEmpty determines whether to show a used match or not.
   */
  private void updateTile(int id, boolean isEmpty) {
    TileView tile = findViewById(id);

    // both destination states are not clickable.
    tile.setClickable(false);

    // Adjust the image dependent on destination state.
    if (isEmpty) {
      tile.setImageDrawable(null);
    } else {
      tile.setImageDrawable(getDrawable(R.drawable.ic_match_b));
    }
  }

  /**
   * Gets the column of a tile. These are one-indexed.
   *
   * @param id The tile's id.
   * @return The 1-indexed column of that tile.
   */
  private int getCol(int id) {
    int col = id % maxCols;

    // If the column is multiple of the highest column, return this one.
    return (col == 0) ? maxCols : col;
  }

  /**
   * Gets the row value for a tile. The rows are 0 indexed.
   *
   * @param id The id of the tile.
   * @return the zero-indexed row of that tile.
   */
  private int getRow(int id) {
    TileView tile = findViewById(id);
    return tile.getRow();
  }

  // fixme: sometimes randomly index out of bounds?!
  /**
   * On click method to perform a PC move.
   *
   * @param match The match that was clicked on.
   */
  private void move(View match) {
    try {
      // Get the row and column of the current tile.
      int currentTile = match.getId();
      int row = getRow(currentTile);
      int col = getCol(currentTile);

      // Determine which tiles are left to remove.
      int toRemove = 0;
      boolean clickable;
      do {
        clickable = findViewById(currentTile).isClickable();
        if (clickable) toRemove++;
        currentTile++;
        col++;
      } while (clickable && col <= maxCols);

      // Get last move and adjust player for wiping the previous npc move along.
      Move previousMove = game.getLastMove();
      if (previousMove != null) previousMove.setPlayer(Player.PC);

      System.out.println("Removing " + toRemove + "matches from row " + row);
      game.move(row, toRemove);

      if (game.isGameOver()) { // Check for winning conditions.
        showWinner();
      } else { // Else update and make a machine move.
        System.out.println("Previously:" + previousMove);
        if (previousMove != null) updateTiles(previousMove);
        updateTiles(game.getLastMove());
        npcMove();
        System.out.println("NPC:" + game.getLastMove().toString());
      }
    } catch (IllegalStateException | IllegalArgumentException | IndexOutOfBoundsException e) {
      // todo: notify user
      e.printStackTrace();
    }
  }

  /**
   * Executes a machine move. Checks for winning conditions: either shows the winner fragment or the
   * last move on this activity.
   */
  private void npcMove() {
    game.move();
    if (game.isGameOver()) {
      showWinner();
    } else { // else visually indicate the move.
      updateTiles(game.getLastMove());
    }
  }

  /**
   * Returns a {@link android.view.View.OnClickListener} that implements the PC move on click of
   * that view.
   */
  private View.OnClickListener onClickMove() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // todo: move and animation.
        System.out.println("clicked tile with id:" + v.getId() + ".");
        move(v);
        updateTile(v.getId(), true);
      }
    };
  }

  /**
   * Replace the game board with a game over overlay by adjusting the visibility from visible to
   * gone and vice versa for its elements.
   */
  private void showWinner() {
    int result = (game.getWinner() == Player.PC) ? R.string.gameOver_win : R.string.gameOver_lose;

    // hide the game.
    findViewById(R.id.board).setVisibility(View.GONE); // GridLayout: the board.
    findViewById(R.id.logo_small).setVisibility(View.GONE); // Small logo.
    findViewById(R.id.btn_game_back).setVisibility(View.GONE); // Ico back button.

    // Show the game over overlay.
    findViewById(R.id.logo_big).setVisibility(View.VISIBLE); // Show big logo.
    findViewById(R.id.btn_go_back).setVisibility(View.VISIBLE); // Show game over button.

    // Show result.
    TextView text = findViewById(R.id.gameOver);
    text.setText(result);
    text.setVisibility(View.VISIBLE);
  }
}
