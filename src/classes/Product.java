package classes;

import services.Database;

abstract public class Product{
    protected String productId;
    protected String productName;
    protected float price;
    protected String description;
    protected float rating;
    protected float preparationTime;

    public Product(String productID,String name, String description, float price, float preparationTime,float rating){
        this.productId = productID;
        this.productName = name;
        this.description = description;
        this.rating = rating ;
        this.price = price;
        this.preparationTime = preparationTime;
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


}
