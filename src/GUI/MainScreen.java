package GUI;

import models.Restaurant;
import services.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;

public class MainScreen extends JFrame{
    private Audit audit = Audit.getAuditInstance();
    private Services services = Services.getServicesInstance();
    private MenuPanel menuPanel;
    private DefaultListModel<Restaurant> restaurants;
    private JScrollPane restaurantScrollList;
    private JList restaurantList;


    MainScreen(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(1000, (int) screenSize.getHeight()-50);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("DelivEAT");
        setJMenuBar(mainMenuBar());

        JPanel mainPanel = (JPanel) getContentPane();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(mainTopPanel(), BorderLayout.NORTH);

        JSplitPane centerPanel = new JSplitPane();
        centerPanel.setResizeWeight(0.5);
        mainPanel.add(centerPanel,BorderLayout.CENTER);
        centerPanel.setLeftComponent(restaurantPanel());

        JPanel panel = new JPanel();
        centerPanel.setRightComponent(panel);
        panel.setLayout(new BorderLayout());
        menuPanel = new MenuPanel();
        panel.add(menuPanel, BorderLayout.CENTER);
        menuPanel.setVisible(false);
    }

    JMenuBar mainMenuBar(){
        JMenuBar menuBar=new JMenuBar();
        JMenu logOut=new JMenu("LOG OUT");
        JMenu account=new JMenu("MY ACCOUNT");
        JMenu orderHistory=new JMenu("MY ORDERS");
        JMenu cart = new JMenu("CART");
        menuBar.add(account);
        menuBar.add(orderHistory);
        menuBar.add(cart);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(logOut);

        logOut.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                int logOut = JOptionPane.showConfirmDialog(getRootPane(),"Are you sure you want to log out?","LOG OUT",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                if(logOut == JOptionPane.YES_OPTION){
                    services.logOut();
                    new LoginScreen();
                    dispose();
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {}

            @Override
            public void menuCanceled(MenuEvent e) {}
        });

        account.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                audit.auditService("displayAccount");
                dispose();
                AccountScreen accountScreen = new AccountScreen();
                accountScreen.setVisible(true);
            }

            @Override
            public void menuDeselected(MenuEvent e) {}

            @Override
            public void menuCanceled(MenuEvent e) {}
        });

        orderHistory.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                audit.auditService("showOrderHistory");
                dispose();
                OrderHistoryScreen orderHistoryScreen = new OrderHistoryScreen();
                orderHistoryScreen.setVisible(true);
            }

            @Override
            public void menuDeselected(MenuEvent e) {}

            @Override
            public void menuCanceled(MenuEvent e) {}
        });


        cart.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                audit.auditService("displayCart");
                dispose();
                CartScreen cartScreen = new CartScreen();
                cartScreen.setVisible(true);
            }

            @Override
            public void menuDeselected(MenuEvent e) {}

            @Override
            public void menuCanceled(MenuEvent e) {}
        });

        return menuBar;
    }

    JPanel mainTopPanel(){
        JPanel topPanel = new JPanel();
        FlowLayout layout = new FlowLayout();
        layout.setVgap(15);
        topPanel.setLayout(layout);
        JLabel labelTitle = new JLabel("RESTAURANTS", JLabel.CENTER);
        labelTitle.setFont(new Font("Arial",Font.BOLD,20));
        topPanel.add(labelTitle);
        return topPanel;
    }

    JPanel restaurantPanel(){
        JPanel restaurantsPanel = new JPanel();
        restaurantsPanel.setLayout(new BorderLayout());

        audit.auditService("showAllRestaurants");
        restaurants = new DefaultListModel<>();
        services.getRestaurantList().forEach(restaurant -> restaurants.addElement(restaurant));
        restaurantList = new JList<>(restaurants);
        restaurantList.setBorder(new EmptyBorder(5,5, 5, 5));
        restaurantList.setFixedCellHeight(100);

        restaurantList.addListSelectionListener(event->{
            Restaurant restaurant = ((JList<Restaurant>) event.getSource()).getSelectedValue();
            if(restaurant!=null){
                menuPanel.setVisible(true);
                menuPanel.resetSearch();
                String selectedRestaurantId = restaurant.getRestaurantId();
                menuPanel.setMenu(selectedRestaurantId);
            }
        });
        restaurantScrollList = new JScrollPane(restaurantList);
        restaurantsPanel.add(restaurantScrollList, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        JTextField searchText =  new JTextField("" ,20);

        JButton clearSearchButton = new JButton("Clear search");
        clearSearchButton.addActionListener(e -> {
            restaurants.removeAllElements();
            services.getRestaurantList().forEach(restaurant -> restaurants.addElement(restaurant));
            clearSearchButton.setVisible(false);
            menuPanel.setVisible(false);
            searchText.setText("");
        });
        clearSearchButton.setVisible(false);

        JButton searchButton = new JButton("Search restaurant");
        searchButton.addActionListener(e -> {
            audit.auditService("searchRestaurantByName");
            String restaurantName = searchText.getText();
            boolean invalidSearch = restaurantName.isEmpty() || restaurantName.isBlank() || restaurantName==null;

            if(!invalidSearch) {
                menuPanel.setVisible(false);
                restaurantName = restaurantName.replace(" ","");
                Restaurant restaurant = services.searchRestaurantByName(restaurantName);
                if(restaurant!=null) {
                    restaurantList.clearSelection();
                    restaurants.removeAllElements();
                    restaurants.addElement(restaurant);
                    clearSearchButton.setVisible(true);
                }
                else{
                    JOptionPane.showMessageDialog(getRootPane(), String.format("No result for %s", restaurantName), "Alert", JOptionPane.WARNING_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(getRootPane(), "Enter the name of the restaurant to search", "Alert", JOptionPane.WARNING_MESSAGE);
            }
        });
        searchPanel.add(searchText);
        searchPanel.add(searchButton);
        searchPanel.add(clearSearchButton);
        restaurantsPanel.add(searchPanel, BorderLayout.NORTH);

        return restaurantsPanel;
    }
}
