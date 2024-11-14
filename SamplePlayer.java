import java.util.Random;
import game.snake.Direction;
import game.snake.SnakeGame;
import game.snake.SnakePlayer;
import game.snake.utils.Cell;
import game.snake.utils.SnakeGameState;
public class SamplePlayer extends SnakePlayer {
    public SamplePlayer(SnakeGameState gameState, int color, Random random) {
        super(gameState, color, random);
    }
    @Override
    public Direction getAction(long remainingTime) {
        Direction direction = null;
        Cell food = findFood();
        if (gameState.getSize() <= 7 || gameState.getSize() > 50)
        direction = theGreedWay(food);

        if (gameState.getSize() > 7 && gameState.getSize() <= 30) {
            direction = theHorizontalWay();
        }

        if (gameState.getSize() > 30 && gameState.getSize() <= 50) {
            direction = theVerticalWay();
        }

        return direction;
    }

    public Cell findFood() {
        Cell food = null;
        for (int i = 0; i < gameState.board.length; i++) {
            for (int j = 0; j < gameState.board[i].length; j++) {
                if (gameState.board[i][j] == SnakeGame.FOOD) {
                    food = new Cell(i, j);
                }
            }
        }
        return food;
    }

    public Direction theGreedWay(Cell food) {
        Cell closest = food;
        Direction direction = null;
        int snakeDistanceToClosest = 0;
        int snakeDistanceToCheck = 0;
        Cell head = gameState.snake.peekFirst();
        int distance = gameState.maxDistance();
        for (Cell c : head.neighbors()) {
            if (distance == c.distance(food)){
                if (countSnakeDistance(head.directionTo(closest)) < countSnakeDistance(head.directionTo(c))) {
                    direction = head.directionTo(c);
                }
            }
            if (gameState.isOnBoard(c) && gameState.getValueAt(c) != SnakeGame.SNAKE && c.distance(food) < distance) {
                distance = c.distance(food);
                closest = c;
                direction = head.directionTo(closest);
            }
        }
        return direction;
    }
    public Direction theHorizontalWay(){
        Cell food = findFood();
        Cell head = gameState.snake.peekFirst();
        boolean isSafeUp = true;
        boolean isSafeDown = true;
        for (int i = head.i - 1; isSafeUp && i > 0; i--) {
            if (gameState.getValueAt(new Cell(i, head.j)) == SnakeGame.SNAKE) {
                isSafeUp = false;
            }
        }
        if (!isValid(new Cell(head.i - 1, head.j))){
            isSafeUp = false;
        }
        if (isSafeUp && head.i != 0 && head.i != gameState.board.length - 1) {
            return head.directionTo(new Cell(head.i - 1, head.j));
        }
        for (int i = head.i + 1; isSafeDown && i < gameState.board.length; i++) {
            if (gameState.getValueAt(new Cell(i, head.j)) == SnakeGame.SNAKE) {
                isSafeDown = false;
            }
        }
        if (!isValid(new Cell(head.i + 1, head.j))){
            isSafeDown = false;
        }
        if (isSafeDown && head.i != 0 && head.i != gameState.board.length - 1) {
            return head.directionTo(new Cell(head.i + 1, head.j));
        }
        if (head.i == 0) {
            if (head.j > food.j) {
                if (!isValid(new Cell(head.i, head.j - 1))){
                    return getValidDirection();
                }
                return head.directionTo(new Cell(head.i, head.j - 1));

            }
            if (head.j < food.j) {
                if (!isValid(new Cell(head.i, head.j + 1))){
                    return getValidDirection();
                }
                return head.directionTo(new Cell(head.i, head.j + 1));
            }
            if (!isValid(new Cell(head.i + 1, head.j))){
                return getValidDirection();
            }
            return head.directionTo(new Cell(head.i + 1, head.j));
        }

        if (head.i == gameState.board.length - 1){
            if (head.j > food.j) {
                if (!isValid(new Cell(head.i, head.j - 1))){
                    return getValidDirection();
                }
                return head.directionTo(new Cell(head.i, head.j - 1));
            }
            if (head.j < food.j) {
                if (!isValid(new Cell(head.i, head.j + 1))){
                    return getValidDirection();
                }
                return head.directionTo(new Cell(head.i, head.j + 1));
            }
            if (!isValid(new Cell(head.i - 1, head.j))){
                return getValidDirection();
            }
            return head.directionTo(new Cell(head.i - 1, head.j));
        }
        return null;
    }

