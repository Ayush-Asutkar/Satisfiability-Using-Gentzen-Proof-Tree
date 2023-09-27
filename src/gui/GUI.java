package gui;

import constants.StringOperators;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GUI {
    private final JFrame frame;
    private final Map<StringOperators, JButton> mapOfStringOperatorButton;
    private JTextField inputTextField;

    public GUI(String message) {
        this.frame = new JFrame(message);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(1500, 1000);
        this.mapOfStringOperatorButton = new HashMap<StringOperators, JButton>();
    }

    public void addTextField(Rectangle rectangle) {
        this.inputTextField = new JTextField();
        this.inputTextField.setBounds(rectangle);

        this.frame.add(this.inputTextField);
    }

    public void addOperatorButton(String message, Rectangle rectangle, StringOperators stringOperators) {
        JButton button = new JButton(message);
        button.setBounds(rectangle);

        button.addActionListener(e -> inputTextField.setText(inputTextField.getText() + stringOperators));

        this.frame.add(button);
        this.mapOfStringOperatorButton.put(stringOperators, button);
    }

    public void runTheGUI() {
        this.frame.setVisible(true);
    }

    public static void main(String[] args) {
    }
}
