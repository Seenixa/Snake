package game.snake.players;

import java.util.Random;

import game.snake.Direction;
import game.snake.SnakePlayer;
import game.snake.ui.SnakeCanvas;
import game.snake.utils.SnakeGameState;

/**
 * For playing manual on the GUI (controlled by arrows).
 */
public class HumanPlayer extends SnakePlayer {

  public HumanPlayer(SnakeGameState gameState, int color, Random random) {
    super(gameState, color, random);
  }

  @Override
  public Direction getAction(long remainingTime) {
    return SnakeCanvas.getLastAction();
  }

}
