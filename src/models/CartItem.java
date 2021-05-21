package models;


// CartItem represents a product added to the cart with its quantity, for which the total price is calculated
public class CartItem {
    private Product product;
    private int productQuantity;
    private float totalItemPrice;
    private String orderId;

    public CartItem(Product product, int quantity){
        this.product = product;
        this.productQuantity= quantity;
        this.totalItemPrice = product.getPrice() * quantity;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Product getProduct() { return product; }

    public float getTotalItemPrice() { return totalItemPrice; }

    public int getProductQuantity() { return productQuantity; }

    @Override
    public String toString() {
        return String.format("<html> Product: %s <br>  %s <br> Unit price: %s <br> Quantity: %s <br> Total price for item: %s </html>",
                product.getProductName(), product.getDescription(),String.format("%.02f", product.getPrice()),productQuantity,String.format("%.02f",totalItemPrice));
    }

    // Change product quantity in cart (and update totalItemPrice)
    public void addQuantity(int quantity){
        this.productQuantity += quantity;
        this.totalItemPrice += product.getPrice() * quantity;
    }

    public String[] getObjectData(){
        String[] objectData = {product.getProductId(),String.valueOf(productQuantity), orderId};
        return objectData;
    }

}
