/**
 * Author: Christopher D. Coleman
 * Date: 11/01/17
 *
 * Description: This program allows the user to create maze based on rows and columns separately and then solves that
 *              maze.
 *
 * Known Bugs: 1. The maze generation does not animate.
 *             2. The corner where the first depth search has to make a decision of two ways and it went
 *                the wrong way that cell does not get painted in the final path.
 */
package com.Christopher_Coleman;

import javax.swing.*;
import java.awt.*;

public class Maze extends JFrame {
    public Maze() {

        super("Maze by Christopher Coleman");

        MazePanel mazePanel = new MazePanel();
        ControlPanel controlPanel = new ControlPanel();

        Controller controller = new Controller();

        //Set the maze panel and control panel in the controller to add to the JFrame
        mazePanel = controller.getMazePanel();
        controlPanel = controller.getControlPanel();

        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(controlPanel);

        getContentPane().add(mazePanel);

        pack();
        setResizable(false);
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        Maze maze = new Maze();
    }
}
