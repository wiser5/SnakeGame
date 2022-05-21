import java.awt.event.*;
import java.util.Map;
import java.util.HashMap;

/**
 * deals with input from a button in the SnakeGame
 */
public class SnakeButtonListener implements ActionListener {

    private SnakeGame g;
    private Map<String, Integer> directionMap;
    private int directionVal;

    /**
     * constructs the button listener based on the type of button
     * @param game
     * @param direction
     */
    public SnakeButtonListener(SnakeGame game, String direction) {
        g = game;
        initializeDirectionMap();
        directionVal = directionMap.get(direction);
    }

    /**
     * creates direction map based on provided string value and direction value
     */
    private void initializeDirectionMap() {
        directionMap = new HashMap<>();
        directionMap.put("Up", Direction.up);
        directionMap.put("Down", Direction.down);
        directionMap.put("Left", Direction.left);
        directionMap.put("Right", Direction.right);
    }

    @Override
    /**
     * changes the direction in the game if the button is clicked
     */
    public void actionPerformed(ActionEvent e) {
        if(!g.pause)    g.currentDirection.setDirection(directionVal);
    }

}
