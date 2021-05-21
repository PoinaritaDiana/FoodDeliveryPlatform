package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class OrderDetailsPanel extends JPanel {
    private JPanel list = new JPanel();

    OrderDetailsPanel(){
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        JLabel labelTitle = new JLabel("Order Details", JLabel.CENTER);
        labelTitle.setFont(new Font("Arial",Font.BOLD,17));
        topPanel.add(labelTitle, BorderLayout.NORTH);
        list.setLayout(new BorderLayout());
        add(list, BorderLayout.CENTER);
    }

    public void setOrder(java.util.List<String> products){
        list.removeAll();
        DefaultListModel<String> orderItems = new DefaultListModel<>();
        products.forEach(cartItem -> orderItems.addElement(cartItem));
        JList menuList = new JList<>(orderItems);
        menuList.setBorder(new EmptyBorder(5,5, 5, 5));
        list.add(new JScrollPane(menuList),BorderLayout.CENTER);
        list.revalidate();
        repaint();
    }
}
