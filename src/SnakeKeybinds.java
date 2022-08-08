import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

/**
 * class that contains the key binds for the snake game
 */
public class SnakeKeybinds {

    private SnakeGame g;
    private SnakeMatrix m;

    /**
     * constructor
     * @param game
     */
    SnakeKeybinds(SnakeGame game) {
        g = game;
    }

    /**
     * adds the key binds for the game
     */
    public void addGameKeyBinds(SnakeMatrix matrix, JPanel panel) {
        m = matrix;
        //input map and action map
        InputMap inMap = panel.getInputMap();
        ActionMap actMap = panel.getActionMap();

        //directions to action map
        actMap.put("Up", new DirectionAction(Direction.up));
        actMap.put("Right", new DirectionAction(Direction.right));
        actMap.put("Down", new DirectionAction(Direction.down));
        actMap.put("Left", new DirectionAction(Direction.left));

        //directions to input map
        List<KeyStroke> arrowKeys = new ArrayList<>();
        arrowKeys.add(KeyStroke.getKeyStroke("UP"));
        arrowKeys.add(KeyStroke.getKeyStroke("RIGHT"));
        arrowKeys.add(KeyStroke.getKeyStroke("DOWN"));
        arrowKeys.add(KeyStroke.getKeyStroke("LEFT"));
        addDirectionInput(inMap, arrowKeys);

        List<KeyStroke> wasd = new ArrayList<>();
        wasd.add(KeyStroke.getKeyStroke('w'));
        wasd.add(KeyStroke.getKeyStroke('d'));
        wasd.add(KeyStroke.getKeyStroke('s'));
        wasd.add(KeyStroke.getKeyStroke('a'));
        addDirectionInput(inMap, wasd);

        List<KeyStroke> upperWasd = new ArrayList<>();
        upperWasd.add(KeyStroke.getKeyStroke('W'));
        upperWasd.add(KeyStroke.getKeyStroke('D'));
        upperWasd.add(KeyStroke.getKeyStroke('S'));
        upperWasd.add(KeyStroke.getKeyStroke('A'));
        addDirectionInput(inMap, upperWasd);

        //space
        inMap.put(KeyStroke.getKeyStroke(' '), "Space");
        actMap.put("Space", new SpaceAction(SnakeGame.game));

        //escape
        inMap.put(KeyStroke.getKeyStroke("ESCAPE"), "Escape");
        actMap.put("Escape", new EscapeAction(SnakeGame.game));
    }

    public void addPGKeys(JPanel p) {
        p.getInputMap().put(KeyStroke.getKeyStroke(' '), "Space");
        p.getActionMap().put("Space", new SpaceAction(SnakeGame.pg));
        onlyEscape(p, SnakeGame.pg);
    }

    public void addHSKeys(JPanel p) {
        onlyEscape(p, SnakeGame.hs);
    }

    /**
     * adds a list of keystrokes to the direction keys of a given input map
     * @param map inputMap of the component the directions are to be mapped to
     * @param keystrokes size of 4: up, right, down, left
     */
    private void addDirectionInput(InputMap map, List<KeyStroke> keystrokes) {
        if(keystrokes.size() != 4) {
            System.out.println("KeyStroke Mapping Error with " + keystrokes + "!!!");
            return;
        }

        map.put(keystrokes.get(0), "Up");
        map.put(keystrokes.get(1), "Right");
        map.put(keystrokes.get(2), "Down");
        map.put(keystrokes.get(3), "Left");
    }

    /**
     * when only the escape button needs to be mapped to a component
     */
    private void onlyEscape(JPanel p, int status) {
        p.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "Escape");
        p.getActionMap().put("Escape", new EscapeAction(status));
    }

    /**
     * action for a direction keybind
     */
    public class DirectionAction extends AbstractAction {

        private int actionDir; //int value for the key's direction

        /**
         * constructor
         * @param direction (int for a direction)
         */
        DirectionAction(int direction) {
            actionDir = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            m.changeDirection(actionDir);
        }

    }

    /**
     * inner class for the space action
     */
    public class SpaceAction extends AbstractAction {

        private int s;

        public SpaceAction(int status) {
            s = status;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(s == SnakeGame.game) m.pause = !m.pause; //resumes or pauses the game
            else if(s == SnakeGame.pg)  g.transition(SnakeGame.hs);
        }

    }

    /**
     * inner class for the escape action
     */
    public class EscapeAction extends AbstractAction {

        private int s;

        public EscapeAction(int status) {
            s = status;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(s == SnakeGame.game) m.loss = true; //ends game
            else g.transition(SnakeGame.quit); //transitions back to menu
        }

    }

}
