package football;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static java.lang.System.exit;

public class CPU implements Serializable{
    Board board;
    int current_ball_position_X, current_ball_position_Y;
    CBox TemporaryCBox;
    int width, height;

    public CPU(Board board) {
        this.board = board;
        height = 15;
        width = 11;
    }

    public void cpuMove() {
        if (board.checkIfPossibleMovesExist()) {

            boolean condition = true;

            while (condition) {
                current_ball_position_X = board.getCurrent_ball_position_X();
                current_ball_position_Y = board.getCurrent_ball_position_Y();

                CBox startingCBox = board.getCBox(current_ball_position_X, current_ball_position_Y);
                Coords startingCoords = new Coords(startingCBox);

                Coords best = calculateMoves(board.cloneIt(), null, startingCoords);
                LinkedList<CBox> list = new LinkedList<>();
                while(best != null){
                    list.addFirst(best.getcBox());
                    best = best.getParent();
                }

                boolean cond = true;

                for (CBox cBox : list) {
                    if(current_ball_position_X == cBox.get_X() && current_ball_position_Y == cBox.get_Y()) continue; //pominiecie pierwszego elementu listy, ktory jest startowym polozeniem piłki
                    TemporaryCBox = board.getCBox(cBox.get_X(), cBox.get_Y());
                    board.executeMove(TemporaryCBox);
                    board.draw(width, height);
                }

                if (!board.checkIfPossibleMovesExist() && cond) {
                    board.setWinner("USER");
                    board.theEnd();
                }

                board.switchTurn();
                condition = false;

                //TODO Refactor isMiddle to isInside in whole project
                //TODO Delete all redundant images

            }
        } else {
            exit(0);
        }
    }

    private Coords calculateMoves(Board board, List<Coords> possibilities, Coords curCoords) {

        int curX = curCoords.getcBox().get_X();
        int curY = curCoords.getcBox().get_Y();

        if (possibilities == null) {
            possibilities = new ArrayList<>();
            curCoords.setExplored(true);
            possibilities.add(curCoords);
        }

        CBox curBox = board.getCBox(curX, curY);
        boolean exploredAll = false;

        while (!exploredAll) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {

                    boolean wasVisited = false;
                    for (Coords c : possibilities) {
                        if ((c.getcBox().get_X() == curX + i) && (c.getcBox().get_Y() == curY + j)) wasVisited = true;
                    }

                    if (i == 0 && j == 0) continue;
                    //Dodajemy nowy Coord, gdy w w jego kierunku nie ma jeszcze kreski oraz nie został odwiedzony juz oraz
                    //CBox jest wewnątrz boiska ( (isMiddle) albo (isEmpty,ale ma kreski - krawędź boiska))
                    if (!curBox.hasDirection(i, j) && !wasVisited && (board.getCBox(curX + i, curY + j).isMiddle() || board.getCBox(curX + i, curY + j).hasAnyLine())) {
                        CBox newCBox = board.getCBox(curX + i, curY + j);
                        Coords newCords = new Coords(newCBox, curCoords);
                        if (!newCBox.hasAnyLine()) newCords.setFinal(true);
                        possibilities.add(newCords);
                    }
                }
            }

            for (Coords coords : possibilities) {
                if (!coords.isFinal() && !coords.isExplored()) { //Jeśli dany punkt nie jest punktem końcowym i nie został wczesniej spenetrowany to spenetruj go
                    board.setCurrent_ball_position_X(curX);//powinno być, bo 'markMoveOnMap' zaznacza
                    board.setCurrent_ball_position_Y(curY);//własnie na podstawie current_ball_position
                    board.markMoveOnMap(coords.getcBox());
                    coords.setExplored(true);
                    return calculateMoves(board, possibilities, coords);
                    // jakby to wywołanie przeniesć poza powyżsżą petle for to wtedy najpierw szukalibysmy wszerz, a tak to szukamy wgłąb

                }
            }
            exploredAll = true;
        }

        //na tym etapie mamy rekursywne drzewo rozrysowane pokonczone w punktach finalnych
        // ta część kodu wykonywana jest tylko dla liści drzewa możliwość, liści finalnych
        // (sprawdzane jest który jest najlepszy i ten jest zwracan)

        Coords best = null;
        for (Coords c : possibilities) {
            if (c.isFinal()) {
                if (best == null) {
                    best = c;
                } else {
                    if (c.getcBox().get_Y() > best.getcBox().get_Y()) {
                        best = c;
                    }else if(c.getcBox().get_Y() == best.getcBox().get_Y()){
                        if(Math.abs(c.getcBox().get_X()-5) < Math.abs(best.getcBox().get_X()-5)){
                            best = c;
                        }
                    }
                }
            }
        }

        return best;
    }
}