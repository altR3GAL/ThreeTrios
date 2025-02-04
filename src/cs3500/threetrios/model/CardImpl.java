package cs3500.threetrios.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * This card class creates a card to be used for ThreeTrios.
 */
public class CardImpl implements Card {
  private Color color;
  private final Value up;
  private final Value down;
  private final Value left;
  private final Value right;
  protected String cardName;


  /**
   * The constructor creates a card with a random name.

   * @param color is the cards color (DEFAULT if it's in the deck)
   * @param up is the Northern/Top value on the card
   * @param down is the Southern/Bottom value on the card
   * @param left is the West/Left value on the card
   * @param right is the East/Right value on the card
   */
  public CardImpl(Color color, Value up, Value down, Value right, Value left) {
    this.color = color;
    this.up = up;
    this.down = down;
    this.left = left;
    this.right = right;

    List<String> mysticalNames = new ArrayList<>(List.of(
            "Tree", "World", "Fire", "Water", "Sky", "Hero",
            "Earth", "Sea", "Dragon", "Knight", "Ice", "King",
            "Champion", "Flower", "Sword", "Flame", "Mountain", "Frozen",
            "Spire", "Frozen", "Queen", "Dark", "Light", "Frost",
            "Emperor", "Bloom", "Inferno", "Tower", "Shadow", "Phoenix",
            "Warrior", "Blaze", "Forest", "Tide", "Paladin", "Blade",
            "Guardian"
    ));

    //Get a card name from a list of random card names
    cardName = getRandName(mysticalNames);
  }

  /**
   * This constructor creates a card.

   * @param color is the cards color (DEFAULT if it's in the deck)
   * @param up is the Northern/Top value on the card
   * @param down is the Southern/Bottom value on the card
   * @param left is the West/Left value on the card
   * @param right is the East/Right value on the card
   * @param cardName is the name of the card
   */
  public CardImpl(Color color, Value up, Value down, Value right, Value left, String cardName) {
    this.color = color;
    this.up = up;
    this.down = down;
    this.left = left;
    this.right = right;
    this.cardName = cardName;
  }

  /**
   * Gets the attack values formatted as a string to be used by the view.

   * @return the attack values string.
   */
  public String getAttackString() {
    return " " + up + " " + "\n" + left + " " + right + "\n" + " " + down + " ";
  }


  /**
   * Gets the String representation of this card.

   * @return the String representation
   */
  @Override
  public String toString() {
    return cardName + " " + up + " " + down + " " + right + " " + left;
  }

  /**
   * Provides a random name using two words selected from the mysticalNames list.

   * @param names is the mysticalNames list
   * @return the randomly created name
   */
  private String getRandName(List<String> names) {
    //Return two randomly selected names put together
    Random random = new Random();
    int randomIndex = random.nextInt(names.size());
    int randomIndex2 = random.nextInt(names.size());

    return names.get(randomIndex) + names.get(randomIndex2);
  }

  /**
   * Gets a hash value for this card using all its fields.

   * @return the hash value
   */
  @Override
  public int hashCode() {
    return Objects.hash(
            this.color, this.cardName, this.up, this.down, this.right, this.left
    );
  }

  /**
   * Returns true if all aspects of the two cards are the same.

   * @param other is the card being compared
   * @return true if the cards are the same
   */
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other instanceof CardImpl) {
      CardImpl other2 = (CardImpl) other;
      return this.cardName.equals(other2.cardName)
              && this.up.equals(other2.up)
              && this.down.equals(other2.down)
              && this.left.equals(other2.left)
              && this.right.equals(other2.right);
    }
    return false;
  }

  /**
   * Gets the color of the card.

   * @return the card's color
   */
  public Color getColor() {
    return this.color;
  }

  /**
   * Change the color of this card to the specified color when removing it from the deck.

   * @param newColor is the color of the player drawing the card from the deck
   */
  public void setColorOnDraw(Color newColor) {
    if (getColor() == Color.DEFAULT) {
      this.color = newColor;
    }
  }



  /**
   * Getter for the Up value.

   * @return the up value
   */
  public Value getUp() {
    return up;
  }

  /**
   * Getter for the Down value.

   * @return the down value
   */
  public Value getDown() {
    return down;
  }

  /**
   * Getter for the Right value.

   * @return the right value
   */
  public Value getRight() {
    return right;
  }

  /**
   * Getter for the Left value.

   * @return the left value
   */
  public Value getLeft() {
    return left;
  }

  /**
   * Changes the color of a card from Red to Blue and vise-versa.
   * Changes nothing if the card is in the deck (it's color is neither red nor blue)
   */
  public void flipCard() {
    // If the card is Red or Blue flip it
    if (this.color != Color.DEFAULT) {
      if (this.color == Color.RED) {
        this.color = Color.BLUE;
      } else {
        this.color = Color.RED;
      }
    }
  }

}
