import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BMICalculator extends JFrame implements ActionListener {

    JTextField weightField, heightField;
    JLabel resultLabel;
    JButton calculateButton;

    BMICalculator() {

        setTitle("BMI Calculator");
        setSize(350,250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel weightLabel = new JLabel("Enter Weight (kg):");
        weightField = new JTextField(10);

        JLabel heightLabel = new JLabel("Enter Height (m):");
        heightField = new JTextField(10);

        calculateButton = new JButton("Calculate BMI");

        resultLabel = new JLabel("Your BMI: ");

        calculateButton.addActionListener(this);

        add(weightLabel);
        add(weightField);
        add(heightLabel);
        add(heightField);
        add(calculateButton);
        add(resultLabel);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        double weight = Double.parseDouble(weightField.getText());
        double height = Double.parseDouble(heightField.getText());

        double bmi = weight / (height * height);

        String category;

        if(bmi < 18.5)
            category = "Underweight";
        else if(bmi < 25)
            category = "Normal";
        else if(bmi < 30)
            category = "Overweight";
        else
            category = "Obese";

        resultLabel.setText("Your BMI: " + String.format("%.2f", bmi) + " (" + category + ")");
    }

    public static void main(String[] args) {
        new BMICalculator();
    }
}