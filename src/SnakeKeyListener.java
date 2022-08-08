import java.awt.event.*;
import java.util.Map;
import java.util.HashMap;

/**
 * deals with input from the keyboard in snake game
 * not used because keybinds do not require focus and can work with buttons on the screen
 */
public class SnakeKeyListener implements KeyListener, ActionListener {

    //TODO: use key bindings instead

    //up, left, down, right
    private final static int[] arrows = {KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT};
    private final static int[] wasd = {87, 65, 83, 68};
    private static Map<Integer, Integer> directionMap;
    private SnakeGame g;

    /**
     * modifies currentDirection of a game
     * @param game
     */
    SnakeKeyListener(SnakeGame game) {
        g = game;
        initializeMap();
    }

    /**
     * creates the directionMap which maps a key value to a direction
     */
    private static void initializeMap() {
        directionMap = new HashMap<>();

        for(int i = 0; i < 4; i++) {
            int currDirVal = -1;
            if(i == 0)  currDirVal = Direction.up;
            else if(i == 1) currDirVal = Direction.left;
            else if(i == 2) currDirVal = Direction.down;
            else currDirVal = Direction.right;
            directionMap.put(arrows[i], currDirVal);
            directionMap.put(wasd[i], currDirVal);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    /**
     * changes direction when appropriate key is pressed
     */
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        /*
        if(directionMap.containsKey(keyCode) && !g.pause)   g.changeDirection(directionMap.get(keyCode)); //changes direction

        if(keyCode == KeyEvent.VK_SPACE)    g.pause = !g.pause; //switches pause

        if(keyCode == KeyEvent.VK_ESCAPE)   g.loss = true; //ends game if escape is entered

         */
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
