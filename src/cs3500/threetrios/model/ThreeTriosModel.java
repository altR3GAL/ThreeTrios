package cs3500.threetrios.model;

import cs3500.threetrios.controller.ModelListener;
import cs3500.threetrios.controller.ModelStatus;
import cs3500.threetrios.view.ThreeTriosTextView;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * The model for TripleTriadModel.
 */
public class ThreeTriosModel implements TripleTriadModel, ModelStatus {
  private PlayerStructure redPlayer;
  private PlayerStructure bluePlayer;
  private PlayerStructure currentTurn;
  protected final Cell[][] grid;
  protected Deck deck;
  private boolean gameQuit = false;
  protected Deque<Battle> battleStack;
  private boolean gameStarted;
  protected PlayerStructure winner;
  private final ThreeTriosTextView view;
  private List<ModelListener> listeners = new ArrayList<>();
  private List<PlayerStructure> players = new ArrayList<>();

  /**
   * Constructor that takes in a list of characters
   * (simulates the pulled characters from
   * a configuration file), and creates a deck of random cards.
   * throws IllegalArgumentException if the rows or columns aren't
   * sufficient to create a valid grid.
   * throws IllegalArgumentException if the configuration file doesn't contain
   *  the necessary information to define a valid grid.

   * @param rows is the number of rows being used in the grid
   * @param cols is the number of columns being used in the grid
   * @param cellList is a list of Characters pulled from the config file
   */
  public ThreeTriosModel(int rows, int cols, List<List<String>> cellList) {
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

    this.view = new ThreeTriosTextView(this);
    grid = new Cell[rows][cols];
    createGrid(cellList);
    deck = new Deck(getNumCells(grid));
    gameStarted = false;
  }

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
  public ThreeTriosModel(ConfigurationFileReader fileReader) {
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

    this.view = new ThreeTriosTextView(this);
    grid = new Cell[configRows][configCol];
    createGrid(cellList);

    List<CardImpl> cardList = fileReader.buildDeckList(getNumCells(grid));

    deck = new Deck(cardList);
    gameStarted = false;
  }

  /**
   * Sets up the game.
   * Deals each player their cards and creates the battle stack.
   * throws IllegalStateException is the game already started.
   * throws IllegalStateException if there aren't enough cards in the deck.
   */
  @Override
  public void setUpGame() {
    if (gameStarted) {
      throw new IllegalStateException("Game already started.");
    } else if (deck.deckSize % 2 != 0) {
      throw new IllegalStateException("There aren't enough cards to deal each player "
              + "the same amount.");
    }
    gameStarted = true;
    battleStack = new ArrayDeque<>();
  }

  /**
   * Begins the game following the initial setup of start
   * throws IllegalStateException if there aren't exactly two players.
   */
  public void startGame() {
    if (players.size() != 2) {
      throw new IllegalStateException("There are not enough players to start the game.");
    }
    //Set the colors of the PlayerStructures
    players.get(0).setPlayerColor(Color.RED);
    players.get(1).setPlayerColor(Color.BLUE);

    //Initialize redPLayer and bluePlayer
    redPlayer = players.get(0);
    bluePlayer = players.get(1);

    int handSize = getHandSize();
    dealCards(handSize);

    currentTurn = redPlayer;
    notifyTurn(currentTurn);
  }

  /**
   * Adds the given playerStructure to the game.

   * @param player is the Player's playerStructure.
   */
  public void addPlayer(PlayerStructure player) {
    players.add(player);
  }

