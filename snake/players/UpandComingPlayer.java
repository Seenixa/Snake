package game.snake.players;

import java.util.Random;

import game.snake.Direction;
import game.snake.SnakeGame;
import game.snake.SnakePlayer;
import game.snake.utils.Cell;
import game.snake.utils.SnakeGameState;

/**
 * Chooses that neighbor of the head which is the closest to the food.
 * As the snake reaches 7 in size, it goes to a side and start going in circles.
 * When it reaches the column of the food, it goes up/down for it, then back to the side.
 */
public class UpandComingPlayer extends SnakePlayer {

    public UpandComingPlayer(SnakeGameState gameState, int color, Random random) {
        super(gameState, color, random);
    }

    @Override
    public Direction getAction(long remainingTime) {
        // find food on the table
        Cell food = null;
        // find closest cell to the food of the head's neighbors
        for (int i = 0; i < gameState.board.length; i++) {
            for (int j = 0; j < gameState.board[i].length; j++) {
                if (gameState.board[i][j] == SnakeGame.FOOD) {
                    food = new Cell(i, j);
                }
            }
        }
        if (gameState.getSize() <= 7) {
            Cell closest = food;
            Cell head = gameState.snake.peekFirst();
            int distance = gameState.maxDistance();
            for (Cell c : head.neighbors()) {
                if (gameState.isOnBoard(c) && gameState.getValueAt(c) != SnakeGame.SNAKE && c.distance(food) < distance) {
                    distance = c.distance(food);
                    closest = c;
                }
            }
            return head.directionTo(closest);
        }
        // The larger the snake becomes the more likely for it to trap itself
        // Until size 8, there's no danger, we can be greedy



    return null;
    }

    private void getToTheSide(){
        Cell checkCell = null;
        Boolean unblockedDown = true;
        Cell head = gameState.snake.peekFirst();
        for (int i = gameState.snake.peekFirst().i; i < gameState.board.length && unblockedDown; i++ ){
            checkCell = new Cell (i, gameState.snake.peekFirst().j);
            if (gameState.getValueAt(checkCell) == SnakeGame.SNAKE){
                unblockedDown = false;
            }
        }
    }
}
