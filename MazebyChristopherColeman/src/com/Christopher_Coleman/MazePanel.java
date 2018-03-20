package com.Christopher_Coleman;

import javax.swing.*;
import java.awt.*;

public class MazePanel extends JPanel {

    private MazeCell cells[][]; //The 2D array that is the maze

    public MazePanel() {
        generateMaze(50,50);
    }

    //Generate the maze by creating MazeCells and adding them to the panel
    public void generateMaze(int rows, int cols) {
        //removeCells();
        cells = new MazeCell[rows][cols];
        for(int i = 0; i < rows; i++) {
            for(int j = 0 ; j < cols; j++) {
                cells[i][j] = new MazeCell();
                add(cells[i][j]);
            }
        }

        setLayout(new GridLayout(rows, rows, 0 ,0));
        setPreferredSize(new Dimension((rows*10) + rows+1,(rows*10) + rows+1));
    }

    //This allows the maze to be referenced by another object
    public MazeCell[][] getCell() {
        return cells;
    }
}
