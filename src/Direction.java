/**
 * class for direction in Snake Game
 */
public class Direction {

    public static final int up = 0;
    public static final int right = 1;
    public static final int down = 2;
    public static final int left = 3;
    public static final int none = 4;
    private static final Location[] directionList = {new Location(-1, 0), new Location(0, 1),
                                                     new Location(1, 0), new Location(0, -1),
                                                     new Location(0, 0)};

    private int index;

    public Direction(int dir) { //constructor
        index = dir;
    }

    /**
     * gets the Location equivalent of the direction from the index value of the current instance
     * @return Location from array
     */
    private Location getFromDirectionList() {
        return directionList[index];
    }

    /**
     * changes the direction
     * @param newDirection
     */
    public void setDirection(int newDirection) {
        index = newDirection;
    }

    /**
     * returns the location of a new point based on the given direction
     * @param original location
     * @return new location
     */
    public Location getNewLocation(Location original) {
        return Location.addLocations(original, getFromDirectionList());
    }

    /**
     * determines whether another direction is opposite
     * @param otherIndex (direction index to test against current instance)
     * @return boolean (true if directions are opposites)
     */
    public boolean isOpposite(int otherIndex) {
        if(index == Direction.none || otherIndex == Direction.none) return false;
        return Math.abs(index - otherIndex) == 2;
    }

    /**
     * determines if 2 directions are equal based on their index values
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if(o instanceof Direction) {
            Direction compare = (Direction) o;
            return index == compare.index;
        }
        return false;
    }

    public String toString() {
        if(index == Direction.up)    return "up";
        if(index == Direction.right)    return "right";
        if(index == Direction.down)    return "down";
        if(index == Direction.left)    return "left";
        if(index == Direction.none)  return "none";
        else return "Direction toString Error!!!";
    }

}
