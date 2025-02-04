package cs3500.threetrios.model;

import java.util.ArrayList;
import java.util.List;

/**
 * the Computer/Strategy class for the first strategy (flip as many cards in one turn), it extends
 * ComputerGeneralFunctions for the necessary general helper functions.
 */
public class MostFlipsComputer extends ComputerGeneralFunctions implements ComputerPlayers {
  protected List<CardImpl> hand;
  protected CardImpl bestCard1;
  protected List<Cell> possibleCells;

  /**
   * Constructs a MostFlipsComputer with a specific ThreeTriosModel model instance.
   *
   * @param model the model for the state of the game.
   */
  public MostFlipsComputer(ReadOnlyTripleTriadModel model) {
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
        if (!model.getGameState()[length][width].isHole()) {
          playableCells.add(model.getGameState()[length][width]);
          if (!model.getGameState()[length][width].hasBeenPlayed()) {
            possibleCells.add(model.getGameState()[length][width]);
          }
        }
      }
    }

    List<CardImpl> tiedCards = new ArrayList<>();
    Cell bestCell = null;
    CardImpl bestCard = null;
    if (possibleCells.size() == playableCells.size()) {
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
    if (card == null || cell == null) {
      return 0;
    }
    return model.canBeFlipped(card, cell.cellRowPosition(), cell.cellColPosition(),
            new ArrayList<>());
  }

  @Override
  public List<Cell> getAvailableCells() {
    return possibleCells;
  }

  @Override
  public String getStrategyName() {
    return "Most Flips";
  }
}
