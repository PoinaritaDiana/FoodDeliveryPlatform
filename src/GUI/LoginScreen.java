package GUI;

import services.Services;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame{
    static private GridBagLayout gridBag;
    static private GridBagConstraints gridCons;
    static private JPanel panel;
    private Services services = Services.getServicesInstance();

    static void addComponentInGrid(Component c, int x, int y, int w, int h){
        gridCons.gridx = x;
        gridCons.gridy = y;
        gridCons.gridwidth = w;
        gridCons.gridheight = h;
        gridBag.setConstraints(c, gridCons);
        panel.add(c);
    }

    public LoginScreen(){
        setTitle("LOG IN PAGE");
        panel = (JPanel) getContentPane();
        gridBag = new GridBagLayout();
        panel.setLayout(gridBag);

        gridCons = new GridBagConstraints();
        gridCons.weightx = 1;
        gridCons.weighty = 1;
        gridCons.insets = new Insets(5,5,5,5);
        JLabel labelLogin = new JLabel("LOGIN", JLabel.CENTER);
        labelLogin.setFont(new Font("Arial",Font.BOLD,22));
        gridCons.fill = GridBagConstraints.BOTH;
        addComponentInGrid(labelLogin,0,0,4,1);

        JLabel labelUsername = new JLabel("Username:");
        gridCons.fill = GridBagConstraints.NONE;
        gridCons.anchor = GridBagConstraints.CENTER;
        addComponentInGrid(labelUsername,0,2,1,1);

        JLabel labelPassword = new JLabel("Password:");
        addComponentInGrid(labelPassword,0,3,1,1);

        gridCons.fill = GridBagConstraints.HORIZONTAL;
        gridCons.anchor = GridBagConstraints.CENTER;
        JTextField textUsername = new JTextField("",30);
        JPasswordField textPassword = new JPasswordField("",30);
        addComponentInGrid(textUsername,1,2,2,1);
        addComponentInGrid(textPassword,1,3,2,1);

        JButton logInButton = new JButton("LOG IN");
        logInButton.addActionListener(e -> {
            String username = textUsername.getText();
            String password = String.valueOf(textPassword.getPassword());
            boolean invalidUsernameInput = username.isEmpty() || username.isBlank() || username==null;
            boolean invalidPasswordInput = password.isEmpty() || password.isBlank() || password==null;
            if(invalidUsernameInput || invalidPasswordInput) {
                JOptionPane.showMessageDialog(getRootPane(), "All fields must be completed!", "Alert", JOptionPane.WARNING_MESSAGE);
            }
            else{
                if(username.contains(" ")){
                    JOptionPane.showMessageDialog(getRootPane(), "Username must not contain spaces", "Alert", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    if (password.contains(" ")) {
                        JOptionPane.showMessageDialog(getRootPane(), "Password must not contain spaces", "Alert", JOptionPane.WARNING_MESSAGE);
                    } else {
                        int checkUser = services.logInUser(username, password);
                        if (checkUser == 0) {
                            JOptionPane.showMessageDialog(getRootPane(), String.format("Welcome back to DeliverEAT,\n %s ", username));
                            MainScreen mainScreen = new MainScreen();
                            mainScreen.setVisible(true);
                            dispose();
                        } else {
                            if (checkUser == 1)
                                JOptionPane.showMessageDialog(getRootPane(), "This username does not exist.", "Alert", JOptionPane.WARNING_MESSAGE);
                            else
                                JOptionPane.showMessageDialog(getRootPane(), "Invalid password.", "Alert", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        });

        gridCons.fill = GridBagConstraints.HORIZONTAL;
        addComponentInGrid(logInButton,1,4,1,1);

        JButton signUpButton = new JButton("SIGN UP");
        signUpButton.addActionListener(e -> {
            RegisterScreen registerScreen = new RegisterScreen();
            dispose();
            registerScreen.setVisible(true);
        });
        addComponentInGrid(signUpButton,2,4,1,1);

        setSize(new Dimension(500,500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

}
