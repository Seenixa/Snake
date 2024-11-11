package game.snake.ui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import game.engine.ui.BoardGameCanvas;
import game.engine.ui.Drawable;
import game.snake.Direction;
import game.snake.SnakeGame;

public class SnakeCanvas extends BoardGameCanvas {
  private static final long serialVersionUID = -7400197786457475777L;
  /** maximal screen height */
  public static final int HEIGHT = 480;
  private static Direction lastAction;

  public SnakeCanvas(int n, int m, Drawable game) {
    super(n, m, HEIGHT/n, game);
    lastAction = SnakeGame.LEFT;
  }

  public void pressed(KeyEvent event) {
    switch (event.getKeyCode()) {
    case 37:
      lastAction = SnakeGame.LEFT;
      break;
    case 39:
      lastAction = SnakeGame.RIGHT;
      break;
    case 38:
      lastAction = SnakeGame.UP;
      break;
    case 40:
      lastAction = SnakeGame.DOWN;
      break;
    default:
      break;
    }
  }
  
  @Override
  public void handle(MouseEvent event) {
  }
  
  @Override
  public void setCoordinates() {
  }
  
  public static final Direction getLastAction() {
    return lastAction;
  }

}