  /**
   * Gets the number of cells in the grid,
   * and calculates the number of cards needed to play this grid.

   * @return the handSize for each player.
   */
  private int getHandSize() {
    int numCells = 0;

    for (Cell[] cells : grid) {
      for (Cell cell : cells) {
        if (!cell.isHole()) {
          numCells++;
        }
      }
    }

    return ((numCells + 1) / 2);
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
    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't started.");
    }
    if ((row >= 0 && row < grid.length) && (col >= 0 && col < grid[0].length)) {
      if (!grid[row][col].isHole() && !grid[row][col].hasBeenPlayed()) {
        getCurrentPlayer().playCard(card, grid[row][col]);
        triggerBattles(grid[row][col]);
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
  @Override
  public int calculateScore() {
    int redScore = 0;
    int blueScore = 0;

    for (Cell[] cells : grid) {
      for (Cell cell : cells) {
        if (cell.hasBeenPlayed()) {
          if (cell.getCard().getColor() == Color.RED) {
            redScore++;
          } else {
            blueScore++;
          }
        }
      }
    }

    if (redScore == blueScore) {
      winner = null;
      return redScore;
    } else if (redScore < blueScore) {
      winner = bluePlayer;
      return blueScore;
    } else {
      winner = redPlayer;
      return redScore;
    }

  }

  /**
   * Fill each player's hands with enough cards.
   * throws IndexOutOfBoundsException if the deck doesn't have enough cards in it.
   */
  private void dealCards(int handSize) {
    while (bluePlayer.getHand().size() < handSize) {
      try {
        redPlayer.drawCard(deck.popCard());
        bluePlayer.drawCard(deck.popCard());
      } catch (IndexOutOfBoundsException e) {
        throw new IndexOutOfBoundsException("The deck didn't have an even number "
                + "of cards.");
      }
    }
  }

  /**
   * Gets the number of cells in a provided grid.

   * @param grid is the 2D array of cells.
   * @return the number of cells.
   */
  private int getNumCells(Cell[][] grid) {
    int numCells = 0;

    for (Cell[] row : grid) {
      for (Cell cell : row) {
        if (!cell.isHole()) {
          numCells++;
        }
      }
    }
    return numCells + 1;
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
    return copy;
  }

  /**
   * Checks if the game is over.
   * throws IllegalStateException if the game hasn't started.

   * @return true if the game is over
   */
  @Override
  public boolean isGameOver() {
    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't started.");
    }
    if (gameQuit || gameFinished()) {
      String winner = "";
      if (getScore(Color.RED) == calculateScore()) {
        winner = redPlayer.toString();
      } else if (getScore(Color.BLUE) == calculateScore()) {
        winner = bluePlayer.toString();
      }
      notifyEnd(winner, calculateScore());
    }
    return (gameQuit || gameFinished());
  }

  /**
   * Checks if a game has concluded due to normal play.

   * @return true is the game has finished
   */
  private boolean gameFinished() {
    for (Cell[] cells : grid) {
      for (Cell cell : cells) {
        if (!cell.hasBeenPlayed() && !cell.isHole()) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * When the controller accepts a quit action this method can be used to end the game.
   */
  public void quitGame() {
    gameQuit = true;
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
    notifyTurn(currentTurn);
  }

  /**
   * Helper method that gets called when a card flips or is placed.
   * Creates a new battle for every cell (and hole) adjacent to the triggering cell
   */
  private void triggerBattles(Cell cardTrigger) {
    //Create new battles of the cells adjacent to the triggering cell

    //With the card above
    if (cardTrigger.cellRowPosition() > 0) {
      Battle newAbove = new Battle(cardTrigger,
              grid[cardTrigger.cellRowPosition() - 1][cardTrigger.cellColPosition()]);
      battleStack.add(newAbove);
    }
    //With the card below
    if (cardTrigger.cellRowPosition() < grid.length - 1) {
      Battle newBelow = new Battle(cardTrigger,
              grid[cardTrigger.cellRowPosition() + 1][cardTrigger.cellColPosition()]);
      battleStack.add(newBelow);
    }
    //With the card to the right
    if (cardTrigger.cellColPosition() < grid[0].length - 1) {
      Battle newRight = new Battle(cardTrigger,
              grid[cardTrigger.cellRowPosition()][cardTrigger.cellColPosition() + 1]);
      battleStack.add(newRight);
    }
    //With the card to the left
    if (cardTrigger.cellColPosition() > 0) {
      Battle newLeft = new Battle(cardTrigger,
              grid[cardTrigger.cellRowPosition()][cardTrigger.cellColPosition() - 1]);
      battleStack.add(newLeft);
    }
  }

  /**
   * Resolves battles from the battle stack until no more battles need to be resolved.
   * throws IllegalArgumentException if the game hasn't started.
   */
  @Override
  public void resolveBattles() {
    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't started.");
    }
    //Go through all the battles in the battleStack and resolve them
    while (!battleStack.isEmpty()) {
      //Get the next battle to be resolved
      Battle newBattle = battleStack.pop();

      //Resolve the battle & return true if the attacker won
      boolean resolvedBattle = newBattle.resolveBattle();

      //If the newBattle resulted in the defending card flipping, trigger new battles
      if (resolvedBattle) {
        triggerBattles(newBattle.getDefendingCell());
      }
    }
  }

  @Override
  public String toString() {
    return view.render();
  }

  @Override
  public Color getPlayer(int row, int col) {
    checkBounds(row, col);
    return this.grid[row][col].getCard().getColor();
  }

  @Override
  public boolean canBePlayed(int row, int col) {
    checkBounds(row, col);
    return !this.grid[row][col].hasBeenPlayed() && !this.grid[row][col].isHole();
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
      if (cellRow < rows && possibleCell.getCard().getDown().getValue()
              < card.getUp().getValue()) {
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
    if (((col - 1 < 0) || this.getGameState()[row][col - 1].isHole())
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

  @Override
  public int getScore(Color playerColor) {
    int score = 0;

    for (Cell[] cells : grid) {
      for (Cell cell : cells) {
        if (cell.hasBeenPlayed()) {
          if (cell.getCard().getColor() == playerColor) {
            score++;
          }
        }
      }
    }
    return score;
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


  /**
   * Helper method to help the model obtain the player not currently playing (opponent).
   *
   * @return PlayerStructure of the opponent.
   */
  @Override
  public PlayerStructure getOpponent() {
    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't started.");
    }
    if (getCurrentPlayer() == redPlayer) {
      return bluePlayer;
    } else {
      return redPlayer;
    }
  }

  /**
   * Used by the view to get a copy of the red player's hand.

   * @return the red player's hand.
   */
  @Override
  public List<CardImpl> getRedHand() {
    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't started.");
    }
    return redPlayer.getHand();
  }

  /**
   * Used by the view to get a copy of the blue player's hand.

   * @return the blue player's hand.
   */
  @Override
  public List<CardImpl> getBlueHand() {
    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't started.");
    }
    return bluePlayer.getHand();
  }

  /**
   * gets the size of the grid.
   *
   * @return integer size (rows*cols).
   */
  @Override
  public int getGridSize() {
    return grid.length * grid[0].length;
  }

  /**
   * gets the Card contents of the cell.
   *
   * @param row the row location of the cell.
   * @param col the col location of the cell.
   * @return the Card in the cell.
   */
  @Override
  public CardImpl getCellContents(int row, int col) {
    return grid[row][col].getCard();
  }

  /**
   * gets the cards in the hand of the desired player.
   *
   * @return the list of cards in the hand of player.
   */
  @Override
  public List<CardImpl> getHandContents(PlayerStructure playerStructure) {
    return playerStructure.getHand();
  }

  /**
   * gets the owner of the cell (player).

   * @param row the row location of the cell.
   * @param col the col location of the cell.
   * @return the color of the player that owns the cell.
   */
  @Override
  public Color getCellOwner(int row, int col) {
    return grid[row][col].getCard().getColor();
  }

  @Override
  public void notifyTurn(PlayerStructure player) {
    for (ModelListener listener : listeners) {
      listener.playerTurnHeard(player);
    }
  }

  @Override
  public void notifyEndOfTurn(PlayerStructure player) {
    for (ModelListener listener : listeners) {
      listener.playerEndOfTurnHeard(player);
    }
  }

  @Override
  public void notifyEnd(String winner, int score) {
    for (ModelListener listener : listeners) {
      listener.gameEndHeard(winner, score);
    }
  }

  @Override
  public void addModelListener(ModelListener listener) {
    if (listener != null) {
      listeners.add(listener);
    }
  }

  @Override
  public void removeModelListener(ModelListener listener) {
    listeners.remove(listener);
  }

}
