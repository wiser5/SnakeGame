import java.util.Scanner;
import javax.swing.*;
import java.awt.*;

/**
 * class to run the Snake Game
 */
public class SnakeGame {

    public boolean win;
    public boolean loss;
    public boolean pause;
    private static final Scanner in = new Scanner(System.in);
    public Direction currentDirection;
    private final static int sideLength = 50;
    private SnakeMatrix matrix;
    private JFrame gameFrame; //JFrame for the game
    private JPanel gamePanel; //JPanel for the matrix

    /**
     * constructor for SnakeGame
     * calls method to start game and calls method to restart if necessary
     */
    public SnakeGame() {
        win = false;
        loss = false;

        if(!Score.scoresExist())    Score.initializeScores();

        //TODO: this will be part of home on SnakeMain
        System.out.println("Snake Game");
        System.out.println(Score.strHighScores());

        //TODO: game JFrame (sizes on components)   add button to end game and return home
        initializeGameJFrame();
        int score = playGame(); //TODO: change snake display while game is working; change game code for continuous movement

        //TODO: use dispose() to end a frame    figure out what to do if game is closed early   make it illegal to move in some directions

        /*
        //TODO: postgame screen (Scores.java)
        if(win) System.out.println("Winner!!!");
        System.out.println("Final Score (Snake Length): " + score);

        //TODO: scoreboard screen (Scores.java)
        if(Score.canAddHighScore(score)) {
            Score.addScore(new Score(getName(), score));
            Score.writeScores();
        }

        playAgain();

         */
    }

    /**
     * plays the game
     * @return snake length (score)
     */
    public int playGame() {

        while(!win && !loss) { //loops until player wins, loses, or quits
            try { //adds pause between movements to make game playable
                Thread.sleep(100);
            }
            catch(InterruptedException e) {
                System.out.println(e.getMessage());
            }
            if(!pause)   matrix.move(currentDirection); //moves the snake
        }

        return matrix.getSnakeLength();
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

    /**
     * initialize the game JFrame with the matrix
     */
    private void initializeGameJFrame() {
        gameFrame = new JFrame();
        gameFrame.setTitle("Snake Game");
        gameFrame.setSize(new Dimension(600, 1000));
        gameFrame.setLayout(new GridLayout(2, 1));
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentDirection = new Direction(Direction.none);
        gameFrame.addKeyListener(new SnakeKeyListener(this));
        gameFrame.setFocusable(true);

        //gamePanel
        initializeGamePanel();

        gameFrame.add(gamePanel);
        gameFrame.add(initializeBottomPanel());
        gameFrame.setVisible(true);
    }

    private void initializeGamePanel() {
        gamePanel = new JPanel();
        gamePanel.setSize(new Dimension(600, 600));
        gamePanel.setLayout(new GridLayout(sideLength, sideLength));
        matrix = new SnakeMatrix(this, sideLength);
        matrix.createOrUpdateMatrixPanel();
        for(int r = 0; r < sideLength; r++) {
            for(int c = 0; c < sideLength; c++) {
                gamePanel.add(matrix.matrixPanels[r][c]);
            }
        }
    }

    private JPanel initializeBottomPanel() {
        //buttonPanel, label describing moves, and label describing colors
        JPanel bottomGamePanel = new JPanel();
        bottomGamePanel.setSize(new Dimension(600, 300));
        bottomGamePanel.setLayout(new GridLayout(3, 1));

        bottomGamePanel.add(getSpacePanel());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setSize(new Dimension(600, 100));
        buttonPanel.setLayout(new BorderLayout());
        addToDirectionPanel(buttonPanel);
        bottomGamePanel.add(buttonPanel);

        bottomGamePanel.add(getColorPanel());

        return bottomGamePanel;
    }

    /**
     * @return label explaining what the space does
     */
    private JPanel getSpacePanel() {
        JPanel spacePanel = new JPanel();
        spacePanel.setSize(new Dimension(600, 100));
        spacePanel.setLayout(new GridLayout(3, 1));

        JLabel start = new JLabel("Enter a Direction to Start");
        start.setFont(new Font(Font.SERIF, Font.BOLD, 15));
        start.setHorizontalAlignment(SwingConstants.CENTER);
        spacePanel.add(start);

        JLabel pause = new JLabel("Press Space to Pause or Resume Game");
        pause.setFont(new Font(Font.SERIF, Font.BOLD, 15));
        pause.setHorizontalAlignment(SwingConstants.CENTER);
        spacePanel.add(pause);

        JLabel esc = new JLabel("Press Escape to Quit Game");
        esc.setFont(new Font(Font.SERIF, Font.BOLD, 15));
        esc.setHorizontalAlignment(SwingConstants.CENTER);
        spacePanel.add(esc);

        return spacePanel;
    }

    /**
     * adds items to the button panel
     * @param panelToAddTo
     */
    private void addToDirectionPanel(JPanel panelToAddTo) {
        for(int i = 0; i < 4; i++) {
            String dir = "Up";
            if(i == 1)  dir = "Down";
            else if(i == 2) dir = "Left";
            else if(i == 3) dir = "Right";

            JButton button = new JButton(dir);
            button.addActionListener(new SnakeButtonListener(this, dir));

            String layoutPosition = BorderLayout.NORTH;
            if(i == 1)  layoutPosition = BorderLayout.SOUTH;
            else if(i == 2) layoutPosition = BorderLayout.WEST;
            else if(i == 3) layoutPosition = BorderLayout.EAST;

            panelToAddTo.add(button, layoutPosition);
        }
        JLabel centerLabel = new JLabel("Use Buttons, Arrow Keys, or WASD");
        centerLabel.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        centerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panelToAddTo.add(centerLabel, BorderLayout.CENTER);
    }

    /**
     * @return the panel listing what each color means
     */
    private JPanel getColorPanel() {
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(1, 5));
        colorPanel.setSize(new Dimension(600, 100));
        for(int i = 0; i < 5; i++) {
            JLabel currentLabel = new JLabel();
            currentLabel.setFont(new Font(Font.SERIF, Font.BOLD, 15));
            currentLabel.setHorizontalAlignment(SwingConstants.CENTER);
            if(i == 0) {
                currentLabel.setText("Open Space");
                currentLabel.setForeground(SnakeSpace.openSpace);
            }
            else if(i == 1) {
                currentLabel.setText("Snake Head");
                currentLabel.setForeground(SnakeSpace.snakeHead);
            }
            else if(i == 2) {
                currentLabel.setText("Snake Body");
                currentLabel.setForeground(SnakeSpace.snakeBody);
            }
            else if(i == 3) {
                currentLabel.setText("Snake Tail");
                currentLabel.setForeground(SnakeSpace.snakeTail);
            }
            else {
                currentLabel.setText("Apple");
                currentLabel.setForeground(SnakeSpace.apple);
            }
            colorPanel.add(currentLabel);
        }
        return colorPanel;
    }

}
