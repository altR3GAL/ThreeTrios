package cs3500.threetrios.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The mock class for the three trios game model. Similar to the model but only with
 * the essential functions needed and added logging functionality to track what cells
 * the strategies visit.
 */
public class MockThreeTriosModel implements TripleTriadModel {
  private final PlayerStructure redPlayer;
  private final PlayerStructure bluePlayer;
  private PlayerStructure currentTurn;
  protected final Cell[][] grid;
  protected Deck deck;
  private boolean gameStarted;
  protected List<List<String>> log;

  /**
   * Constructor that takes a configuration file reader, and creates the grid
   * and deck with it.
   * throws IllegalArgumentException if the rows or columns aren't sufficient
   * to create a valid grid.
   * throws IllegalArgumentException if the configuration file doesn't contain
   *  the necessary information to define a valid grid.

   * @param fileReader is a file reading class designed to translate
   *                   configuration files into usable information for creating a ThreeTrios game
   */
  public MockThreeTriosModel(ConfigurationFileReader fileReader) {
    int configRows = fileReader.getNumRows();
    int configCol = fileReader.getNumColumns();

    if (configRows < 1 || configCol < 1) {
      throw new IllegalArgumentException("There are not enough rows or columns to "
              + "create a grid.");
    }

    List<List<String>> cellList = fileReader.buildCellList();

    if (cellList.isEmpty()) {
      throw new IllegalArgumentException("The Configuration File could not be read or "
              + "is empty.");
    } else if (cellList.size() != configRows || cellList.get(0).size() != configCol) {
      throw new IllegalArgumentException("The data contained in the Configuration File "
              + "cannot be used to create a grid with the required size");
    }

    redPlayer = new PlayerStructure(Color.RED);
    bluePlayer = new PlayerStructure(Color.BLUE);
    currentTurn = redPlayer;
    grid = new Cell[configRows][configCol];
    createGrid(cellList);
    log = new ArrayList<>();
    List<CardImpl> cardList = fileReader.buildDeckList(10);
    deck = new Deck(cardList);
    gameStarted = true;
    int handSize = 0;
  }


  /**
   * Constructor that creates a new MockThreeTriosModel with given row and column dimensions
   * along with an accompanying list of cell states.

   * @param rows the int representation of the height of the grid.
   * @param cols the int representation of the length of the grid.
   * @param cellList a list of list of strings which represent the individual cells and what
   *                 type they are (hole or cell).
   */
  public MockThreeTriosModel(int rows, int cols, List<List<String>> cellList) {
    if (rows < 1 || cols < 1) {
      throw new IllegalArgumentException("There are not enough rows or columns to"
              + " create a grid.");
    } else if (cellList.isEmpty()) {
      throw new IllegalArgumentException("The Configuration File could not be"
              + " read or is empty.");
    } else if (cellList.size() != rows || cellList.get(0).size() != cols) {
      throw new IllegalArgumentException("The data contained in the Configuration File "
              + "cannot be used to create a grid with the required size");
    }
    redPlayer = new PlayerStructure(Color.RED);
    bluePlayer = new PlayerStructure(Color.BLUE);
    currentTurn = redPlayer;
    grid = new Cell[rows][cols];
    createGrid(cellList);
    log = new ArrayList<>();
    deck = new Deck(10);
    gameStarted = true;
    int handSize = 0;
  }

  /**
   * Starts the game.
   * Deals each player their cards and creates the battle stack.
   * throws IllegalStateException is the game already started.
   * throws IllegalStateException if there aren't enough cards in the deck.
   */
  @Override
  public void setUpGame() {
    //Mock doesn't need to start the game.
  }

  /**
   * Plays a card for the current player at the given cell coordinate.
   * throws IllegalStateException if the game hasn't started.
   * throws IllegalArgumentException if the cell is invalid or doesn't exist in the grid.

   * @param card is the card being played
   * @param row is the row being played to
   * @param col is the column being played to
   */
  @Override
  public void playCard(CardImpl card, int row, int col) {
    if ((row >= 0 && row < grid.length) && (col >= 0 && col < grid[0].length)) {
      if (!grid[row][col].isHole() && !grid[row][col].hasBeenPlayed()) {
        getCurrentPlayer().playCard(card, grid[row][col]);
      } else {
        throw new IllegalArgumentException("This cell is a hole or has already been played to");
      }
    } else {
      throw new IllegalArgumentException("Row and/or column is out of bounds");
    }
  }

