/**
 * class for direction in Snake Game
 */
public class Direction {

    public static final int up = 0;
    public static final int right = 1;
    public static final int down = 2;
    public static final int left = 3;
    private static final Location[] directionList = {new Location(-1, 0), new Location(0, 1),
                                                     new Location(1, 0), new Location(0, -1)};

    private Location direction;

    public Direction(int dir) { //constructor
        direction = getFromDirectionList(dir);
    }

    /**
     * gets the Location equivalent of the direction from a given index value
     * @param index of value in directions array
     * @return Location from array
     */
    private Location getFromDirectionList(int index) {
        return directionList[index];
    }

    /**
     * changes the direction
     * @param newDir
     */
    public void setDirection(int newDir) {
        direction = getFromDirectionList(newDir);
    }

    /**
     * returns the location of a new point based on the given direction
     * @param original
     * @return new location
     */
    public Location getNewLocation(Location original) {
        return Location.addLocations(original, direction);
    }

}
