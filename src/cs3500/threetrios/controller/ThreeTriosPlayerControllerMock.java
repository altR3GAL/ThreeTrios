package cs3500.threetrios.controller;

import cs3500.threetrios.model.CardImpl;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.PlayerStructure;
import cs3500.threetrios.model.ThreeTriosModel;
import cs3500.threetrios.view.ThreeTriosView;

/**
 * This mock allows the testing of a player controller by removing the pop-up features.
 */
public class ThreeTriosPlayerControllerMock implements  ModelListener, PlayerListener {
  private final ThreeTriosModel model;
  private final HumanPlayer player;
  public CardImpl currentChosenCard;

  /**
   * controller for player.
   *
   * @param model is the model being played.
   * @param player is the player.
   * @param view is the view.
   */
  public ThreeTriosPlayerControllerMock(ThreeTriosModel model, HumanPlayer player,
                                        ThreeTriosView view) {
    this.model = model;
    this.player = player;

    view.playerListenerAdd(this);
    model.addModelListener(this);
  }

  @Override
  public void cardChosenHeard(int index) {
    if (model.getCurrentPlayer() == this.player.getPlayerStructure()) {
      if (index == -1) {
        currentChosenCard = null;
      } else {
        currentChosenCard = getChosenCard(index);
      }
    }
  }

  /**
   * Gets the card from this player's hand after the controller hears a selection.

   * @param index is the index of the card.
   * @return the card.
   */
  private CardImpl getChosenCard(int index) {
    if (index == -1) {
      throw new IllegalArgumentException("An error occurred selecting a card.");
    } else if (index >= player.getHand().size()) {
      throw new IndexOutOfBoundsException("The given index is out of bounds.");
    }
    return player.getHand().get(index);
  }

  @Override
  public void positionChosenHeard(int row, int col) {
    if (model.getCurrentPlayer() == this.player.getPlayerStructure()) {
      if (currentChosenCard != null && (!getCell(row, col).hasBeenPlayed()
              || !getCell(row, col).isHole())) {
        try {
          model.playCard(currentChosenCard, row, col);
          if (getCell(row, col).getCard() == currentChosenCard) {
            boolean cardPlayedSuccessfully = true;
            model.resolveBattles();
            model.switchTurn();
            model.isGameOver();
          }
        } catch (IllegalArgumentException e) {
          throw new IllegalArgumentException("Error playing your card.");
        }
      }
    }
  }

  /**
   * Gets the cell at the given row and column.

   * @param row is the row.
   * @param col is the column.
   * @return the cell at that position.
   */
  private Cell getCell(int row, int col) {
    return model.getGameState()[row][col];
  }

  @Override
  public void playerTurnHeard(PlayerStructure player) {
    //Not needed for mock.
  }

  @Override
  public void playerEndOfTurnHeard(PlayerStructure player) {
    //Not needed for mock.
  }

  @Override
  public void gameEndHeard(String winner, int score) {
    //Not needed for mock.
  }
}
