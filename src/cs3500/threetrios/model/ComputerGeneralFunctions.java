package cs3500.threetrios.model;

import java.util.List;

/**
 * the class that provides the redundant helper functions and their
 * implementations to all the computer/strategy classes.
 */
public class ComputerGeneralFunctions {
  protected ReadOnlyTripleTriadModel model;

  /**
   * constructor that creates a general game using the base ThreeTriosModel.

   * @param model the specific model being used.
   */
  public ComputerGeneralFunctions(ReadOnlyTripleTriadModel model) {
    this.model = model;
  }

  /**
   * helper to choose the uppermost left-most cell,
   * and NOT select the card since that is done separately.
   *
   * @param tiedCells the list of cell candidates we have.
   * @return the winning cell.
   */
  protected Cell resolveTiedCells(List<Cell> tiedCells) {
    Cell bestCell = tiedCells.get(0);
    for (Cell cell : tiedCells) {
      if (cell == null) {
        continue;
      }
      if ((cell.cellRowPosition() < bestCell.cellRowPosition()
              && cell.cellColPosition() <= bestCell.cellColPosition())
              || (cell.cellRowPosition() <= bestCell.cellRowPosition()
              && cell.cellColPosition() < bestCell.cellColPosition())) {
        bestCell = cell;
      }
    }
    return bestCell;
  }

  /**
   * helper function to resolve ties and specifically the card aspect,
   * choosing the best card closest to the
   * 0th index.
   *
   * @param cell the cell that we need to find the best card for.
   * @param cards the list of cards that we need to choose the best from.
   * @return the card that is the best fit for this cell.
   */
  protected CardImpl resolveTiedCards(Cell cell, List<CardImpl> cards) {
    int row = cell.cellRowPosition();
    int col = cell.cellColPosition();
    int bestScore = 0;
    int tempScore = 0;
    CardImpl bestCard = cards.get(0);

    // via rules
    for (CardImpl card : cards) {
      if (row - 1 > 0 && !model.getGameState()[row - 1][col].isHole()
              && model.getGameState()[row - 1][col].hasBeenPlayed()) {
        if (model.getGameState()[row - 1][col].getCard().getDown().getValue()
                < card.getUp().getValue()) {
          tempScore += 5;
        }
        if (!model.getGameState()[row - 1][col].hasBeenPlayed()) {
          tempScore += 10 - card.getUp().getValue();
        }
      }
      if (row + 1 < model.getGameState().length && !model.getGameState()[row + 1][col].isHole()
              && model.getGameState()[row + 1][col].hasBeenPlayed()) {
        if (model.getGameState()[row + 1][col].getCard().getUp().getValue()
                < card.getDown().getValue()) {
          tempScore += 5;
        }
        if (model.getGameState()[row + 1][col].hasBeenPlayed()) {
          tempScore += 10 - card.getDown().getValue();
        }
      }
      if (col - 1 > 0 && !model.getGameState()[row][col - 1].isHole()
              && model.getGameState()[row][col - 1].hasBeenPlayed()) {
        if (model.getGameState()[row][col - 1].getCard().getRight().getValue()
                < card.getLeft().getValue()) {
          tempScore += 5;
        }
        if (model.getGameState()[row][col - 1].hasBeenPlayed()) {
          tempScore += 10 - card.getLeft().getValue();
        }
      }
      if (col + 1 < model.getGameState()[0].length && !model.getGameState()[row][col + 1].isHole()
              && model.getGameState()[row][col + 1].hasBeenPlayed()) {
        if (model.getGameState()[row][col + 1].getCard().getLeft().getValue()
                < card.getRight().getValue()) {
          tempScore += 5;
        }
        if (model.getGameState()[row][col + 1].hasBeenPlayed()) {
          tempScore += 10 - card.getRight().getValue();
        }
      }
      if (tempScore > bestScore) {
        bestScore = tempScore;
        bestCard = card;
      }
      tempScore = 0;
    }
    return bestCard;
  }

  /**
   * Handle the case in which there are no valid moves under the current ruleset.

   * @return the Cell in which the alternate move is played.
   */
  protected Cell handleNoValidMove() {
    int length = model.getGameState().length;
    int width = model.getGameState()[0].length;
    Cell altCell = null;
    for (int col = 0; col < width; col++) {
      for (int row = 0; row < length; row++) {
        if (!model.getGameState()[row][col].hasBeenPlayed()
                && !model.getGameState()[row][col].isHole()) {
          altCell = model.getGameState()[row][col];
          break;
        }
      }
      if (altCell != null) {
        break;
      }
    }
    return altCell;
  }
}
