package football;


/**
 * Helps to calculate the best move for the computer. Contains information about the parent.
 * * */
public class Coords {

    /**
     * parent Coordinate
     * */
    private Coords parent;
    /**
     * current tile
     * */
    private final CBox cBox;
    /**
     * tells if we cannot make another move from this tile
     * */
    private boolean isFinal;

    /**
     * tells if this file has been visited
     * */
    private boolean isExplored;


    /**
     * Coord constructor
     * @param cBox tile
     * */
    public Coords(CBox cBox){
        this.parent = null;
        this.cBox = cBox;
        this.isFinal = false;
        this.isExplored = false;
    }
    /**
     * Coord constructor
     * @param cBox tile
     * @param parent parent Coord
     * */

    public Coords(CBox cBox, Coords parent){
        this.parent = parent;
        this.cBox = cBox;
        this.isFinal = false;
        this.isExplored = false;
    }

    public CBox getcBox() {
        return cBox;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public boolean isExplored() {
        return isExplored;
    }

    public void setExplored(boolean isExplored) {
        this.isExplored = isExplored;
    }

    public Coords getParent() {
        return parent;
    }
}
