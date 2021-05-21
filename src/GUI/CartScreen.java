package GUI;

import models.CartItem;
import services.Audit;
import services.Services;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CartScreen extends JFrame {
    private Services services = Services.getServicesInstance();
    private Audit audit = Audit.getAuditInstance();
    private JLabel productsPrice, deliveryPrice;
    private PlaceOrderPanel orderPanel;
    private JPanel newOrderPanel;
    private JPanel cartItemsPanel;
    private boolean emptyCart;
    private JList cartList;

    CartScreen() {
        setTitle("DeliverEAT");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = (JPanel) getContentPane();
        panel.setLayout(new BorderLayout());
        panel.add(cartTopPanel(), BorderLayout.NORTH);

        JSplitPane centerPanel = new JSplitPane();
        centerPanel.setResizeWeight(0.5);
        panel.add(centerPanel, BorderLayout.CENTER);

        cartItemsPanel = getCartItemsPanel();
        centerPanel.setLeftComponent(cartItemsPanel);

        newOrderPanel = new JPanel();
        centerPanel.setRightComponent(newOrderPanel);
        newOrderPanel.setLayout(new BorderLayout());
        orderPanel = new PlaceOrderPanel();
        JLabel title = new JLabel("Place a new order", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(new EmptyBorder(10,5,10,5));
        newOrderPanel.add(title,BorderLayout.NORTH);
        newOrderPanel.add(orderPanel);
        newOrderPanel.setVisible(false);
    }


    JPanel cartTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 20));
        JButton backToMainScreen = new JButton("<< Back to Main Page");
        backToMainScreen.addActionListener(e -> {
            dispose();
            MainScreen mainScreen = new MainScreen();
            mainScreen.setVisible(true);
        });
        topPanel.add(backToMainScreen);
        JLabel labelTitle = new JLabel("MY CART", JLabel.CENTER);
        labelTitle.setFont(new Font("Arial", Font.BOLD, 20));
        labelTitle.setBorder(new EmptyBorder(0, 60, 0, 0));
        topPanel.add(labelTitle);
        return topPanel;
    }

    JPanel cartBottomPanel() {
        JPanel bottomPanel = new JPanel();
        GridLayout layout = new GridLayout(3, 2);
        layout.setHgap(5);
        bottomPanel.setLayout(layout);

        String[] price = services.getCartPrice();
        JLabel productsPriceLabel = new JLabel("PRODUCTS:");
        productsPriceLabel.setBorder(new EmptyBorder(0, 10, 5, 5));
        bottomPanel.add(productsPriceLabel);
        productsPrice = new JLabel(String.format("%s LEI", price[0]));
        bottomPanel.add(productsPrice);

        JLabel deliveryPriceLabel = new JLabel("DELIVERY PRICE:");
        deliveryPriceLabel.setBorder(new EmptyBorder(0, 10, 5, 5));
        bottomPanel.add(deliveryPriceLabel);
        deliveryPrice = new JLabel(String.format("%s LEI", price[1]));
        bottomPanel.add(deliveryPrice);

        JButton emptyCartButton = new JButton("EMPTY CART");
        emptyCartButton.addActionListener(e -> {
            if(emptyCart){
                JOptionPane.showMessageDialog(getRootPane(), "Your cart is already empty", "Empty cart", JOptionPane.WARNING_MESSAGE);
            }
            else {
                int emptyCart = JOptionPane.showConfirmDialog(getRootPane(), "Are you sure you want to empty cart?", "EMPTY CART",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (emptyCart == JOptionPane.YES_OPTION) {
                    services.clearMyCart();
                    JOptionPane.showMessageDialog(getRootPane(), "Your cart has been emptied.");
                    CartScreen cartScreen = new CartScreen();
                    cartScreen.setVisible(true);
                    dispose();
                }
            }
        });
        bottomPanel.add(emptyCartButton);

        JButton placeOrderButton = new JButton("PLACE ORDER");
        placeOrderButton.addActionListener(e -> {
            if(emptyCart){
                JOptionPane.showMessageDialog(getRootPane(), "Your cart is empty", "Place Order", JOptionPane.WARNING_MESSAGE);
            }
            else {
                System.out.println("Afafad");
                newOrderPanel.setVisible(true);
                orderPanel.setVisible(true);
            }

        });
        bottomPanel.add(placeOrderButton);
        return bottomPanel;
    }


    JPanel getCartItemsPanel() {
        DefaultListModel<CartItem> cartItems = new DefaultListModel<>();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton removeProductFromCart = new JButton("Remove product from cart");
        removeProductFromCart.addActionListener(e -> {
            try {
                String quantityText = JOptionPane.showInputDialog("Enter quantity to be removed from cart:");
                boolean invalidQuantity = quantityText.isEmpty() || quantityText.isBlank() || quantityText == null;
                if (!invalidQuantity) {
                    int quantity = Integer.parseInt(quantityText);
                    if (quantity <= 0) throw new Exception();

                    boolean removed = services.removeProductFromCart((CartItem) cartList.getSelectedValue(), quantity);
                    if (removed) {
                        JOptionPane.showMessageDialog(getRootPane(), "Product removed from your cart");
                        CartScreen cartScreen = new CartScreen();
                        cartScreen.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(getRootPane(), "The quantity of product in the cart is less than the value entered!",
                                "Alert", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (Exception exception) {
                System.out.println(exception);
                JOptionPane.showMessageDialog(getRootPane(), "Quantity must be a positive integer", "Alert", JOptionPane.WARNING_MESSAGE);
            }
        });
        removeProductFromCart.setVisible(false);
        panel.add(removeProductFromCart, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        java.util.List<CartItem> cart = services.showProductsMyCart();
        if (cart == null) {
            centerPanel.add(new JLabel("Your cart is empty", JLabel.CENTER), BorderLayout.CENTER);
            emptyCart = true;
        } else {
            emptyCart = false;
            cart.forEach(cartItem -> cartItems.addElement(cartItem));
            cartList = new JList<>(cartItems);
            cartList.setBorder(new EmptyBorder(5, 5, 5, 5));
            cartList.addListSelectionListener(event -> {
                removeProductFromCart.setVisible(true);
            });
            JScrollPane cartScrollList = new JScrollPane(cartList);
            centerPanel.add(cartScrollList, BorderLayout.CENTER);
        }

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(cartBottomPanel(), BorderLayout.SOUTH);
        return panel;
    }
}

