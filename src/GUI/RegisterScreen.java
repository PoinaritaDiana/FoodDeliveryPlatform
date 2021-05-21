package GUI;

import services.Services;

import javax.swing.*;
import java.awt.*;

public class RegisterScreen extends JFrame {
    private Services services = Services.getServicesInstance();

    RegisterScreen() {
        setSize(550, 550);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Registration Form");

        JLabel labelTitle = new JLabel("New user registration:");
        labelTitle.setFont(new Font("Ariel", Font.BOLD, 20));
        labelTitle.setBounds(150, 20, 400, 30);
        add(labelTitle);

        JLabel labelLastName = new JLabel("Last Name:");
        labelLastName.setBounds(80, 120, 200, 30);
        add(labelLastName);

        JLabel labelFirstName = new JLabel("First Name:");
        labelFirstName.setBounds(80, 160, 200, 30);
        add(labelFirstName);

        JLabel labelPhoneNumber = new JLabel("Phone number:");
        labelPhoneNumber.setBounds(80, 200, 200, 30);
        add(labelPhoneNumber);

        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(80, 240, 200, 30);
        add(labelEmail);

        JLabel labelUsername = new JLabel("Username:");
        labelUsername.setBounds(80, 280, 200, 30);
        add(labelUsername);

        JLabel labelPassword = new JLabel("Password:");
        labelPassword.setBounds(80, 320, 200, 30);
        add(labelPassword);

        JTextField lastNameTextField = new JTextField();
        lastNameTextField.setBounds(220, 120, 200, 30);
        add(lastNameTextField);

        JTextField firstNameTextField = new JTextField();
        firstNameTextField.setBounds(220, 160, 200, 30);
        add(firstNameTextField);

        JTextField emailTextField = new JTextField();
        emailTextField.setBounds(220, 200, 200, 30);
        add(emailTextField);

        JTextField phoneTextField = new JTextField();
        phoneTextField.setBounds(220, 240, 200, 30);
        add(phoneTextField);

        JTextField usernameTextField = new JTextField();
        usernameTextField.setBounds(220, 280, 200, 30);
        add(usernameTextField);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(220, 320, 200, 30);
        add(passwordField);

        JButton submitButton = new JButton("ADD ACCOUNT");
        submitButton.addActionListener(e -> {
            String[] userData =  new String[6];
            int countValidInput = 0;
            String field = lastNameTextField.getText();
            if(!(field.isEmpty() || field.isBlank() || field==null)) {
                userData[0] = field;
                countValidInput++;
            }
            field = firstNameTextField.getText();
            if(!(field.isEmpty() || field.isBlank() || field==null)) {
                userData[1] = field;
                countValidInput++;
            }
            field = phoneTextField.getText();
            if(!(field.isEmpty() || field.isBlank() || field==null)) {
                userData[2] = field;
                countValidInput++;
            }
            field = emailTextField.getText();
            if(!(field.isEmpty() || field.isBlank() || field==null)) {
                userData[3] = field;
                countValidInput++;
            }
            field = usernameTextField.getText();
            if(!(field.isEmpty() || field.isBlank() || field==null)) {
                userData[4] = field;
                countValidInput++;
            }
            field = String.valueOf(passwordField.getPassword());
            if(!(field.isEmpty() || field.isBlank() || field==null)) {
                userData[5] = field;
                countValidInput++;
            }

            if(countValidInput==6) {
                if (userData[4].contains(" ")) {
                    JOptionPane.showMessageDialog(getRootPane(), "Username must not contain spaces", "Alert", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (userData[5].contains(" ")) {
                        JOptionPane.showMessageDialog(getRootPane(), "Password must not contain spaces", "Alert", JOptionPane.WARNING_MESSAGE);
                    } else {
                        boolean checkRegistration = services.registerCustomer(userData);
                        if (checkRegistration) {
                            JOptionPane.showMessageDialog(getRootPane(), "Successful user registration!");
                            new LoginScreen();
                            dispose();
                        } else
                            JOptionPane.showMessageDialog(getRootPane(), "Username already exists.", "Alert", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
            else
                JOptionPane.showMessageDialog(getRootPane(), "All fields must be completed!", "Alert", JOptionPane.WARNING_MESSAGE);

        });

        submitButton.setBounds(100, 430, 300, 30);
        add(submitButton);

        JLabel alreadyHaveAccount = new JLabel("Already have an account?");
        alreadyHaveAccount.setBounds(100, 470, 200, 30);
        add(alreadyHaveAccount);

        JButton loginButton = new JButton("SIGN IN");
        loginButton.addActionListener(e -> {
            new LoginScreen();
            dispose();
        });

        loginButton.setBounds(305, 470, 100, 30);
        add(loginButton);
    }
}