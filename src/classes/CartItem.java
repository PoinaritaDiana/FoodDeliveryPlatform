package classes;

import java.util.Map;
import java.util.TreeMap;

// CartItem represents a product added to the cart with its quantity, for which the total price is calculated
public class CartItem {
    private Product product;
    private int productQuantity;
    private float totalItemPrice;

    public CartItem(Product product, int quantity){
        this.product = product;
        this.productQuantity= quantity;
        this.totalItemPrice = product.getPrice() * quantity;
    }

    public Product getProduct() { return product; }

    public float getTotalItemPrice() { return totalItemPrice; }

    public int getProductQuantity() { return productQuantity; }

    @Override
    public String toString() {
        return String.format("Product %s: %s, Quantity: %d, Total price: %f",product.getProductId(),product.getProductName(), productQuantity,totalItemPrice);
    }

    // Change product quantity in cart (and update totalItemPrice)
    public void addQuantity(int quantity){
        this.productQuantity += quantity;
        this.totalItemPrice += product.getPrice() * quantity;
    }
}
