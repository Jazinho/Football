package football;

import java.util.Random;
import static java.lang.System.exit;

public class CPU {
    Board board;
    int FirstClickedX,FirstClickedY;
    int current_ball_position_X,current_ball_position_Y;
    CBox TemporaryCBox;
    int width,height;

   public CPU(Board board){
        this.board=board;
       height=15;
       width=11;
    }


    public void cpuMove(){
        if(board.checkIfPossibleMovesExist()){
int unnecessaryvariable=0;
            System.out.println("AAA");
            Random random = new Random();
            boolean condition=true;
            while(condition) {
             if(unnecessaryvariable%100000==0) System.out.println("X");
                unnecessaryvariable++;
                current_ball_position_X=board.getCurrent_ball_position_X();
                current_ball_position_Y=board.getCurrent_ball_position_Y();
                int x = random.nextInt(3)-1;
                int y = random.nextInt(3)-1;
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
                }
            }

        }
        else{
            exit(0);
        }
        System.out.println("WYCHODZE");
    }


}
