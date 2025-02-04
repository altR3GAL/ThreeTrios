package cs3500.threetrios.model;

/**
 * Values are the attack ratings used on cards.
 * 1 being the lowest, and A being the highest.
 */
public enum Value {
  ONE(1),
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5),
  SIX(6),
  SEVEN(7),
  EIGHT(8),
  NINE(9),
  A(10);  // 'A' as 10

  private final int value;

  Value(int value) {
    this.value = value;
  }

  /**
   * This method allows the Value to be used for math operations.

   * @return the integer value of a Value.
   */
  public int getValue() {
    return value;
  }

  /**
   * Provides the textual version of the Value.

   * @return the textual version.
   */
  @Override
  public String toString() {
    if (value == 10) {
      return "A";
    }
    return String.valueOf(value);
  }

}
