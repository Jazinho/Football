package football;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

/**
 * The board extending JFrame that we will be playing on. It aggregates User and CPU object as well as the tiles where
 * we make moves and replay button
 * */
public class Board extends JFrame implements Serializable{
    /**
     * static Board reference
     * */
    public static Board game=null;
    /**
     * CPU reference
     * */
    private CPU cpu;
    /**
     * User reference
     * */
    private User user=null;

    /**
     * Describes who is the winner
     * */
    private static String winner="NULL";
    /**
     * Describes how many playing fields vertically
     * */
    private static int width=11;
    /**
     * Describes how many playing fields horizontally
     * */
    private static int height=15;
    /**
     * Describes board height in pixels
     * */
    private int BoardHeight=715;
    /**
     * Describes board width in pixels
     * */
    private int BoardWidth=478;

    /**
     * current vertical ball position
     * */
    private int current_ball_position_X=5;

    /**
     * current horizontal ball position
     * */
    private int current_ball_position_Y=7;


    /**
     * array of array of our tiles meaning CBox
     * */
    public CBox[][] Fields = new CBox[height][width];

    /**
     * current player turn
     * */
    private char Turn = 'U';

    /**
     * determines if the game is over
     * */
    public static boolean isFinished = false;

    /**
     * Checks if the game is finished and prints the name of the winner.
     * Doesn't return anything.
     */

    public void theEnd(){
        if(!isFinished) System.out.println("THE WINNER IS: "+getWinner());
        isFinished = true;
    }

    /**
     * Switches the turn.
     * */

    public void switchTurn(){
        if(getTurn()=='U')
            setTurn('C');
        else setTurn('U');

        if(getTurn()=='C')
            cpu.cpuMove();
    }

    /**
     *
     * @param  x    x coordinate of the tile in Fields array
     * @param  y    y coordinate of the tile in Fields array
     * @return      the tile at a specified position
     * @see         CBox
     */
    public CBox getCBox(int x,int y){
        return Fields[y][x];
    }

    /**
     * Initializes the Fields array with CBox elements
     * @param  width  width of our football pitch
     * @param  height width of our football pitch
     */
    public void initialize(int width, int height) {
        for (int i = 0; i < height + 4; i++) {
            for (int j = 0; j < width + 2; j++) {
                Fields[i][j] = new CBox(j, i);
                Fields[i][j].setEmpty(true);
            }
        }

        for (int i = 3; i < height + 1; i++)
            for (int j = 2; j < width; j++) {
                Fields[i][j].setInside(true);
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

        Fields[2][width / 2 + 1].setInside(true);

        Fields[height + 1][width / 2 + 1].setInside(true);

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

    /**
     * Checks whether possible moves exist or not
     * @return      response if the possible move exists or not
     */
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

    /**
     * Makes a move and checks if there is already a winner
     * @param  TemporaryCBox the tile that we want to move to
     * @see    CBox
     */

    public void executeMove(CBox TemporaryCBox){

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
    }

    /**
     * Set appropriate fields in CBox where the current ball's position field and the field where we want to move
     * @param  TemporaryCBox the tile that we want to move to
     * @see    CBox
     */

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
    /**
     * Repaints the whole board
     * @param  width    board width
     * @param  height   board height
     */

    public void draw(int width, int height){
        for(int i=0; i<height; i++)
            for(int j=0; j<width; j++)
                (Fields[i][j]).paintIt();
    }


    /**
     * initializes the board with empty tiles, adds ActionListeners to the tiles and creates the replay button
     * */
    public Board(){
        JPanel panel = new JPanel();
        JButton button = new JButton("replay");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                initializeGame();
            }
        });
        panel.add(button);
        panel.setVisible(true);

        initialize(9,11);
        setSize(BoardWidth, BoardHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("Football");
        GridLayout layout = new GridLayout(height+1,width);
        FlowLayout fl = new FlowLayout();
        this.setLayout(layout);
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
        this.add(new JPanel());
        this.add(new JPanel());
        this.add(new JPanel());
        this.add(new JPanel());
        this.add(new JPanel());

        this.add(panel);
        setVisible(true);
        this.setVisible(true);
    }

    /**
     * Clones the board
     @return copy of the board
     */

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

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
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

    public static int getheight() {
        return height;
    }

    /**
     * returns current vertical ball's position
     * */
    public int getCurrent_ball_position_X() {
        return current_ball_position_X;
    }

    /**
     * sets current vertical ball's position
     * */
    public void setCurrent_ball_position_X(int current_ball_position_X) {
        this.current_ball_position_X = current_ball_position_X;
    }

    /**
     * returns current horizontal ball's position
     * */
    public int getCurrent_ball_position_Y() {
        return current_ball_position_Y;
    }

    /**
     * sets current horizontal ball's position
     * */
    public void setCurrent_ball_position_Y(int current_ball_position_Y) {
        this.current_ball_position_Y = current_ball_position_Y;
    }

    public char getTurn() {
        return Turn;
    }

    public void setTurn(char turn) {
        Turn = turn;
    }

    /**
     * removes old game and creates a new one
     * */
    public static void initializeGame(){
        if(!(game==null))
            game.setVisible(false);
        game = new Board();
    }

    public static void main(String[] arg){
        initializeGame();
    }

}