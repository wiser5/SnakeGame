import java.util.Scanner;

/**
 * class to run the Snake Game
 */
public class SnakeGame {

    public boolean win;
    public boolean loss;
    private static final Scanner in = new Scanner(System.in);
    private Direction currentDirection;
    private final static int sideLength = 15;

    /**
     * constructor for SnakeGame
     * calls method to start game and calls method to restart if necessary
     */
    public SnakeGame() {
        win = false;
        loss = false;

        if(!Score.scoresExist())    Score.initializeScores();

        System.out.println("Snake Game");
        System.out.println(Score.strHighScores());

        int score = playGame();

        if(win) System.out.println("Winner!!!");
        System.out.println("Final Score (Snake Length): " + score);

        if(Score.canAddHighScore(score)) {
            Score.addScore(new Score(getName(), score));
            Score.writeScores();
        }

        playAgain();
    }

    /**
     * plays the game
     * @return snake length (score)
     */
    public int playGame() {
        SnakeMatrix g = new SnakeMatrix(this, sideLength);
        currentDirection = new Direction(Direction.right);

        boolean done = false;
        while(!done) { //loops until player wins, loses, or quits
            System.out.println("\n" + g);
            String dirDisplay = "Directions\nUp    (1)\nRight (2)\nDown  (3)\nLeft  (4)\nQuit  (5)\nEnter a Direction: ";
            int userInput = getNumInput(dirDisplay, 1, 5);

            if(userInput != 5)  currentDirection.setDirection(getDirection(userInput)); //change direction
            else    done = true; //player quits

            if(!done)   g.move(currentDirection); //moves the snake

            if(win || loss) done = true; //changes done if the player wins or loses
        }

        return g.getSnakeLength();
    }

    /**
     * gets user input for direction
     * @return
     */
    private int getNumInput(String display, int min, int max) {
        System.out.print(display);
        String input = in.nextLine();
        try {
            int numInput = Integer.parseInt(input);
            if(numInput < min || numInput > max)    return getNumInput(display, min, max);
            return numInput;
        }
        catch(NumberFormatException e) {
            return getNumInput(display, min, max);
        }
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

    /**
     * plays the Snake Game again if requested
     */
    private void playAgain() {
        String display = "Play Again (1) or Quit (2): ";
        int input = getNumInput(display, 1, 2);
        System.out.println();
        if(input == 1)  new SnakeGame();
    }

    /**
     * gets a name from the user for a high score
     * @return name
     */
    private String getName() {
        System.out.print("Enter A Name for High Score: ");
        return in.nextLine();
    }

}