    public Direction theVerticalWay() {
        Cell food = findFood();
        Cell head = gameState.snake.peekFirst();
        boolean isSafeLeft = true;
        boolean isSafeRight = true;
        for (int j = head.j - 1; isSafeLeft && j > 0; j--) {
            if (gameState.getValueAt(new Cell(head.i, j)) == SnakeGame.SNAKE) {
                isSafeLeft = false;
            }
        }
        if (!isValid(new Cell(head.i, head.j - 1))){
            isSafeLeft = false;
        }
        for (int j = head.j + 1; isSafeRight && j < gameState.board[0].length; j++) {
            if (gameState.getValueAt(new Cell(head.i, j)) == SnakeGame.SNAKE) {
                isSafeRight = false;
            }
        }
        if (!isValid(new Cell(head.i, head.j + 1))){
            isSafeRight = false;
        }

        if (isSafeLeft && head.j != 0 && head.j != gameState.board[0].length - 1) {
            return head.directionTo(new Cell(head.i, head.j - 1));
        }
        if (isSafeRight && head.j != 0 && head.j != gameState.board.length - 1) {
            return head.directionTo(new Cell(head.i, head.j + 1));
        }
        if (head.j == 0) {
            if(head.i == 0 || head.i == gameState.board.length - 1){
                return getValidDirection();
            }
            if (head.i > food.i) {
                if (!isValid(new Cell(head.i - 1, head.j))){
                    return null;
                }
                return head.directionTo(new Cell(head.i - 1, head.j));
            }
            if (head.i < food.i) {
                if (!isValid(new Cell(head.i + 1, head.j))){
                    return null;
                }
                return head.directionTo(new Cell(head.i + 1, head.j));
            }
            if (!isSafeRight && gameState.getSize() == 31){
                return null;
            }
            if (head.i == food.i && gameState.getSize() < 35){
                return getValidDirection();
            }
            return head.directionTo(new Cell(head.i, head.j + 1));
        }
        if (head.j == gameState.board[0].length - 1){
            if(head.i == 0 || head.i == gameState.board.length - 1){
                return getValidDirection();
            }
            if (head.i > food.i) {
                if (!isValid(new Cell(head.i - 1, head.j))){
                    return null;
                }
                return head.directionTo(new Cell(head.i - 1, head.j));
            }
            if (head.i < food.i) {
                if (!isValid(new Cell(head.i + 1, head.j))){
                    return null;
                }
                return head.directionTo(new Cell(head.i + 1, head.j));
            }
            if (!isSafeLeft && gameState.getSize() == 31){
                return null;
            }
            if (head.i == food.i  && gameState.getSize() < 35){
                return getValidDirection();
            }
            return head.directionTo(new Cell(head.i, head.j - 1));
        }


        return null;
    }

    public boolean isValid(Cell cell){
        return (gameState.isOnBoard(cell) && gameState.getValueAt(cell) != SnakeGame.SNAKE);
    }

    public Direction getValidDirection(){
        Cell head = gameState.snake.peekFirst();
        for (Cell c : head.neighbors()){
            if (isValid(c)) {
                return head.directionTo(c);
            }
        }
        return null;
    }

    public int countSnakeDistance(Direction direction){
        int distanceToSnake = 0;
        Cell head = gameState.snake.peekFirst();
        if (direction == new Direction(0,1)) {
            for (int j = head.j + 1; gameState.getValueAt(new Cell(head.i,j)) != SnakeGame.SNAKE &&
                    gameState.isOnBoard(new Cell(head.i,j)); j++){
                distanceToSnake++;
            }
        }
        if (direction == new Direction(0,-1)){
            for (int j = head.j - 1; gameState.getValueAt(new Cell(head.i,j)) != SnakeGame.SNAKE &&
                    gameState.isOnBoard(new Cell(head.i,j)); j--){
                distanceToSnake++;
            }
        }

        if (direction == new Direction(1,0)){
            for (int i = head.i + 1; gameState.getValueAt(new Cell(i,head.j)) != SnakeGame.SNAKE &&
                    gameState.isOnBoard(new Cell(i,head.j)); i++){
                distanceToSnake++;
            }
        }
        if (direction == new Direction(-1,0)){
            for (int i = head.i - 1 ; gameState.getValueAt(new Cell(i,head.j)) != SnakeGame.SNAKE &&
                    gameState.isOnBoard(new Cell(i,head.j)); i--){
                distanceToSnake++;
            }
        }
        return distanceToSnake;
    }

}
