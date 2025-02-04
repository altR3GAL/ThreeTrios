package cs3500.threetrios.model;


import java.util.List;

/**
 * the interface for all computers/strategies, contains the 3 essential functions:
 * selecting the best move, finding the score of a cell and card, and getting the name
 * of the current strategy.
 */
public interface ComputerPlayers {
  /**
   * Selects the best move based on the player's strategy.
   * If there is a tie, break the tie by selecting the move
   * with the uppermost leftmost coordinate and best card.
   *
   * @return A Move object representing the best move, including the chosen card and position.
   */
  Cell selectBestMove();

  /**
   * Selects and returns the best card.

   * @return the card representation of the best card.
   */
  CardImpl getBestCard();

  /**
   * Calculates a score for a given move to assist in move selection.

   * @param cell the cell that is being queried for the score.
   * @param card the card we are playing to a certain cell.
   * @return An integer score that represents how good the move is.
   */
  int calculateMoveScore(Cell cell, CardImpl card);

  /**
   * Method that obtains all possible playable cells currently in the grid.
   *
   * @return a list of Cells that are playable.
   */
  List<Cell> getAvailableCells();

  /**
   * Gets the name of the strategy implemented by this player.

   * @return The strategy name.
   */
  String getStrategyName();
}
