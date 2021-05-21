package models;


public class Restaurant {
    private String restaurantId;
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantType;
    private String phoneNumber;
    private float rating;
    private float deliveryPrice;
    private Menu menu;


    public Restaurant(String restaurantID, String restaurantName, String restaurantAddress,
                      String restaurantType, String phoneNumber, float rating, float deliveryPrice) {
        this.restaurantId = restaurantID;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantType = restaurantType;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.deliveryPrice = deliveryPrice;
    }

    public void setMenu(Menu menu) {
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


    public void addRestaurantRating(float newRating){
        this.rating = (this.rating + newRating)/2;
    }

    @Override
    public String toString(){
        return String.format("<html>Restaurant %s <br> Type: %s <br> Contact: %s <br> Rating: %s <br> Delivery Price: %s <html>", restaurantName.toUpperCase(), restaurantType,phoneNumber, String.format("%.02f", rating),String.format("%.02f", deliveryPrice));
    }

    public String[] getData(){
        String[] objectData = {restaurantName, restaurantAddress, restaurantType, phoneNumber, String.format("%.02f", rating), String.format("%.02f", deliveryPrice)};
        return objectData;
    }
}
