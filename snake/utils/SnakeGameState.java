package game.snake.utils;

import java.util.LinkedList;

import game.engine.utils.Utils;
import game.snake.Direction;
import game.snake.SnakeGame;

/**
 * Represents the sate of the game.
 */
public class SnakeGameState {
  /** represents the board */
  public final int[][] board;
  /** represents the snake by its coordinates */
  public final LinkedList<Cell> snake;
  /** represents the current direction of the snake */
  public Direction direction;
  /** the current score */
  public int score;
  /**
   * Constructs a game state and sets the specified parameters.
   * @param boart game board
   * @param snake snake coordinates
   * @param direction current direction
   * @param score current score
   */
  public SnakeGameState(int[][] boart, LinkedList<Cell> snake, Direction direction, int score) {
    this.board = boart;
    this.snake = snake;
    this.direction = direction;
    this.score = score;
  }
  /**
   * Copy constructor.
   * @param gameState to be cloned
   */
  public SnakeGameState(SnakeGameState gameState) {
    board = Utils.copy(gameState.board);
    snake = new LinkedList<Cell>();
    for (Cell c : gameState.snake) {
      snake.add(new Cell(c));
    }
    direction = new Direction(gameState.direction);
    score = gameState.score;
  }
  /**
   * Returns the head of the snake (the first cell in the list).
   * @return the head of the snake
   */
  public Cell getHead() {
    return snake.peekFirst();
  }
  /**
   * Adds the specified cell as the head of the snake (new first cell into the list).
   * @param head to be added
   */
  public void addHead(Cell head) {
    snake.addFirst(head);
  }
  /**
   * Removes and returns the head of the snake.
   * @return the removed head
   */
  public Cell removeHead() {
    return snake.pollFirst();
  }
  /**
   * Returns the tail of the snake (the last cell in the list).
   * @return the tail of the snake
   */
  public Cell getTail() {
    return snake.peekLast();
  }
  /**
   * Adds the specified cell as the tail of the snake (new last cell into the list).
   * @param tail to be added
   */
  public void addTail(Cell tail) {
    snake.add(tail);
  }
  /**
   * Removes and returns the tail of the snake.
   * @return the removed tail
   */
  public Cell removeTail() {
    return snake.pollLast();
  }
  /**
   * Returns the length of the snake (number of snake cells).
   * @return the length of the snake
   */
  public int getSize() {
    return snake.size();
  }
  /**
   * Returns true iff the specified cell is on the board (inside the borders).
   * @param c to be checked
   * @return true if the cell is on the board
   */
  public boolean isOnBoard(Cell c) {
    return 0 <= c.i && c.i < board.length && 0 <= c.j && c.j < board[c.i].length;
  }
  /**
   * Returns the value of the table at the specified cell.
   * @param c to be returned at 
   * @return the value at the cell
   */
  public int getValueAt(Cell c) {
    return board[c.i][c.j];
  }
  /**
   * Sets the specified value at the specified cell on the board.
   * @param c to be set at
   * @param value to be set
   */
  public void setValueAt(Cell c, int value) {
    board[c.i][c.j] = value;
  }
  /**
   * Returns the Hamming maximal distance at the board (width + heights)
   * @return maximal distance
   */
  public int maxDistance() {
    return board.length + board[0].length;
  }
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("SCORE: " + score + " HEAD: " + snake.peekFirst() + " D: " + direction + " L: " + snake.size() + "\n");
    sb.append("/");
    for (int j = 0; j < board[0].length; j++) {
      sb.append("-");
    }
    sb.append("\\\n");
    for (int i = 0; i < board.length; i++) {
      sb.append("|");
      for (int j = 0; j < board[i].length; j++) {
        if (snake.peekFirst().same(i, j)) {
          sb.append(SnakeGame.TILES.get(SnakeGame.HEAD));
        } else {
          sb.append(SnakeGame.TILES.get(board[i][j]));
        }
      }
      sb.append("|\n");
    }
    sb.append("\\");
    for (int j = 0; j < board[0].length; j++) {
      sb.append("-");
    }
    sb.append("/");
    return sb.toString();
  }
}
