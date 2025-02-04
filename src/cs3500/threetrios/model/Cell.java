package cs3500.threetrios.model;

/**
 * Cell class that holds all information about a cell.
 * Stores the position of the cell in a grid (rows and columns)
 * Knows if it is a hole or not.
 * Knows if it is storing a card.
 * Can store a card.
 */
public class Cell {
  private final int row;
  private final int col;
  private final boolean isHole;
  private boolean cellPlayed;
  private CardImpl card;
  protected boolean visited;
  protected boolean placed;

  /**
   * Constructor for the Cell class.

   * @param row the row that the cell is in.
   * @param col the column that the cell is in.
   * @param isHole boolean that tells if cell is a hole or not.
   */
  public Cell(int row, int col, boolean isHole) {
    this.row = row;
    this.col = col;
    this.isHole = isHole;
    this.cellPlayed = false;
    this.card = null;
    visited = false;
    placed = false;
  }


  /**
   * Takes a card and places it into this cell if not a hole nor has been played to.
   * This method is to be called until an IllegalArgumentException isn't thrown
   * IE. a card is placed successfully. Thus, an individual cell either throws an
   * exception or accepts the card.

   * @param card the card that will be placed if allowed.
   */
  public void placeCard(CardImpl card) {
    if (isHole || cellPlayed) { // if cell is hole or is played the cant play
      throw new IllegalArgumentException("cell is already played or hole");
    }
    placed = true;
    this.card = card; // set card of this cell to card
    this.cellPlayed = true;
  }

  /**
   * Flips the card held by this cell.
   */
  public void flipCard() {
    if (card != null) {
      card.flipCard();
    }
  }

  /**
   * Helper to retrieve the Card from the call.

   * @return the Card if it exists or null.
   */
  public CardImpl getCard() {
    return card;
  }

  /**
   * Gets the row of this cell.

   * @return the row
   */
  public int cellRowPosition() {
    return this.row;
  }

  /**
   * Gets the column of this cell.

   * @return the col
   */
  public int cellColPosition() {
    return this.col;
  }

  /**
   * Checks if this cell has been played to.

   * @return true if it has
   */
  public boolean hasBeenPlayed() {
    visited = true;
    return cellPlayed;
  }

  /**
   * Tells the caller whether this cell is a hole or not.

   * @return true if it is a hole
   */
  public boolean isHole() {
    visited = true;
    return isHole;
  }

}
