package cs3500.threetrios.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represents a deck of cards to be used in the game of ThreeTrios
 * A deck contains a list of cards of a specified size.
 * Deck can be created randomly by only providing the desired size of the deck,
 *  or with a preexisting list of cards, as is the case when deriving cards from a
 *  configuration file.
 */
public class Deck {
  private final List<CardImpl> cards;
  protected final int deckSize;

  /**
   * This constructor initializes a random deck of cards based on the size of the desired deck.

   * @param deckSize is the size of the deck.
   */
  public Deck(int deckSize) {
    this.deckSize = deckSize;
    this.cards = createRandomDeck();
  }

  /**
   * This constructor initializes a deck of cards based list of cards.

   * @param cardList is the list of cards being used to construct this deck.
   */
  public Deck(List<CardImpl> cardList) {
    this.cards = cardList;
    this.deckSize = cardList.size();
  }

  /**
   * Build the deck using randomly generated cards,
   *  with a randomly generated name and values.

   * @return the deck once it's populated with cards
   */
  public List<CardImpl> createRandomDeck() {
    List<CardImpl> newDeck = new ArrayList<>();

    for (int numCard = 0; numCard < deckSize; numCard++) {
      Value up = randValue();
      Value down = randValue();
      Value left = randValue();
      Value right = randValue();

      CardImpl newCard = new CardImpl(
              Color.DEFAULT, up, down, left, right
              );
      newDeck.add(newCard);
    }
    return newDeck;
  }

  /**
   * Gets a randomly selected value.

   * @return the new value
   */
  private Value randValue() {
    Random newRand = new Random();
    int randomVal = newRand.nextInt(9);

    switch (randomVal) {
      case 0:
        return Value.ONE;
      case 1:
        return Value.TWO;
      case 2:
        return Value.THREE;
      case 3:
        return Value.FOUR;
      case 4:
        return Value.FIVE;
      case 5:
        return Value.SIX;
      case 6:
        return Value.SEVEN;
      case 7:
        return Value.EIGHT;
      case 8:
        return Value.NINE;
      default:
        return Value.A;
    }
  }


  /**
   * Remove the top card from the deck and return it.
   */
  public CardImpl popCard() {
    CardImpl card = null;
    if (!cards.isEmpty()) {
      card = cards.get(0);
      cards.remove(0);
    }
    return card;
  }

  /**
   * Gets the number of cards in the deck.

   * @return the number of cards
   */
  public int getCurrentDeckSize() {
    return cards.size();
  }

  /**
   * Creates a copy of the deck.

   * @return the copy
   */
  public List<CardImpl> copyDeck() {
    return new ArrayList<>(cards);
  }
}
