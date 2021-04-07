package classes;

import auxiliar.IDGenerator;
import usersManagement.Customer;
import usersManagement.DeliveryPerson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Order implements IDGenerator {
    private String orderId;
    private String clientId;
    private String deliveryPersonId;

    private List<CartItem> cartProducts;
    private Float totalPrice;

    private String payment;
    private String deliveryAddress;

    private LocalDateTime createTime;
    private float preparationTime;

    public Order(String clientId, String deliveryPersonId, List<CartItem> cartProducts, Float totalPrice, String payment, String deliveryAddress, float preparationTime) {
        this.orderId = generateID();
        this.clientId = clientId;
        this.deliveryPersonId = deliveryPersonId;
        this.cartProducts = new ArrayList<>(cartProducts);
        this.totalPrice = totalPrice;
        this.payment = payment;
        this.deliveryAddress = deliveryAddress;
        this.createTime = LocalDateTime.now();
        this.preparationTime = preparationTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getDeliveryPersonId() {
        return deliveryPersonId;
    }

    public List<CartItem> getCartProducts() {
        return cartProducts;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public String getPayment() {
        return payment;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public float getPreparationTime() {
        return preparationTime;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String output = String.format("Order no. %s \r\n Client ID: %s , Delivery Person ID: %s \r\n Total price: %s \r\n Payment type: %s, Delivery Address: %s \r\n" +
                        "Preparation time: %s \r\n Date: " + createTime.format(dateFormat) + "\r\n" +
                        "Products:", orderId, clientId, deliveryPersonId,totalPrice,payment, deliveryAddress, preparationTime);
        for(CartItem cartItem: cartProducts)
            output = output + "\r\n" + cartItem;

        return output;
    }
}
