import java.awt.Color;

/**
 * class to store values held in the SnakeGame matrix
 */
public class SnakeSpace {

    private Color c;
    public static final Color snakeHead = Color.BLACK;
    public static final Color snakeBody = Color.BLUE;
    public static final Color snakeTail = Color.ORANGE;
    public static final Color apple = Color.RED;
    public static final Color openSpace = Color.GREEN;

    public SnakeSpace(Color value) { //constructor
        c = value;
    }

    public Color getVal() { //value getter
        return c;
    }

    public void setVal(Color value) { //value setter
        c = value;
    }

    public String toString() { //TODO: change colors if needed
        String result = "";
        if(c.equals(snakeHead)) result += "snakeHead";
        else if(c.equals(snakeBody)) result += "snakeBody";
        else if(c.equals(snakeTail)) result += "snakeTail";
        else if(c.equals(apple)) result += "apple";
        else if(c.equals(openSpace)) result += "openSpace";
        else return "SnakeSpace toString Error!!!";
        return result + c;
    }

}
