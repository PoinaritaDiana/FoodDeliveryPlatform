package GUI;
import services.Audit;
import services.Services;
import usersManagement.Customer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AccountScreen extends JFrame{
    private Audit audit = Audit.getAuditInstance();
    private Services services = Services.getServicesInstance();
    private JTextField lastNameTextField,firstNameTextField,emailTextField,usernameTextField,phoneTextField,passwordField;
    private boolean editMode = false;

    AccountScreen() {
        setSize(550, 550);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("My Account");
        setLayout(new BorderLayout());
        add(topPanel(), BorderLayout.NORTH);
        add(infoPanel(), BorderLayout.CENTER);
        add(bottomPanel(), BorderLayout.SOUTH);
    }

    JPanel infoPanel(){
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);

        JLabel labelLastName = new JLabel("Last Name:");
        labelLastName.setBounds(80, 185, 100, 30);
        add(labelLastName);

        JLabel labelFirstName = new JLabel("First Name:");
        labelFirstName.setBounds(80, 160, 200, 30);
        infoPanel.add(labelFirstName);

        JLabel labelPhoneNumber = new JLabel("Phone number:");
        labelPhoneNumber.setBounds(80, 200, 200, 30);
        infoPanel.add(labelPhoneNumber);

        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(80, 240, 200, 30);
        infoPanel.add(labelEmail);

        JLabel labelUsername = new JLabel("Username:");
        labelUsername.setBounds(80, 280, 200, 30);
        infoPanel.add(labelUsername);

        JLabel labelPassword = new JLabel("Password:");
        labelPassword.setBounds(80, 320, 200, 30);
        infoPanel.add(labelPassword);

        String[] data = ((Customer)services.getCurrentUser()).getObjectData();
        lastNameTextField = new JTextField();
        lastNameTextField.setBounds(220, 120, 200, 30);
        lastNameTextField.setText(data[1]);
        lastNameTextField.setEnabled(false);
        infoPanel.add(lastNameTextField);

        firstNameTextField = new JTextField();
        firstNameTextField.setBounds(220, 160, 200, 30);
        firstNameTextField.setText(data[2]);
        firstNameTextField.setEnabled(false);
        infoPanel.add(firstNameTextField);

        phoneTextField = new JTextField();
        phoneTextField.setBounds(220, 240, 200, 30);
        phoneTextField.setText(data[3]);
        phoneTextField.setEnabled(false);
        infoPanel.add(phoneTextField);

        emailTextField = new JTextField();
        emailTextField.setBounds(220, 200, 200, 30);
        emailTextField.setText(data[4]);
        emailTextField.setEnabled(false);
        infoPanel.add(emailTextField);

        usernameTextField = new JTextField();
        usernameTextField.setBounds(220, 280, 200, 30);
        usernameTextField.setText(data[5]);
        usernameTextField.setEnabled(false);
        infoPanel.add(usernameTextField);

        passwordField = new JTextField();
        passwordField.setBounds(220, 320, 200, 30);
        passwordField.setText(data[6]);
        passwordField.setEnabled(false);
        infoPanel.add(passwordField);

        return infoPanel;
    }

    JPanel topPanel(){
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 20));
        JButton backToMainScreen = new JButton("<< Back to Main Page");
        backToMainScreen.addActionListener(e -> {
            dispose();
            MainScreen mainScreen = new MainScreen();
            mainScreen.setVisible(true);
        });
        topPanel.add(backToMainScreen);

        JLabel labelTitle = new JLabel("MY ACCOUNT", JLabel.CENTER);
        labelTitle.setFont(new Font("Arial", Font.BOLD, 20));
        labelTitle.setBorder(new EmptyBorder(0, 60, 0, 0));
        topPanel.add(labelTitle);
        return topPanel;
    }

    JPanel bottomPanel(){
        JPanel bottomPanel = new JPanel();
        GridLayout layout = new GridLayout(1, 2);
        layout.setHgap(5);
        bottomPanel.setLayout(layout);

        JButton editButton = new JButton("EDIT INFO");
        editButton.addActionListener(e -> {
            if(editMode==false) {
                lastNameTextField.setEnabled(true);
                firstNameTextField.setEnabled(true);
                emailTextField.setEnabled(true);
                usernameTextField.setEnabled(true);
                phoneTextField.setEnabled(true);
                passwordField.setEnabled(true);
                editButton.setText("SAVE CHANGES");
                editMode=true;
            }
            else{
                audit.auditService("editAccountInfo");
                String[] customerData = new String[6];
                customerData[0] = lastNameTextField.getText();
                customerData[1] = firstNameTextField.getText();
                customerData[2] = phoneTextField.getText();
                customerData[3] = emailTextField.getText();
                customerData[4] = usernameTextField.getText();;
                customerData[5] = passwordField.getText();

                int editAccount = JOptionPane.showConfirmDialog(getRootPane(),"Are you sure you want to update your info?","EDIT INFO",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if(editAccount == JOptionPane.YES_OPTION) {
                    editButton.setText("EDIT INFO");
                    editMode = false;
                    if(services.editCustomerAccount(customerData)){
                        JOptionPane.showMessageDialog(getRootPane(), "Your account has been successfully updated");
                        dispose();
                        AccountScreen account = new AccountScreen();
                        account.setVisible(true);
                    }
                }
                if(editAccount == JOptionPane.NO_OPTION) {
                    editButton.setText("EDIT INFO");
                    editMode = false;
                    lastNameTextField.setEnabled(false);
                    firstNameTextField.setEnabled(false);
                    emailTextField.setEnabled(false);
                    usernameTextField.setEnabled(false);
                    phoneTextField.setEnabled(false);
                    passwordField.setEnabled(false);
                }
            }
        });
        bottomPanel.add(editButton);

        JButton deleteButton = new JButton("DELETE ACCOUNT");
        deleteButton.addActionListener(e -> {
            int deleteAccount = JOptionPane.showConfirmDialog(getRootPane(),"Are you sure you want to delete your account?","DELETE ACCOUNT",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if(deleteAccount == JOptionPane.YES_OPTION){
                try {
                    services.deleteAccount();
                    new LoginScreen();
                    dispose();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        bottomPanel.add(deleteButton);

        return bottomPanel;
    }
}
