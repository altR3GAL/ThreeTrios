package cs3500.threetrios.view;

import cs3500.threetrios.model.CardImpl;
import cs3500.threetrios.model.ThreeTriosViewModel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * This extension of JPanel allows these panels to be updated based on the model's data.
 */
public class ThreeTriosHandPanel extends ThreeTriosPanel implements TripleTriadHandPanel {
  private final Color playerColor;
  private final ThreeTriosViewModel model;
  private final ThreeTriosView frame;
  private Graphics2D g2d;
  private int boldRow;
  private boolean cellClicked = false;
  private int currentRow = -1;
  protected Color clickedColor;

  public static final double FONT_CONVERSION = 2;
  public static final double PIXEL_TO_FONT = 1.33;
  private static final int HEIGHT_RATIO = 162;
  private static final int HEIGHT_ADJUSTER = HEIGHT_RATIO / 5;

  /**
   * A hand panel is the panel which contains all the visual data associated with a player's hand.

   * @param model is the ReadOnly model being viewed.
   * @param frame is the frame this panel is being added to.
   * @param playerColor is the color of the player whose hand is being represented by this panel.
   */
  public ThreeTriosHandPanel(ThreeTriosViewModel model, ThreeTriosView frame, Color playerColor) {
    super(frame);
    this.playerColor = playerColor;
    this.model = model;
    this.frame = frame;
  }

  /**
   * This click listener allows a response to the user clicking on this panel.
   */
  public void addClickListener() {
    this.addMouseListener(new TTHandleClick());
  }


  /**
   * Handles clicking using MouseAdapter.
   */
  private class TTHandleClick extends MouseAdapter {
    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
      currentRow = e.getY() / frame.getCardHeight();

      //If the cell that has been clicked is different from the currently bolded cell
      // Set cellClicked to true
      cellClicked = true;
      clickedColor = playerColor;
      
      updateHand();
    }
  }

  /**
   * Gets the current copy of the hands.
   */
  private List<CardImpl> refreshHands() {
    if (playerColor == Color.RED) {
      return model.getRedHand();
    } else {
      return model.getBlueHand();
    }
  }


  /**
   * Draws the hands on the panel based on the model.
   */
  public void updateHand() {
    repaint();
  }

  /**
   * Paints the hands with the proper cards.

   * @param g the <code>Graphics</code> object to protect
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    g2d = (Graphics2D) g;

    renderHand(refreshHands(), g2d);
  }

  /**
   * Renders the visual for a given hand.

   * @param hand is the hand being drawn.
   * @param g2d is the grapic.
   */
  private void renderHand(List<CardImpl> hand, Graphics2D g2d) {
    int yPosition = 0;

    if (hand.isEmpty()) {
      return;
    }

    int cardHeight = (frame.getHeight() - HEIGHT_ADJUSTER) / hand.size();

    for (CardImpl card : hand) {

      renderCard(g2d, card, 0, yPosition, frame.getTileWidth(), cardHeight);
      g2d.setStroke(new BasicStroke(1));
      g2d.setColor(Color.BLACK);
      g2d.drawRect(0, yPosition, frame.getTileWidth(), cardHeight);
      yPosition += cardHeight; // Move down for the next card, with spacing
    }

    //If the card clicked is in the hand of the current player, and it has been clicked
    if (cellClicked) {
      String color;

      if (playerColor == Color.RED) {
        color = "Red";
      } else {
        color = "Blue";
      }

      if (sameColor()) {
        System.out.println("Player: " + color + ", Card Index " + currentRow);
        System.out.println(model.getCurrentPlayer().getHand().get(currentRow).toString());
        if (boldRow != currentRow) {
          triggerBold(currentRow);
          frame.cardChosen(currentRow);
        } else {
          triggerUnBold(currentRow);
          frame.cardChosen(-1);
        }
      }
    }
  }

  /**
   * Sets the thickness of the card at a given row to 4.

   * @param row is the card's index.
   */
  private void triggerBold(int row) {
    boldRow = currentRow;

    g2d.setStroke(new BasicStroke(4));
    g2d.setColor(Color.BLACK);

    int translatedRow = row * frame.getCardHeight();
    g2d.drawRect(0, translatedRow, frame.getTileWidth(), frame.getCardHeight());
  }

  /**
   * Resets the thickness of the card at a given row to 1.

   * @param row is the card's index.
   */
  private void triggerUnBold(int row) {
    boldRow = -1;

    g2d.setStroke(new BasicStroke(1));
    g2d.setColor(Color.BLACK);

    int translatedRow = row * frame.getCardHeight();
    g2d.drawRect(0, translatedRow, frame.getTileWidth(), frame.getCardHeight());
  }

  /**
   * Returns true if the current player's color is the same as the hand being clicked on.
   * Prevents the Red player from selecting a blue card.

   * @return true if the colors are the same.
   */
  private boolean sameColor() {
    cs3500.threetrios.model.Color currentColor = model.getCurrentPlayer().getColor();

    if (currentColor.equals(cs3500.threetrios.model.Color.RED)) {
      return playerColor == Color.RED;
    } else {
      return playerColor == Color.BLUE;
    }
  }

  /**
   * Sets the font size based on the size of the card.

   * @param g is the graphic.
   * @param color is the color of the card.
   */
  public void setFontSize(Graphics g, cs3500.threetrios.model.Color color) {
    //Set the size for the hand
    g.setFont(new Font("Arial", Font.BOLD, getGridFontSize(color)));
  }

  /**
   * Gets the font size based on the pixel height of the card.

   * @return the font size.
   */
  private int getGridFontSize(cs3500.threetrios.model.Color color) {
    int handSize;
    int cardHeight;
    //Do some math for each hand
    if (color == cs3500.threetrios.model.Color.RED) {
      handSize = model.getRedHand().size();
    } else {
      handSize = model.getBlueHand().size();
    }

    //cardHeight is in pixels
    cardHeight = frame.getCardHeight() / handSize;

    double fontPixels = cardHeight * FONT_CONVERSION;

    return (int) Math.ceil(fontPixels / PIXEL_TO_FONT);
  }


}
