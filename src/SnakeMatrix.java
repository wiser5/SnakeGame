import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * class that holds and modifies the matrix for the Snake Game
 */
public class SnakeMatrix {

    private SnakeSpace[][] matrix; //matrix of SnakeSpaces
    private int sLength; //length of the sides of the matrix (given when initialized, the matrix is a square)
    private static final int initialSnakeLength = 3; //length of the snake at the start of the game; should be less than sLength and greater than or equal to 3
    private Location snakeHead; //location of the head of the snake
    private List<Location> snakeBody; //location of the parts of the body of the snake (head and tail not included)
    private Location snakeTail; //location of the tail of the snake
    private Location apple; //location of the apple currently on the map
    private Random rand; //random class used in generation of the snake (initially) and apples
    private SnakeGame currentGame; //holds the instance of the current snake game

    /**
     * constructor for snakeMatrix based on a given sideLength
     * matrix is a square
     * uses initializeMatrix() to create open space and then initialize snake and apple
     * @param sideLength
     */
    public SnakeMatrix(SnakeGame game, int sideLength) {
        currentGame = game;
        sLength = sideLength;
        initializeMatrix();
    }

    /**
     * fills the board with open space
     * initializes the snake
     * adds the apple
     */
    private void initializeMatrix() {
        //open space
        matrix = new SnakeSpace[sLength][sLength];
        for(int r = 0; r < sLength; r++) {
            for(int c = 0; c < sLength; c++) {
                matrix[r][c] = new SnakeSpace(SnakeSpace.openSpace);
            }
        }

        rand = new Random();
        initializeSnake(); //snake
        addApple(); //apple
    }

    /**
     * initializes the snake
     * takes random location on the board and then uses values to the left or right for the rest of the snake
     * head is ensured to be at least two spaces from the edge of the matrix
     */
    private void initializeSnake() {
        snakeBody = new ArrayList<>();
        Location loc1 = new Location(rand.nextInt(sLength), rand.nextInt(sLength)); //initial location used to create the rest of the snake from
        snakeBody.add(loc1);
        snakeHead = loc1;
        snakeTail = loc1;

        boolean adjusted = false; //variable to see if values were changed
        //adds columns to the right if they exist in the matrix and if initialSnakeLength not reached
        for(int c = loc1.c + 1; c < sLength && snakeBody.size() < initialSnakeLength; c++) {
            adjusted = true;
            snakeBody.add(new Location(loc1.r, c));
        }
        if(adjusted)    snakeHead = snakeBody.get(snakeBody.size() - 1); //changes head location if values were changed

        adjusted = false; //resets adjusted
        //adds columns to the left if they exist in the matrix and if initialSnakeLength not reached
        for(int c = loc1.c - 1; c >= 0 && snakeBody.size() < initialSnakeLength; c--) {
            adjusted = true;
            snakeBody.add(new Location(loc1.r, c));
        }
        if(adjusted)    snakeTail = snakeBody.get(snakeBody.size() - 1); //changes tail location if values were changed

        if(snakeBody.size() != initialSnakeLength)  System.out.println("SNAKE LENGTH INITIALIZATION ERROR!!!");

        //adjusts locations if head too close to the edge of the matrix and if space is available
        if(snakeHead.c >= sLength - 2 && minCol(snakeBody) - 2 >= 0) {
            for(int i = 0; i < snakeBody.size(); i++) {
                Location mod = snakeBody.get(i);
                mod.c -= 2;
            }
        }

        //removes snake head and tail from the snakeBody list
        snakeBody.remove(snakeHead);
        snakeBody.remove(snakeTail);

        if(snakeBody.size() > 1)    sortInitialSnake();

        //changes value for snake tail and head in matrix
        changeSpace(snakeHead, SnakeSpace.snakeHead);
        changeSpace(snakeTail, SnakeSpace.snakeTail);

        //changes value for snake body parts
        for(Location body : snakeBody) {
            changeSpace(body, SnakeSpace.snakeBody);
        }

    }

