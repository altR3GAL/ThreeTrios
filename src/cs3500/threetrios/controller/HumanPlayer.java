package cs3500.threetrios.controller;

import cs3500.threetrios.model.CardImpl;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Color;
import cs3500.threetrios.model.PlayerStructure;
import cs3500.threetrios.model.ThreeTriosModel;
import java.util.List;

/**
 * Human players are the Player object associated with a human played game.
 */
public class HumanPlayer extends AbstractPlayer implements Player<CardImpl> {
  private final ThreeTriosModel model;
  protected Color color;
  private final PlayerStructure playerStructure;

  /**
   * This constructor builds the player's structures and adds that structure to the model.

   * @param model is the model this player is associated with.
   */
  public HumanPlayer(ThreeTriosModel model) {
    this.model = model;
    playerStructure = new PlayerStructure();
    model.addPlayer(playerStructure);
  }

  /**
   * Gets this Player's playerStructure.

   * @return the playerStructure.
   */
  public PlayerStructure getPlayerStructure() {
    return playerStructure;
  }

  @Override
  public void playCard(CardImpl card, Cell cell) {
    playerStructure.playCard(card, cell);
  }

  @Override
  public void drawCard(CardImpl card) {
    playerStructure.drawCard(card);
  }

  @Override
  public List<CardImpl> getHand() {
    return playerStructure.getHand();
  }

  @Override
  public int numCardsInHand() {
    return playerStructure.getHand().size();
  }

  @Override
  public boolean isTurn() {
    return model.getCurrentPlayer() == playerStructure;
  }

}
