package GUI;

import services.Services;
import usersManagement.Customer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PlaceOrderPanel extends JPanel {
    private Services services = Services.getServicesInstance();

    PlaceOrderPanel(){
        setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight= 1;
        JLabel payment = new JLabel("PAYMENT: ");
        add(payment, gridBagConstraints);

        JPanel radioPanel = new JPanel(new FlowLayout());
        TitledBorder border = BorderFactory.createTitledBorder("Options:");
        radioPanel.setBorder(border);
        JRadioButton radio1 = new JRadioButton("Cash");
        radioPanel.add(radio1);
        JRadioButton radio2 = new JRadioButton("Card", true);
        radioPanel.add(radio2);
        ButtonGroup group = new ButtonGroup();
        group.add(radio1);
        group.add(radio2);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        add(radioPanel,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        JLabel address = new JLabel("DELIVERY ADDRESS: ");
        add(address,gridBagConstraints);

        JTextArea deliveryAddress = new JTextArea(4,10);
        deliveryAddress.setLineWrap (true);
        JScrollPane scroll=new JScrollPane(deliveryAddress);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight= 2;
        add(scroll, gridBagConstraints);

        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight= 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        add(new JLabel("PREPARATION TIME: "), gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        float time = ((Customer)services.getCurrentUser()).getCart().getTotalPreparationTime();
        JLabel preparationTime = new JLabel(String.format("%s minute", time));
        add(preparationTime,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        add(new JLabel("TOTAL: "), gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        String[] price = services.getCartPrice();
        JLabel totalPrice = new JLabel(String.format("%s LEI", Float.parseFloat(price[0])+Float.parseFloat(price[1])));
        add(totalPrice,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight= 2;
        JButton confirmOrderButton = new JButton("CONFIRM ORDER");
        confirmOrderButton.addActionListener(e -> {
            String paymentType = radio1.isSelected() ? radio1.getText() : radio2.getText();
            String addressInput = deliveryAddress.getText();
            boolean invalidAddress = addressInput.isEmpty() || addressInput.isBlank() || addressInput==null;
            if(invalidAddress){
                JOptionPane.showMessageDialog(getRootPane(), "Enter an address for delivery",
                        "Alert", JOptionPane.WARNING_MESSAGE);
            }
            else{
                try {
                    services.placeNewOrder(paymentType,addressInput);
                    SwingUtilities.getWindowAncestor(this).dispose();
                    MainScreen mainScreen = new MainScreen();
                    mainScreen.setVisible(true);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(getRootPane(), "The order has not been placed.",
                            "Alert", JOptionPane.WARNING_MESSAGE);
                }
            }

        });
        add(confirmOrderButton,gridBagConstraints);
    }
}
