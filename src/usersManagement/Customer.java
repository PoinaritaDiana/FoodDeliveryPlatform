package usersManagement;

import classes.Cart;
import classes.Order;
import usersManagement.User;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    List<Order> ordersList = new ArrayList<>();
    Cart cart;

    public Customer(String lastName, String firstName, String phoneNumber, String email, String username, String password) {
        super(lastName, firstName, phoneNumber, email, username, password);
        cart = new Cart();
    }

    public void displayOrdersList() {
        for(Order order: ordersList)
            System.out.println(order);
    }
}
