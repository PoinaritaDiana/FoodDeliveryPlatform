package models;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> cartProducts = new ArrayList<>();
    private String productsFromRestaurantID = null;

    public List<CartItem> getCartProducts (){
        return cartProducts;
    }

    public String getProductsFromRestaurantID() {
        return productsFromRestaurantID;
    }


    // Add product to cart
    // If the product already exists in the cart, then only increase the quantity with the given number
    public void addProductToCart(Product product, int quantity, String restaurantId){
        productsFromRestaurantID = restaurantId;
        boolean newProduct = true;
        for (CartItem cartProduct : cartProducts) {
            if (cartProduct.getProduct().getProductId().equals(product.getProductId())){
                cartProduct.addQuantity(quantity);
                newProduct =  false;
            }
        }

        if(newProduct == true){
            CartItem newCartItem = new CartItem(product, quantity);
            cartProducts.add(newCartItem);
        }
    }


    public boolean isEmpty(){
        if(cartProducts.size()==0)
            return true;
        return false;
    }

    public List<CartItem> showCartItems(){
        if(cartProducts.size()==0)
            System.out.println("No items in the cart");
        else {
            for (CartItem cartProduct : cartProducts)
                System.out.println(cartProduct);
            return cartProducts;
        }
        return null;
    }


    public void clearCart(){
        cartProducts.clear();
        productsFromRestaurantID = null;
    }


    public void removeCartItem(CartItem cartItem){
        int index = cartProducts.indexOf(cartItem);
        cartProducts.remove(index);
        if(this.isEmpty())
            this.productsFromRestaurantID = null;
    }


    public float getCartTotalPrice(){
        float totalPrice = 0 ;
        for (CartItem cartProduct : cartProducts)
            totalPrice += cartProduct.getTotalItemPrice();
        return totalPrice;
    }


    // Get total preparation time for cart items
    // Quantity does not matter
    public long getTotalPreparationTime(){
        long preparationTime = 0 ;
        for (CartItem cartProduct : cartProducts)
            preparationTime += cartProduct.getProduct().getPreparationTime();
        return preparationTime;
    }
}
