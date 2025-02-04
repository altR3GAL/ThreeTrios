package cs3500.threetrios.model;

import java.util.ArrayList;
import java.util.List;

/**
 * the Computer/Strategy class for the fourth strategy (mini max), it extends
 * ComputerGeneralFunctions for the necessary general helper functions.
 */
public class MiniMaxComputer extends ComputerGeneralFunctions implements ComputerPlayers {
  private final int rows;
  private final int cols;
  private final MostFlipsComputer mostFlipsComputer;
  private final CornerComputer cornerComputer;
  private final LeastFlipsComputer leastFlipsComputer;
  protected CardImpl bestCard1;
  protected List<Cell> possibleCells;
  protected List<CardImpl> hand;

  /**
   * Constructs a MiniMaxComputer with a specific ThreeTriosModel model instance.
   *
   * @param model the model for the state of the game.
   */
  public MiniMaxComputer(ReadOnlyTripleTriadModel model) {
    super(model);
    this.mostFlipsComputer = new MostFlipsComputer(model);
    this.cornerComputer = new CornerComputer(model);
    this.leastFlipsComputer = new LeastFlipsComputer(model);
    this.rows = model.getGameState().length;
    this.cols = model.getGameState()[0].length;
    bestCard1 = null;
    possibleCells = new ArrayList<>();
    hand = model.getCurrentPlayer().getHand();
  }

  @Override
  public Cell selectBestMove() {
    Cell bestMove = null;
    for (int length = 0; length < rows; length++) {
      for (int width = 0; width < cols; width++) {
        if (!model.getGameState()[length][width].isHole()
                && !model.getGameState()[length][width].hasBeenPlayed()) {
          possibleCells.add(model.getGameState()[length][width]);
        }
      }
    }
    if (checkCornerStrat()) {
      bestMove = cornerComputer.selectBestMove();
      bestCard1 = cornerComputer.getBestCard();
    } else if (checkMostFlipsStrat()) {
      bestMove = mostFlipsComputer.selectBestMove();
      bestCard1 = mostFlipsComputer.getBestCard();
    } else if (checkLeastFlipsStrat()) {
      bestMove = leastFlipsComputer.selectBestMove();
      bestCard1 = leastFlipsComputer.getBestCard();
    } else if (checkMiniMaxStrat()) {
      bestMove = selectBestMiniMax();
    } else {
      bestMove = handleNoValidMove();
    }
    return bestMove;
  }

  @Override
  public CardImpl getBestCard() {
    return bestCard1;
  }


  /**
   * helper to select the best move when playing against another minimax strategy.

   * @return the cell that would be the best along with the card that would work with it.
   */
  private Cell selectBestMiniMax() {
    // find the move that would be at the cell that would make the next bot have the worst best
    int bestScore = 0;
    Cell bestCell = null;
    List<Cell> cells = getPossibleMiniMaxCells();
    for (Cell cell : cells) {
      Cell[][] copyState = model.getGameState().clone();
      int tempScore = calculateMiniMaxSecondBestScore(copyState, cell);
      if (tempScore >= bestScore) {
        bestScore = tempScore;
        bestCell = cell;
      }
    }
    CardImpl bestCard = findBestCard(bestCell);
    bestCard1 = bestCard;
    return bestCell;
  }

  /**
   * helper that finds the best score that can be obtained from the current game state
   * when considering that
   * one cell cannot be placed in (since it is where we pretend a card is placed).

   * @param copyState a copy of the game state/grid/board.
   * @param cell the cell that we are pretending is having card placed in it.
   * @return the score of the best move the player can make
   */
  private int calculateMiniMaxSecondBestScore(Cell[][] copyState, Cell cell) {
    int bestScore = 0;
    for (int length = 0; length < rows; length++) {
      for (int width = 0; width < cols; width++) {
        if (length != cell.cellRowPosition() && width != cell.cellColPosition()
                && !copyState[length][width].hasBeenPlayed()
                && !copyState[length][width].isHole()) {
          for (CardImpl card : model.getCurrentPlayer().getHand()) {
            int tempScore = mostFlipsComputer.calculateMoveScore(copyState[length][width], card);
            if (tempScore >= bestScore) {
              bestScore = tempScore;
            }
          }
        }
      }
    }
    return bestScore;
  }

