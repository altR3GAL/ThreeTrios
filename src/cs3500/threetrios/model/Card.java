package cs3500.threetrios.model;

/**
 * Interface for cards and one card's methods.
 */
public interface Card {

  /**
   * Gets the String representation of this card.

   * @return the String representation
   */
  String toString();

  /**
   * Gets a hash value for this card using all its fields.

   * @return the hash value
   */
  @Override
  int hashCode();

  /**
   * Returns true if all aspects of the two cards are the same.

   * @param other is the card being compared
   * @return true if the cards are the same
   */
  @Override
  boolean equals(Object other);

  /**
   * Gets the color of the card.

   * @return the card's color
   */
  Color getColor();

  /**
   * Getter for the Up value.

   * @return the up value
   */
  Value getUp();

  /**
   * Getter for the Down value.

   * @return the down value
   */
  Value getDown();

  /**
   * Getter for the Right value.

   * @return the right value
   */
  Value getRight();

  /**
   * Getter for the Left value.

   * @return the left value
   */
  Value getLeft();

  /**
   * Changes the color of a card from Red to Blue and vise-versa.
   * Changes nothing if the card is in the deck (it's color is neither red nor blue)
   */
  void flipCard();

}
