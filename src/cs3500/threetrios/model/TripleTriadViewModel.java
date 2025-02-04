package cs3500.threetrios.model;

import java.util.List;

/**
 * interface for the view model for the game that is needed.
 */
public interface TripleTriadViewModel {

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

  /**
   * Used by view to get the current player.

   * @return the current player
   */
  PlayerStructure getCurrentPlayer();


  /**
   * Copies the grid and returns it (for viewing only).

   * @return the current state of the game (view only)
   */
  Cell[][] getGameState();
}
