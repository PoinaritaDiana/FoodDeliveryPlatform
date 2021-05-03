package classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Order{
    // Id count for orders
    private static int lastOrderID = 0;

    private String orderId;
    private String clientId;
    private String deliveryPersonId;

    private List<CartItem> cartProducts;
    private Float totalPrice;

    private String payment;
    private String deliveryAddress;

    private String createTime;
    private float preparationTime;

    public Order(String orderId, String clientId, String deliveryPersonId, Float totalPrice, String payment, String deliveryAddress,
                 float preparationTime,  String createTime,List<CartItem> cartProducts) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.deliveryPersonId = deliveryPersonId;
        this.cartProducts = new ArrayList<>(cartProducts);
        this.totalPrice = totalPrice;
        this.payment = payment;
        this.deliveryAddress = deliveryAddress;
        this.createTime = createTime;
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

    public String getCreateTime() {
        return createTime;
    }

    public float getPreparationTime() {
        return preparationTime;
    }

    @Override
    public String toString() {
        String output = String.format("Order no. %s \r\n Client ID: %s , Delivery Person ID: %s \r\n Total price: %s \r\n Payment type: %s, Delivery Address: %s \r\n" +
                        "Preparation time: %s \r\n Date: " + createTime + "\r\n" +
                        "Products:", orderId, clientId, deliveryPersonId,totalPrice,payment, deliveryAddress, preparationTime);
        for(CartItem cartItem: cartProducts)
            output = output + "\r\n" + cartItem;

        return output;
    }

    public String[] getObjectData(){
        String[] objectData = {orderId, clientId,  deliveryPersonId,  String.valueOf(totalPrice),  payment,  deliveryAddress, String.valueOf(preparationTime),createTime};
        return objectData;
    }


    public static void setLastOrderId(int orderID){
        lastOrderID = orderID;
    }

    public static String generateID(){
        lastOrderID +=1;
        return "ORD"+lastOrderID;
    }
}
