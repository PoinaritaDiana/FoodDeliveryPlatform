package classes;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class Order {
    private int orderId;
    private Customer client;
    private DeliveryPerson deliveryPerson;
    private String payment;
    private String deliveryAddress;
    private Date createTime;
    private int preparationTime;


    /**
    private Float totalPrice;

    /** The no of items = len(orderedItemsList) */
    private int noOfItems;

    /** A list of products ordered, along with the quantity of each */
    private Map<Product, Integer> orderedItemsList;

    /** The order status. */
    private OrderStatus orderStatus;


}
