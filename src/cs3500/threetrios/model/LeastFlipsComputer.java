package cs3500.threetrios.model;

import java.util.ArrayList;
import java.util.List;

/**
 * the Computer/Strategy class for the third strategy
 * (least likely to be flipped once placed), it extends
 * ComputerGeneralFunctions for the necessary general helper functions.
 */
public class LeastFlipsComputer extends ComputerGeneralFunctions implements ComputerPlayers {
  protected List<CardImpl> hand;
  protected CardImpl bestCard1;
  protected List<Cell> possibleCells;

  /**
   * Constructs a LeastFlipsComputer with a specific ThreeTriosModel model instance.
   *
   * @param model the model for the state of the game.
   */
  public LeastFlipsComputer(ReadOnlyTripleTriadModel model) {
    super(model);
    hand = model.getCurrentPlayer().getHand();
    bestCard1 = null;
    possibleCells = new ArrayList<>();
  }

  @Override
  public Cell selectBestMove() {
    List<Cell> playableCells = new ArrayList<>();
    List<Cell> tiedCells = new ArrayList<>();
    for (int length = 0; length < model.getGameState().length; length++) {
      for (int width = 0; width < model.getGameState()[0].length; width++) {
        if (!model.getGameState()[length][width].isHole()
                && !model.getGameState()[length][width].hasBeenPlayed()) {
          possibleCells.add(model.getGameState()[length][width]);
          if (checkValid(model.getGameState()[length][width])) {
            playableCells.add(model.getGameState()[length][width]);
          }
        }
      }
    }

    List<CardImpl> tiedCards = new ArrayList<>();
    Cell bestCell = null;
    CardImpl bestCard = null;
    if (playableCells.isEmpty()) {
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
        int tempScore = 0;
        tempScore = calculateMoveScore(bestCell, card);
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

  @Override
  public int calculateMoveScore(Cell cell, CardImpl card) {
    if (cell == null || card == null) {
      return 0;
    }
    int row = cell.cellRowPosition();
    int col = cell.cellColPosition();
    int score = 0;
    int sideCount = 0;
    if (row - 1 >= 0) {
      if (!model.getGameState()[row - 1][col].isHole()
              && !model.getGameState()[row - 1][col].hasBeenPlayed()) {
        sideCount++;
        score += card.getUp().getValue();
      }
    }
    if (row + 1 < model.getGameState().length) {
      if (!model.getGameState()[row + 1][col].isHole()
              && !model.getGameState()[row + 1][col].hasBeenPlayed()) {
        sideCount++;
        score += card.getUp().getValue();
      }
    }
    if (col - 1 >= 0) {
      if (!model.getGameState()[row][col - 1].isHole()
              && !model.getGameState()[row][col - 1].hasBeenPlayed()) {
        sideCount++;
        score += card.getUp().getValue();
      }
    }
    if (col + 1 < model.getGameState()[0].length) {
      if (!model.getGameState()[row][col + 1].isHole()
              && !model.getGameState()[row][col + 1].hasBeenPlayed()) {
        sideCount++;
        score += card.getUp().getValue();
      }
    }
    int finalScore = 0;
    if (sideCount == 1) {
      finalScore = score * 4;
    } else if (sideCount == 2) {
      finalScore = score * 2;
    } else if (sideCount == 3) {
      finalScore = score * 2 + score;
    } else if (sideCount == 4) {
      finalScore = score;
    }
    return finalScore;
  }

  @Override
  public List<Cell> getAvailableCells() {
    return possibleCells;
  }

  @Override
  public String getStrategyName() {
    return "Least Flips";
  }

  /**
   * helper to check if there are cells adjacent to the main cell
   * that need to be considered in defense value.

   * @param cell the main cell that needs to be defended.
   * @return true if main cell has to defend itself from other possible attacks.
   */
  private boolean checkValid(Cell cell) {
    int row = cell.cellRowPosition();
    int col = cell.cellColPosition();
    if (row - 1 >= 0) {
      if (!model.getGameState()[row - 1][col].isHole()) {
        return true;
      }
    }
    if (row + 1 < model.getGameState().length) {
      if (!model.getGameState()[row + 1][col].isHole()) {
        return true;
      }
    }
    if (col - 1 >= 0) {
      if (!model.getGameState()[row][col - 1].isHole()) {
        return true;
      }
    }
    if (col + 1 < model.getGameState().length) {
      if (!model.getGameState()[row][col + 1].isHole()) {
        return true;
      }
    }
    return false;
  }
}
