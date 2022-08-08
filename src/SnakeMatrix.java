import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * class that holds and modifies the matrix for the Snake Game
 */
public class SnakeMatrix {

    private JFrame frame = new JFrame("Snake Game: Game");
    private SnakeSpace[][] matrix; //matrix of SnakeSpaces
    private int sLength; //length of the sides of the matrix (given when initialized, the matrix is a square)
    private static final int initialSnakeLength = 3; //length of the snake at the start of the game; should be less than sLength and greater than or equal to 3
    private Location snakeHead; //location of the head of the snake
    private List<Location> snakeBody; //location of the parts of the body of the snake (head and tail not included)
    private Location snakeTail; //location of the tail of the snake
    private Location apple; //location of the apple currently on the map
    private Random rand; //random class used in generation of the snake (initially) and apples
    private SnakeGame currentGame; //holds the instance of the current snake game
    private JPanel matrixP;
    private JPanel[][] matrixPanel; //matrix of JPanels to be displayed
    public boolean win;
    public boolean loss;
    public boolean pause;
    private Direction currentDirection;

    /**
     * constructor for snakeMatrix based on a given sideLength
     * matrix is a square
     * uses initializeMatrix() to create open space and then initialize snake and apple
     * @param game
     * @param sideLength
     */
    public SnakeMatrix(SnakeGame game, SnakeKeybinds k, int sideLength) {
        currentGame = game;
        sLength = sideLength;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 1000));
        frame.setLayout(new GridLayout(2, 1));
        frame.setResizable(false);
        createOrUpdateMatrixPanel();
        k.addGameKeyBinds(this, matrixP);
        frame.add(matrixP);
        frame.add(initGB());
    }

    /**
     * resets the game and then starts it
     */
    public void playGame() {
        initializeMatrix();
        createOrUpdateMatrixPanel();
        frame.setVisible(true);
        win = false;
        loss = false;
        pause = false;
        currentDirection = new Direction(Direction.none);
        while(!win && !loss) { //loops until player wins, loses, or quits
            try { //adds pause between movements
                Thread.sleep(100);
            }
            catch(InterruptedException e) {
                System.out.println(e.getMessage());
            }
            if(!pause)   move(currentDirection); //moves the snake
        }
        frame.setVisible(false);
        currentGame.transition(SnakeGame.postGame); //moves the game to postgame
    }

    /**
     * changes the direction of the snake
     * does not change direction if the game is paused, going opposite direction of the head, or on the snake as the first move
     * @param newDirection
     */
    public void changeDirection(int newDirection) {
        if(pause || currentDirection.isOpposite(newDirection))   return;
        if(currentDirection.equals(new Direction(Direction.none)) && snakeBody.contains((new Direction(newDirection)).getNewLocation(snakeHead)))  return;
        currentDirection.setDirection(newDirection);
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
        //todo: optimize to pick from list of remaining spaces
        isWon();
        if(win)    return;
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
        if(dir.equals(new Direction(Direction.none)))   return;

        Location newLoc = dir.getNewLocation(snakeHead);

        if(isLost(newLoc)) { //lost
            loss = true;
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

        createOrUpdateMatrixPanel();

    }

    /**
     * changes a spaces based on a given location and a new value
     * @param loc: space in matrix to change
     * @param newVal: value to change location to
     */
    private void changeSpace(Location loc, Color newVal) {
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
        return loc.c < 0 || loc.c >= sLength || loc.r < 0 || loc.r >= sLength || snakeBody.contains(loc);
    }

    /**
     * changes the value for currentGame.win if the board is full
     */
    public void isWon() {
        if(getSnakeLength() == sLength * sLength) { //board full, won
            win = true;
        }
    }

    /**
     * returns the size of the snake
     * @return
     */
    public int getSnakeLength() {
        return snakeBody.size() + 2;
    }

    /**
     * creates or updates matrixPanel to correct colors
     */
    private void createOrUpdateMatrixPanel() {
        if(matrixP == null) {
            matrixP = new JPanel();
            matrixP.setSize(new Dimension(600, 600));
            matrixP.setLayout(new GridLayout(sLength, sLength));
            matrixPanel = new JPanel[sLength][sLength];
            for(int r = 0; r < sLength; r++) {
                for(int c = 0; c < sLength; c++) {
                    JPanel newPanel = new JPanel();
                    //newPanel.setBorder(new LineBorder(Color.BLACK));
                    matrixPanel[r][c] = newPanel;
                    matrixP.add(newPanel);
                }
            }
        }
        else {
            //TODO: optimize to only update needed panels
            for(int r = 0; r < sLength; r++) {
                for(int c = 0; c < sLength; c++) {
                    matrixPanel[r][c].setBackground(matrix[r][c].getVal());
                }
            }
        }
    }

    /**
     * sets up the bottom panel during the game
     * @return the bottom panel
     */
    private JPanel initGB() {
        //buttonPanel, label describing moves, and label describing colors
        JPanel bottomGamePanel = new JPanel();
        bottomGamePanel.setSize(new Dimension(600, 400));
        bottomGamePanel.setLayout(new GridLayout(3, 1));

        bottomGamePanel.add(initExp());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setSize(new Dimension(600, 100));
        buttonPanel.setLayout(new BorderLayout());
        addToDirectionPanel(buttonPanel);
        bottomGamePanel.add(buttonPanel);

        bottomGamePanel.add(initC());

        return bottomGamePanel;
    }

    /**
     * @return panel explaining what the space and other buttons do
     */
    private JPanel initExp() {
        JPanel exp = new JPanel();
        exp.setSize(new Dimension(600, 100));
        exp.setLayout(new GridLayout(3, 1));

        JLabel start = new JLabel("Enter a Direction to Start");
        start.setFont(new Font(Font.SERIF, Font.BOLD, 15));
        start.setHorizontalAlignment(SwingConstants.CENTER);
        exp.add(start);

        JLabel pause = new JLabel("Press Space to Pause or Resume Game");
        pause.setFont(new Font(Font.SERIF, Font.BOLD, 15));
        pause.setHorizontalAlignment(SwingConstants.CENTER);
        exp.add(pause);

        JLabel esc = new JLabel("Press Escape to Quit Game");
        esc.setFont(new Font(Font.SERIF, Font.BOLD, 15));
        esc.setHorizontalAlignment(SwingConstants.CENTER);
        exp.add(esc);

        return exp;
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
    private JPanel initC() {
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
