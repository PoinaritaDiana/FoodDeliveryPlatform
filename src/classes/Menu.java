package classes;
import auxiliar.Pair;
import auxiliar.ProductComparator;

import java.util.Set;
import java.util.TreeSet;

// Each menu has a list of food products and a list of drink products (sorted alphabetically)
public class Menu {
    private Set <FoodProduct> foodProductList= new TreeSet<>(new ProductComparator());
    private Set <BeverageProduct>  beverageProductList= new TreeSet<>(new ProductComparator());

    public Set <FoodProduct> getFoodProductList() {
        return foodProductList;
    }
    public Set <BeverageProduct> getBeverageProductList() {
        return beverageProductList;
    }


    public Menu(Product [] productsList) {
        for(Product product: productsList) {
            if (product instanceof FoodProduct)
                this.foodProductList.add((FoodProduct) product);
            else
                this.beverageProductList.add((BeverageProduct) product);
        }
    }


    public void showMenu(){
        System.out.println("Food:");
        for(FoodProduct product : foodProductList)
            System.out.println(product);
        System.out.println("Beverage:");
        for(BeverageProduct product : beverageProductList)
            System.out.println(product);
    }


    // Search a product in the menu by name
    public boolean searchProductInMenu(String productName){
        boolean found = false;
        for(FoodProduct product : foodProductList)
            if(product.getProductName().equalsIgnoreCase(productName))
                found=true;

        for(BeverageProduct product : beverageProductList)
            if(product.getProductName().equalsIgnoreCase(productName))
                found=true;

        return found;
    }


    // Search a product in the menu by ID
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


    // Show product in the menu by name
    public void displayProductInMenu(String productName){
        for(FoodProduct product : foodProductList)
            if(product.getProductName().equalsIgnoreCase(productName))
                System.out.println(product);

        for(BeverageProduct product : beverageProductList)
            if(product.getProductName().equalsIgnoreCase(productName))
                System.out.println(product);
    }


}
