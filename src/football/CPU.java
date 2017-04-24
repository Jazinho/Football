package football;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static java.lang.System.exit;

public class CPU implements Serializable{
    Board board;
    int FirstClickedX, FirstClickedY;
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


                List<CBox> list = calculateMoves(board.cloneIt(), null, board.getCurrent_ball_position_X(), board.getCurrent_ball_position_Y());
                boolean cond = true;

                for (CBox cBox : list) {
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
                //TODO Delete redundant code in 'Board'
                //TODO SetResizable in Board() to false
                //TODO Fix sizes in Board() initialization
                //TODO i oraz j są w złej kolejności u mnie
                //Todo Zmienić kolejność dodawania do possibilities (najpierw środek, potem te boczne)

                /*
                int x = list.get(0).getX();
                int y = list.get(0).getY();


                FirstClickedX = x+current_ball_position_X;
                FirstClickedY = y+current_ball_position_Y;
                TemporaryCBox = board.getCBox(FirstClickedX,FirstClickedY);

                if (((Math.abs(current_ball_position_X - FirstClickedX) > 1) || (Math.abs(current_ball_position_Y - FirstClickedY) > 1))
                        || ((current_ball_position_X - FirstClickedX == 0) && (current_ball_position_Y - FirstClickedY == 0)))
                    continue;
                if (FirstClickedY == 0 || FirstClickedY == height - 1 || FirstClickedX == 0 || FirstClickedX == width - 1 || (FirstClickedY == 1 && FirstClickedX < 4)
                        || (FirstClickedY == 1 && FirstClickedX > 6) || (FirstClickedY == height - 2 && FirstClickedX < 4)
                        || (FirstClickedY == height - 2 && FirstClickedX > 6)
                        || (current_ball_position_X == 3 && current_ball_position_Y == 2 && FirstClickedX == 4 && FirstClickedY == 1)
                        || (current_ball_position_X == 7 && current_ball_position_Y == 2 && FirstClickedX == 6 && FirstClickedY == 1)
                        || (current_ball_position_X == 3 && current_ball_position_Y == height - 3 && FirstClickedX == 4 && FirstClickedY == height - 2)
                        || (current_ball_position_X == 7 && current_ball_position_Y == height - 3 && FirstClickedX == 6 && FirstClickedY == height - 2))
                    continue;
                if ((current_ball_position_X < FirstClickedX && current_ball_position_Y < FirstClickedY && !TemporaryCBox.isLeft_up())
                        || (current_ball_position_X < FirstClickedX && current_ball_position_Y > FirstClickedY && !TemporaryCBox.isLeft_down())
                        || (current_ball_position_X > FirstClickedX && current_ball_position_Y < FirstClickedY && !TemporaryCBox.isRight_up())
                        || (current_ball_position_X > FirstClickedX && current_ball_position_Y > FirstClickedY && !TemporaryCBox.isRight_down())
                        || (current_ball_position_X == FirstClickedX && current_ball_position_Y < FirstClickedY && !TemporaryCBox.isUp())
                        || (current_ball_position_X == FirstClickedX && current_ball_position_Y > FirstClickedY && !TemporaryCBox.isDown())
                        || (current_ball_position_X > FirstClickedX && current_ball_position_Y == FirstClickedY && !TemporaryCBox.isRight())
                        || (current_ball_position_X < FirstClickedX && current_ball_position_Y == FirstClickedY && !TemporaryCBox.isLeft())) {
                    boolean cond = TemporaryCBox.isMiddle();
                    System.out.println("MID: "+cond);
                    System.out.println(FirstClickedX+" "+FirstClickedY);
                    System.out.println(">>>>    cbp("+current_ball_position_X+","+current_ball_position_Y+")  fk("+FirstClickedX+","+FirstClickedY+")   "+"tmp("+TemporaryCBox.get_X()+","+TemporaryCBox.get_Y()+")");
                    System.out.println("^^^^^     cbp("+board.getCurrent_ball_position_X()+","+board.getCurrent_ball_position_Y()+")");
                    board.executeMove(TemporaryCBox);
                    System.out.println("^^^^^     cbp("+board.getCurrent_ball_position_X()+","+board.getCurrent_ball_position_Y()+")");
                    board.draw(width, height);
                    if(cond) {
                        board.switchTurn();
                        condition = false;
                    }
                    if(!board.checkIfPossibleMovesExist() && cond){
                        board.setWinner("USER");
                        board.theEnd();
                    }
                }*/
            }
        } else {
            exit(0);
        }
        System.out.println("WYCHODZE");
    }

    private List<CBox> calculateMoves(Board board, List<Coords> possibilities, int curX, int curY) {
        List<CBox> res = new LinkedList<>();
        if (possibilities == null) possibilities = new ArrayList<>();

        CBox curBox = board.getCBox(curX, curY);
        boolean exploredAll = false;

        while (!exploredAll) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {

                    boolean wasVisited = false;
                    for (Coords c : possibilities) {
                        if (c.getcBox().getX() == curX + i && c.getcBox().getY() == curY + j) wasVisited = true;
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
                if (!coords.isExplored()) { //Jeśli dany punkt nie został jeszcze spenetrowany to zaznacz go na mapie
                    board.markMoveOnMap(coords.getcBox());
                }

                if (!coords.isFinal()) { //Jeśli dany punkt nie jest punktem końcowy to spenetruj go
                    coords.setExplored(true);
                    res = calculateMoves(board, possibilities, coords.getcBox().get_X(), coords.getcBox().get_Y());
                    // jakby to wywołanie przeniesć poza powyżsżą petle for to wtedy najpierw szukalibysmy wszerz, a tak to szukamy wgłąb
                    res.add(coords.getcBox());
                    //końcowy przypadek: nie wchodzimy wewnątrz tego if'a bo wszystkie node'y na mapie sa finalne

                    return res;
                }
            }
            exploredAll = true;
        }
        //exploredAll zawsze będzie mieć tu wartość true (bo opuszczona została petla while

        //na tym etapie mamy rekursywne drzewo rozrysowane pokonczone w punktach finalnych
        // ta część kodu wykonywana jest tylko dla liści drzewa możliwość, liści finalnych
        // (sprawdzane jest który jest najlepszy i ten jest zwracan)

        CBox best = null;
        for (Coords c : possibilities) {
            if (c.isFinal()) {
                if (best == null) {
                    best = c.getcBox();
                } else {
                    if (c.getcBox().get_Y() < best.get_Y()) {
                        best = c.getcBox();
                    }
                }
            }
        }

        res.add(best);

        return res;
    }
}