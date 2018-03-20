package com.Christopher_Coleman;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class ControlPanel extends JPanel {

    private final int MAXSPEED = 5; //The maximum value of the speed slider
    private final int MINSPEED = 1; //The minimum value of the speed slider
    private final int INITSPEED = 5; //The initial value of the speed slider

    private final int MAZEMAX = 50; //The maximum value of the speed slider
    private final int MAZEMIN = 10; //The minimum value of the speed slider
    private final int MAZEINIT = 50; //The initial value of the speed slider

    private JSlider speed; //The speed slider
    private JSlider rows; //The row slider allowing the user to customize the maze
    private JSlider columns; //The columns slider allowing the user to customize the maze

    private JLabel percentDone; //The amount of the maze that had to be traversed to the end

    private ArrayList<Button> buttons; //A list to hold the generate button and the start button
    private ArrayList<JSlider> sliders; //A list to hold the sliders for speed, rows, and columns
    private Button solve; //The start button
    private Button generate; //The generate button

    private Hashtable speedLabel;
    private Hashtable columnTable;
    private Hashtable rowTable;
    public ControlPanel() {

        setLayout(new GridLayout(6, 1));

        //Initialize variables
        buttons = new ArrayList<>();
        sliders = new ArrayList<>();
        speedLabel = new Hashtable();
        solve = new Button("Start/Pause");
        generate = new Button("Generate");
        percentDone = new JLabel("Visited: 0%");
        percentDone.setForeground(Color.WHITE);

        //Speed Slider
        speed = new JSlider(JSlider.HORIZONTAL, MINSPEED, MAXSPEED, INITSPEED);

        speed.setMajorTickSpacing(1);
        speed.setPaintTicks(true);

        speedLabel.put(MINSPEED, new JLabel("Slow"));
        speedLabel.put(MAXSPEED, new JLabel("Fast"));

        speed.setLabelTable(speedLabel);
        speed.setPaintLabels(true);
        sliders.add(speed);
        //End Speed Slider

        rowTable = new Hashtable();

        //Row Slider
        rows = new JSlider(JSlider.HORIZONTAL, MAZEMIN, MAZEMAX, MAZEINIT);

        rows.setMajorTickSpacing(5);
        rows.setPaintTicks(true);

        rowTable.put(MAZEMIN, new JLabel("Row: 10"));
        rowTable.put(MAZEMAX, new JLabel("Row: 50"));

        rows.setLabelTable(rowTable);
        rows.setPaintLabels(true);
        sliders.add(rows);
        //End Row

        columnTable = new Hashtable();

        //Columns Slider
        columns = new JSlider(JSlider.HORIZONTAL, MAZEMIN, MAZEMAX, MAZEINIT);

        columns.setMajorTickSpacing(5);
        columns.setPaintTicks(true);

        columnTable.put(MAZEMIN, new JLabel("Column: 10"));
        columnTable.put(MAZEMAX, new JLabel("Column: 50"));

        columns.setLabelTable(columnTable);
        columns.setPaintLabels(true);
        sliders.add(columns);
        //End Columns

        buttons.add(solve);
        buttons.add(generate);

        add(generate);
        add(solve);
        add(speed);
        add(rows);
        add(columns);
        add(percentDone);

        setBackground(Color.BLACK);
        setSize(500, 600);
        setPreferredSize(new Dimension(500, 600));
    }

    /**addMListener adds mouse listeners to the generate button and the solve button
     *
     * @param controller
     */
    public void addMListerner(Controller controller) {
        generate.addMouseListener(controller);
        solve.addMouseListener(controller);
    }

    public ArrayList<Button> getButtons() {
        return buttons;
    }

    public ArrayList<JSlider> getSliders() {
        return sliders;
    }

    /**addSListener adds change listeners to the rows, columns, and speed sliders
     *
     * @param controller
     */
    public void addSListerner(Controller controller) {
        speed.addChangeListener(controller);
        rows.addChangeListener(controller);
        columns.addChangeListener(controller);
    }

    public void setPercentDone(float percentDone) {this.percentDone.setText("Visited: " + String.valueOf(percentDone) + "%");}

}
