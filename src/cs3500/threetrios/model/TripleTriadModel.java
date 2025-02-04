package cs3500.threetrios.model;

/**
 * TripleTriad ruleSet interface.
 * Provides the basic aspects/rules of the TripleTriad game.
 */
public interface TripleTriadModel extends ReadOnlyTripleTriadModel {
  /**
   * Starts the game.
   * Deals each player their cards and creates the battle stack.
   * throws IllegalStateException is the game already started.
   * throws IllegalStateException if there aren't enough cards in the deck.
   */
  void setUpGame();

  /**
   * Plays a card for the current player at the given cell coordinate.
   * throws IllegalStateException if the game hasn't started.
   * throws IllegalArgumentException if the cell is invalid or doesn't exist in the grid.

   * @param card is the card being played
   * @param row is the row being played to
   * @param col is the column being played to
   */
  void playCard(CardImpl card, int row, int col);

  /**
   * Resolves battles from the battle stack until no more battles need to be resolved.
   * throws IllegalArgumentException if the game hasn't started.
   */
  void resolveBattles();

  /**
   * When the controller accepts a quit action this method can be used to end the game.
   */
  void quitGame();

  /**
   * Now that the game is over, count the number of red tiles and the number of blue tiles.
   * Set the winning player.

   * @return the number of tiles for the winning player
   */
  int calculateScore();

  /**
   * Changes the current turn to the next player
   * throws IllegalStateException if the game hasn't started.
   */
  void switchTurn();

}
