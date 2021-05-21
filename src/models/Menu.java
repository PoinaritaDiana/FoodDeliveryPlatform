package models;
import auxiliar.ProductComparator;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

// Each menu has a list of food products and a list of drink products (sorted alphabetically)
public class Menu {
    private Set <FoodProduct> foodProductList= new TreeSet<>(new ProductComparator());
    private Set <BeverageProduct>  beverageProductList= new TreeSet<>(new ProductComparator());


    public Menu(List<FoodProduct> foodProductsList, List<BeverageProduct> beverageProductsList) {
        if (foodProductsList!=null) {
            for (FoodProduct product : foodProductsList)
                this.foodProductList.add(product);
        }
        if (beverageProductsList!=null) {
            for (BeverageProduct product : beverageProductsList)
                this.beverageProductList.add(product);
        }
    }

    public Set<FoodProduct> getFoodProductList() {
        return foodProductList;
    }

    public Set<BeverageProduct> getBeverageProductList() {
        return beverageProductList;
    }

    public void showMenu(){
        System.out.println("Food:");
        for(FoodProduct product : foodProductList)
            System.out.println(product);
        System.out.println("Beverage:");
        for(BeverageProduct product : beverageProductList)
            System.out.println(product);
    }



    public Product searchProductInMenuByName(String productName){
        for(FoodProduct product : foodProductList)
            if(product.getProductName().equalsIgnoreCase(productName))
                return product;
        for(BeverageProduct product : beverageProductList)
            if(product.getProductName().equalsIgnoreCase(productName))
                return product;
        return null;
    }


    public Product searchProductInMenuByID(String productId){
        Product searchedProduct = null;
        for(FoodProduct product : foodProductList)
            if(product.getProductId().equals(productId)){
                searchedProduct = product;
                break;
            }
        for(BeverageProduct product : beverageProductList)
            if(product.getProductId().equals(productId)){
                searchedProduct = product;
                break;
            }
        return searchedProduct;
    }


    public void displayProductInMenu(String productName){
        for(FoodProduct product : foodProductList)
            if(product.getProductName().equalsIgnoreCase(productName))
                System.out.println(product);
        for(BeverageProduct product : beverageProductList)
            if(product.getProductName().equalsIgnoreCase(productName))
                System.out.println(product);
    }

    public void addFoodProductInMenu(FoodProduct product){
        this.foodProductList.add(product);
    }

    public void addBeverageProductInMenu(BeverageProduct product){
        this.beverageProductList.add(product);
    }


}
