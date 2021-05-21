package models;

import java.util.*;

public class Order {
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

    public Order(String orderId, String clientId, String deliveryPersonId, Float totalPrice, String payment,
                 String deliveryAddress, float preparationTime,  String createTime) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.deliveryPersonId = deliveryPersonId;
        this.totalPrice = totalPrice;
        this.payment = payment;
        this.deliveryAddress = deliveryAddress;
        this.createTime = createTime;
        this.preparationTime = preparationTime;
    }

    public void setCartProducts(List<CartItem> cartProducts) {
        this.cartProducts = new ArrayList<>(cartProducts);
    }

    public String getOrderId() {
        return orderId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getCreateTime() {
        return createTime;
    }

    /*
    @Override
    public String toString() {
        String output = String.format("Order no. %s \r\n Total price: %s \r\n Payment type: %s, Delivery Address: %s \r\n" +
                        "Preparation time: %s \r\n Date: " + createTime + "\r\n" +
                        "Products:", orderId,totalPrice,payment, deliveryAddress, preparationTime);
        for(CartItem cartItem: cartProducts)
            output = output + "\r\n" + cartItem;

        return output;
    }
     */

    @Override
    public String toString(){
        return String.format("<html> Order %s <br> Total price: %s <br> Payment type: %s <br> Delivery Address: %s <br> Date : %s <html>",
                orderId, totalPrice,payment, deliveryAddress, createTime);
    }

    public List<String> displayProducts(){
        List<String> products = new ArrayList<>();
        for(CartItem cartItem: cartProducts)
            products.add(cartItem.toString());
        return products;
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
