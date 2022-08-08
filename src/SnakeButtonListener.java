import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.HashMap;
import javax.swing.*;

/**
 * deals with input from a button in the SnakeGame
 */
public class SnakeButtonListener implements ActionListener {

    private SnakeGame g;
    private SnakeMatrix m;
    private static Map<String, Integer> directionMap;
    private int val;
    private JTextField field;
    private JPanel panel;
    private int type;

    /**
     * direction button constructor
     * @param matrix
     * @param direction
     */
    public SnakeButtonListener(SnakeMatrix matrix, String direction) {
        m = matrix;
        if(directionMap == null)    initializeDirectionMap();
        val = directionMap.get(direction);
        type = 1;
    }

    /**
     * transition button constructor
     * @param game
     * @param transitionDestination
     */
    public SnakeButtonListener(SnakeGame game, int transitionDestination) {
        g = game;
        val = transitionDestination;
        type = 2;
    }

    /**
     * new high score button constructor
     * @param game
     * @param text
     * @param p
     * @param score
     */
    public SnakeButtonListener(SnakeGame game, JTextField text, JPanel p, int score) {
        g = game;
        field = text;
        panel = p;
        val = score;
        type = 3;
    }

    /**
     * creates direction map based on provided string value and direction value
     */
    private static void initializeDirectionMap() {
        directionMap = new HashMap<>();
        directionMap.put("Up", Direction.up);
        directionMap.put("Down", Direction.down);
        directionMap.put("Left", Direction.left);
        directionMap.put("Right", Direction.right);
    }

    @Override
    /**
     * performs an action appropriate for the type of button
     */
    public void actionPerformed(ActionEvent e) {
        if(type == 1) m.changeDirection(val);
        else if(type == 2)  g.transition(val);
        else if(type == 3) {
            Score.addScore(new Score(field.getText(), val));
            g.removeHSPanel();
        }
    }

}
