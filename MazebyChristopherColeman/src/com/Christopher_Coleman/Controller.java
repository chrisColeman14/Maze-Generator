/**
 * The Controller object adds listeners to Java swing objects that the user interacts with
 */
package com.Christopher_Coleman;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.util.ArrayList;

public class Controller implements ChangeListener, MouseListener{

    private MazePanel mazePanel; //The panel that holds the maze
    private ControlPanel controlPanel; //The JPanel that holds the controls including buttons, sliders, and labels
    private Logic logic; //The logic controller that handles the algorithms like first depth search used on the maze

    private int rows; //The amount of rows the user has specified
    private int cols; //The amount of the columns the user has specified
    private int speed; //The speed of the animations the user has specified

    private boolean hasGenerated; //A boolean value that determines if the maze has been generated
    private boolean stopped;

    private ArrayList<Button> buttons;
    private ArrayList<JSlider> sliders = new ArrayList<>();

    private MazeCell cell[][] = new MazeCell[50][50]; //The 2D MazeCell representing the maze with a max row and column
                                                       //value of 50

    public Controller() {
        //Initializing Variables
        logic = new Logic();
        buttons = new ArrayList<>();
        sliders = new ArrayList<>();
        mazePanel = new MazePanel();
        controlPanel = new ControlPanel();
        rows = 50;
        cols = 50;
        speed = 5;
        hasGenerated = false;
        stopped = false;
        addListeners();
        setRowAndCol();
    }

    public MazePanel getMazePanel() {
        return mazePanel;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    //Set the values of the row, column, and speed sliders
    public void setRowAndCol() {
        if(!sliders.isEmpty()) {

            JSlider s = sliders.get(1);
            rows = s.getValue();
            s = sliders.get(2);
            cols = s.getValue();
            s = sliders.get(0);
            speed = s.getValue();

        }
    }

    //Add mouse listeners to the buttons in the control panel
    public void addListeners() {
        controlPanel.addMListerner(this);
        controlPanel.addSListerner(this);
        buttons = controlPanel.getButtons();
        sliders = controlPanel.getSliders();
    }

    //If the user has clicked a button
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        int index = 0; //The index of the button the user pressed
        if(buttons.contains(mouseEvent.getSource())) {
            index = buttons.indexOf(mouseEvent.getSource());
            if(buttons.get(index).getId() == "Generate" && !logic.getIsRunning()) {
                logic.setController(this);
                setCompletion(0);
                cell = mazePanel.getCell();
                logic.setControlSpeed(speed);
                logic.generateMaze(cell, rows, cols);
                logic.setBeginningAndEnd(cell, rows, cols);
                hasGenerated = true;
            } else if(buttons.get(index).getId() == "Start/Pause") {
                //If the maze is not animating
                if(!logic.getIsRunning()) {
                    if(hasGenerated && !stopped) {
                        logic.setControlSpeed(speed);
                        logic.solver(cell);
                    } else {
                        logic.setIsRunning(true);
                        logic.getTimer().start();
                        stopped = false;
                    }
                //If the maze is animating
                } else if(logic.getIsRunning()) {
                    logic.setIsRunning(false);
                    stopped = true;
                    logic.getTimer().stop();
                }
                logic.setIsRunning(false);
                stopped = false;
                hasGenerated = false;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {}
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}
    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        if(sliders.contains(changeEvent.getSource())) {
            int index = sliders.indexOf(changeEvent.getSource());
            JSlider s = sliders.get(index);
            if(index == 0) {
                speed = s.getValue();
                //logic.setControlSpeed(speed);
                if(logic.getTimer() != null) {
                    if(speed == 1) {
                        logic.getTimer().setDelay(1000);
                    } else if(speed == 2) {
                        logic.getTimer().setDelay(400);
                    } else if(speed == 3) {
                        logic.getTimer().setDelay(200);
                    } else if(speed == 4) {
                        logic.getTimer().setDelay(60);
                    } else if(speed == 5) {
                        logic.getTimer().setDelay(0);
                    }
                }
            } else if (index == 1) {
                rows = s.getValue();
            } else if (index == 2) {
                cols = s.getValue();
            }
        }
    }

    /**
     * Set the JLabel that represents the visited cells
     */
    public void setCompletion() {
        float percent;
        //If the animation is running
        if(logic.getIsRunning()) {
            percent = logic.getPercentDone();
        } else {
            logic.setPercentDone(0);
            logic.setControlPanelVisited();
            percent = logic.getPercentDone();
        }
        percent = percent / ((float)rows*(float)cols);
        percent = percent * 100;
        String.format("%.2f", percent);
        controlPanel.setPercentDone(percent);
    }

    //Set the JLabel that represents the visited cells to what the controller sets it to which is 0
    public void setCompletion(int zero) {
        float percent = zero;
        percent = percent / ((float)rows*(float)cols);
        percent = percent * 100;
        String.format("%.2f", percent);
        controlPanel.setPercentDone(percent);
    }
}
