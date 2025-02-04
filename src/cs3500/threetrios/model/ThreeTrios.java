package cs3500.threetrios.model;

import cs3500.threetrios.controller.HumanPlayer;
import cs3500.threetrios.controller.MachinePlayer;
import cs3500.threetrios.controller.ThreeTriosComputerController;
import cs3500.threetrios.controller.ThreeTriosPlayerController;
import cs3500.threetrios.view.ThreeTriosView;
import java.io.File;

/**
 * Main for ThreeTrios, allows a user to run the game.
 */
public class ThreeTrios {
  /**
   * Main.
   */
  public static void main(String[] args) {
    final String cellPath = "docs" + File.separator
            + "BoardFile_ReachableCells.config";
    final String cardPath = "docs" + File.separator
            + "CardFile_EnoughForAll.config";
    final String cellPath2 = "docs" + File.separator
            + "BoardFile_NoHoles.config";
    final String cellPath3 = "docs" + File.separator
            + "BoardFile_StrandedCell.config";
    ConfigurationFileReader configReader =
            new ConfigurationFileReader(new File(cellPath), new File(cardPath));

    playMixedGame(configReader);
  }

  /**
   * A series of commands used to test the view.
   * In later assignments, these commands will be used by the controller.

   * @param model is the model being played.
   * @param guiView is the view.
   */
  private static void testPlayGame(ThreeTriosModel model, ThreeTriosView guiView) {
    model.playCard(model.getCurrentPlayer().getHand().get(4), 0, 2);
    model.switchTurn();
    model.playCard(model.getCurrentPlayer().getHand().get(4), 2, 0);
    model.switchTurn();
    model.playCard(model.getCurrentPlayer().getHand().get(4), 4, 0);
    model.switchTurn();
    guiView.updateView();
  }

  private static void playHumanOnlyGame(ConfigurationFileReader configReader) {
    ThreeTriosModel model = new ThreeTriosModel(configReader);
    ThreeTriosViewModel viewModel = new ThreeTriosViewModel(model);
    HumanPlayer player1 = new HumanPlayer(model);
    HumanPlayer player2 = new HumanPlayer(model);
    ThreeTriosView viewPlayer1 = new ThreeTriosView(viewModel);
    ThreeTriosView viewPlayer2 = new ThreeTriosView(viewModel);
    ThreeTriosPlayerController redController
            = new ThreeTriosPlayerController(model, player1, viewPlayer1);
    ThreeTriosPlayerController blueController
            = new ThreeTriosPlayerController(model, player2, viewPlayer2);
    model.startGame();
  }

  private static void playMixedGame(ConfigurationFileReader configReader) {
    ThreeTriosModel model = new ThreeTriosModel(configReader);
    ThreeTriosViewModel viewModel = new ThreeTriosViewModel(model);
    ComputerPlayers cornerComputer = new CornerComputer(model);
    HumanPlayer player2 = new HumanPlayer(model);
    MachinePlayer player1 = new MachinePlayer(cornerComputer, model);
    ThreeTriosView viewComputer = new ThreeTriosView(viewModel);
    ThreeTriosView viewPlayer = new ThreeTriosView(viewModel);
    ThreeTriosComputerController redController
            = new ThreeTriosComputerController(model, player1, viewComputer);
    ThreeTriosPlayerController blueController
            = new ThreeTriosPlayerController(model, player2, viewPlayer);
    model.startGame();
  }

}
