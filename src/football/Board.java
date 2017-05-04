package football;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import javax.swing.*;


public class Board extends JFrame implements Serializable{
    private CPU cpu;
    private User user=null;

    private static String winner="NULL";
    private static int width=11,height=15;

    private int BoardHeight=668;
    private int BoardWidth=478;

    private int current_ball_position_X=5;
    private int current_ball_position_Y=7;

    public CBox[][] Fields = new CBox[height][width];

    private char Turn = 'U';
    public static boolean isFinished = false;

    public void theEnd(){
        if(!isFinished) System.out.println("THE WINNER IS: "+getWinner());
        isFinished = true;
    }

    public void switchTurn(){
        if(getTurn()=='U')
            setTurn('C');
        else setTurn('U');

        if(getTurn()=='C')
            cpu.cpuMove();
    }

    public CBox getCBox(int x,int y){
        return Fields[y][x];
    }


    public void initialize(int width, int height) {
        for (int i = 0; i < height + 4; i++) {
            for (int j = 0; j < width + 2; j++) {
                Fields[i][j] = new CBox(j, i);
                Fields[i][j].setEmpty(true);
            }
        }

        for (int i = 3; i < height + 1; i++)
            for (int j = 2; j < width; j++) {
                Fields[i][j].setMiddle(true);
            }

        for (int i = 1; i < height + 3; i++)
            for (int j = 0; j < width + 2; j++)
                if (i == 2 && j == 1) {
                    Fields[i][j].setRight(true);
                    Fields[i][j].setDown(true);
                }
                else if (i == height + 1 && j == 1) {
                    Fields[i][j].setRight(true);
                    Fields[i][j].setUp(true);
                }
                else if (i == 2 && j == width) {
                    Fields[i][j].setLeft(true);
                    Fields[i][j].setDown(true);
                }
                else if (i == height + 1 && j == width) {
                    Fields[i][j].setUp(true);
                    Fields[i][j].setLeft(true);

                }
                else if (i == 2 && j == width / 2) {
                    Fields[i][j].setLeft(true);
                    Fields[i][j].setUp(true);
                }
                else if (i == 2 && j == width / 2 + 2) {
                    Fields[i][j].setRight(true);
                    Fields[i][j].setUp(true);
                }
                else if (i == height + 1 && j == width / 2) {
                    Fields[i][j].setDown(true);
                    Fields[i][j].setLeft(true);
                }
                else if (i == height + 1 && j == width / 2 + 2) {
                    Fields[i][j].setRight(true);
                    Fields[i][j].setDown(true);
                }
                else if ((i == 2 || i == height + 1) && (j != 0) && (j != width + 1)) {
                    Fields[i][j].setRight(true);
                    Fields[i][j].setLeft(true);
                }
                else if ((j == 1 || j == width) && (i != 1) && (i != height + 2)) {
                    Fields[i][j].setUp(true);
                    Fields[i][j].setDown(true);
                }

        Fields[2][width / 2 + 1].setMiddle(true);

        Fields[height + 1][width / 2 + 1].setMiddle(true);

        Fields[1][width / 2 + 1].setRight(true);
        Fields[1][width / 2 + 1].setLeft(true);

        Fields[height + 2][width / 2 + 1].setLeft(true);
        Fields[height + 2][width / 2 + 1].setRight(true);

        Fields[1][width / 2 + 2].setDown(true);
        Fields[1][width / 2 + 2].setLeft(true);

        Fields[1][width / 2].setRight(true);
        Fields[1][width / 2].setDown(true);

        Fields[height + 2][width / 2 + 2].setUp(true);
        Fields[height + 2][width / 2 + 2].setLeft(true);

        Fields[height + 2][width / 2].setRight(true);
        Fields[height + 2][width / 2].setUp(true);
    }

    public boolean checkIfPossibleMovesExist(){
        for(int y=current_ball_position_Y-1; y<=current_ball_position_Y+1; y++)
            for(int x=current_ball_position_X-1;x<=current_ball_position_X+1; x++) {
                CBox Temp = Fields[y][x];
                if (y == 0 || y == height - 1 || x == 0 || x == width - 1 || (y == 1 && x < 4)
                        || (y == 1 && x > 6) || (y == height - 2 && x < 4)
                        || (y == height - 2 && x > 6)
                        || (current_ball_position_X == 3 && current_ball_position_Y == 2 && x == 4 && y == 1)
                        || (current_ball_position_X == 7 && current_ball_position_Y == 2 && x == 6 && y == 1)
                        || (current_ball_position_X == 3 && current_ball_position_Y == height - 3 && x == 4 && y == height - 2)
                        || (current_ball_position_X == 7 && current_ball_position_Y == height - 3 && x == 6 && y == height - 2))
                    continue;
                if(((current_ball_position_X < x && current_ball_position_Y < y && !Temp.isLeft_up())
                        || (current_ball_position_X < x && current_ball_position_Y > y && !Temp.isLeft_down())
                        || (current_ball_position_X > x && current_ball_position_Y < y && !Temp.isRight_up())
                        || (current_ball_position_X > x && current_ball_position_Y > y && !Temp.isRight_down())
                        || (current_ball_position_X == x && current_ball_position_Y < y && !Temp.isUp())
                        || (current_ball_position_X == x && current_ball_position_Y > y && !Temp.isDown())
                        || (current_ball_position_X > x && current_ball_position_Y == y && !Temp.isRight())
                        || (current_ball_position_X < x && current_ball_position_Y == y && !Temp.isLeft())))
                    return true;
            }
        return false;
    }

