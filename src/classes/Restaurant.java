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
    private Menu menu;

    public Restaurant(String restaurantName, String restaurantAddress, String restaurantType, String phoneNumber, float rating, Menu menu) {
        this.restaurantId = generateID();
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantType = restaurantType;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.menu = menu;
        // this.menu = new Menu (menu);
        // trebuie sa fac copy constructor la menu
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public String getRestaurantType() {
        return restaurantType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public float getRating() {
        return rating;
    }

    // Add new rating for restaurant
    public void addRestaurantRating(float newRating){
        this.rating = (this.rating + newRating)/2;
    }

    @Override
    public String toString(){
        return String.format("Restaurant %d: %s. Contact: %d, Rating: %f", restaurantId, restaurantName, phoneNumber, rating);
    }
}
