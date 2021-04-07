package classes;

import auxiliar.IDGenerator;

import java.util.List;
import java.util.Map;

public class Restaurant implements IDGenerator {
    private String restaurantId;
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantType;
    private String phoneNumber;
    private float rating;
    private float deliveryPrice;
    private Menu menu;


    public Restaurant(String restaurantName, String restaurantAddress, String restaurantType, String phoneNumber, float rating, float deliveryPrice, Menu menu) {
        this.restaurantId = generateID();
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantType = restaurantType;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.deliveryPrice = deliveryPrice;
        this.menu = menu;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public float getRating() {
        return rating;
    }

    public Menu getMenu() { return menu; }

    public float getDeliveryPrice() { return deliveryPrice; }

    // Add new rating for restaurant
    public void addRestaurantRating(float newRating){
        this.rating = (this.rating + newRating)/2;
    }

    @Override
    public String toString(){
        return String.format("Restaurant %s: %s. \r\n Contact: %s, Rating: %f, Delivery Price: %f", restaurantId, restaurantName, phoneNumber, rating,deliveryPrice);
    }
}
