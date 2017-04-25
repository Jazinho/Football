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

                LinkedList<CBox> list = calculateMoves(board.cloneIt(), null, board.getCurrent_ball_position_X(), board.getCurrent_ball_position_Y());
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
                //TODO Fix sizes in Board() initialization
                //TODO i oraz j są w złej kolejności u mnie
                //Todo Zmienić kolejność dodawania do possibilities (najpierw środek, potem te boczne)

            }
        } else {
            exit(0);
        }
    }

    private LinkedList<CBox> calculateMoves(Board board, List<Coords> possibilities, int curX, int curY) {
        LinkedList<CBox> res = new LinkedList<>();
        if (possibilities == null) {
            possibilities = new ArrayList<>();
            CBox startingCBox = board.getCBox(curX, curY);
            Coords startingCoords = new Coords(startingCBox);
            startingCoords.setExplored(true);
            possibilities.add(startingCoords);
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
                        Coords newCords = new Coords(newCBox);
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
                    res = calculateMoves(board, possibilities, coords.getcBox().get_X(), coords.getcBox().get_Y());
                    // jakby to wywołanie przeniesć poza powyżsżą petle for to wtedy najpierw szukalibysmy wszerz, a tak to szukamy wgłąb
                    res.addFirst(coords.getcBox());
                    //końcowy przypadek: nie wchodzimy wewnątrz tego if'a bo wszystkie node'y na mapie sa finalne

                    return res;
                }
            }
            exploredAll = true;
        }

        //na tym etapie mamy rekursywne drzewo rozrysowane pokonczone w punktach finalnych
        // ta część kodu wykonywana jest tylko dla liści drzewa możliwość, liści finalnych
        // (sprawdzane jest który jest najlepszy i ten jest zwracan)

        CBox best = null;
        for (Coords c : possibilities) {
            if (c.isFinal()) {
                if (best == null) {
                    best = c.getcBox();
                } else {
                    if (c.getcBox().get_Y() > best.get_Y()) {
                        best = c.getcBox();
                    }else if(c.getcBox().get_Y() == best.get_Y()){
                        if(Math.abs(c.getcBox().get_X()-6) < Math.abs(best.get_Y())){
                            best = c.getcBox();
                        }
                    }
                }
            }
        }

        res.add(best);

        return res;
    }
}