package football;

import java.awt.event.MouseEvent;
import java.io.Serializable;

/**
 * Represents the user player
 *  */
public class User implements Serializable {
    /**
     * Board reference
     * */
    Board board;
    /**
     * vertical coordinate of the fieldwhre we want to move
     * */
    int FirstClickedX;
    /**
     * horizontal coordinate of the fieldwhre we want to move
     * */
    int FirstClickedY;
    /**
     * additional tile which is helpful when makingmove
     * */
    CBox TemporaryCBox;
    /**
     * vertical coordinate of the current ball position
     * */
    int current_ball_position_X;
    /**
     * horizontal coordinate of the current ball position
     * */
    int current_ball_position_Y;
    /**
     * height of the board
     * */
    int height;
    /**
     * width of the board
     * */
    int width;
    /**
     * Coord constructor
     * @param board tile
     * */

    public User(Board board){
        this.board=board;
        height=board.getheight();
        width=board.getwidth();
    }
    /**
     * Reads user's mouse event and if the chosen move is a valid move then executes it, repaints the boardand switches turn
     * @param  e user's mouse event
     */
    public void move(MouseEvent e){
        {
            if(board.getTurn()=='U')
            {
                current_ball_position_X=board.getCurrent_ball_position_X();
                current_ball_position_Y=board.getCurrent_ball_position_Y();
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
                    boolean cond = TemporaryCBox.isInside();
                    board.executeMove(TemporaryCBox);
                    board.draw(width, height);
                    if(cond) {
                       board.switchTurn();
                    }
                }
            }
        }
    }
}
