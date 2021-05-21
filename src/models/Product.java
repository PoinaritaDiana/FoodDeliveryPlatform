package models;

abstract public class Product{
    protected String productId;
    protected String productName;
    protected float price;
    protected String description;
    protected float rating;
    protected float preparationTime;
    protected String restaurantId;

    public Product(String productID,String name, String description, float price, float preparationTime,float rating, String restaurantId){
        this.productId = productID;
        this.productName = name;
        this.description = description;
        this.rating = rating ;
        this.price = price;
        this.preparationTime = preparationTime;
        this.restaurantId = restaurantId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public float getRating() {
        return rating;
    }

    public void updateRating(float rating){
        this.rating = (this.rating+rating)/2;
    }

    public float getPreparationTime() {
        return preparationTime;
    }

    public abstract String[] getObjectData();
}
