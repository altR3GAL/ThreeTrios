package cs3500.threetrios.view;

import cs3500.threetrios.model.CardImpl;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JPanel;

abstract class ThreeTriosPanel extends JPanel {
  private final ThreeTriosView frame;
  protected int fontSize;

  public ThreeTriosPanel(ThreeTriosView frame) {
    this.frame = frame;
  }

  /**
   * Render a given card on the GUI.

   * @param g is the graphic.
   * @param card is the card being rendered.
   * @param xPos is the x position of the cell being drawn in.
   * @param yPos is the y position of the cell being drawn in.
   * @param cellWidth is the width of the cell being drawn in.
   * @param cellHeight is the width of the cell being drawn in.
   */
  public void renderCard(Graphics g, CardImpl card, int xPos, int yPos, int cellWidth,
                         int cellHeight) {
    Color cardColor;

    if (card.getColor() == cs3500.threetrios.model.Color.RED) {
      cardColor = new Color(255, 102, 102);
      setFontSize(g, cs3500.threetrios.model.Color.RED);
    } else if (card.getColor() == cs3500.threetrios.model.Color.BLUE) {
      cardColor = new Color(173, 210, 255);
      setFontSize(g, cs3500.threetrios.model.Color.BLUE);
    } else {
      cardColor = Color.LIGHT_GRAY;
    }

    String attackValueText = card.getAttackString();

    g.setColor(cardColor);
    g.fillRect(xPos, yPos, cellWidth, cellHeight);


    // Draw the text on the card, supporting multiple lines
    g.setColor(Color.BLACK); // Set text color
    FontMetrics fm = g.getFontMetrics();
    String[] lines = attackValueText.split("\n"); // Split text by line breaks

    // Calculate initial y-position for text so it’s centered vertically
    int textY = yPos + (cellHeight - (fm.getHeight() * lines.length)) / 2 + fm.getAscent();

    for (String line : lines) {
      int textX = xPos + (cellWidth - fm.stringWidth(line)) / 2; // Center each line horizontally
      g.drawString(line, textX, textY);
      textY += fm.getHeight(); // Move down for the next line
    }
  }

  /**
    * Gets the default font size.

    * @param color is the color of card.
    * @return the font size.
    */
  int getCurrentFontSize(cs3500.threetrios.model.Color color) {
    double cardHeight = frame.getCardHeight();
    double startingCardHeight = frame.determineStartingHeight();

    return (int) (18 * Math.ceil(cardHeight / startingCardHeight));
  }

  /**
   * Called at the start and whenever the GUI is resized.

   * @param color is the color of the card the font is on.
   */
  public void updateFontSize(cs3500.threetrios.model.Color color) {
    fontSize = getCurrentFontSize(color);
  }


  abstract void setFontSize(Graphics g, cs3500.threetrios.model.Color color);



}