  /**
   * Now that the game is over, count the number of red tiles and the number
   * of blue tiles.
   * Set the winning player.

   * @return the number of tiles for the winning player
   */
  public int calculateScore() {
    return 0;
  }

  /**
   * Helper method that creates the grid using a list of Xs and Cs from a config file.
   */
  private void createGrid(List<List<String>> cellList) {
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[row].length; col++) {
        if (cellList.get(row).get(col).equals("C")) { //Create a Cell here
          grid[row][col] = new Cell(row, col, false);
        } else { //Create a hole here
          grid[row][col] = new Cell(row, col, true);
        }
      }
    }
  }

  /**
   * Copies the grid and returns it (for viewing only).
   * throws IllegalStateException if the game hasn't started.

   * @return the current state of the game (view only)
   */
  @Override
  public Cell[][] getGameState() {
    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't started.");
    }
    Cell[][] copy = new Cell[grid.length][];
    for (int row = 0; row < grid.length; row++) {
      copy[row] = Arrays.copyOf(grid[row], grid[row].length);
    }
    List<String> visitString = new ArrayList<>();
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[row].length; col++) {
        // Assuming isVisited() returns the value of the visited field.
        if (grid[row][col].visited) {
          visitString.add("visited cell at: " + grid[row][col].cellRowPosition()
                  + " " + grid[row][col].cellColPosition());
        }
      }
    }
    log.add(visitString);
    return copy;
  }

  /**
   * Checks if the game is over.
   * throws IllegalStateException if the game hasn't started.

   * @return true if the game is over
   */
  @Override
  public boolean isGameOver() {
    return false;
  }

  /**
   * Checks if a game has concluded due to normal play.

   * @return true is the game has finished
   */
  private boolean gameFinished() {
    return false;
  }

  /**
   * When the controller accepts a quit action this method can be used to end the game.
   */
  public void quitGame() {
    boolean gameQuit = true;
  }

  /**
   * Gets the current player.
   * throws IllegalStateException if the game hasn't started.

   * @return the current player
   */
  @Override
  public PlayerStructure getCurrentPlayer() {
    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't started.");
    }
    return currentTurn;
  }

  @Override
  public int getGridSize() {
    return 0;
  }

  @Override
  public CardImpl getCellContents(int row, int col) {
    return null;
  }

  @Override
  public List<CardImpl> getHandContents(PlayerStructure playerStructure) {
    return List.of();
  }

  @Override
  public Color getCellOwner(int row, int col) {
    return null;
  }

  @Override
  public List<CardImpl> getRedHand() {
    return List.of();
  }

  @Override
  public List<CardImpl> getBlueHand() {
    return List.of();
  }

  @Override
  public PlayerStructure getOpponent() {
    if (getCurrentPlayer() == redPlayer) {
      return bluePlayer;
    } else {
      return redPlayer;
    }
  }

  /**
   * Changes the current turn to the next player
   * throws IllegalStateException if the game hasn't started.
   */
  @Override
  public void switchTurn() {
    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't started.");
    }
    if (getCurrentPlayer() == redPlayer) {
      currentTurn = bluePlayer;
    } else {
      currentTurn = redPlayer;
    }
  }

  /**
   * Resolves battles from the battle stack until no more battles need to be resolved.
   * throws IllegalArgumentException if the game hasn't started.
   */
  @Override
  public void resolveBattles() {
    //Mock doesn't need to resolve battles
  }

  @Override
  public String toString() {
    return "";
  }

  @Override
  public Color getPlayer(int row, int col) {
    checkBounds(row, col);
    return this.grid[row][col].getCard().getColor();
  }

  @Override
  public boolean canBePlayed(int row, int col) {
    return false;
  }

  @Override
  public int canBeFlipped(CardImpl card, int row, int col, List<Cell> visitedCells) {
    if (visitedCells.contains(this.getGameState()[row][col])) {
      return 0;
    }
    visitedCells.add(this.getGameState()[row][col]);

    int score = 0;
    Color color = this.getCurrentPlayer().color;
    List<Cell> possibleCells = getPossibleCells(this.getGameState()[row][col], color);

    int rows = this.getGameState()[row][col].cellRowPosition();
    int cols = this.getGameState()[row][col].cellColPosition();

    for (Cell possibleCell : possibleCells) {
      if (possibleCell.getCard() == null) {
        continue;
      }
      int cellRow = possibleCell.cellRowPosition();
      int cellCol = possibleCell.cellColPosition();

      if (cellRow > rows && possibleCell.getCard().getUp().getValue() < card.getDown().getValue()) {
        score++;
        score += canBeFlipped(possibleCell.getCard(), possibleCell.cellRowPosition(),
                possibleCell.cellColPosition(), visitedCells);
      }
      if (cellCol > cols && possibleCell.getCard().getLeft().getValue()
              < card.getRight().getValue()) {
        score++;
        score += canBeFlipped(possibleCell.getCard(), possibleCell.cellRowPosition(),
                possibleCell.cellColPosition(), visitedCells);
      }
      if (cellRow < rows && possibleCell.getCard().getDown().getValue() < card.getUp().getValue()) {
        score++;
        score += canBeFlipped(possibleCell.getCard(), possibleCell.cellRowPosition(),
                possibleCell.cellColPosition(), visitedCells);
      }
      if (cellCol < cols && possibleCell.getCard().getRight().getValue()
              < card.getLeft().getValue()) {
        score++;
        score += canBeFlipped(possibleCell.getCard(), possibleCell.cellRowPosition(),
                possibleCell.cellColPosition(), visitedCells);
      }
    }
    return score;
  }

  /**
   * Helper function that gets all cells adjacent to a given cell that contain
   * a card belonging to the opponent.

   * @param cell the center cell that wants to find all adjacent opponent cells.
   * @param color the color of the center cell.
   * @return a list of cells that are adjacent and contain card belonging to opponent.
   */
  private List<Cell> getPossibleCells(Cell cell, Color color) {
    List<Cell> possibleCells = new ArrayList<>();
    if (cell == null) {
      return possibleCells;
    }
    int row = cell.cellRowPosition();
    int col = cell.cellColPosition();
    if (((col - 1 < 0) || !this.getGameState()[row][col - 1].isHole())
            && this.getGameState()[row][col - 1].hasBeenPlayed()
            && !this.getPlayer(row, col - 1).equals(color)) {
      possibleCells.add(this.getGameState()[row][col - 1]);
    }
    if (((row - 1 < 0) || this.getGameState()[row - 1][col].isHole())
            && this.getGameState()[row - 1][col].hasBeenPlayed()
            && !this.getPlayer(row - 1, col).equals(color)) {
      possibleCells.add(this.getGameState()[row - 1][col]);
    }
    if (((row + 1 >= this.getGameState()[0].length) || this.getGameState()[row + 1][col].isHole())
            && this.getGameState()[row + 1][col].hasBeenPlayed()
            && !this.getPlayer(row + 1, col).equals(color)) {
      possibleCells.add(this.getGameState()[row + 1][col]);
    }
    if (((col + 1 >= this.getGameState().length) || this.getGameState()[row][col + 1].isHole())
            && this.getGameState()[row][col + 1].hasBeenPlayed()
            && !this.getPlayer(row, col + 1).equals(color)) {
      possibleCells.add(this.getGameState()[row][col + 1]);
    }
    return possibleCells;
  }

  /**
   * helper method to check if the coordinates are within the grid.

   * @param row the row of the coordinates.
   * @param col the col of the coordinates.
   */
  private void checkBounds(int row, int col) {
    int length = this.grid.length; // row
    int width = this.grid[0].length; // col
    if (row < 0 || col < 0 || row >= length || col >= width) {
      throw new IllegalArgumentException("row or col OB");
    }
  }

  @Override
  public int getScore(Color playerColor) {
    return 0;
  }
}
