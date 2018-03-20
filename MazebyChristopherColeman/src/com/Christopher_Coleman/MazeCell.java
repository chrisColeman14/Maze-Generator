package com.Christopher_Coleman;

import javax.swing.*;
import java.awt.*;

public class MazeCell extends JComponent{

    private Rectangle rectangle; //This rectangle represents the inside of the maze cell
    private Color color; //This color is the color of the walls of the maze cell
    private boolean isBeginning; //This boolean represents the beginning of the maze
    private boolean isEnd; //This boolean represents the end of the maze
    private boolean isVisited; //This boolean represents if this maze cell has been visited by the first depth search
    private boolean walls[]; //A 2D boolean that represents if the wall of the maze cell exists
    private char direction; //A character that represents the direction the cell is according to another cells position
    private int row, col; //The row and col the maze cell is located at
    private Color fillColor; //This color is the color of the rectangle or the inside of the maze cell

    public MazeCell() {
        //Initialize variables
        row = 0; col = 0;
        walls = new boolean[4];
        //Walls are true if they exists
        walls[0] = true;
        walls[1] = true;
        walls[2] = true;
        walls[3] = true;
        isVisited = false;
        color = Color.BLACK;
        direction = ' ';
        rectangle = new Rectangle(10,10);
        fillColor = Color.WHITE;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Dimension dimension = new Dimension();

        int size = 10;

        int x = size;
        int y = size;

        //Draw the rectangle
        g2.setColor(fillColor);
        g2.fill(rectangle);

        g2.setColor(color);

        //Draw the walls of every maze cell
        /*
        walls[0] is the north wall, so index 0 -> north wall of mazecell
        walls[1] is the east wall, so index 1 -> east wall of mazecell
        walls[2] is the south wall, so index 2 -> south wall of mazecell
        walls[3] is the west wall, so index 3 -> west wall of mazecell
        */
        if(walls[0]) {
            g2.setColor(color);
            g2.drawLine(0,0, x, 0);
        }
        else {
            g2.setColor(Color.WHITE);
            g2.drawLine(0,0, x, 0);
        }

        if(walls[1]) {
            g2.setColor(color);
            g2.drawLine(x,0, x, y);
        }
        else {
            g2.setColor(Color.WHITE);
            g2.drawLine(x,0, x, y);
        }

        if(walls[2]) {
            g2.setColor(color);
            g2.drawLine(x,y, 0, y);
        }
        else {
            g2.setColor(Color.WHITE);
            g2.drawLine(x, y, 0, y);
        }

        if(walls[3]) {
            g2.setColor(color);
            g2.drawLine(0, y, 0, 0);
        }
        else {
            g2.setColor(Color.WHITE);
            g2.drawLine(0, y, 0, 0);
        }
        g2.dispose();
    }

    //Change the color of the walls and repaint
    public void paint(Color color) {
        this.color = color;
        repaint();
    }

    //Set all the walls to the boolean passed through
    public void setWall(boolean isWall) {
        walls[0] = isWall;
        walls[1] = isWall;
        walls[2] = isWall;
        walls[3] = isWall;
    }

    /**
     * Accessors and Getters
     */

    public void setDirection(char direction) { this.direction = direction;}

    public char getDirection() { return direction;}

    public void setIsBeginning(boolean isBeginning) {
        this.isBeginning = isBeginning;
    }

    public boolean getIsBeginning() {
        return isBeginning;
    }

    public void setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public boolean getIsEnd() { return isEnd; }

    public void setIsVisited(boolean isVisited) { this.isVisited = isVisited;}

    public boolean getIsVisited() {return isVisited;}

    public void setWallValue(boolean wall, int i) {
        if(i < walls.length && i >= 0) {
            walls[i] = wall;
        }
    }

    public void setRow(int row) {this.row = row;}

    public int getRow() {return row;}

    public void setCol(int col) {this.col = col;}

    public int getCol() {return col;}

    public boolean getNorthWall() { return walls[0]; }

    public boolean getEastWall() { return walls[1]; }

    public boolean getSouthWall() { return walls[2]; }

    public boolean getWestWall() { return walls[3]; }

    public void setFillColor(Color color) {
        fillColor = color;
        repaint();
    }

    public Color getFillColor() {
        return fillColor;
    }
}
