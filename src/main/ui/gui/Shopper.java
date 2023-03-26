package ui.gui;

import model.Person;
import model.Warehouse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Shopper extends JFrame implements ActionListener {
    private JPanel panel;
    private JFrame frame;
    private JLabel label;
    private JButton button;
    private Warehouse warehouse;
    private Person user;


    public Shopper(Warehouse warehouse, Person user) {
        this.warehouse = warehouse;
        this.user = user;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
