package game.snake.utils;

import java.util.Comparator;

/**
 * Comparator for cell objects. Uses singleton pattern. <br/>
 * usage: {@link CellComparator#newInstance()}
 */
public final class CellComparator implements Comparator<Cell> {
  private static final CellComparator instance = new CellComparator();
  
  /**
   * Returns the instance of the class.
   * @return the comparator instance
   */
  public static final CellComparator newInstance() {
    return instance;
  }
  
  private CellComparator() {}

  @Override
  public int compare(Cell o1, Cell o2) {
    if (o1.i < o2.i || (o1.i == o2.i && o1.j < o2.j)) {
      return -1;
    }
    if (o2.i < o1.i || (o1.i == o2.i && o2.j < o1.j)) {
      return 1;
    }
    return 0;
  }

}
