package cs3500.threetrios.controller;

import cs3500.threetrios.model.CardImpl;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.ComputerPlayers;
import cs3500.threetrios.model.ThreeTriosModel;
import java.util.List;

/**
 * Machine players are the Player object associated with a computer played game.
 */
public class MachinePlayer extends HumanPlayer {
  private final ComputerPlayers computer;

  /**
   * This constructor builds the player's structures and adds that structure to the model.

   * @param model is the model this player is associated with.
   */
  public MachinePlayer(ComputerPlayers computer, ThreeTriosModel model) {
    super(model);
    this.computer = computer;
  }

  /**
   * Obtains the best cell for the machine to place it's card at.
   *
   * @return the Cell that is best.
   */
  public Cell getMachineCellPlacement() {
    return computer.selectBestMove();
  }

  /**
   * Obtains the best card for the machine to place at the best cell location.
   *
   * @return the Card that is best
   */
  public CardImpl getMachineCard() {
    return computer.getBestCard();
  }

  /**
  * Returns possible moves.

  * @return a list of cell that can be played to
  */
  public List<Cell> getAvailableMoves() {
    return computer.getAvailableCells();
  }

}
