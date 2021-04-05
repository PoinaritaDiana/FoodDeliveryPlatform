package classes;

import auxiliar.IDGenerator;

import java.util.Set;

abstract public class Product implements IDGenerator {
    private String productId;
    private String productName;
    private float price;
    private String description;
    private float rating;
    private float preparationTime;

    public Product(String name, String description, float price, float preparationTime){
        this.productId = generateID();
        this.productName = name;
        this.description = description;
        this.rating = 0 ;
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

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void updateRating(float rating){
        this.rating = (this.rating+rating)/2;
    }

    public float getPreparationTime() {
        return preparationTime;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                '}';
    }


}
