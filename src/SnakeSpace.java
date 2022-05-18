/**
 * class to store values held in the SnakeGame matrix
 */
public class SnakeSpace {

    private char val;
    public static final char snakeHead = 'h';
    public static final char snakeTail = 't';
    public static final char snakeBody = 's';
    public static final char apple = 'a';
    public static final char openSpace = '-';

    public SnakeSpace(char value) { //constructor
        val = value;
    }

    public char getVal() { //value getter
        return val;
    }

    public void setVal(char value) { //value setter
        val = value;
    }

    public String toString() {
        return String.valueOf(val);
    }

}
