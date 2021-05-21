package auxiliar;

import models.Product;

import java.util.Comparator;

public class ProductComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2){
        return p1.getProductName().compareTo(p2.getProductName());
    }
}
