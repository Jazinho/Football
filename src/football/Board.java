package football;

import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

import static java.lang.System.exit;


public class Board extends JFrame {
    static String winner="NULL";
    boolean endTurn=false;
    static int width=11,height=15;

    private int BoardHeight=668;
    private int BoardWidth=478;

    public int current_ball_position_X=5;
    public int current_ball_position_Y=7;

    private static CBox[][] Fields = new CBox[height][width];

    int FirstClickedX;
    int FirstClickedY;
    static char Turn = 'U';

    public void switchTurn(){
        if(Turn=='U')
            Turn='C';
        else Turn='U';

        if(Turn=='C')
            cpuMove();
    }
    CBox TemporaryCBox = null;
    private boolean IsTimerStarted;

    public static char getTurn(){
        return Turn;
    }

    public void cpuMove(){
        if(checkIfPossibleMovesExist()){
            System.out.println("AAA");
            Random random = new Random();
            boolean condition=true;
            while(condition) {
                int x = random.nextInt(3)-1;
                int y = random.nextInt(3)-1;
                FirstClickedX = x+current_ball_position_X;
                FirstClickedY = y+current_ball_position_Y;
                TemporaryCBox = Fields[FirstClickedY][FirstClickedX];
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
                    System.out.println(FirstClickedX+" "+FirstClickedY);
                    executeMove();
                    draw(width, height);
                    if(cond) {
                        switchTurn();
                        condition = false;
                    }
                }
            }

        }
        else{
            exit(0);
        }

    }



    public void initialize(int width, int height) {
        for (int i = 0; i < height + 4; i++)
            for (int j = 0; j < width + 2; j++) {
                Fields[i][j] = new CBox(j,i);
                Fields[i][j].setEmpty(true);
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
    /////////////////////////////////////////////////////

    public void move(MouseEvent e){
        System.out.println("QQQQQ");
        {
            if(Turn=='U')
            {
                System.out.println("BBBB");
                FirstClickedX = ((CBox)e.getSource()).get_X();
                FirstClickedY = ((CBox)e.getSource()).get_Y();
                TemporaryCBox = (CBox)e.getSource();
                if (((Math.abs(current_ball_position_X-FirstClickedX)>1) ||(Math.abs(current_ball_position_Y-FirstClickedY)>1))
                        ||((current_ball_position_X-FirstClickedX==0)&&(current_ball_position_Y-FirstClickedY==0)))
                    return;
                if(FirstClickedY==0||FirstClickedY==height-1||FirstClickedX==0||FirstClickedX==width-1 ||(FirstClickedY==1&&FirstClickedX<4)
                        ||(FirstClickedY==1&&FirstClickedX>6)||(FirstClickedY==height-2&&FirstClickedX<4)
                        ||(FirstClickedY==height-2&&FirstClickedX>6)
                        ||(current_ball_position_X==3 && current_ball_position_Y==2 && FirstClickedX==4 && FirstClickedY==1)
                        ||(current_ball_position_X==7 && current_ball_position_Y==2 && FirstClickedX==6 && FirstClickedY==1)
                        ||(current_ball_position_X==3 && current_ball_position_Y==height-3 && FirstClickedX==4 && FirstClickedY==height-2)
                        ||(current_ball_position_X==7 && current_ball_position_Y==height-3 && FirstClickedX==6 && FirstClickedY==height-2))
                    return;
                if((current_ball_position_X<FirstClickedX && current_ball_position_Y<FirstClickedY && !TemporaryCBox.isLeft_up() )
                        ||(current_ball_position_X<FirstClickedX && current_ball_position_Y>FirstClickedY && !TemporaryCBox.isLeft_down())
                        ||(current_ball_position_X>FirstClickedX && current_ball_position_Y<FirstClickedY && !TemporaryCBox.isRight_up())
                        ||(current_ball_position_X>FirstClickedX && current_ball_position_Y>FirstClickedY && !TemporaryCBox.isRight_down())
                        ||(current_ball_position_X==FirstClickedX && current_ball_position_Y<FirstClickedY && !TemporaryCBox.isUp())
                        ||(current_ball_position_X==FirstClickedX && current_ball_position_Y>FirstClickedY && !TemporaryCBox.isDown())
                        ||(current_ball_position_X>FirstClickedX && current_ball_position_Y==FirstClickedY && !TemporaryCBox.isRight())
                        ||(current_ball_position_X<FirstClickedX && current_ball_position_Y==FirstClickedY && !TemporaryCBox.isLeft()))
                {
                    boolean cond = TemporaryCBox.isMiddle();
                    executeMove();
                    draw(width, height);
                    if(cond) {
                        switchTurn();
                    }
                }
            }

        }
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

    private boolean executeMove(){

        int x = TemporaryCBox.get_X()-current_ball_position_X;
        int y = TemporaryCBox.get_Y()-current_ball_position_Y;
        switch(x){
            case -1:
                switch(y){
                    case -1:
                        TemporaryCBox.setRight_down(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setLeft_up(true);
                        TemporaryCBox.setRight_down(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setLeft_up(true);
                        break;
                    case 0:
                        TemporaryCBox.setRight(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setLeft(true);

                        TemporaryCBox.setRight(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setLeft(true);

                        break;
                    case 1:
                        TemporaryCBox.setRight_up(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setLeft_down(true);

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

                        TemporaryCBox.setDown(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setUp(true);
                        break;

                    case 1:
                        TemporaryCBox.setUp(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setDown(true);

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
                        TemporaryCBox.setLeft_down(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setRight_up(true);
                        break;
                    case 0:
                        TemporaryCBox.setLeft(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setRight(true);
                        TemporaryCBox.setLeft(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setRight(true);
                        break;
                    case 1:
                        TemporaryCBox.setLeft_up(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setRight_down(true);
                        TemporaryCBox.setLeft_up(true);
                        Fields[current_ball_position_Y][current_ball_position_X].setRight_down(true);
                        break;
                }
                break;
        }

        Fields[current_ball_position_Y][current_ball_position_X].repaint();
        current_ball_position_Y=TemporaryCBox.get_Y();
        current_ball_position_X=TemporaryCBox.get_X();

        TemporaryCBox.repaint();

        if(current_ball_position_Y==1 &&(current_ball_position_X==4 || current_ball_position_X==5 || current_ball_position_X==6))
            winner="USER";
        if(current_ball_position_Y==height-2 &&(current_ball_position_X==4 || current_ball_position_X==5 || current_ball_position_X==6))
            winner="CPU";
        if(winner!="NULL")
            exit(0);


        return true;

    }

    public void draw(int width, int height){
        for(int i=0; i<height; i++)
            for(int j=0; j<width; j++)
                (Fields[i][j]).maluj();
    }

    public Board(){
        initialize(9,11);
        this.setSize(BoardWidth, BoardHeight);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //  this.setResizable(false);
        this.setLocationRelativeTo(null);
        //this.setLocation(315, 5);
        this.setTitle("Football");

        File f = new File("src\\resources\\background.jpg");
        BufferedImage myImage = null;
        try {
            myImage = ImageIO.read(f);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        IsTimerStarted = true;
//        Pict = new ImageIcon(getClass().getResource("board.jpg"));
        //     this.setContentPane(new BoardBackground(myImage));;
        GridLayout layout = new GridLayout(height,width);
        setLayout(layout);
        for(int i=0; i < height; i++)
            for(int j=0; j<width; j++){
                CBox cb = new CBox(Fields[i][j],j,i);
                cb.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent e){

                        move(e);
                        if(!checkIfPossibleMovesExist())
                            exit(0);

                    }
                });
                add(cb);
                Fields[i][j]=cb;
            }
        this.setVisible(true);
        IsTimerStarted = false;
    }

    public static void main(String[] arg){
        Board gra = new Board();
    }
}

















 /*
        private void deexecute(){
            BoardPositions[TemporaryCBox.get_X()][TemporaryCBox.get_Y()] = TemporaryCBox_2.getFigure();
            TemporaryCBox.setFigure(TemporaryCBox_2.getFigure());
            TemporaryCBox_2.setFigure(TemporaryCBox_5.getFigure());
            BoardPositions[TemporaryCBox_2.get_X()][TemporaryCBox_2.get_Y()] = TemporaryCBox_2.getFigure();

        }

        private void deexecuteMove(CBox T1, CBox T2, CBox T3)
        {
            TemporaryCBox=T1;
            TemporaryCBox_2=T2;
            TemporaryCBox_3=T3;

            TemporaryCBox.setFigure(TemporaryCBox_2.getFigure());
            BoardPositions[TemporaryCBox.get_X()][TemporaryCBox.get_Y()] = TemporaryCBox.getFigure();
            TemporaryCBox_2.setFigure(TemporaryCBox_3.getFigure());
            BoardPositions[TemporaryCBox_2.get_X()][TemporaryCBox_2.get_Y()] = TemporaryCBox_2.getFigure();
        }
    */