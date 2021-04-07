package classes;

import java.util.HashSet;
import java.util.Set;

public class FoodProduct extends Product{
    private Set<String> categories = new HashSet<>();

    public FoodProduct(String name, String description, float price, float preparationTime,String[] categories){
        super(name,description,price, preparationTime);
        for(String category: categories)
            this.categories.add(category);
    }

    public Set<String> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        return "Food Product no." + getProductId() + "\r\n" +
                "\t Product Name: " + getProductName() + "- price: " + getPrice() + ", rating=" + getRating() + "\r\n" +
                "\t Description='" + getDescription() + '\'' +  "\r\n" +
                "\t Preparation time=" + preparationTime + "minutes" +  "\r\n" +
                "\t Category: " + categories +"\r\n";
    }
}
