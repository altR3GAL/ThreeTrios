package cs3500.threetrios.view;

import cs3500.threetrios.model.ReadOnlyTripleTriadModel;
import java.io.IOException;

/**
 * Textual view for TripleTriad.
 * Allows visualization of a game of TripleTriad using the ThreeTriosModel rule set.
 */
public class ThreeTriosTextView {
  private final ReadOnlyTripleTriadModel model;

  /**
   * Constructor that takes in a model and ensures it can be viewed (that it's not null).

   * @param model is the model being passed in.
   */
  public ThreeTriosTextView(ReadOnlyTripleTriadModel model) {
    if (model == null) {
      throw new IllegalArgumentException("model cannot be null");
    }
    this.model = model;
  }


  /**
   * Renders the board, hand, and displays whose turn it is.

   * @return the textual representation of the ThreeTrios game.
   */
  public String render() {
    StringBuilder sb = new StringBuilder();
    try {
      sb.append(model.getCurrentPlayer().toString());
      sb.append("\n");
      sb.append(renderBoard());
      sb.append(renderHand());
      return sb.toString();
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Helper method that gets the String representation of a ThreeTrios board state.

   * @return the Textual Representation of the board.
   * @throws IOException is the appending fails.
   */
  private String renderBoard() throws IOException {
    StringBuilder sb = new StringBuilder();
    for (int row = 0; row < model.getGameState().length; row++) {
      for (int col = 0; col < model.getGameState()[0].length; col++) {
        if (model.getGameState()[row][col].isHole()) {
          sb.append(" ");
        } else if (!model.getGameState()[row][col].hasBeenPlayed()) {
          sb.append("_");
        } else if (model.getGameState()[row][col].hasBeenPlayed()) {
          sb.append(model.getGameState()[row][col].getCard().getColor().toString());
        }
      }
      sb.append("\n");
    }
    return sb.toString();
  }


  /**
   * Helper method that gets the String representation of a hand.

   * @return the String representation.
   * @throws IOException is the appending fails.
   */
  private String renderHand() throws IOException {
    StringBuilder sb = new StringBuilder();
    for (int cards = 0; cards < model.getCurrentPlayer().getHand().size(); cards++) {
      sb.append(model.getCurrentPlayer().getHand().get(cards).toString()).append("\n");
    }
    return sb.toString();
  }
}
