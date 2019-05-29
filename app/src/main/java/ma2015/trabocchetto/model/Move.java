package ma2015.trabocchetto.model;

/**
 * This class represents a single bar movement. It contains the orientation, bar, direction of the movement
 * (as attributes) and the methods that allow to get them.
 */
public class Move {

    private char orientation; //orientation of the bar (horizontal 'h' or vertical 'v')
    private int bar; //number of the bar
    private char direction; //direction of the bar's movement (inward or outward)

    /**
     * Move Constructor: given the orientation, number of the bar, direction of the movement, it stores
     * them into the object private attributes.
     * @param orientation : horizontal 'h' or vertical 'v'
     * @param bar : number of the bar
     * @param direction : direction of the bar (inward 'i' or outward 'o')
     */
    public Move(char orientation,int bar,char direction){
        this.orientation=orientation;
        this.bar=bar;
        this.direction=direction;
    }

    /**
     * Getter method: it returns the orientation of the bar (char, 'h' for horizontal, 'v' for vertical)
     * @return char orientation ('h' or 'v')
     */
    public char getOrientation() {
        return orientation;
    }

    /**
     * Getter method: it returns the number of the bar involved in the movement.
     * @return integer, number of the bar.
     */
    public int getBar() {
        return bar;
    }

    /**
     * Getter method: it returns the char direction of the movement ('i' inward or 'o' outward)
     * @return char, direction of movement ('i' or 'o')
     */
    public char getDirection() {
        return direction;
    }
}
