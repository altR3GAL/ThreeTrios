package cs3500.threetrios.view;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * This listener triggers the resizing of the panels upon the resizing of the frame.
 */
public class ResizablePanelListener extends ComponentAdapter {
  private final ThreeTriosView frame;
  private final ThreeTriosHandPanel bluePanel;
  private final ThreeTriosHandPanel redPanel;

  /**
   * Resizable Panel Listener Constructor.

   * @param frame is the frame being resized.
   * @param bluePanel is the right-most panel.
   * @param redPanel is the left-most panel.
   */
  public ResizablePanelListener(ThreeTriosView frame, ThreeTriosHandPanel bluePanel,
                                ThreeTriosHandPanel redPanel) {
    this.frame = frame;
    this.bluePanel = bluePanel;
    this.redPanel = redPanel;
  }

  @Override
  public void componentResized(ComponentEvent e) {
    triggerResize();
  }

  /**
   * Triggers the resizing of the two hand panels whenever the frame is resized.
   * The grid panel (which is centered) automatically resizes based on the new
   * sizes of the hand panels.
   */
  public void triggerResize() {
    int frameHeight = frame.getHeight();

    int handPanelWidth = frame.getTileWidth();

    bluePanel.setPreferredSize(new Dimension(handPanelWidth, frameHeight));
    redPanel.setPreferredSize(new Dimension(handPanelWidth, frameHeight));

    //Resize font
    redPanel.updateFontSize(cs3500.threetrios.model.Color.RED);
    bluePanel.updateFontSize(cs3500.threetrios.model.Color.BLUE);

    // Revalidate the frame to apply the new sizes
    frame.revalidate();
  }
}
