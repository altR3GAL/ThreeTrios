package cs3500.threetrios.view;

import cs3500.threetrios.controller.PlayerAction;
import cs3500.threetrios.controller.PlayerListener;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.ThreeTriosViewModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

/**
 * This GUI view is represented by a Frame.
 */
public class ThreeTriosView extends JFrame implements TripleTriadFrame, PlayerAction {

  private static final int WIDTH_RATIO = 112;
  private static final int HEIGHT_RATIO = 162;
  private static final int HEIGHT_ADJUSTMENT = HEIGHT_RATIO / 5;
  private static final int WIDTH_ADJUSTMENT = WIDTH_RATIO / 10;

  private final int gridWidth;
  private final ThreeTriosHandPanel redHandPanel;
  private final ThreeTriosHandPanel blueHandPanel;
  private final ThreeTriosViewModel model;
  private final ThreeTriosGridPanel grid;

  private List<PlayerListener> observers = new ArrayList<>();
  /**
   * Constructor, needs to take in a readOnlyModel.

   * @param model is the readOnlyModel being used.
   */
  public ThreeTriosView(ThreeTriosViewModel model) {
    this.model = model;

    gridWidth = getNumCols() * WIDTH_RATIO;

    this.setSize(determineStartingWidth(), determineStartingHeight());
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    Color redPlayerColor = Color.RED;
    Color bluePlayerColor = Color.BLUE;

    redHandPanel = new ThreeTriosHandPanel(model, this, redPlayerColor);
    redHandPanel.addClickListener();
    blueHandPanel = new ThreeTriosHandPanel(model, this, bluePlayerColor);
    blueHandPanel.addClickListener();
    grid = new ThreeTriosGridPanel(model, this);  // Initialize the grid here
    grid.addClickListener();
    this.build();
  }

  /**
   * Gets the starting width of the GUI based on the number of cells in the grid.

   * @return the starting width
   */
  public int determineStartingWidth() {
    return ((getNumCols() + 2) * WIDTH_RATIO) + WIDTH_ADJUSTMENT;
  }

  /*
  public void notify(String message) {
    JOptionPane.showMessageDialog(null, message);
  }
  */

  /**
   * Gets the starting height of the GUI based on the number of cells in the grid.

   * @return the starting height
   */
  public int determineStartingHeight() {
    return ((getNumRows()) * HEIGHT_RATIO) + HEIGHT_ADJUSTMENT;
  }

  /**
   * Gets the number of cells/cards wide the GUI needs to be.

   * @return the number of tiles.
   */
  public int getWidthNumTiles() {
    return (getNumCols() + 2);
  }

  /**
   * Gets the number of cells/cards tall the GUI needs to be.

   * @return the number of tiles.
   */
  public int getHeightNumTiles() {
    return getNumRows();
  }


  /**
   * Builds the GUI.
   */
  public void build() {
    this.setTitle("Current Player: Red");
    // Main panel with BorderLayout to hold multiple panels
    this.setLayout(new BorderLayout());

    // Red Player Hand
    redHandPanel.setPreferredSize(new Dimension(getTileWidth(), determineStartingHeight()));
    redHandPanel.updateHand();
    this.add(redHandPanel, BorderLayout.WEST);

    // Blue Player Hand
    blueHandPanel.setPreferredSize(new Dimension(getTileWidth(), determineStartingHeight()));
    blueHandPanel.updateHand();
    this.add(blueHandPanel, BorderLayout.EAST);

    // Board
    grid.setPreferredSize(new Dimension(gridWidth, determineStartingHeight()));
    this.add(grid, BorderLayout.CENTER);

    this.addComponentListener(new ResizablePanelListener(this, blueHandPanel, redHandPanel));
    this.addWindowStateListener(new MaximizeListener(this, blueHandPanel, redHandPanel));
  }



  /**
   * Refreshes the view with the updated information.
   * (called at end of every phase)
   */
  public void updateView() {
    String playerDisplay = "Current " + model.getCurrentPlayer().toString();

    this.setTitle(playerDisplay);

    if (model.getCurrentPlayer().getColor().equals(cs3500.threetrios.model.Color.RED)) {
      redHandPanel.updateHand();
    } else {
      blueHandPanel.updateHand();
    }
    grid.updateBoard();
  }


  /**
   * Gets the width of the tiles based on the number of columns on the board.

   * @return the width of the tiles.
   */
  public int getTileWidth() {
    return getWidth() / getWidthNumTiles();
  }

  /**
   * Gets the height of the tiles based on the number of rows on the board.

   * @return the height of the tiles.
   */
  public int getCardHeight() {
    if (getNumCardsInHand() == 0) {
      return 0;
    }
    return ((getHeight() - HEIGHT_ADJUSTMENT) / getNumCardsInHand());
  }

  public int getCellHeight() {
    return ((getHeight() - HEIGHT_ADJUSTMENT) / getHeightNumTiles());
  }


  /**
   * Checks the grid in the model and returns the number of rows.

   * @return the number of rows
   */
  public int getNumRows() {
    Cell[][] currentBoard = model.getGameState();

    return currentBoard.length;
  }

  /**
   * Checks the grid in the model and returns the number of columns.

   * @return the number of columns
   */
  public int getNumCols() {
    Cell[][] currentBoard = model.getGameState();

    return currentBoard[0].length;
  }

  /**
   * Checks the hands in the model and returns the number of cards in each.

   * @return the number of cards
   */
  public int getNumCardsInHand() {
    return model.getCurrentPlayer().getHand().size();
  }


  @Override
  public void cardChosen(int index) {
    //When a card is unselected the returned index is -1
    if (index != -1) {
      for (PlayerListener listener : observers) {
        listener.cardChosenHeard(index);
      }
    }
  }

  @Override
  public void positionChosen(int row, int col) {
    for (PlayerListener listener : observers) {
      listener.positionChosenHeard(row, col);
    }
  }

  @Override
  public void playerListenerAdd(PlayerListener listener) {
    if (listener != null) {
      observers.add(listener);
    }
  }

  @Override
  public void playerListenerRemove(PlayerListener listener) {
    observers.remove(listener);
  }
}
