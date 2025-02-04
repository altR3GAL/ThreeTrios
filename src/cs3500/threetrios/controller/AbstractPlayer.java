package cs3500.threetrios.controller;

import cs3500.threetrios.model.CardImpl;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Color;
import cs3500.threetrios.model.PlayerStructure;
import cs3500.threetrios.model.ThreeTriosModel;
import java.util.List;

/**
 * This abstract class handles shared methods for all player objects.
 */
public abstract class AbstractPlayer {
  private ThreeTriosModel model;
  protected Color color;
  private PlayerStructure playerStructure;

  abstract void playCard(CardImpl card, Cell cell);

  abstract void drawCard(CardImpl card);

  abstract List<CardImpl> getHand();

  abstract int numCardsInHand();

  /**
   * Gets the number of cards owned by a player.

   * @return the number of cards.
   */
  public int getNumClaimedCards() {
    Cell[][] grid = model.getGameState();
    int rows = grid.length;
    int cols = grid[0].length;
    int claimCount = 0;
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        if (grid[row][col].hasBeenPlayed() && grid[row][col].getCard().getColor() == color) {
          claimCount++;
        }
      }
    }
    return claimCount;
  }

  abstract boolean isTurn();
}
