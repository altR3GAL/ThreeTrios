package cs3500.threetrios.model;

/**
 * Cards are Red or Blue when owned by a player and DEFAULT when in the deck.
 */
public enum Color {
  DEFAULT,
  RED,
  BLUE;

  /**
   * Returns the textual representation of a color.

   * @return D for Default, R for Red, and B for Blue
   */
  @Override
  public String toString() {
    switch (this) {
      case RED:
        return "R";
      case BLUE:
        return "B";
      default:
        return "D";
    }
  }
}
