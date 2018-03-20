package com.Christopher_Coleman;

import javax.swing.*;

//This is an extend JButton class to allow adding id's to buttons and have a getter for those id's
public class Button extends JButton {

    String id;

    public Button(String name) {
        setText(name);
        id = name;
        setOpaque(false);
    }

    public String getId() {
        return id;
    }
}
