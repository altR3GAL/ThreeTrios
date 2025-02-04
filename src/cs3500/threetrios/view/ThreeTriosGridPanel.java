package cs3500.threetrios.view;

import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.ThreeTriosViewModel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A grid panel is the panel which contains all the visual data associated with the Cell grid
 * used by the model for storing the game state.
 */
public class ThreeTriosGridPanel extends ThreeTriosPanel implements TripleTriadGridPanel {
  private final ThreeTriosViewModel model;
  private final ThreeTriosView frame;
  private int boldRow;
  private int boldCol;
  private boolean cellClicked = false;

  public static final double FONT_CONVERSION = 0.14245;
  public static final double PIXEL_TO_FONT = 1.33;


  /**
   * Default Constructor.

   * @param model is the readOnly model being viewed.
   * @param frame is the frame this panel is being added to.
   */
  public ThreeTriosGridPanel(ThreeTriosViewModel model, ThreeTriosView frame) {
    super(frame);
    this.model = model;
    this.frame = frame;
  }


  /**
   * This click listener allows a response to the user clicking on this panel.
   */
  public void addClickListener() {
    this.addMouseListener(new TTClickListener());
  }

  /**
   * Upon a click, the translated row and column clicked is reported.

   * @param row is the translated row.
   * @param col is the translated column.
   */
  public void handleGridClick(int row, int col) {
    System.out.println("Column (X): " + col + " Row (Y), " + row);
    frame.positionChosen(row, col);
  }


  /**
   * Handles Clicking using MouseAdapter.
   */
  private class TTClickListener extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent e) {
      int currentCol = e.getY() / frame.getCellHeight();
      int currentRow = e.getX() / frame.getTileWidth();

      //If the cell that has been clicked is different from the currently bolded cell
      // Set cellClicked to true
      cellClicked = true;

      boldRow = currentCol;
      boldCol = currentRow;

      updateBoard();
    }
  }

  /**
   * Updates the board drawing.
   */
  public void updateBoard() {
    frame.revalidate();
    repaint();
  }

  public ThreeTriosView getFrame() {
    return frame;
  }

  /**
   * Paints the cells with the proper color.

   * @param g the <code>Graphics</code> object to protect
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    Cell[][] currentBoard = model.getGameState();
    int rows = currentBoard.length;
    int cols = currentBoard[0].length;

    int cellWidth = frame.getTileWidth();
    int cellHeight = frame.getCellHeight();

    // Draw each cell with its color
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        if (!currentBoard[row][col].hasBeenPlayed()) {
          if (currentBoard[row][col].isHole()) {
            g2d.setColor(Color.LIGHT_GRAY);
          } else {
            g2d.setColor(Color.YELLOW);
          }
          g2d.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
          g2d.setStroke(new BasicStroke(1));
          g2d.setColor(Color.BLACK);
          g2d.drawRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
        } else {
          renderCard(g, currentBoard[row][col].getCard(), col * cellWidth,
                  row * cellHeight, cellWidth, cellHeight);
          g2d.setStroke(new BasicStroke(1));
          g2d.setColor(Color.BLACK);
          g2d.drawRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
        }
      }
    }

    if (cellClicked) { // issue here
      cellClicked = false;
      System.out.println("fuck me");
      handleGridClick(boldRow, boldCol);
    }

  }

  /**
   * Sets the font size based on the size of the card.

   * @param g is the graphic.
   * @param color is the color of the card.
   */
  public void setFontSize(Graphics g, cs3500.threetrios.model.Color color) {
    //Set the size for the grid
    g.setFont(new Font("Arial", Font.BOLD, getGridFontSize()));
  }

  /**
   * Gets the font size based on the pixel height of the card.

   * @return the font size.
   */
  private int getGridFontSize() {
    double fontPixels = frame.getCellHeight() * FONT_CONVERSION;
    //Do some math to determine the font size for the grid
    return (int) Math.ceil(fontPixels / PIXEL_TO_FONT);
  }
}
