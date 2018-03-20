/**
 * The Logic object that handles running algorithms
 */
package com.Christopher_Coleman;

import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Logic {

    private int startingRow; //The random row to start generating the maze
    private int startingCol; //The random column to start generating the maze
    private int maxRows; //The max size of the rows of the maze being generated
    private int maxCols; //The max size of the columns of the maze being generated
    private LinkedList<MazeCell> currentQueue; //The linked list which represents the queue
    private MazeCell[][] maze; //A reference to the maze
    private Timer timer; //A timer to animated the final path
    private float percentDone; //The amount of maze cells the algorithm must traverse
    private boolean timerLoop; //A boolean that determines if the timer has been triggered or not
    private boolean isRunning; //A boolean that tells the controller if the solver is running
    private int adjacentQueueIndex;
    private int index; //The index of the currentQueue to paint them green
    private int controlSpeed; //A case variable for if-else statement to determine timerLoop
    private int time; //The amount of time for the timer to wait
    private LinkedList<MazeCell> adjacentQueue;
    private Controller controller;

    /**
     * Initializing variables in Constructor
     */
    public Logic() {
        time = 5;
        controlSpeed = 0;
        index = 0;
        adjacentQueueIndex = 0;
        timerLoop = true;
        isRunning = false;
        percentDone = 0;
        maze = null;
        currentQueue = new LinkedList();
        adjacentQueue = new LinkedList();
        startingRow = 2;
        startingCol = 3;
        maxRows = 50;
        maxCols = 50;
    }

    /** generateMaze allows for the creation of the maze based on the description provided by Dr.Seales in the requirements
     *
     * @param cells The maze being passed through as a 2D MazeCell array
     * @param rows The amount of rows to set maxRows to
     * @param cols The amount of columns to set maxCols to
     */
    public void generateMaze(MazeCell cells[][], int rows, int cols) {
        maze = cells;
        resetCells(cells, rows, cols);

        //Set all MazeCells that aren't the beginning and end to black
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                cells[i][j].setRow(i);
                cells[i][j].setCol(j);
                cells[i][j].setWall(true); //Set all walls to true drawing a square
                if(!cells[i][j].getIsEnd() && !cells[i][j].getIsBeginning()) {
                    cells[i][j].paint(Color.BLACK);
                }
            }
        }

        //Clear out all MazeCells that are not in the rows and columns provided by user
        if(rows < 50) {
            for(int i = rows; i < 50; i++) {
                for(int j = 0; j < 50; j++) {
                    cells[i][j].setWall(true);
                    cells[i][j].paint(Color.WHITE);
                }
            }
        }

        if(cols < 50) {
            for(int i = 0; i < 50; i++) {
                for(int j = cols; j < 50; j++) {
                    cells[i][j].setWall(true);
                    cells[i][j].paint(Color.WHITE);
                }
            }
        }

        maxRows = rows;
        maxCols = cols;

        //Randomly select a MazeCell to start the generation from
        Random rand = new Random();
        int randomRow = rand.nextInt(rows);
        int randomCol = rand.nextInt(cols);
        if(randomRow >= rows-1 || randomRow <= 0) { randomRow = rows/2; }
            startingRow = randomRow;
        if(randomCol >= cols-1 || randomCol <= 0) { randomCol = cols/2; }
            startingCol = randomCol;
        //Use first depth search algorithm to create maze
        firstDepth(cells, startingRow, startingCol);
    }

    /** This algorithm was based on the algorithm provided http://www.algosome.com/articles/maze-generation-depth-first.html
     *
     * 1. Randomly select a node (or cell) N.
     * 2. Push the node N onto a queue Q.
     * 3. Mark the cell N as visited.
     * 4. Randomly select an adjacent cell A of node N that has not been visited. If all the neighbors of N have been visited:
     * 4a. Continue to pop items off the queue Q until a node is encountered with at least one non-visited neighbor - assign this node to N and go to step 4.
     * 4b. If no nodes exist: stop.
     * 5. Break the wall between N and A.
     * 6. Assign the value A to N.
     * 7. Go to step 2.
     *
     * @param cell The maze being passed through as a 2D MazeCell array
     * @param row The row to start from
     * @param col The column to start from
     */
    public void firstDepth(MazeCell cell[][], int row, int col) {

        MazeCell currentAdjacent = new MazeCell();
        MazeCell currentCell;

        //1. Randomly select a node (or cell) N.
        currentCell = cell[row][col];

        //Get the adjacent MazeCells next to the start
        ArrayList<MazeCell> currentAdjacentList = checkAdjacent(currentCell);

        //2. Push the node N onto a queue Q.
        currentQueue.push(currentCell);
        do {
            //Add initial random cell
            currentQueue.push(currentCell);

            //3. Mark the cell N as visited.
            currentCell.setIsVisited(true);

            //4. Randomly select an adjacent cell A of node N that has not been visited. If all the neighbors of N have been visited:
            Collections.shuffle(currentAdjacentList);

            int nullAdjacents = 0; //Condition that is not 0 when there is an adjacent MazeCell to visit

            //While there are no adjacent cells to traverse to
            while(nullAdjacents == 0) {

                for (int i = 0; i < currentAdjacentList.size(); i++) {
                    //Increment nullAdjacents so if it is four we know there are none adjacent
                    if (currentAdjacentList.get(i) == null) {
                        nullAdjacents++;
                    } else {
                        //If there is a cell adjacent to travel to set the current adjacent cell to that cell
                        currentAdjacent = currentAdjacentList.get(i);
                        nullAdjacents = 1; //Set nullAdjacents to a value that will break the loop
                    }
                }
                //4a. Continue to pop items off the queue Q until a node is encountered with at least
                // one non-visited neighbor - assign this node to N and go to step 4.
                if (nullAdjacents >= 4) {
                    if (!currentQueue.isEmpty()) {
                        currentCell = currentQueue.pop();
                        if(currentCell == null)
                            break;
                        row = currentCell.getRow();
                        col = currentCell.getCol();
                        currentAdjacentList = checkAdjacent(currentCell);
                        nullAdjacents = 0;
                    }
                }
            }

            //5. Break the wall between N and A.
            if (currentAdjacent.getDirection() == 'N') {
                if(row-1 < 0) {break;}
                currentAdjacent.setWallValue(false, 2);
                currentCell.setWallValue(false, 0);
                row -= 1;
                currentCell = cell[row][col];
                currentAdjacentList = checkAdjacent(currentCell);
            } else if (currentAdjacent.getDirection() == 'E') {
                currentAdjacent.setWallValue(false, 3);
                currentCell.setWallValue(false, 1);
                col += 1;
                currentCell = cell[row][col];
                currentAdjacentList = checkAdjacent(currentCell);
            } else if (currentAdjacent.getDirection() == 'S') {
                currentAdjacent.setWallValue(false, 0);
                currentCell.setWallValue(false, 2);
                row += 1;
                currentCell = cell[row][col];
                currentAdjacentList = checkAdjacent(currentCell);
            } else if (currentAdjacent.getDirection() == 'W') {
                if(col-1 < 0) {break;}
                currentAdjacent.setWallValue(false, 1);
                currentCell.setWallValue(false, 3);
                col -= 1;
                currentCell = cell[row][col];
                currentAdjacentList = checkAdjacent(currentCell);
            }
            //7. Go to step 2.
        } while (!currentQueue.isEmpty()); //4b. If no nodes exist: stop.
    }

    /** setBeginningAndEnd sets the color of the beginning and end to their corresponding colors
     *
     * @param cells the maze
     * @param rows the size of rows of the maze
     * @param cols the size of columns of the maze
     */
    public void setBeginningAndEnd(MazeCell cells[][], int rows, int cols) {
        cells[0][0].paint(Color.GREEN);
        cells[0][0].setFillColor(Color.GREEN);
        cells[0][0].setIsBeginning(true);
        cells[maxRows-1][maxCols-1].paint(Color.RED);
        cells[maxRows-1][maxCols-1].setFillColor(Color.RED);
        cells[maxRows-1][maxCols-1].setIsEnd(true);
    }

    /**checkAdjacent is a function that determines the adjacent cells around a specific MazeCell
     * and even if there is an adjacent cell but it has been visited it
     * is treated like it doesn't exist
     *
     * @param cell The specific MazeCell that it is checking the adjacent MazeCells for
     * @return A ArrayList of MazeCells that contain either the cell that is adjacent or null
     */
    public ArrayList<MazeCell> checkAdjacent(MazeCell cell) {
        boolean northCellVisited = checkNorth(cell);
        boolean eastCellVisited = checkEast(cell);
        boolean southCellVisited = checkSouth(cell);
        boolean westCellVisited = checkWest(cell);

        ArrayList<MazeCell> adjacent = new ArrayList<>(); //Create a new adjacency list for every cell

        int row = cell.getRow();
        int col = cell.getCol();

        /**
         * If the cell hasn't been visited and is in bounds,
         *  set that cells direction to the current cell,
         *  and add that cell to the adjacency list
         * Else return null
         */
        if(!northCellVisited) { maze[row-1][col].setDirection('N'); adjacent.add(maze[row-1][col]);}
        else {adjacent.add(null);}

        if(!eastCellVisited) {maze[row][col+1].setDirection('E'); adjacent.add(maze[row][col+1]);}
        else {adjacent.add(null);}

        if(!southCellVisited) {maze[row+1][col].setDirection('S'); adjacent.add(maze[row+1][col]);}
        else {adjacent.add(null);}

        if(!westCellVisited) { maze[row][col-1].setDirection('W'); adjacent.add(maze[row][col-1]);}
        else {adjacent.add(null);}

        return adjacent;
    }

    /**
     * @param cell The current cell to check north of
     * @return false if the cell is in the bounds and hasn't been visited else return true
     */
    public boolean checkNorth(MazeCell cell) {
        int row = cell.getRow();
        int col = cell.getCol();
        if (row - 1 >= 0 && row-1 < maxRows) {
            return maze[row - 1][col].getIsVisited();
        }
        return true;
    }

    /**
     * @param cell The current cell to check north of
     * @return false if the cell is in the bounds and hasn't been visited else return true
     */
    public boolean checkEast(MazeCell cell) {
        int row = cell.getRow();
        int col = cell.getCol();
        if(col+1 < maxCols && col+1 < 50) {
            return maze[row][col+1].getIsVisited();
        }
        return true;
    }

    /**
     * @param cell The current cell to check north of
     * @return false if the cell is in the bounds and hasn't been visited else return true
     */
    public boolean checkSouth(MazeCell cell) {
        int row = cell.getRow();
        int col = cell.getCol();
        if(row+1 < maxRows && row+1 < 50) {
            return maze[row+1][col].getIsVisited();
        }
        return true;
    }

    /**
     * @param cell The current cell to check north of
     * @return false if the cell is in the bounds and hasn't been visited else return true
     */
    public boolean checkWest(MazeCell cell) {
        int row = cell.getRow();
        int col = cell.getCol();
        if(col-1 >= 0 && col-1 < maxCols) {
            return maze[row][col-1].getIsVisited();
        }
        return true;
    }

    /**resetCells resets most of the variables of the MazeCells
     *
     * @param cells The maze
     * @param row The max rows to set the color of the beginning and end
     * @param col The max columns to set the color of the beginning and end
     */
    public void resetCells(MazeCell cells[][], int row, int col) {
        for(int i = 0; i < 50; i++)
            for(int j = 0; j < 50; j++) {
                cells[i][j].setIsVisited(false);
                cells[i][j].setWall(true);
                cells[i][j].setFillColor(Color.WHITE);
                cells[i][j].setIsEnd(false);
                cells[i][j].setIsBeginning(false);
                if(i == 0 && j == 0) {
                    cells[i][j].paint(Color.GREEN);
                } else if(i == row -1 && j == col-1) {
                    cells[i][j].paint(Color.RED);
                }
                else {
                    cells[i][j].paint(Color.BLACK);
                }
            }
        index = 0;
        timerLoop = true;
        percentDone = 0;
        isRunning = false;
        adjacentQueue.clear();
    }

    /**
     * 1. Select top left node (or cell) N.
     * 2. Push the node N onto a queue Q.
     * 3. Mark the cell N as visited.
     * 4. Randomly select an adjacent cell A of node N that has not been visited. If all the neighbors of N have been visited:
     * 4a. Continue to pop items off the queue Q until a node is encountered with at least one non-visited neighbor - assign this node to N and go to step 4.
     * 4b. If no nodes exist: stop.
     * 5. Assign the value A to N.
     * 6. Go to step 2.
     *
     * @param cell The maze
     */
    public void solver(MazeCell cell[][]) {
        MazeCell currentAdjacent = new MazeCell();
        MazeCell currentCell;
        int row = 0;
        int col = 0;
        timerLoop = true;

        //Clear the queue
        currentQueue.clear();

        //Reset all cells isVistied to false
        for (int i = 0; i < maxRows; i++)
            for (int j = 0; j < maxCols; j++)
                cell[i][j].setIsVisited(false);

        // Select top left node (or cell) N.
        currentCell = cell[row][col];
        ArrayList<MazeCell> currentAdjacentList = checkAdjacent(currentCell);

        //2. Push the node N onto a queue Q.
        currentQueue.push(currentCell);
        do {
            //Add initial random cell
            currentQueue.push(currentCell);

            //3. Mark the cell N as visited.
            currentCell.setIsVisited(true);

            if (currentCell.getIsEnd()) {
                break;
            }

            int nullAdjacents = 0;

            //While there are no adjacent cells to traverse to
            while (nullAdjacents == 0) {

                for (int i = 0; i < currentAdjacentList.size(); i++) {
                    //Increment nullAdjacents so if it is four we know there are none adjacent
                    if (currentAdjacentList.get(i) == null) {
                        nullAdjacents++;
                    } else {
                        currentAdjacent = currentAdjacentList.get(i);
                        //This algorithm prefers the left wall
                        //5. Assign the value A to N.
                        if (i == 2) {
                            if (!currentAdjacent.getNorthWall()) {
                                currentCell = currentAdjacentList.get(i);
                                nullAdjacents = 1;
                                break;
                            } else
                                nullAdjacents++;

                        } else if (i == 1) {
                        if (!currentAdjacent.getWestWall()) {
                            currentCell = currentAdjacentList.get(i);
                            nullAdjacents = 1;
                            break;
                        } else
                            nullAdjacents++;

                        } else if (i == 0) {
                            if (!currentAdjacent.getSouthWall()) {
                                currentCell = currentAdjacentList.get(i);
                                nullAdjacents = 1;
                                break;
                            } else
                                nullAdjacents++;
                        } else if (i == 3) {
                            if (!currentAdjacent.getEastWall()) {
                                currentCell = currentAdjacentList.get(i);
                                nullAdjacents = 1;
                                break;
                            } else
                                nullAdjacents++;
                        }
                    }
                }
                /**
                 * 4a. Continue to pop items off the queue Q until a node is encountered with at least
                 * one non-visited neighbor - assign this node to N and go to step 4.
                 * */
                if (nullAdjacents >= 4) {
                    if (!currentQueue.isEmpty()) {
                        currentCell = currentQueue.pop();
                        if (currentCell == null)
                            break;
                        currentAdjacentList = checkAdjacent(currentCell);
                        nullAdjacents = 0;
                    }
                }
            }

            adjacentQueue.push(currentCell);
            //Set every cell traversed gray
            //currentCell.setFillColor(Color.GRAY);

            //Get new adjacent list
            currentAdjacentList = checkAdjacent(currentCell);

            //6. Go to step 2.
        } while (!currentQueue.isEmpty()); //4b. If no nodes exist: stop.

        //Set index to the last item in the queue or the starting maze cell
        index = currentQueue.size()-1;
        adjacentQueueIndex = adjacentQueue.size() - 1;

        //Create a timer to animate the final path to the end
        timer = new Timer(time, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                isRunning = true;
                if (adjacentQueueIndex >= 0 && adjacentQueue.size() > 0) {
                    adjacentQueue.get(adjacentQueueIndex).setFillColor(Color.GRAY);
                    adjacentQueueIndex--;
                    setTimerLoop(false);
                }

                //If the index is in the bounds
                else if(index >= 0 && currentQueue.size() > 0) {
                    //Set that cell to green
                    currentQueue.get(index).setFillColor(Color.decode("#a3c3ed"));
                    index--;
                    setTimerLoop(false);
                }
                else {
                    isRunning = false;
                    setControlPanelVisited();
                    if(controller != null) {controller.setCompletion();}
                    timer.stop();
                }
            }
        });



        if(timerLoop) {
            timer.start();
        }

        isRunning = false;
    }

    /**
     * setControlPanelVisited calculates the amount of cells the first depth search algorithm had to visit
     */
    public void setControlPanelVisited() {
        for (int i = 0; i < maxRows; i++) {
            for (int j = 0; j < maxCols; j++)
                if (maze[i][j].getFillColor() != Color.WHITE)
                    percentDone++;
        }
        setBeginningAndEnd(maze, maxRows, maxCols);
    }

    /**setTimerLoop  set timerLoop to boolean being passed through
     *
     * @param timerLoop
     */
    public void setTimerLoop(boolean timerLoop) { this.timerLoop = timerLoop;}

    /**setPercentDone set percentDone to int being passed through
     *
     * @param percentDone
     */
    public void setPercentDone(int percentDone) {this.percentDone = percentDone;}

    /**
     * @return percentDone
     */
    public float getPercentDone() {
        return percentDone;
    }

    /**setControl Speed sets the time the timer waits to trigger on the speed chosen by the user using the speed slider
     *
     * @param controlSpeed the integer picked by user from the speed slider
     */
    public void setControlSpeed(int controlSpeed) {
        this.controlSpeed = controlSpeed;
        if(controlSpeed == 1) {
            time = 1000;
        } else if(controlSpeed == 2) {
            time = 400;
        } else if(controlSpeed == 3) {
            time = 200;
        } else if(controlSpeed == 4) {
            time = 60;
        } else if(controlSpeed == 5) {
            time = 0;
        }
    }

    /**setController sets controller to a controller being passed through to allow the Visited label in control panel
     *
     * @param controller
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * @return isRunning, true if the animation timers are running else false
     */
    public boolean getIsRunning() { return isRunning; }

    public void setIsRunning(boolean isRunning) {this.isRunning = isRunning;}

    public Timer getTimer() { return timer; }

}
