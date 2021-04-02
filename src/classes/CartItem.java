package classes;

import java.util.Map;
import java.util.TreeMap;

public class CartItem {
    private Product product;
    private int productQuantity;
    private float totalItemPrice;

    public CartItem(Product product, int quantity){
        this.product=product;
        this.productQuantity= quantity;
        this.totalItemPrice = product.getPrice() * quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public float getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(float totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "Product=" + product.getName() +
                ", Quantity=" + productQuantity +
                ", Item Price=" + totalItemPrice +
                '}';
    }
}
