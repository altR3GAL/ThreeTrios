package cs3500.threetrios.controller;

import cs3500.threetrios.model.PlayerStructure;

/**
 * interface for the model listener and it's specific functionalities.
 */
public interface ModelListener {
  /**
   * Listener for when player knows it is their turn and the game has notified the listener.

   * @param player the player whose turn it is.
   */
  void playerTurnHeard(PlayerStructure player);

  /**
   * Listener for when the player's turn is over.

   * @param player is the structure of the player whose turn ended.
   */
  void playerEndOfTurnHeard(PlayerStructure player);

  /**
   * Listener for when the game notifies the player that the game has ended.

   * @param winner the name of the winner.
   * @param score the winner's score.
   */
  void gameEndHeard(String winner, int score);
}
