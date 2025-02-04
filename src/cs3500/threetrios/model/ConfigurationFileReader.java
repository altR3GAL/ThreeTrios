package cs3500.threetrios.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * This class reads cell and card configuration files and outputs
 * a usable set of data based on that file.
 * Any invalid file formats result in default cells and cards.
 * Default cells are constructed either with the rows and columns
 * given by the file, or if the rows and/or columns
 * are invalid, default value(s) are used.
 * Default cards are constructed RANDOMLY, with random names and values.
 * The amount of cards constructed
 * is based on the number of cells provided by the cell configuration file
 * (Or the default values if the cell file is invalid).
 */
public class ConfigurationFileReader {

  private final File cellConfig;
  private final File cardConfig;


  /**
   * Take in two configuration files, one for cells and one for cards,
   * then translate them to be used by the model.

   * @param cellConfig is the configuration file for cells
   * @param cardConfig is the configuration file for cards
   */
  public ConfigurationFileReader(File cellConfig, File cardConfig) {
    this.cellConfig = cellConfig;
    this.cardConfig = cardConfig;
  }

  /**
   * Take the cell configuration file and return a list of lists of characters
   * (C's and X's).
   * If the file is not formatted properly, return a default cell list.

   * @return the list of characters to represent the desired grid
   */
  public List<List<String>> buildCellList() {
    List<List<String>> cellList = new ArrayList<>();

    try (Scanner scanner = new Scanner(cellConfig)) {
      String firstLine = scanner.nextLine();

      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();

        String[] charArray = line.split("");

        cellList.add(Arrays.asList(charArray));
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Cell file not found");
    }

    return cellList;
  }

  /**
   * Builds a default cell list.

   * @param numRows is the number of rows.
   * @param numColumns is the number of columns
   * @return a legal cell list using the given dimensions
   */
  private List<List<String>> defaultCellList(int numRows, int numColumns) {
    List<List<String>> cellList = new ArrayList<>();

    for (int row = 0; row < numRows - 1; row++) {
      cellList.add(new ArrayList<>());
      for (int col = 0; col < numColumns; col++) {
        cellList.get(row).add("C");
      }
    }

    //Add the final row
    cellList.add(new ArrayList<>());

    //If the number of cells is even, make the final cell a hole
    if ((numRows * numColumns) % 2 == 0) {
      for (int col = 0; col < numColumns - 1; col++) {
        cellList.get(numRows - 1).add("C");
      }
      cellList.get(numRows - 1).add("X");
    } else {
      for (int col = 0; col < numColumns; col++) {
        cellList.get(numRows - 1).add("C");
      }
    }

    return cellList;
  }

  /**
   * Take the card configuration file and return a list of cards.
   * If the file is not formatted properly, return an empty list

   * @param numCells is the size of the list of cards
   * @return the list of cards
   */
  public List<CardImpl> buildDeckList(int numCells) {
    List<CardImpl> cardList = new ArrayList<>();

    try (Scanner scanner = new Scanner(cardConfig)) {
      while (scanner.hasNextLine()) {
        String cardString = scanner.nextLine();
        String[] cardArray  = cardString.split(" ");

        cardList.add(buildCard(cardArray));

      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Card file not found");
    } catch (NumberFormatException e) {
      return defaultCardList(numCells);
    }

    return cardList;
  }

  /**
   * Builds a card using the string format of a card pulled from a configuration file.

   * @param cardArray is the list representation of a card.
   * @return the new card
   */
  private CardImpl buildCard(String[] cardArray) {
    String name = cardArray[0];

    for (int index = 0; index < cardArray.length; index++) {
      if (cardArray[index].equals("A")) {
        cardArray[index] = "10";
      }
    }

    try {
      Value north = getValue(Integer.parseInt(cardArray[1]));
      Value south = getValue(Integer.parseInt(cardArray[2]));
      Value east = getValue(Integer.parseInt(cardArray[3]));
      Value west = getValue(Integer.parseInt(cardArray[4]));

      return new CardImpl(Color.DEFAULT, north, south, east, west, name);
    } catch (NumberFormatException e) {
      throw new NumberFormatException("Failure to parse at least one value");
    }
  }

  /**
   * Helper that creates a list of random cards if the config file is not
   * formatted properly.

   * @param numCells is the number of cells on the grid
   * @return the card list
   */
  private List<CardImpl> defaultCardList(int numCells) {
    Deck tempDeck = new Deck(numCells);
    return tempDeck.createRandomDeck();
  }


  /**
   * Helper method that turns integers into Values.

   * @param parsedInt is the integer pulled from the config file.
   * @return the corresponding value.
   */
  private Value getValue(Integer parsedInt) {
    switch (parsedInt) {
      case 1:
        return Value.ONE;
      case 2:
        return Value.TWO;
      case 3:
        return Value.THREE;
      case 4:
        return Value.FOUR;
      case 5:
        return Value.FIVE;
      case 6:
        return Value.SIX;
      case 7:
        return Value.SEVEN;
      case 8:
        return Value.EIGHT;
      case 9:
        return Value.NINE;
      case 10:
        return Value.A;
      default:
        throw new IllegalArgumentException("Invalid value " + parsedInt);
    }
  }


  /**
   * Take the cell configuration file and return the number of rows specified
   * for this game.
   * If a non-valid number, return a default value.

   * @return the number of rows to be used.
   */
  public int getNumRows() {
    try (Scanner scanner = new Scanner(cellConfig)) {
      String firstLine = scanner.nextLine();

      String[] rowsAndCol = firstLine.split(" ");

      return Integer.parseInt(rowsAndCol[0]);

    } catch (FileNotFoundException e) {
      throw new RuntimeException("Cell file not found");
    } catch (NumberFormatException e) {
      return 3;
    }
  }

  /**
   * Take the cell configuration file and return the number of columns
   * specified for this game.
   * If a non-valid number, return a default value.

   * @return the number of columns to be used.
   */
  public int getNumColumns() {
    try (Scanner scanner = new Scanner(cellConfig)) {
      String firstLine = scanner.nextLine();

      String[] rowsAndCol = firstLine.split(" ");

      return Integer.parseInt(rowsAndCol[1]);

    } catch (FileNotFoundException e) {
      throw new RuntimeException("Cell file not found");
    } catch (NumberFormatException e) {
      return 3;
    }
  }
}
