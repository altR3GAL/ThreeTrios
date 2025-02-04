package cs3500.threetrios.controller;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Cell;
import java.util.List;

/**
 * Players can be humans or AI.

 * @param <C> is the card type the player is using.
 */
public interface Player<C extends Card> {
  /**
   * Plays a card and removes from hand.

   * @param card the card to be played
   * @param cell is the cell that the card is being played to
   */
  void playCard(C card, Cell cell);

  /**
   * Adds the given card to the player's hand.
   * and sets the color of the card to this player's color

   * @param card is the card being added
   */
  void drawCard(C card);

  /**
   * Get string description of player.
   * Ex: Player: BLUE
   * Ex: Player: BLUE_AI

   * @return the Player's color
   */
  String toString();

  /**
   * Get a list of cards in this player's hand.

   * @return the hand
   */
  List<C> getHand();

  /**
   * Get the number of cards in the player's hand.

   * @return the number of cards
   */
  int numCardsInHand();


  /**
   * Gets the number of cards on the board that are the player's color.

   * @return the number of cards claimed by this player
   */
  int getNumClaimedCards();


  /**
   * Checks if it's this player's turn.

   * @return true if it's their turn
   */
  boolean isTurn();

}
