/**
 * simple class to store a location in a matrix (row, column)
 */
public class Location {

    public int r; //row
    public int c; //column

    public Location(int row, int col) { //constructor
        r = row;
        c = col;
    }

    public Location(Location loc) {
        r = loc.r;
        c = loc.c;
    }

    public String toString() { //row then column
        return r + ", " + c;
    }

    /**
     * equals method compares rows and columns
     * does not compare values in matrix as that is not stored
     * @param o
     * @return true if same, false if different
     */
    public boolean equals(Object o) {
        if(o instanceof Location) {
            Location compare = (Location) o;
            return r == compare.r && c == compare.c;
        }
        return false;
    }

    /**
     * adds the respective values of two locations (r + r, c + c)
     * @param location1
     * @param location2
     * @return new location with the sum of both locations
     */
    public static Location addLocations(Location location1, Location location2) {
        return new Location(location1.r + location2.r, location1.c + location2.c);
    }

}
