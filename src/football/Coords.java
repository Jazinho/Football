package football;


public class Coords {
    private final CBox cBox;
    private boolean isFinal;
    private boolean isExplored;

    public Coords(CBox cBox){
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
}