  /**
   * helper to find the best card at the location of the best cell.

   * @param bestCell the location that the card needs to be.
   * @return the card that will score the highest.
   */
  private CardImpl findBestCard(Cell bestCell) {
    CardImpl bestCard = model.getCurrentPlayer().getHand().get(0);
    int bestScore = 0;
    for (CardImpl card : model.getCurrentPlayer().getHand()) {
      int tempScore = leastFlipsComputer.calculateMoveScore(bestCell, card);
      if (tempScore >= bestScore) {
        bestScore = tempScore;
        bestCard = card;
      }
    }
    return bestCard;
  }

  /**
   * helper to get all the cells that the game can be played in.

   * @return a list of cells that can be played in.
   */
  private List<Cell> getPossibleMiniMaxCells() {
    List<Cell> cells = new ArrayList<>();
    for (int length = 0; length < rows; length++) {
      for (int width = 0; width < cols; width++) {
        if (!model.getGameState()[length][width].isHole()
                && !model.getGameState()[length][width].hasBeenPlayed()) {
          cells.add(model.getGameState()[length][width]);
        }
      }
    }
    return cells;
  }

  /**
   * helper function to check if the opponent is using the corner strategy. If the game has not
   * progressed halfway and 2 or more corners have already been filled,
   * or if the game has progressed
   * halfway and 3 or more corners have been filled then the player is using the corner strategy.
   *
   * @return true if the opponent is using the corner strategy.
   */
  private boolean checkCornerStrat() {
    List<Cell> playedCells = new ArrayList<>();
    int length = rows - 1;
    int width = cols - 1;
    if (model.getGameState()[0][0].hasBeenPlayed() && !model.getGameState()[0][0].isHole()
            && model.getOpponent().color == model.getGameState()[0][0].getCard().getColor()) {
      playedCells.add(model.getGameState()[0][0]);
    }
    if (model.getGameState()[0][width].hasBeenPlayed() && !model.getGameState()[0][width].isHole()
            && model.getOpponent().color == model.getGameState()[0][width].getCard().getColor()) {
      playedCells.add(model.getGameState()[0][width]);
    }
    if (model.getGameState()[length][0].hasBeenPlayed() && !model.getGameState()[length][0].isHole()
            && model.getOpponent().color == model.getGameState()[length][0].getCard().getColor()) {
      playedCells.add(model.getGameState()[length][0]);
    }
    if (model.getGameState()[length][width].hasBeenPlayed()
            && !model.getGameState()[length][width].isHole()
            && model.getOpponent().color
            == model.getGameState()[length][width].getCard().getColor()) {
      playedCells.add(model.getGameState()[length][width]);
    }
    int cornerCount = cornerComputer.checkCorners().size();
    if (checkGameHalfway() && cornerCount > 2) {
      return playedCells.size() > 2;
    } else {
      return playedCells.size() > 1;
    }
  }

