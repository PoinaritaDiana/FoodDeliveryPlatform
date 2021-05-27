package GUI;
import models.Order;
import services.Audit;
import services.Services;
import usersManagement.Customer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class OrderHistoryScreen extends JFrame {
    private Services services = Services.getServicesInstance();
    private OrderDetailsPanel orderDetailsPanel;

    OrderHistoryScreen() {
        setTitle("DeliverEAT");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = (JPanel) getContentPane();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(ordersTopPanel(), BorderLayout.NORTH);

        JSplitPane centerPanel = new JSplitPane();
        centerPanel.setResizeWeight(0.5);
        mainPanel.add(centerPanel,BorderLayout.CENTER);
        centerPanel.setLeftComponent(ordersList());

        JPanel panel = new JPanel();
        centerPanel.setRightComponent(panel);
        panel.setLayout(new BorderLayout());
        orderDetailsPanel = new OrderDetailsPanel();
        panel.add(orderDetailsPanel, BorderLayout.CENTER);
        orderDetailsPanel.setVisible(false);
    }


    JPanel ordersList(){
        JPanel ordersPanel = new JPanel();
        ordersPanel.setLayout(new BorderLayout());
        DefaultListModel<Order> orders = new DefaultListModel<>();

        Customer currentUser = (Customer) services.getCurrentUser();
        java.util.Set<Order> userOrders = currentUser.getOrdersHistoryList();
        if(userOrders.isEmpty()){
            JLabel noOrdersLabel = new JLabel("You have no orders placed yet",JLabel.CENTER);
            ordersPanel.add(noOrdersLabel, BorderLayout.CENTER);
        }
        else {
            userOrders.forEach(order -> orders.addElement(order));
            JList ordersList = new JList<>(orders);
            ordersList.setBorder(new EmptyBorder(5, 5, 5, 5));
            ordersList.setFixedCellHeight(100);
            ordersList.addListSelectionListener(event -> {
                orderDetailsPanel.setVisible(true);
                List<String> selectedOrder = ((JList<Order>) event.getSource()).getSelectedValue().displayProducts();
                orderDetailsPanel.setOrder(selectedOrder);
            });
            JScrollPane ordersScrollList = new JScrollPane(ordersList);
            ordersPanel.add(ordersScrollList, BorderLayout.CENTER);
        }
        return ordersPanel;
    }


    JPanel ordersTopPanel(){
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout( FlowLayout.LEADING,5,20));

        JButton backToMainScreen = new JButton("<< Back to Main Page");
        backToMainScreen.addActionListener(e -> {
            dispose();
            MainScreen mainScreen = new MainScreen();
            mainScreen.setVisible(true);
                });
        topPanel.add(backToMainScreen);

        JLabel labelTitle = new JLabel("MY ORDERS", JLabel.CENTER);
        labelTitle.setFont(new Font("Arial",Font.BOLD,20));
        labelTitle.setBorder(new EmptyBorder(0,30,0,0));
        topPanel.add(labelTitle);

        return topPanel;
    }
}
