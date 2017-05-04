package football;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

class CBox extends JPanel implements Serializable{

   private int XX;
   private int YY;

    public transient BufferedImage image;
    private boolean left=false;
    private boolean right=false;
    private boolean up=false;
    private boolean down=false;
    private boolean left_up=false;
    private boolean left_down=false;
    private boolean right_down=false;
    private boolean right_up=false;
    private boolean empty=true;
    private boolean inside=false;

    public boolean isEmpty() {return empty;}

    public void setEmpty(boolean empty) {
        this.empty = empty;
        if(empty==true) {
            this.setInside(false);
            this.setUp(false);
            this.setRight_up(false);
            this.setRight(false);
            this.setRight_down(false);
            this.setDown(false);
            this.setLeft_down(false);
            this.setLeft(false);
            this.setLeft_up(false);
        }
    }

    public boolean isInside() {return inside;}

    public void setInside(boolean inside) {
        this.inside = inside;
        if(inside==true) {
            this.setEmpty(false);
            this.setUp(false);
            this.setRight_up(false);
            this.setRight(false);
            this.setRight_down(false);
            this.setDown(false);
            this.setLeft_down(false);
            this.setLeft(false);
            this.setLeft_up(false);
        }
    }

    public boolean isRight_up() {return right_up;}

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
        if(left==true) {
            this.setInside(false);
            this.setEmpty(false);
        }
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
        if(right==true) {
            this.setInside(false);
            this.setEmpty(false);
        }
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
        if (up == true) {
            this.setInside(false);
            this.setEmpty(false);
        }
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
        if(down==true) {
            this.setInside(false);
            this.setEmpty(false);
        }
    }

    public boolean isLeft_up() {
        return left_up;
    }

    public void setLeft_up(boolean left_up) {
        this.left_up = left_up;
        if(left_up==true) {
            this.setInside(false);
            this.setEmpty(false);
        }
    }

    public boolean isLeft_down() {return left_down;}

    public void setLeft_down(boolean left_down) {
        this.left_down = left_down;
        if (left_down == true) {
            this.setInside(false);
            this.setEmpty(false);
        }
    }

    public void setRight_up(boolean right_up) {
        this.right_up = right_up;
        if (right_up == true) {
            this.setInside(false);
            this.setEmpty(false);
        }
    }

    public boolean isRight_down() {
        return right_down;
    }

    public void setRight_down(boolean right_down) {
        this.right_down = right_down;
        if (right_down == true) {
            this.setInside(false);
            this.setEmpty(false);
        }
    }

    public int get_X() {return XX;}
    public int get_Y() {return YY;}
    public CBox(int x, int y) {
        XX=x;
        YY=y;
        paintIt();
    }
    public CBox(CBox cbox,int x, int y) {
        this.left=cbox.isLeft();
        this.right=cbox.isRight();
        this.up=cbox.isUp();
        this.down=cbox.isDown();
        this.left_up=cbox.isLeft_up();
        this.left_down=cbox.isLeft_down();
        this.right_down=cbox.isRight_down();
        this.right_up=cbox.isRight_up();
        this.empty=cbox.isEmpty();
        this.inside=cbox.isInside();
        XX=x;
        YY=y;
        paintIt();
    }

    public void paintIt(){
        String tileName ="";
        if(isUp()){
            tileName=tileName.concat("1");
        }
        if(isRight_up()){
            tileName=tileName.concat("2");
        }
        if(isRight()){
            tileName=tileName.concat("3");
        }
        if(isRight_down()){
            tileName=tileName.concat("4");
        }
        if(isDown()){
            tileName=tileName.concat("5");
        }
        if(isLeft_down()){
            tileName=tileName.concat("6");
        }
        if(isLeft()){
            tileName=tileName.concat("7");
        }
        if(isLeft_up()){
            tileName=tileName.concat("8");
        }
        if(tileName=="")
            if(isEmpty())
                tileName="empty";
            else if(isInside())
                tileName="inside";
        try {
            image = ImageIO.read(new File("src/resources/"+tileName+".png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.setOpaque(false);
        validate();
        this.setVisible(true);
    }

    public boolean hasDirection(int i, int j){
        if(i==-1){
            if(j==-1)return isLeft_up();
            if(j==0)return isLeft();
            if(j==1)return isLeft_down();
        }else if(i==0){
            if(j==-1)return isUp();
            if(j==1)return isDown();
        }else if(i==1){
            if(j==-1)return isRight_up();
            if(j==0)return isRight();
            if(j==1)return isRight_down();
        }
        System.out.println("ŻADEN Z KIERUNKÓW NIE ZOSTAŁ WYBRANY");
        System.exit(0);
        return true;
    }

    public boolean hasAnyLine(){
        return up || down || left || right || right_down || right_up || left_down || left_up;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}
