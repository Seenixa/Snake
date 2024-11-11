package game.snake.players;

import java.util.Random;

import game.snake.Direction;
import game.snake.SnakeGame;
import game.snake.SnakePlayer;
import game.snake.utils.Cell;
import game.snake.utils.SnakeGameState;

/**
 * Chooses that neighbor of the head which is the closest to the food.
 */
public class RoundAndRoundPlayer extends SnakePlayer {

    public RoundAndRoundPlayer(SnakeGameState gameState, int color, Random random) {
        super(gameState, color, random);
    }

    @Override
    public Direction getAction(long remainingTime) {
        // find food on the table
        Cell food = null;
        for (int i = 0; i < gameState.board.length; i++) {
            for (int j = 0; j < gameState.board[i].length; j++) {
                if (gameState.board[i][j] == SnakeGame.FOOD) {
                    food = new Cell(i, j);
                }
            }
        }
        // find closest cell to the food of the head's neighbors
        Cell closest = food;
        Cell head = gameState.snake.peekFirst();
        int distance = gameState.maxDistance();
        for (Cell c : head.neighbors()) {
            if (gameState.isOnBoard(c) && gameState.getValueAt(c) != SnakeGame.SNAKE && c.distance(food) < distance) {
                distance = c.distance(food);
                closest = c;
            }
        }
        // The larger the snake becomes the more likely for it to trap itself
        // Until size 8, there's no danger, we can be greedy

        if (gameState.getSize() >= 8){

        }

        return head.directionTo(closest);
    }

}
