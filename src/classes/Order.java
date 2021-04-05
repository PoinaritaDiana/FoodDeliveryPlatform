package classes;

import auxiliar.IDGenerator;
import usersManagement.Customer;
import usersManagement.DeliveryPerson;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Order implements IDGenerator {
    private String orderId;
    private String clientId;
    private String deliveryPersonId;

    private List<CartItem> cartProducts;
    private Float totalPrice;

    private String payment;
    private String deliveryAddress;

    private Date createTime;
    private float preparationTime;

    public Order(String clientId, String deliveryPersonId, List<CartItem> cartProducts, Float totalPrice, String payment, String deliveryAddress, float preparationTime) {
        this.orderId = generateID();
        this.clientId = clientId;
        this.deliveryPersonId = deliveryPersonId;
        this.cartProducts = cartProducts;
        this.totalPrice = totalPrice;
        this.payment = payment;
        this.deliveryAddress = deliveryAddress;
        this.createTime = new Date();
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

    public Date getCreateTime() {
        return createTime;
    }

    public float getPreparationTime() {
        return preparationTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", deliveryPersonId='" + deliveryPersonId + '\'' +
                ", cartProducts=" + cartProducts +
                ", totalPrice=" + totalPrice +
                ", payment='" + payment + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", createTime=" + createTime +
                ", preparationTime=" + preparationTime +
                '}';
    }
}
