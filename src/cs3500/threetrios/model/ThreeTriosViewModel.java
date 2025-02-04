package cs3500.threetrios.model;

import java.util.List;

/**
 * class of the view only model that is used in the view.
 */
public class ThreeTriosViewModel  implements TripleTriadViewModel {
  private final ThreeTriosModel model;

  /**
   * View only model constructor.

   * @param model is the model being played that the view is reading from.
   */
  public ThreeTriosViewModel(ThreeTriosModel model) {
    this.model = model;
    model.setUpGame();
  }

  /**
   * Used by the view to get a copy of the red player's hand.

   * @return the red player's hand.
   */
  @Override
  public List<CardImpl> getRedHand() {
    return model.getRedHand();
  }

  /**
   * Used by the view to get a copy of the blue player's hand.

   * @return the blue player's hand.
   */
  @Override
  public List<CardImpl> getBlueHand() {
    return model.getBlueHand();
  }

  /**
   * Used by view to get the current player.

   * @return the current player
   */
  @Override
  public PlayerStructure getCurrentPlayer() {
    return model.getCurrentPlayer();
  }


  /**
   * Copies the grid and returns it (for viewing only).

   * @return the current state of the game (view only)
   */
  @Override
  public Cell[][] getGameState() {
    return model.getGameState();
  }
}
