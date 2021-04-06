package usersManagement;

import auxiliar.OrderComparator;
import classes.Cart;
import classes.Order;
import usersManagement.User;

import java.util.Set;
import java.util.TreeSet;

// Class that extends the user base class, representing the consumer/customer
// Each customer has a list of orders placed (in ascending order according to the date they were placed)
// and a shopping cart, where the user can add different products

public class Customer extends User {
    private Set <Order> ordersHistoryList = new TreeSet<>(new OrderComparator());
    private Cart cart;

    public Customer(String lastName, String firstName, String phoneNumber, String email, String username, String password) {
        super(lastName, firstName, phoneNumber, email, username, password);
        cart = new Cart();
    }

    //Get user's cart
    public Cart getCart() {
        return cart;
    }

    // Display the list of all placed orders so far
    public void displayOrdersHistory() {
        for(Order order: ordersHistoryList)
            System.out.println(order);
    }

    // Add new order to the list of placed orders
    public void addOrderHistoryList(Order newOrder){
        ordersHistoryList.add(newOrder);
    }

    @Override
    public String toString() {
        return String.format("Customer %s : %s %s",getUserId(), getLastName(), getFirstName());
    }
}
