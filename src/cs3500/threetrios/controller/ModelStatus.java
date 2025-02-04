package cs3500.threetrios.controller;

import cs3500.threetrios.model.PlayerStructure;

/**
 * Interface for all information about the model during a game
 * that is needed, note it is similar but different to the
 * readonly interface.
 */
public interface ModelStatus {
  /**
   * Notifies the listener that it is the given player's turn.

   * @param player the player whose turn it is.
   */
  void notifyTurn(PlayerStructure player);

  /**
   * Notifies the listener that the player's turn is over.

   * @param player the given player.
   */
  void notifyEndOfTurn(PlayerStructure player);

  /**
   * Notifies the listeners about the end of the game.

   * @param winner the winner's name.
   * @param score the winner's score.
   */
  void notifyEnd(String winner, int score);

  /**
   * Adds a listener for model events.

   * @param listener the listener to add.
   */
  void addModelListener(ModelListener listener);

  /**
   * Removes a listener for model events.

   * @param listener the listener to remove.
   */
  void removeModelListener(ModelListener listener);
}