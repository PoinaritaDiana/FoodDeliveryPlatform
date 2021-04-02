package classes;

import auxiliar.Pair;
import java.util.Map;
import java.util.TreeMap;

public class Menu {
    // Each product in the menu has its own price and quantity left
    private Map < FoodProduct, Pair<Float,Integer> >  foodProductList= new TreeMap<>();
    private Map < BeverageProduct, Pair<Float,Integer> >  beverageProductList= new TreeMap<>();

}