    /**
     * sorts the initial snake body
     */
    private void sortInitialSnake() {
        List<Location> newLocs = new ArrayList<>(); //eventual snakeBody list

        Location currentLocation = new Location(snakeHead); //the location to add to the new locations
        Location left = new Direction(Direction.left).getNewLocation(currentLocation); //left position
        Location right = new Direction(Direction.right).getNewLocation(currentLocation); //right position

        Direction dirToAdd = new Direction(Direction.up); //direction that the points are located from the head
        if(snakeBody.contains(left))    dirToAdd.setDirection(Direction.left); //changes direction to left if appropriate
        if(snakeBody.contains(right))   dirToAdd.setDirection(Direction.right); //changes direction to right if appropriate
        if(dirToAdd.equals(new Direction(Direction.up))) System.out.println("INITIAL SNAKE BODY ERROR!!!");

        //adds nodes until size reached
        while(newLocs.size() < snakeBody.size()) {
            currentLocation = dirToAdd.getNewLocation(currentLocation);
            newLocs.add(currentLocation);
        }

        snakeBody = newLocs; //switches snakeBody lists
    }

    /**
     * adds a new apple to the map in an unoccupied location
     */
    private void addApple() {
        isWon();
        if(currentGame.win)    return;
        apple = new Location(rand.nextInt(sLength), rand.nextInt(sLength)); //chooses random space
        //choose random space until chosen space is unoccupied
        while(matrix[apple.r][apple.c].getVal() != SnakeSpace.openSpace) {
            apple = new Location(rand.nextInt(sLength), rand.nextInt(sLength));
        }
        changeSpace(apple, SnakeSpace.apple); //updates location
    }

    /**
     * returns the minimum column value from a list of locations
     * @param list (List<Location>)
     * @return minimum column value
     */
    private int minCol(List<Location> list) {
        int min = Integer.MAX_VALUE;
        for(Location loc : list) {
            if(loc.c < min) min = loc.c;
        }
        return min;
    }

    /**
     * @return String representation of the matrix
     */
    public String toString() {
        String result = "{";
        for(int r = 0; r < sLength; r++) {
            if(r != 0)  result += " ";
            result += " [";
            for(int c = 0; c < sLength; c++) {
                result += matrix[r][c];
                if(c != sLength - 1)    result += " ";
            }
            result += "]";
            if(r != sLength - 1)    result += "\n";
        }
        result += " }";
        return result;
    }

    /**
     * moves the snake if it does not result in a loss
     * @param dir (direction to move)
     */
    public void move(Direction dir) {
        Location newLoc = dir.getNewLocation(snakeHead);

        if(isLost(newLoc)) { //lost
            currentGame.loss = true;
            return;
        }

        if(apple.equals(newLoc)) { //eating
            snakeBody.add(0, new Location(snakeHead)); //adds previous head position to body
            changeSpace(snakeHead, SnakeSpace.snakeBody); //changes matrix for previous head position

            snakeHead = newLoc; //updates head location
            changeSpace(snakeHead, SnakeSpace.snakeHead); //updates head matrix location

            addApple(); //new apple
        }

        else { //not eating
            changeSpace(snakeTail, SnakeSpace.openSpace); //changes previous tail to open space in matrix
            snakeTail = snakeBody.get(snakeBody.size() - 1); //gets new tail position
            changeSpace(snakeTail, SnakeSpace.snakeTail); //changes matrix for new snake tail
            snakeBody.remove(snakeTail); //removes the new snake tail from the snake body list

            snakeBody.add(0, new Location(snakeHead)); //adds the new body location (previously a head) to the start of the list
            changeSpace(snakeHead, SnakeSpace.snakeBody); //changes old head in matrix
            snakeHead = newLoc; //updates head location
            changeSpace(snakeHead, SnakeSpace.snakeHead); //changes new head in matrix
        }
    }

    /**
     * changes a spaces based on a given location and a new value
     * @param loc: space in matrix to change
     * @param newVal: value to change location to
     */
    private void changeSpace(Location loc, char newVal) {
        matrix[loc.r][loc.c].setVal(newVal);
    }

    /**
     * returns if a game is lost based on a new location
     * a game is lost if the snake goes out of the matrix
     * or if the snake runs into itself (new position, movement of head, is location on snake body before body is moved)
     * @param loc
     * @return
     */
    private boolean isLost(Location loc) {
        if(loc.c < 0 || loc.c >= sLength)   return true;
        if(loc.r < 0 || loc.r >= sLength)   return true;
        if(snakeBody.contains(loc))    return true;
        return false;
    }

    /**
     * changes the value for currentGame.win if the board is full
     */
    public void isWon() {
        if(getSnakeLength() == sLength * sLength) { //board full, won
            currentGame.win = true;
        }
    }

    /**
     * returns the size of the snake
     * @return
     */
    public int getSnakeLength() {
        return snakeBody.size() + 2;
    }

}
