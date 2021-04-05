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
}