    public boolean executeMove(CBox TemporaryCBox){

        markMoveOnMap(TemporaryCBox);

        Fields[current_ball_position_Y][current_ball_position_X].repaint();
        current_ball_position_Y=TemporaryCBox.get_Y();
        current_ball_position_X=TemporaryCBox.get_X();

        TemporaryCBox.repaint();

        if(current_ball_position_Y==1 &&(current_ball_position_X==4 || current_ball_position_X==5 || current_ball_position_X==6))
            winner="USER";
        if(current_ball_position_Y==height-2 &&(current_ball_position_X==4 || current_ball_position_X==5 || current_ball_position_X==6))
            winner="CPU";
        if(winner!="NULL")
            theEnd();

        return true;
    }

    public void markMoveOnMap(CBox TemporaryCBox){

        int x = TemporaryCBox.get_X()-current_ball_position_X;
        int y = TemporaryCBox.get_Y()-current_ball_position_Y;
        switch(x){
            case -1:
                switch(y){
                    case -1:
                        TemporaryCBox.setRight_down(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setLeft_up(true);
                        break;
                    case 0:
                        TemporaryCBox.setRight(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setLeft(true);
                        break;
                    case 1:
                        TemporaryCBox.setRight_up(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setLeft_down(true);
                        break;
                }
                break;
            case 0:
                switch(y){
                    case -1:
                        TemporaryCBox.setDown(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setUp(true);
                        break;
                    case 1:
                        TemporaryCBox.setUp(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setDown(true);
                        break;
                }
                break;
            case 1:
                switch(y){
                    case -1:
                        TemporaryCBox.setLeft_down(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setRight_up(true);
                        break;
                    case 0:
                        TemporaryCBox.setLeft(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setRight(true);
                        break;
                    case 1:
                        TemporaryCBox.setLeft_up(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setRight_down(true);
                        break;
                }
                break;
        }
    }

    public void draw(int width, int height){
        for(int i=0; i<height; i++)
            for(int j=0; j<width; j++)
                (Fields[i][j]).paintIt();
    }

    public Board(){
        initialize(9,11);
        this.setSize(BoardWidth, BoardHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("Football");

        GridLayout layout = new GridLayout(height,width);
        setLayout(layout);
        setUser(new User(this));
        setCpu(new CPU(this));
        for(int i=0; i < height; i++)
            for(int j=0; j<width; j++){
                CBox cb = new CBox(Fields[i][j],j,i);
                cb.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent e){
                        user.move(e);
                        if(!checkIfPossibleMovesExist()) {
                            setWinner("CPU");
                            theEnd();
                        }
                    }
                });
                add(cb);
                Fields[i][j]=cb;
            }
        this.setVisible(true);
    }

    public Board cloneIt(){
        Board board = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();

            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            board = (Board) new ObjectInputStream(bais).readObject();

        }catch(Exception e){
            e.printStackTrace();
        }
        return board;
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static String getWinner() {
        return winner;
    }

    public static void setWinner(String winner) {
        Board.winner = winner;
    }

    public static int getwidth() {
        return width;
    }


    public static void setwidth(int width) {
        Board.width = width;
    }

    public static int getheight() {
        return height;
    }

    public static void setheight(int height) {
        Board.height = height;
    }

    public int getBoardHeight() {
        return BoardHeight;
    }

    public void setBoardHeight(int boardHeight) {
        BoardHeight = boardHeight;
    }

    public int getBoardWidth() {
        return BoardWidth;
    }

    public void setBoardWidth(int boardWidth) {
        BoardWidth = boardWidth;
    }

    public int getCurrent_ball_position_X() {
        return current_ball_position_X;
    }

    public void setCurrent_ball_position_X(int current_ball_position_X) {
        this.current_ball_position_X = current_ball_position_X;
    }

    public int getCurrent_ball_position_Y() {
        return current_ball_position_Y;
    }

    public void setCurrent_ball_position_Y(int current_ball_position_Y) {
        this.current_ball_position_Y = current_ball_position_Y;
    }

    public CBox[][] getFields() {
        return Fields;
    }

    public void setFields(CBox[][] fields) {
        Fields = fields;
    }

    public char getTurn() {
        return Turn;
    }

    public void setTurn(char turn) {
        Turn = turn;
    }

    public static void main(String[] arg){
        Board gra = new Board();
    }
}