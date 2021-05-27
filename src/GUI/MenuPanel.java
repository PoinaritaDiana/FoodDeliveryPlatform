package GUI;

import models.Menu;
import models.Product;
import services.Audit;
import services.Services;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuPanel extends JPanel {
    private Audit audit = Audit.getAuditInstance();
    private Services services = Services.getServicesInstance();
    private JPanel addProductPanel;
    private DefaultListModel<Product> menu = new DefaultListModel<>();
    private JList menuList = new JList<>(menu);
    private JScrollPane menuScrollList =  new JScrollPane(menuList);
    private String currentRestaurantId;
    private JButton clearSearchButton;
    private JTextField searchText;

    MenuPanel(){
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        JLabel labelTitle = new JLabel("Menu", JLabel.CENTER);
        labelTitle.setFont(new Font("Arial",Font.BOLD,17));
        topPanel.add(labelTitle, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(3,2));
        searchText =  new JTextField("" ,20);
        clearSearchButton = new JButton("Clear search");
        clearSearchButton.addActionListener(e -> {
            menu.removeAllElements();
            Menu restaurantMenu = services.getRestaurantMenuById(currentRestaurantId);
            if (restaurantMenu!=null){
                restaurantMenu.getFoodProductList().forEach(product -> menu.addElement(product));
                restaurantMenu.getBeverageProductList().forEach(product -> menu.addElement(product));
            }
            clearSearchButton.setVisible(false);
            searchText.setText("");
        });
        clearSearchButton.setVisible(false);

        JButton searchButton = new JButton("Search product");
        searchButton.addActionListener(e -> {
            audit.auditService("searchProductInMenuByName");
            String productName = searchText.getText();
            boolean invalidSearch = productName.isEmpty() || productName.isBlank() || productName==null;
            if(!invalidSearch) {
                productName = productName.replace(" ","");
                Menu restaurantMenu = services.getRestaurantMenuById(currentRestaurantId);
                Product product = restaurantMenu.searchProductInMenuByName(productName);
                if(product!=null) {
                    menuList.clearSelection();
                    menu.removeAllElements();
                    menu.addElement(product);
                    clearSearchButton.setVisible(true);
                }
                else{
                    JOptionPane.showMessageDialog(getRootPane(), String.format("No result for %s", productName), "Alert", JOptionPane.WARNING_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(getRootPane(), "Enter the name of the product to search", "Alert", JOptionPane.WARNING_MESSAGE);
            }
        });
        searchPanel.add(new JLabel(""));
        searchPanel.add(new JLabel(""));
        searchPanel.add(searchText);
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel(""));
        searchPanel.add(clearSearchButton);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        add(menuScrollList, BorderLayout.CENTER);

        addProductPanel = new JPanel();
        addProductPanel.setLayout(new GridLayout(2,1));

        JPanel quantityInput = new JPanel();
        quantityInput.setLayout(new GridLayout(1,2));
        quantityInput.add(new JLabel("Product Quantity:",JLabel.CENTER));
        JTextField quantity = new JTextField("0");
        quantityInput.add(quantity);
        JButton addProductButton = new JButton("Add product to cart");
        addProductButton.addActionListener(e -> {
            try {
                String quantityText =  quantity.getText();
                boolean invalidQuantity = quantityText.isEmpty() || quantityText.isBlank() || quantityText==null;
                if(invalidQuantity) throw new Exception();
                int number = Integer.parseInt(quantityText);
                if(number<=0) throw new Exception();

                Product product = (Product) menuList.getSelectedValue();
                if(services.checkOrderRestaurant(product)){
                    services.addProductInCart(product,number);
                    JOptionPane.showMessageDialog(getRootPane(), "Product added to your cart");
                    quantity.setText("0");
                }
                else{
                    int addProduct = JOptionPane.showConfirmDialog(getRootPane(),
                            "You have already selected products from a different restaurant. If you continue, your cart and selections will be canceled.\n" +
                                    "Do you want to continue with the newly selected product?",
                            "ADD PRODUCT TO CART",
                            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if(addProduct == JOptionPane.YES_OPTION){
                        services.addProductInCart(product,number);
                        JOptionPane.showMessageDialog(getRootPane(), "Product added to your cart");
                        quantity.setText("0");
                    }
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(getRootPane(), "Quantity must be a positive integer", "Alert", JOptionPane.WARNING_MESSAGE);
            }
        });
        addProductPanel.add(quantityInput);
        addProductPanel.add(addProductButton);
        addProductPanel.setVisible(false);
        add(addProductPanel, BorderLayout.SOUTH);
    }


    public void resetSearch(){
        clearSearchButton.setVisible(false);
        searchText.setText("");
    }

    public void setMenu(String restaurantId){
        audit.auditService("showRestaurantMenu");
        currentRestaurantId = restaurantId;
        menu.removeAllElements();
        Menu restaurantMenu = services.getRestaurantMenuById(restaurantId);
        if (restaurantMenu!=null){
            restaurantMenu.getFoodProductList().forEach(product -> menu.addElement(product));
            restaurantMenu.getBeverageProductList().forEach(product -> menu.addElement(product));
        }
        menuList.setBorder(new EmptyBorder(5,5, 5, 5));
        menuList.addListSelectionListener(event->{
            addProductPanel.setVisible(true);
        });
        addProductPanel.setVisible(false);
    }
}
