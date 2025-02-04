package cs3500.threetrios.controller;

import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.PlayerStructure;
import cs3500.threetrios.model.ThreeTriosModel;
import cs3500.threetrios.view.ThreeTriosView;
import javax.swing.JOptionPane;

/**
 * This controller allows a computer to control a game of ThreeTrios.
 */
public class ThreeTriosComputerController implements  ModelListener, PlayerListener {
  private final ThreeTriosModel model;
  private final MachinePlayer computer;
  private final ThreeTriosView view;

  /**
   * controller for player.

   * @param model is the model for the game.
   * @param computer is the computer strategy being used.
   * @param view is the view.
   */
  public ThreeTriosComputerController(ThreeTriosModel model, MachinePlayer computer,
                                      ThreeTriosView view) {
    this.model = model;
    this.computer = computer;
    this.view = view;

    view.playerListenerAdd(this);
    model.addModelListener(this);
  }

  @Override
  public void cardChosenHeard(int index) {
    //Not needed for computer.
  }


  @Override
  public void positionChosenHeard(int row, int col) {
    //Not needed for computer.
  }

  /**
   * Gets the cell at the using the computer strategy.

   * @return the cell at that position.
   */
  private Cell getCell() {
    return computer.getMachineCellPlacement();
  }

  @Override
  public void playerTurnHeard(PlayerStructure player) {
    if (model.getCurrentPlayer() == this.computer.getPlayerStructure()) {
      Cell machinePlay = getCell();
      int row = machinePlay.cellRowPosition();
      int col = machinePlay.cellColPosition();
      model.playCard(computer.getMachineCard(), row, col);
      model.resolveBattles();
      model.isGameOver();
      model.switchTurn();
      view.setVisible(false);
      view.updateView();
      view.setVisible(true);
    }
  }

  @Override
  public void playerEndOfTurnHeard(PlayerStructure player) {
    if (model.getCurrentPlayer() == this.computer.getPlayerStructure()) {
      //model.notifyTurn(model.getCurrentPlayer());
      view.setVisible(true);
    }
  }

  @Override
  public void gameEndHeard(String winner, int score) {
    if (model.getCurrentPlayer() == this.computer.getPlayerStructure()) {
      view.updateView();
      view.setVisible(true);
      JOptionPane.showMessageDialog(null, (winner + " has won with a score of: " + score));
    }
  }
}
