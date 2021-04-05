package usersManagement;

import classes.Cart;
import classes.Order;
import usersManagement.User;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private List<Order> orderHistoryList = new ArrayList<>();
    private Cart cart;

    public Customer(String lastName, String firstName, String phoneNumber, String email, String username, String password) {
        super(lastName, firstName, phoneNumber, email, username, password);
        cart = new Cart();
    }

    public void displayOrdersHistory() {
        for(Order order: orderHistoryList)
            System.out.println(order);
    }

    public Cart getCart() {
        return cart;
    }

    public void addOrderHistoryList(Order order){
        orderHistoryList.add(order);
    }
}
