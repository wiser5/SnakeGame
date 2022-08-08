import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * class to run the Snake Game
 */
public class SnakeGame {

    public static final int game = 0;
    public static final int pg = 1;
    public static final int hs = 2;
    public static final int quit = 3;
    private JFrame frame;
    private static final Scanner in = new Scanner(System.in); //scanner used when game was text based
    private final static int sideLength = 50;
    private SnakeKeybinds keys = new SnakeKeybinds(this);
    private SnakeMatrix matrix;

    /**
     * constructor for SnakeGame
     * calls method to start game and calls method to restart if necessary
     */
    public SnakeGame() {
        Score.initializeScores();
        matrix = new SnakeMatrix(this, keys, sideLength);
        transition(pg);
    }

    /**
     * transitions into a new frame
     * @param val (frame to transition to)
     */
    public void transition(int val) {
        if(val == pg)   initPG();
        else if(val == hs)  initScores();
        else if(val == quit)    frame.dispose();
    }

    /**
     * returns a transition button that moves to a specified destination
     * @param destination (where button will transition to)
     * @return
     */
    private JButton getTransitionButton(int destination) {
        JButton button = new JButton();
        if(destination == hs)   button.setText("(Space) High Scores");
        else if(destination == quit)    button.setText("(Esc) Quit, Restart to Play Again");
        button.addActionListener(new SnakeButtonListener(this, destination));
        return button;
    }

    /**
     * disposes an old frame (if there is an old frame) and creates a new frame
     */
    private void resetFrame() {
        if(frame != null)   frame.dispose();
        frame = new JFrame();
        frame.setSize(new Dimension(600, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * creates the high score screen
     */
    private void initScores() {
        JPanel p = new JPanel();
        List<Score> currScores = Score.getHighScores();
        if(currScores.isEmpty()) { //no scores
            p.setLayout(new GridLayout(2, 1));
            JLabel label = new JLabel("No High Scores Currently");
            label.setFont(new Font(Font.SERIF, Font.BOLD, 15));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            p.add(label);
        }
        else {
            p.setLayout(new GridLayout(currScores.size() + 2, 1));
            JPanel top = new JPanel();
            top.setLayout(new GridLayout(1,3));
            //creates the header row for the scores table
            JLabel rank = new JLabel("Rank");
            rank.setHorizontalAlignment(SwingConstants.CENTER);
            rank.setFont(new Font(Font.SERIF, Font.BOLD, 15));
            top.add(rank);
            JLabel tname = new JLabel("Name");
            tname.setHorizontalAlignment(SwingConstants.CENTER);
            tname.setFont(new Font(Font.SERIF, Font.BOLD, 15));
            top.add(tname);
            JLabel ts = new JLabel("Score");
            ts.setHorizontalAlignment(SwingConstants.CENTER);
            ts.setFont(new Font(Font.SERIF, Font.BOLD, 15));
            top.add(ts);
            p.add(top);
            for(int i = 0; i < currScores.size(); i++) { //adds a score to the display
                JPanel inner = new JPanel();
                inner.setLayout(new GridLayout(1, 3));
                JLabel num = new JLabel(String.valueOf(i + 1));
                num.setHorizontalAlignment(SwingConstants.CENTER);
                num.setFont(new Font(Font.SERIF, Font.BOLD, 15));
                inner.add(num);
                JLabel name = new JLabel(currScores.get(i).getName());
                name.setHorizontalAlignment(SwingConstants.CENTER);
                name.setFont(new Font(Font.SERIF, Font.BOLD, 15));
                inner.add(name);
                JLabel s = new JLabel(String.valueOf(currScores.get(i).getScore()));
                s.setHorizontalAlignment(SwingConstants.CENTER);
                s.setFont(new Font(Font.SERIF, Font.BOLD, 15));
                inner.add(s);
                p.add(inner);
            }
        }
        p.add(getTransitionButton(quit));
        keys.addHSKeys(p);
        resetFrame();
        frame.setTitle("Snake Game: High Scores");
        frame.add(p);
        frame.setVisible(true);
    }

    /**
     * panel for the after game (displaying score and returning to menu)
     */
    private void initPG() {
        JPanel p = new JPanel();
        int rows = 4;
        if(matrix.win) rows++;
        if(Score.canAddHighScore(matrix.getSnakeLength()))    rows += 2;
        p.setLayout(new GridLayout(rows, 1));
        if(matrix.win) {
            JLabel winner = new JLabel("Winner!!!");
            winner.setHorizontalAlignment(SwingConstants.CENTER);
            p.add(winner);
        }
        JLabel finalScore = new JLabel("Final Score (Snake Length): " + matrix.getSnakeLength());
        finalScore.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(finalScore);

        if(Score.canAddHighScore(matrix.getSnakeLength()))    p.add(initHS());

        p.add(getTransitionButton(hs));
        p.add(getTransitionButton(quit));

        keys.addPGKeys(p);
        resetFrame();
        frame.setTitle("Snake Game: Post Game");
        frame.add(p);
        frame.setVisible(true);
    }

    /**
     * panel to add high score
     * @return high score panel
     */
    private JPanel initHS() {
        JPanel outerP = new JPanel();
        outerP.setLayout(new GridLayout(2, 1));
        outerP.setSize(new Dimension(400, 200));
        JPanel innerP = new JPanel();
        innerP.setLayout(new GridLayout(1, 2));
        innerP.setSize(new Dimension(400, 100));
        JLabel label = new JLabel("\tEnter Name: ");
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setFont(new Font(Font.SERIF, Font.PLAIN, 15));
        innerP.add(label);
        JTextField name = new JTextField();
        innerP.add(name);
        outerP.add(innerP);
        JButton button = new JButton("Save Name");
        button.addActionListener(new SnakeButtonListener(this, name, outerP, matrix.getSnakeLength()));
        outerP.add(button);
        return outerP;
    }

    /**
     * sets up the frame after a name is entered of a high score
     */
    public void removeHSPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(4, 1));
        JLabel finalScore = new JLabel("Final Score (Snake Length): " + matrix.getSnakeLength());
        finalScore.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(finalScore);
        JLabel label = new JLabel("Score Added");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(label);
        p.add(getTransitionButton(hs));
        p.add(getTransitionButton(quit));

        keys.addPGKeys(p);
        resetFrame();
        frame.setTitle("Snake Game: Post Game");
        frame.add(p);
        frame.setVisible(true);
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
