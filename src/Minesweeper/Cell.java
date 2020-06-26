package Minesweeper;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Cell extends Pane {
    private int neighbours = 0;
    private boolean hidden = true;
    private boolean mine = false;
    private boolean flag = false;
    Rectangle rc;
    public void drawMine(){
        hidden = false;
        Rectangle mine  = new Rectangle(9,3,2,15);
        Rectangle mine1 = new Rectangle(7,5,6,11);
        Rectangle mine2 = new Rectangle(5,7,10,7);
        Rectangle mine3 = new Rectangle(3,9,14,3);
        this.getChildren().addAll(mine,mine1,mine2,mine3);
    }
    public void drawVoidCell(){
        hidden = false;
        this.setFill(Color.BLACK);
    }
    public void drawText(){
        hidden = false;
        Text text = new Text(Integer.toString(neighbours));
        text.setLayoutY(17.5);
        text.setLayoutX(3.5);
        text.setFont(Font.font("Verdana",20));
        switch (neighbours) {
            case 1: {
                text.setFill(Color.RED);
                break;
            }
            case 2: {
                text.setFill(Color.GREEN);
                break;
            }
            case 3: {
                text.setFill(Color.BLUE);
                break;
            }
            case 4: {
                text.setFill(Color.web("#ff6600"));
                break;
            }
            case 5: {
                text.setFill(Color.web("#961d1d"));
                break;
            }
            case 6: {
                text.setFill(Color.web("#248f8b"));
                break;
            }
            case 7: {
                text.setFill(Color.BLACK);
                break;
            }
            case 8: {
                text.setFill(Color.web("#cc00cc"));
                break;
            }
        }
        this.getChildren().add(text);
    }
    public void drawFlag(){
        hidden = false;
        Rectangle flag1 = new Rectangle(10,4,2,10);
        Rectangle flag2 = new Rectangle(6,14,10,2.5);
        Rectangle flag3 = new Rectangle(6,4,5,4);

        flag3.setFill(Color.RED);
        this.getChildren().addAll(flag1,flag2,flag3);
    }
    public void createRect(double width, double height){
        rc = new Rectangle(width,height);
        this.getChildren().clear();
        this.getChildren().add(rc);
    }
    public void setFill(Color color){
        rc.setFill(color);
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public int getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(int neighbours) {
        this.neighbours = neighbours;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
