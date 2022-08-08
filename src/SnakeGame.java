import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * class to run the Snake Game
 */
public class SnakeGame {

    //int values to use as placeholders for stages in the game
    public static final int menu = 0;
    public static final int rules = 1;
    public static final int scores = 2;
    public static final int game = 3;
    public static final int postGame = 4;
    public static final int quit = 5;
    private JPanel[] panels = new JPanel[5]; //array for the panels used in the game
    private static final Scanner in = new Scanner(System.in); //scanner used when game was text based
    private final static int sideLength = 50;
    private SnakeMatrix matrix;
    private SnakeKeybinds keys = new SnakeKeybinds(this, panels);; //keybind class for the game
    private JFrame currentFrame; //the current frame being used in the game

    /**
     * constructor for SnakeGame
     * calls method to start game and calls method to restart if necessary
     */
    public SnakeGame() {
        Score.initializeScores();
        transition(menu);
    }

    /**
     * transitions to a new screen
     * @param newP
     */
    public void transition(int newP) {
        if(currentFrame != null)    currentFrame.dispose(); //removes old frame
        if(newP == game) { //creates game if not already created and runs the game
            if(matrix == null)  matrix = new SnakeMatrix(this, keys, sideLength);
            matrix.playGame();
            return;
        }
        else if(newP == quit)    return; //quit
        String title = "Snake Game: ";
        if(newP == menu)    title += "Menu";
        else if(newP == rules)  title += "Rules";
        else if(newP == postGame)   title += "Post Game";
        currentFrame = new JFrame(title);
        //initialize panel if it needs to be initialized
        boolean addKB = true;
        if(newP == scores) initScores();
        else if(newP == postGame) initPG();
        else if(panels[newP] == null) {
            if (newP == menu) initMenu();
            else if (newP == rules) initRules();
        }
        else    addKB = false;
        keys.addKeyBinds(addKB, newP); //keybinds
        currentFrame.setSize(new Dimension(600, 600));
        currentFrame.add(panels[newP]);
        currentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentFrame.setVisible(true);
    }

    /**
     * creates the menu frame
     */
    private void initMenu() {
        panels[menu] = new JPanel();
        panels[menu].setLayout(new GridLayout(5, 1));
        JLabel label = new JLabel("Snake Game");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panels[menu].add(label);
        panels[menu].add(getTransitionButton("(Space) Play", game));
        panels[menu].add(getTransitionButton("(1) Rules", rules));
        panels[menu].add(getTransitionButton("(2) High Scores", scores));
        panels[menu].add(getTransitionButton("(Esc) Quit", quit));
    }

    /**
     * display for the rules
     */
    private void initRules() {
        panels[rules] = new JPanel();
        List<String> r = new ArrayList<>();
        r.add("Enter a Direction to Start");
        r.add("Directions Can Be Entered Via Buttons or Keyboard");
        r.add("Controls Are Shown in Game");
        r.add("The Snake Moves Around Based on the Specified Direction");
        r.add("Eat Apples to Expand in Size");
        r.add("Score is Based on Size");
        r.add("If a New High Score is Reached, the User Will Be Prompted for a Name to Record");

        panels[rules].setLayout(new GridLayout(r.size() + 1, 1));
        for (String rule : r) {
            JLabel label = new JLabel(rule);
            label.setFont(new Font(Font.SERIF, Font.PLAIN, 15));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            panels[rules].add(label);
        }
        panels[rules].add(getTransitionButton("(Esc) Return to Menu", menu));
    }

    /**
     * creates the high score screen
     */
    private void initScores() {
        panels[scores] = new JPanel();
        List<Score> currScores = Score.getHighScores();
        if(currScores.isEmpty()) {
            panels[scores].setLayout(new GridLayout(2, 1));
            JLabel label = new JLabel("No High Scores Currently");
            label.setFont(new Font(Font.SERIF, Font.BOLD, 15));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            panels[scores].add(label);
        }
        else {
            panels[scores].setLayout(new GridLayout(currScores.size() + 2, 1));
            JPanel top = new JPanel();
            top.setLayout(new GridLayout(1,3));
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
            panels[scores].add(top);
            for(int i = 0; i < currScores.size(); i++) {
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
                panels[scores].add(inner);
            }
        }
        panels[scores].add(getTransitionButton("(Esc) Return to Menu", menu));
    }

    /**
     * panel for the after game (displaying score and returning to menu)
     */
    private void initPG() {
        panels[postGame] = new JPanel();
        int rows = 2;
        if(matrix.win) rows++;
        if(Score.canAddHighScore(matrix.getSnakeLength()))    rows += 2;
        panels[postGame].setLayout(new GridLayout(rows, 1));
        if(matrix.win) {
            JLabel winner = new JLabel("Winner!!!");
            winner.setHorizontalAlignment(SwingConstants.CENTER);
            panels[postGame].add(winner);
        }
        JLabel finalScore = new JLabel("Final Score (Snake Length): " + matrix.getSnakeLength());
        finalScore.setHorizontalAlignment(SwingConstants.CENTER);
        panels[postGame].add(finalScore);

        if(rows > 3)    panels[postGame].add(initHS());

        panels[postGame].add(getTransitionButton("(Esc) Return to Menu", menu));
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
        panels[postGame] = new JPanel();
        panels[postGame].setLayout(new GridLayout(3, 1));
        JLabel finalScore = new JLabel("Final Score (Snake Length): " + matrix.getSnakeLength());
        finalScore.setHorizontalAlignment(SwingConstants.CENTER);
        panels[postGame].add(finalScore);
        JLabel label = new JLabel("Score Added");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panels[postGame].add(label);
        panels[postGame].add(getTransitionButton("(Esc) Return to Menu", menu));
        keys.addKeyBinds(true, postGame);

        currentFrame.dispose();
        currentFrame = new JFrame("Snake Game: Post Game");
        currentFrame.setSize(new Dimension(600, 600));
        currentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentFrame.add(panels[postGame]);
        currentFrame.setVisible(true);
    }

    /**
     * returns a transition button that moves to a specified destination
     * @param name (displayed name of the button)
     * @param destination (where button will transition to)
     * @return
     */
    private JButton getTransitionButton(String name, int destination) {
        JButton button = new JButton(name);
        button.addActionListener(new SnakeButtonListener(this, destination));
        return button;
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
