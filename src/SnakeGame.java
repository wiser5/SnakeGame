import java.util.Scanner;

/**
 * class to run the Snake Game
 */
public class SnakeGame {

    public boolean win;
    public boolean loss;
    private static final Scanner in = new Scanner(System.in);
    private Direction currentDirection;

    public SnakeGame() { //constructor
        win = false;
        loss = false;
        int score = playGame();
    }

    /**
     * plays the game
     * @return snake length (score)
     */
    public int playGame() {
        System.out.println("Snake Game");
        SnakeMatrix g = new SnakeMatrix(50);

        boolean done = false;
        if(!done) {
            int userInput = getInput();

            if(userInput != 5)  currentDirection = new Direction(getDirection(userInput));
            else    done = true;

            if(!done)   g.move(this, currentDirection);

            if(win || loss) done = true;
        }

        return g.getSnakeLength();
    }

    /**
     * gets user input for direction
     * @return
     */
    private int getInput() {
        System.out.println("Directions: Up (1), Right (2), Down (3), Left (4), Quit (5)");
        System.out.print("Enter a Direction: ");
        int input = in.nextInt();
        while(input < 1 || input > 5) {
            System.out.println("Directions: Up (1), Right (2), Down (3), Left (4), Quit (5)");
            System.out.print("Enter a Direction: ");
            input = in.nextInt();
        }
        return input;
    }

    /**
     * gets a direction based on user input
     * @param userInput
     * @return direction
     */
    private int getDirection(int userInput) {
        if(userInput == 1)  return Direction.up;
        if(userInput == 2)  return Direction.right;
        if(userInput == 3)  return Direction.down;
        if(userInput == 4)  return Direction.left;
        else return -1; //error
    }

}
