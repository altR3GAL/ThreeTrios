package cs3500.threetrios.model;

import java.util.List;

/**
 * This version of model is only for viewing.
 * It allows structures like the View to access game data
 * without the risk of mutating the game state.
 */
public interface ReadOnlyTripleTriadModel {

  /**
   * Copies the grid and returns it (view only).
   * throws IllegalStateException if the game hasn't started.

   * @return the current state of the game (view only)
   */
  Cell[][] getGameState();

  /**
   * Checks if the game is over.
   * throws IllegalStateException if the game hasn't started.

   * @return true if the game is over
   */
  boolean isGameOver();

  /**
   * Function that gets the player/color of card at the desired location.
   *
   * @param row the row that the player/card is at.
   * @param col the column that the player/card is at.
   * @return the Color indicating the player at the location.
   */
  Color getPlayer(int row, int col);

  /**
   * Function that determines if a card can be played at a certain desired location.
   *
   * @param row the row that the card is at.
   * @param col the column that the card is at.
   * @return the boolean value indicating if a card can be played.
   */
  boolean canBePlayed(int row, int col);

  /**
   * Function that gets the amount of possible flips if player plays card at certain location.
   *
   * @param card the card tht we want to check flip count for.
   * @param row the row that the player/card will be played.
   * @param col the column that the player/card will be played.
   * @param visitedCells the list of already visited cells
   * @return the integer value of the amount of possible flips at the location.
   */
  int canBeFlipped(CardImpl card, int row, int col, List<Cell> visitedCells);

  /**
   * Function that gets the score of a certain player at any time in the game.
   *
   * @param playerColor the color of the player that we want to get the score of.
   * @return the integer value of the score of the player
   */
  int getScore(Color playerColor);

  /**
   * Helper method to help the model obtain the player not currently playing (opponent).
   *
   * @return PlayerStructure of the opponent.
   */
  PlayerStructure getOpponent();

  /**
   * Gets the playerStructure associated with the current player.

   * @return the playerStructure.
   */
  PlayerStructure getCurrentPlayer();


  /**
   * gets the size of the grid.
   *
   * @return integer size (rows*cols).
   */
  int getGridSize();

  /**
   * gets the Card contents of the cell.
   *
   * @param row the row location of the cell.
   * @param col the col location of the cell.
   * @return the Card in the cell.
   */
  CardImpl getCellContents(int row, int col);

  /**
   * gets the cards in the hand of the desired player.
   *
   * @return the list of cards in the hand of player.
   */
  List<CardImpl> getHandContents(PlayerStructure playerStructure);

  /**
   * gets the owner of the cell (player).
   *
   * @param row the row location of the cell.
   * @param col the col location of the cell.
   * @return the color of the player that owns the cell.
   */
  Color getCellOwner(int row, int col);

  /**
   * Used by the view to get a copy of the red player's hand.

   * @return the red player's hand.
   */
  List<CardImpl> getRedHand();

  /**
   * Used by the view to get a copy of the blue player's hand.

   * @return the blue player's hand.
   */
  List<CardImpl> getBlueHand();
}
