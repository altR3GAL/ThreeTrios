package cs3500.threetrios.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A player structure is a container for a ThreeTrios hand and color identity.
 * Unlike a Player object, the playerStructure simply allows the model to store each
 * color's hand and color identify in the same space, rather than having the ability
 * to play the game.
 */
public class PlayerStructure {
  protected final List<CardImpl> hand;
  protected Color color;

  /**
   * constructor for a human player.
   * Every human player has a hand and a color.

   * @param color is the color of the player (blue or red)
   */
  public PlayerStructure(Color color) {
    this.color = color;
    hand = new ArrayList<>();
  }

  /**
   * Default constructor, requires the Player object to set the color.
   */
  public PlayerStructure() {
    hand = new ArrayList<>();
  }

  /**
   * Allows the Player object to set the color of a default constructed PlayerStructure.
   */
  public void setPlayerColor(Color color) {
    this.color = color;
  }

  /**
   * From the player objects POV this action is only accepting a card.
   * given to them by the model
   * Drawing a card from the deck and giving it to the player is done by the model

   * @param card is the card being added
   */
  public void drawCard(CardImpl card) {
    card.setColorOnDraw(color);
    hand.add(card);
  }

  /**
   * Calls the placeCard method on the target cell, removes card from
   * hand after played.
   * The model will check if the cell and card are valid

   * @param card the card to be played
   * @param cell is the cell that the card is being played to
   */
  public void playCard(CardImpl card, Cell cell) {
    if (hand.contains(card)) {
      cell.placeCard(card);
      if (cell.placed) {
        hand.remove(card);
      }
    } else {
      throw new IllegalArgumentException("Card is not in the hand");
    }
  }

  /**
   * Get string description of player.
   * Ex: Player: BLUE

   * @return the Player's color
   */
  @Override
  public String toString() {
    if (this.color == Color.RED) {
      return "Player: Red";
    } else {
      return "Player: Blue";
    }
  }


  /**
   * Gets a copy of this players hand.

   * @return the copied hand.
   */
  public List<CardImpl> getHand() {
    return new ArrayList<>(hand);
  }

  /**
   * returns a copy of the color of the current player.

   * @return the color.
   */
  public Color getColor() {
    return this.color;
  }
}
