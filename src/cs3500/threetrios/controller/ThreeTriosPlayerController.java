package cs3500.threetrios.controller;

import cs3500.threetrios.model.CardImpl;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.PlayerStructure;
import cs3500.threetrios.model.ThreeTriosModel;
import cs3500.threetrios.view.ThreeTriosView;
import javax.swing.JOptionPane;

/**
 * This controller allows a human to control a game of ThreeTrios.
 */
public class ThreeTriosPlayerController implements  ModelListener, PlayerListener {
  private final ThreeTriosModel model;
  private final HumanPlayer player;
  private final ThreeTriosView view;

  private CardImpl currentChosenCard;
  private boolean cardPlayedSuccessfully = false;

  /**
   * controller for player.
   *
   * @param model is the model for this game.
   * @param player is the player playing.
   * @param view is the view.
   */
  public ThreeTriosPlayerController(ThreeTriosModel model, HumanPlayer player,
                                    ThreeTriosView view) {
    this.model = model;
    this.player = player;
    this.view = view;

    view.playerListenerAdd(this);
    model.addModelListener(this);
  }

  @Override
  public void cardChosenHeard(int index) {
    if (model.getCurrentPlayer() == this.player.getPlayerStructure()) {
      if (index == -1) {
        System.out.println("I heard someone unselect a card");
        currentChosenCard = null;
      } else {
        //System.out.println("I heard someone clicked on this index: " + index);
        currentChosenCard = getChosenCard(index); //Set the current card
        //System.out.println("Current chosen card: " + currentChosenCard);
      }
    }
  }

  /**
   * Gets the card from this player's hand after the controller hears a selection.

   * @param index is the index of the card.
   * @return the card.
   */
  private CardImpl getChosenCard(int index) {
    if (index == -1) {
      throw new IllegalArgumentException("An error occurred selecting a card.");
    } else if (index >= player.getHand().size()) {
      throw new IndexOutOfBoundsException("The given index is out of bounds.");
    }

    return player.getHand().get(index);
  }

  @Override
  public void positionChosenHeard(int row, int col) {
    if (model.getCurrentPlayer() == this.player.getPlayerStructure()) {
      if (currentChosenCard == null) {
        JOptionPane.showMessageDialog(null, "You must select a card first.");
      } else if (getCell(row, col).hasBeenPlayed() || getCell(row, col).isHole()) {
        System.out.println("hi2");
        JOptionPane.showMessageDialog(null, "Cell is hole or played");
      } else {
        try {
          model.playCard(currentChosenCard, row, col);

          if (getCell(row, col).getCard() == currentChosenCard) {
            cardPlayedSuccessfully = true;
            model.resolveBattles();
            model.switchTurn();
            model.isGameOver();
          }
          view.setVisible(false);
        } catch (IllegalArgumentException e) {
          JOptionPane.showMessageDialog(null, "Card is not in the hand");
          view.updateView();
        }
      }
      view.updateView();
      view.setVisible(true);
    }
  }

  /**
   * Gets the cell at the given row and column.

   * @param row is the row.
   * @param col is the column.
   * @return the cell at that position.
   */
  private Cell getCell(int row, int col) {
    return model.getGameState()[row][col];
  }

  @Override
  public void playerTurnHeard(PlayerStructure player) {
    if (player == this.player.getPlayerStructure()) {
      System.out.println("my turn");
      view.setVisible(false);
      view.updateView();
      view.setVisible(true);
      JOptionPane.showMessageDialog(null, ("It's your turn" + player));
    }
  }

  @Override
  public void playerEndOfTurnHeard(PlayerStructure player) {
    if (model.getCurrentPlayer() == this.player.getPlayerStructure()) {
      view.setVisible(false);
    }
  }

  @Override
  public void gameEndHeard(String winner, int score) {
    if (model.getCurrentPlayer() == this.player.getPlayerStructure()) {
      view.updateView();
      view.setVisible(true);
      JOptionPane.showMessageDialog(null, (winner + " has won with a score of: " + score));
    }
  }
}