  /**
   * helper function to check if the opponent is using the most flips strategy. Check by looking
   * for opponent cards that are adjacent to each other, indicating won battles (sometimes not
   * but most likely it is this case). If the game has progressed halfway, the amount of adjacent
   * card pairs doubled should be greater than the amount of total playable cells (filled or not),
   * if the game has not progressed halfway, triple the count should be greater than the amount of
   * playable cells to indicate usage of the strategy.
   *
   * @return true if opponent is using the most flips strategy.
   */
  private boolean checkMostFlipsStrat() {
    int count = 0;
    Color opponentColor = model.getOpponent().color;
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        if (!model.getGameState()[row][col].isHole()
                && model.getGameState()[row][col].hasBeenPlayed()) {
          count = getCount(count, opponentColor, opponentColor, row, col);
        }
      }
    }
    int cellCount = 0;
    for (int length = 0; length < model.getGameState().length; length++) {
      for (int width = 0; width < model.getGameState()[0].length; width++) {
        if (!model.getGameState()[length][width].isHole()
                && !model.getGameState()[length][width].hasBeenPlayed()) {
          cellCount++;
        }
      }
    }

    if (checkGameHalfway()) {
      return count * 2 > cellCount;
    } else {
      return count * 3 > cellCount;
    }
  }

  /**
   * helper function to check if the opponent is using the least flips (defensive) strategy.
   * Check by looking
   * for allied cards that are next to opponent cards, indicating failed battles (sometimes not
   * but most likely it is this case). If the game has progressed halfway, the amount of adjacent
   * card pairs doubled should be greater than the amount of total playable cells (filled or not),
   * if the game has not progressed halfway, triple the count should be greater than the amount of
   * playable cells to indicate usage of the strategy.
   *
   * @return true if opponent is using the least flips strategy.
   */
  private boolean checkLeastFlipsStrat() {
    int count = 0;
    Color playerColor = model.getCurrentPlayer().color;
    Color opponentColor = null;
    if (playerColor == Color.RED) {
      opponentColor = Color.BLUE;
    } else if (playerColor == Color.BLUE) {
      opponentColor = Color.RED;
    }
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        if (!model.getGameState()[row][col].isHole()
                && model.getGameState()[row][col].hasBeenPlayed()) {
          count += getCount(count, opponentColor, playerColor, row, col);
          count += getCount(count, playerColor, opponentColor, row, col);
        }
      }
    }
    int cellCount = 0;
    for (int length = 0; length < model.getGameState().length; length++) {
      for (int width = 0; width < model.getGameState()[0].length; width++) {
        if (!model.getGameState()[length][width].isHole()
                && !model.getGameState()[length][width].hasBeenPlayed()) {
          cellCount++;
        }
      }
    }
    if (checkGameHalfway()) {
      return count * 2 > cellCount;
    } else {
      return count * 3 > cellCount;
    }
  }

  /**
   * helper for checking if the main cell at the coordinate location has defended
   * against opponent cards.

   * @param count the amount of cards that the cell has defended itself from.
   * @param playerColor the color of the current player.
   * @param opponentColor the color of the opponent player.
   * @param row the row of the defending cell that we check.
   * @param col the col of the defending cell that we check.
   * @return the number of the times main cell has defended from attacks.
   */
  private int getCount(int count, Color playerColor, Color opponentColor, int row, int col) {
    if (model.getGameState()[row][col].getCard().getColor() == playerColor) {
      if (col + 1 < model.getGameState()[0].length && !model.getGameState()[row][col + 1].isHole()
              && model.getGameState()[row][col + 1].hasBeenPlayed()) {
        if (col + 1 < cols && model.getGameState()[row][col + 1].getCard().getColor()
                == opponentColor) {
          count++;
        }
      }
      if (row + 1 < model.getGameState().length && !model.getGameState()[row + 1][col].isHole()
              && model.getGameState()[row + 1][col].hasBeenPlayed()) {
        if (row + 1 < rows && model.getGameState()[row + 1][col].getCard().getColor()
                == opponentColor) {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * helper to determine if the opponent is also using the mini max strategy. Check by testing
   * the opponents aggression and defense with the helpers helpCheckAgression and
   * helpCheckSecurity. If the count of cards that fulfill either the aggression or security
   * are over 0.7 times the amount of played opponent cells, then the opponent is using
   * the mini max strategy.
   *
   * @return true if the opponent is using mini max.
   */
  private boolean checkMiniMaxStrat() {
    int cellCount = 0;
    int count = 0;
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        Cell cell = model.getGameState()[row][col];
        if (!cell.isHole() && cell.hasBeenPlayed() && cell.getCard().getColor()
                == model.getOpponent().color) {
          cellCount++;
          CardImpl opponentCard = cell.getCard();
          if (helpCheckAgression(cell, opponentCard) || helpCheckSecurity(cell, opponentCard)) {
            count++;
          }
        }
      }
    }
    return count > cellCount * 0.7;
  }

  /**
   * Helper method to check if the player is being aggresive enough in placements.
   * Checks if the player is making a move that is at least 80% as aggressive as
   * the most aggresive possible move. This means that the play has a high chance
   * of flipping opponent cards.
   *
   * @param cell the cell indicating the location of the play.
   * @param opponentCard the card that the we are evaluating the aggresion of.
   * @return true if the move is at least 80% as agressive as it's maximum potential.
   */
  private boolean helpCheckAgression(Cell cell, CardImpl opponentCard) {
    int max = 0;
    int trueScore = calculateMoveScore(cell, opponentCard);
    List<CardImpl> cards = model.getOpponent().getHand();
    for (CardImpl card : cards) {
      int score = calculateMoveScore(cell, card);
      max = Math.max(max, score);
    }
    return trueScore >= max * 0.8;
  }

  /**
   * Helper method to check if the player is being defensive enough in placements.
   * Checks if the player is making a move that is at least 50% likely to defend
   * it's cell/placement. This means that the play has a 50% or higher chance
   * to not be flipped when an opponent places a card adjacent to it.
   *
   * @param cell the cell indicating the location of the play.
   * @param opponentCard the card that the we are evaluating the defense of.
   * @return true if the move is able to defend 50% of cards played.
   */
  private boolean helpCheckSecurity(Cell cell, CardImpl opponentCard) {
    int max = 0;
    int opponentScore = calculateMoveScore(cell, opponentCard);
    List<CardImpl> cards = model.getCurrentPlayer().getHand();
    for (CardImpl card : cards) {
      int score = calculateMoveScore(cell, card);
      max = Math.max(max, score);
    }
    return opponentScore > max * 0.5;
  }


  /**
   * helper to check if game has gone halfway, for the strat checkers.

   * @return true if game has gone more than halfway.
   */
  private boolean checkGameHalfway() {
    int totalCells = 0;
    int playedCells = 0;
    for (int row = 0; row < model.getGameState().length; row++) {
      for (int col = 0; col < model.getGameState()[0].length; col++) {
        Cell cell = model.getGameState()[row][col];
        if (!cell.isHole()) {
          totalCells++;
          if (cell.hasBeenPlayed()) {
            playedCells++;
          }
        }
      }
    }
    return (double) playedCells / totalCells > 0.5;
  }


  // most defensive card/best to defend his best cell.
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
      if (!model.getGameState()[row - 1][col].isHole()) {
        sideCount++;
        if (!model.getGameState()[row - 1][col].hasBeenPlayed()) {
          score += card.getUp().getValue();
        }
      }
    }
    if (row + 1 < model.getGameState().length) {
      if (!model.getGameState()[row + 1][col].isHole()) {
        sideCount++;
        if (!model.getGameState()[row + 1][col].hasBeenPlayed()) {
          score += card.getUp().getValue();
        }
      }
    }
    if (col - 1 >= 0) {
      if (!model.getGameState()[row][col - 1].isHole()) {
        sideCount++;
        if (!model.getGameState()[row][col - 1].hasBeenPlayed()) {
          score += card.getUp().getValue();
        }
      }
    }
    if (col + 1 < model.getGameState()[0].length) {
      if (!model.getGameState()[row][col + 1].isHole()) {
        sideCount++;
        if (!model.getGameState()[row][col + 1].hasBeenPlayed()) {
          score += card.getUp().getValue();
        }
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
    return "Mini Max";
  }
}
