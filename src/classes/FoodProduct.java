package classes;

import java.util.HashSet;
import java.util.Set;

public class FoodProduct extends Product{
    private String category ;

    public FoodProduct(String productID,String name, String description, float price, float rating,float preparationTime,String category){
        super(productID,name,description,price, rating,preparationTime);
        this.category = category;
    }

    @Override
    public String toString() {
        return "Food Product no." + getProductId() + "\r\n" +
                "\t Product Name: " + getProductName() + "- price: " + getPrice() + ", rating=" + getRating() + "\r\n" +
                "\t Description='" + getDescription() + '\'' +  "\r\n" +
                "\t Preparation time=" + preparationTime + "minutes" +  "\r\n" +
                "\t Category: " + category +"\r\n";
    }
}
