package cs3500.threetrios.controller;

/**
 * Player actions represent the events a player can create.
 */
public interface PlayerAction {

  /**
   * when a card is selected to be played.
   *
   * @param index the index of the selected card.
   */
  void cardChosen(int index);

  /**
   * when a position is chosen for a card to be played.
   *
   * @param row the row index for the position.
   * @param col the col index for the position.
   */
  void positionChosen(int row, int col);

  /**
   * Add listener.
   *
   * @param listener the listener being added.
   */
  void playerListenerAdd(PlayerListener listener);

  /**
   * Remove listener.

   * @param listener the listener being removed.
   */
  void playerListenerRemove(PlayerListener listener);
}