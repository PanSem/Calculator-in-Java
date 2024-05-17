import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Calculator implements ActionListener {

    JFrame frame; //main window

    JTextField textField; // text field where results and user input will be displayed

    JButton[] numberButtons = new JButton[10]; // buttons represent 0 - 9
    JButton[] mathButtons = new JButton[8]; // button represent +-*/.=⌫C
    String[] mathSymbols = {"+", "-", "*", "/", ".", "=", "⌫", "C"};
    JPanel panel;

    // Numbers which will hold the input of the user and the final result
    double firstNumber = Double.POSITIVE_INFINITY;
    double secondNumber = 0;
    double res = 0;
    // Holds the operation that is going to happen
    String operator = "";

    Calculator() {

        // Specify the main window
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 550);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);

        // Specify the text field
        setUpTextField();

        // Specify the math symbols buttons
        setUpMathButtons();

        // Specify the number buttons
        setUpNumberButtons();

        // Specify the width and height for the Delete and Clear buttons
        // and position them within the window.
        setUpDeleteAndClear();

        //Set the grid of numbers
        setUpPanel();

        // Add all the individual components to the main window
        frame.add(panel); // Grid with all the numbers and math symbols
        frame.add(mathButtons[6]); // Delete button
        frame.add(mathButtons[7]); // Clear button
        frame.add(textField); // Text field where the numbers appear
        frame.setVisible(true);
    }

    private void setUpTextField() {

        textField = new JTextField();
        textField.setBounds(50, 25, 300, 50);
        textField.setEditable(false);
    }

    private void setUpMathButtons() {

        for(int i = 0; i < mathButtons.length; i++) {
            mathButtons[i] = new JButton(mathSymbols[i]);
            mathButtons[i].addActionListener(this);
            mathButtons[i].setFocusable(false);
        }
    }

    private void setUpNumberButtons() {

        for(int i = 0; i < numberButtons.length; i++) {

            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            numberButtons[i].setFocusable(false);
        }
    }

    private void setUpDeleteAndClear() {

        mathButtons[6].setBounds(50, 430, 145, 50);
        mathButtons[7].setBounds(205, 430, 145, 50);
    }

    private void setUpPanel() {

        //General set up for the grid
        panel = new JPanel();
        panel.setBounds(50, 100, 300, 300);
        panel.setLayout(new GridLayout(4, 4, 10, 10));

        // 1 2 3 + <- 1st row
        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(mathButtons[0]);

        // 4 5 6 - <- 2nd row
        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(mathButtons[1]);

        // 7 8 9 * <- 3rd row
        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(mathButtons[2]);

        // . 0 / = <- 4th row
        panel.add(mathButtons[4]);
        panel.add(numberButtons[0]);
        panel.add(mathButtons[3]);
        panel.add(mathButtons[5]);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Retrieve the value of the clicked button
        String value = getButtonValue(e);

        // Verify whether the math symbol button was pressed when there was no data in the text field
        if(mathSymbolPressedWhenNoData(value)) return;

        // Check if . (dot) button was pressed and not allow second . (dot)
        // ex. 19.1 -> correct 19..2 -> false
        if(value.equals(".") && !textField.getText().contains(".")) {
            textField.setText(textField.getText().concat("."));
            return;
        }

        // Check whether any of the +-*/= buttons was pressed and perform the corresponding operation
        if("+-*/=".contains(value)) {

            // Verify whether the input is occurring for the first time, and if so, store it in the variable firstNumber
            // also store the operator (+-*/=)
            if(firstNumber == Double.POSITIVE_INFINITY && !value.equals("=")) {
                firstNumber = Double.parseDouble(textField.getText());
                textField.setText("");
                operator = value;
                return;
            }

            // If firstNumber is set, store the input in the secondNumber
            secondNumber = Double.parseDouble(textField.getText());

            // Perform the appropriate operation according to operator's value
            switch(operator) {
                case "+" -> res = firstNumber + secondNumber;
                case "-" -> res = firstNumber - secondNumber;
                case "*" -> res = firstNumber * secondNumber;
                case "/" -> {

                    if(secondNumber == 0) return;
                    res=firstNumber / secondNumber;
                }
            }

            // Display the result on the text field when the user presses the = button
            if(value.equals("=")) {

                if(firstNumber == Double.POSITIVE_INFINITY) return;

                textField.setText(String.valueOf(res));
                firstNumber = Double.POSITIVE_INFINITY;
                operator = "";
                return;
            }

            // Delete the whole text field if the operator is one of +-*/
            // and store res in firstNumber
            operator = value;
            textField.setText("");
            firstNumber = res;
            return;
        }

        // Delete everything on text field
        if(value.equals("C")) {

            textField.setText("");
            res = 0;
            firstNumber = Double.POSITIVE_INFINITY;
            return;
        }

        // Delete only the last number on the text field
        if(value.equals("⌫")) {

            String string = textField.getText();
            textField.setText("");
            textField.setText(string.substring(0, string.length() - 1));
            return;
        }

        // Check whether any of the +-*/= buttons was pressed and display it on the screen
        if(Integer.parseInt(value) >= 0 || Integer.parseInt(value) <= 9)
            textField.setText(textField.getText().concat(value));
    }

    private String getButtonValue(ActionEvent e) {

        JButton b = (JButton) e.getSource();
        return b.getText();
    }

    private boolean mathSymbolPressedWhenNoData(String value) {

        return textField.getText().isEmpty() && Arrays.asList(mathSymbols).contains(value);
    }
}
