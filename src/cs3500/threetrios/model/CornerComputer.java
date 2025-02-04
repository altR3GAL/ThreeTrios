package cs3500.threetrios.model;

import java.util.ArrayList;
import java.util.List;

/**
 * the Computer/Strategy class for the second strategy (go for corners), it extends
 * ComputerGeneralFunctions for the necessary general helper functions.
 */
public class CornerComputer extends ComputerGeneralFunctions implements ComputerPlayers {
  //protected List<CardImpl> hand;
  protected CardImpl bestCard1;
  protected List<Cell> possibleCells;

  /**
   * Constructs a CornerComputer with a specific ThreeTriosModel model instance.
   *
   * @param model the model for the state of the game.
   */
  public CornerComputer(ReadOnlyTripleTriadModel model) {
    super(model);
    // hand = model.getCurrentPlayer().getHand();
    bestCard1 = null;
    possibleCells = new ArrayList<>();
  }

  @Override
  public Cell selectBestMove() {
    possibleCells = checkCorners();
    List<Cell> tiedCells = new ArrayList<>();
    List<CardImpl> tiedCards = new ArrayList<>();
    Cell bestCell = null;
    CardImpl bestCard = null;
    if (possibleCells.isEmpty()) {
      bestCell = handleNoValidMove();
      bestCard = model.getCurrentPlayer().getHand().get(0);
      bestCard1 = bestCard;
      return bestCell;
    }
    for (Cell cell : possibleCells) {
      for (int hands = 0; hands < model.getCurrentPlayer().getHand().size(); hands++) {
        CardImpl card = model.getCurrentPlayer().getHand().get(hands);
        if (calculateMoveScore(cell, card) > calculateMoveScore(bestCell, bestCard)) {
          bestCell = cell;
          bestCard = card;
          tiedCells.clear();
        } else if (calculateMoveScore(cell, card) == calculateMoveScore(bestCell, card)) {
          if (!tiedCells.contains(cell)) {
            tiedCells.add(cell);
          }
          if (!tiedCells.contains(bestCell)) {
            tiedCells.add(bestCell);
          }
        } else if (calculateMoveScore(bestCell, card) == calculateMoveScore(bestCell, bestCard)) {
          if (!tiedCards.contains(card)) {
            tiedCards.add(card);
          }
          if (!tiedCards.contains(bestCard)) {
            tiedCards.add(bestCard);
          }
        }
      }
    }
    if (tiedCells.size() > 1) {
      bestCell = resolveTiedCells(tiedCells);
    }
    if (tiedCards.size() > 1) {
      int bestScore = 0;
      for (CardImpl card : tiedCards) {
        int tempScore = calculateMoveScore(bestCell, card);
        if (tempScore > bestScore) {
          bestScore = tempScore;
          bestCard = card;
        }
      }
    }
    bestCard1 = bestCard;
    return bestCell;
  }

  @Override
  public CardImpl getBestCard() {
    return bestCard1;
  }

  /**
   * helper function that checks the corners of the grid/board.
   *
   * @return a list of cells that can be played to and are in corners (list can be empty).
   */
  protected List<Cell> checkCorners() {
    List<Cell> possibleCells = new ArrayList<>();
    int length = model.getGameState().length - 1;
    int width = model.getGameState()[0].length - 1;

    if (!model.getGameState()[0][0].hasBeenPlayed() && !model.getGameState()[0][0].isHole()) {
      possibleCells.add(model.getGameState()[0][0]);
    }
    if (!model.getGameState()[0][width].hasBeenPlayed()
            && !model.getGameState()[0][width].isHole()) {
      possibleCells.add(model.getGameState()[0][width]);
    }
    if (!model.getGameState()[length][0].hasBeenPlayed()
            && !model.getGameState()[length][0].isHole()) {
      possibleCells.add(model.getGameState()[length][0]);
    }
    if (!model.getGameState()[length][width].hasBeenPlayed()
            && !model.getGameState()[length][width].isHole()) {
      possibleCells.add(model.getGameState()[length][width]);
    }

    return possibleCells;
  }

  @Override // note score is just the value of the cards directional values, the higher the better.
  public int calculateMoveScore(Cell cell, CardImpl card) {
    if (card == null) {
      return 0;
    }
    int row = cell.cellRowPosition();
    int col = cell.cellColPosition();
    int length = model.getGameState().length;
    int width = model.getGameState()[0].length;
    int score = 0;

    if (row == 0 && col == 0) {
      score += card.getDown().getValue() + card.getRight().getValue();
    } else if (row == length - 1 && col == width - 1) {
      score += card.getUp().getValue() + card.getLeft().getValue();
    } else if (row == length - 1 && col == 0) {
      score += card.getUp().getValue() + card.getRight().getValue();
    } else if (row == 0 && col == width - 1) {
      score += card.getDown().getValue() + card.getLeft().getValue();
    }

    return score;
  }

  @Override
  public List<Cell> getAvailableCells() {
    return possibleCells;
  }

  @Override
  public String getStrategyName() {
    return "Corner";
  }
}
