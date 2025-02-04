package cs3500.threetrios.model;

import java.util.ArrayList;
import java.util.List;

/**
 * the Computer/Strategy class that is used for mixing strategies together,
 * takes in two separate strategies
 * under the same model and mixes them. It extends ComputerGeneralFunctions
 * for the necessary general helper functions.
 */
public class MergerComputer extends ComputerGeneralFunctions implements ComputerPlayers {
  private final CornerComputer cornerComputer;
  private final LeastFlipsComputer leastFlipsComputer;
  private final MiniMaxComputer miniMaxComputer;
  private final MostFlipsComputer mostFlipsComputer;
  private final String comp1;
  private final String comp2;
  protected CardImpl bestCard1;
  protected List<Cell> possibleCells;
  protected List<CardImpl> hand;

  /**
   * Constructor that creates a version of the MergerComputer
   * given the model and string representations
   * of the two strategies that are to be mixed.
   *
   * @param model the model for the state of the game.
   * @param comp1 the string representation of the first computer/strategy.
   * @param comp2 the string representation of the second computer/strategy.
   */
  public MergerComputer(ReadOnlyTripleTriadModel model, String comp1, String comp2) {
    super(model);
    this.cornerComputer = new CornerComputer(model);
    this.leastFlipsComputer = new LeastFlipsComputer(model);
    this.miniMaxComputer = new MiniMaxComputer(model);
    this.mostFlipsComputer = new MostFlipsComputer(model);
    this.comp1 = comp1;
    this.comp2 = comp2;
    bestCard1 = null;
    possibleCells = new ArrayList<>();
    hand = model.getCurrentPlayer().getHand();
  }

  @Override
  public Cell selectBestMove() {
    Cell bestCell = null;
    CardImpl bestCard = null;
    List<ComputerPlayers> strats = chooseStrategies(comp1, comp2);
    ComputerPlayers strat1 = strats.get(0);
    ComputerPlayers strat2 = strats.get(1);
    ComputerPlayers bestStrat = strat1;
    ComputerPlayers worseStrat = strat2;
    List<Cell> tiedCells = new ArrayList<>();
    for (int length = 0; length < model.getGameState().length; length++) {
      for (int width = 0; width < model.getGameState()[0].length; width++) {
        if (!model.getGameState()[length][width].isHole()
                && !model.getGameState()[length][width].hasBeenPlayed()) {
          possibleCells.add(model.getGameState()[length][width]);
        }
      }
    }
    if (possibleCells.isEmpty()) {
      bestCell = handleNoValidMove();
      bestCard = model.getCurrentPlayer().getHand().get(0);
      bestCard1 = bestCard;
      return bestCell;
    }
    for (Cell cell : possibleCells) {
      for (int hands = 0; hands < model.getCurrentPlayer().getHand().size(); hands++) {
        CardImpl card = model.getCurrentPlayer().getHand().get(hands);
        worseStrat = helpFindNotBestStrat(strats, bestStrat);
        if (worseStrat.calculateMoveScore(bestCell, bestCard)
                > bestStrat.calculateMoveScore(bestCell, bestCard)) {
          bestStrat = worseStrat;
        }
        if (bestStrat.calculateMoveScore(cell, card)
                > bestStrat.calculateMoveScore(bestCell, bestCard)) {
          bestCell = cell;
          bestCard = card;
          tiedCells.clear();
        } else if (bestStrat.calculateMoveScore(cell, card)
                == bestStrat.calculateMoveScore(bestCell, card)) {
          tiedCells.add(cell);
          if (!tiedCells.contains(bestCell)) {
            tiedCells.add(bestCell);
          }
        }
      }
    }
    if (tiedCells.size() > 1) {
      bestCell = resolveTiedCells(tiedCells);
      bestCard = resolveTiedCards(bestCell, model.getCurrentPlayer().getHand());
    }

    bestCard1 = bestCard;
    return bestCell;
  }

  @Override
  public CardImpl getBestCard() {
    return bestCard1;
  }

  /**
   * helper function that gets the strategy that is not the current/"best".
   *
   * @param strats the 2 item list of computers/strategies that are being mixed.
   * @param current the current/"best" strategy.
   * @return the ComputerPlayers representation of the strategy that is not the current/"best".
   */
  private ComputerPlayers helpFindNotBestStrat(List<ComputerPlayers> strats,
                                               ComputerPlayers current) {
    if (strats.get(0) == current) {
      return strats.get(1);
    } else {
      return strats.get(0);
    }
  }

  /**
   * helper that takes the string representation of the two desired computers/strategies and
   * converts it into a list of ComputerPlayers.
   *
   * @param comp1 the string representation of the first computer/strategy.
   * @param comp2 the string representation of the second computer/strategy.
   * @return a list of ComputerPlayers that are the strategies to be mixed.
   */
  private List<ComputerPlayers> chooseStrategies(String comp1, String comp2) {
    List<ComputerPlayers> strategies = new ArrayList<>();
    if (comp1.equals("corner") || comp2.equals("corner")) {
      strategies.add(cornerComputer);
    }
    if (comp1.equals("least") || comp2.equals("least")) {
      strategies.add(leastFlipsComputer);
    }
    if (comp1.equals("most") || comp2.equals("most")) {
      strategies.add(mostFlipsComputer);
    }
    if (comp1.equals("minimax") || comp2.equals("minimax")) {
      strategies.add(miniMaxComputer);
    }
    if (strategies.isEmpty()) {
      throw new IllegalArgumentException("No valid strategies");
    }
    if (strategies.size() == 1) {
      throw new IllegalArgumentException("Only one was valid");
    }
    return strategies;
  }

  @Override
  public int calculateMoveScore(Cell cell, CardImpl card) {
    List<ComputerPlayers> strats = chooseStrategies(comp1, comp2);
    ComputerPlayers strat1 = strats.get(0);
    ComputerPlayers strat2 = strats.get(1);
    if (strat1.calculateMoveScore(cell, card) > strat2.calculateMoveScore(cell, card)) {
      return strat1.calculateMoveScore(cell, card);
    } else {
      return strat2.calculateMoveScore(cell, card);
    }
  }

  @Override
  public List<Cell> getAvailableCells() {
    return possibleCells;
  }

  @Override
  public String getStrategyName() {
    return comp1 + comp2 + "mix computer";
  }
}
