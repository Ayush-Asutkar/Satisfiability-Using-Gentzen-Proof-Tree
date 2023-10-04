package gui;

import constants.StringOperators;
import model.GentzenTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GUI {
    private static JButton createButton(String message, String operator, Rectangle rectangle, JTextField textField) {
        JButton button = new JButton(message);
        button.setBounds(rectangle);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText(textField.getText() + operator + " ");
            }
        });

        return button;
    }

    private static JButton createClearButton(Rectangle rectangle, JTextField textField) {
        JButton button = new JButton("Clear Text");
        button.setBounds(rectangle);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText("");
            }
        });

        return button;
    }

    private static JButton createGentzenTreeEvaluationButton(Rectangle rectangle, JTextField textField, JTextArea message) {
        JButton button = new JButton("Evaluate Gentzen Tree");
        button.setBounds(rectangle);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String expression = textField.getText();
                String[] expressionSplit = expression.split(" ");
                List<String> expressionList = new ArrayList<>();

                for(String in: expressionSplit) {
                    if(in != null  &&  !in.isEmpty()) {
                        expressionList.add(in);
                    }
                }
                System.out.println(expressionList);

                GentzenTree gentzenTree = new GentzenTree(expressionList);
                gentzenTree.applyAlgorithmForGentzenSystemCreateTree();
                gentzenTree.printTreeToJTextArea(message);
            }
        });

        return button;
    }

    public static void main(String[] args) {
        JFrame frame=new JFrame("Gentzen Tree Proof system");
        frame.setSize(1500,1000);

        JLabel inputLabel = new JLabel("Input Expression (Space separated):");
        inputLabel.setBounds(50, 50, 250, 30);

        final JTextField textField = new JTextField();
        textField.setBounds(300, 50, 1200, 30);

        JLabel buttonLabel = new JLabel("Button for adding operators:");
        buttonLabel.setBounds(50, 100, 200, 30);

        JButton negationButton = createButton("Negation", StringOperators.NEGATION,
                new Rectangle(300, 100, 100, 30), textField);
        JButton andButton = createButton("And", StringOperators.AND,
                new Rectangle(450, 100, 100, 30), textField);
        JButton orButton = createButton("Or", StringOperators.OR,
                new Rectangle(600, 100, 100, 30), textField);
        JButton impliesButton = createButton("Implication", StringOperators.IMPLICATION,
                new Rectangle(750, 100, 100, 30), textField);
        JButton doubleImpliesButton = createButton("Double Implication", StringOperators.DOUBLE_IMPLICATION,
                new Rectangle(900, 100, 150, 30), textField);


        JButton clearButton = createClearButton(new Rectangle(200, 200, 100, 30), textField);


        JTextArea message = new JTextArea();
        message.setWrapStyleWord(true);
        message.setLineWrap(true);
        message.setEditable(false);
        message.setFocusable(false);
        message.setOpaque(false);


        JScrollPane scrollPane =  new JScrollPane(message,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scrollPane.setBounds(50, 300, 1000, 600);


        JButton evaluateGentzenTree = createGentzenTreeEvaluationButton(new Rectangle(400, 200, 200, 30), textField, message);

        frame.add(inputLabel);
        frame.add(buttonLabel);
        frame.add(scrollPane);

        frame.add(textField);
        frame.add(negationButton);
        frame.add(andButton);
        frame.add(orButton);
        frame.add(impliesButton);
        frame.add(doubleImpliesButton);
        frame.add(clearButton);
        frame.add(evaluateGentzenTree);

        frame.setLayout(null);
        frame.setVisible(true);
    }
}
