package cs3500.threetrios.view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This listener triggers the resizing of the panels upon the frame being maximized.
 */
public class MaximizeListener extends WindowAdapter {
  private final ThreeTriosView frame;
  private final ThreeTriosHandPanel bluePanel;
  private final ThreeTriosHandPanel redPanel;

  /**
   * Constructor for the maximize listener.

   * @param frame is the frame being maximized.
   * @param bluePanel is the right-most panel.
   * @param redPanel is the left-most panel.
   */
  public MaximizeListener(ThreeTriosView frame, ThreeTriosHandPanel bluePanel,
                          ThreeTriosHandPanel redPanel) {
    this.frame = frame;
    this.bluePanel = bluePanel;
    this.redPanel = redPanel;
  }

  @Override
  public void windowStateChanged(WindowEvent e) {
    ResizablePanelListener resizer = new ResizablePanelListener(frame, bluePanel, redPanel);
    resizer.triggerResize();
  }
}
