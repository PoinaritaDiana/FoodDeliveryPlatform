package classes;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> cartProducts = new ArrayList<>();

    public List<CartItem> getCartProducts (){
        return cartProducts;
    }

    public void addProductToCart(Product product, int quantity){
        boolean newProduct = true;
        if (cartProducts.size() == 0) {
            for (CartItem cartProduct : cartProducts) {
                if (cartProduct.getProduct().getProductId() == product.getProductId()){
                    cartProduct.addQuantity(quantity);
                    newProduct =  false;
                }
            }
        }
        if(newProduct == true){
            CartItem newCartItem = new CartItem(product, quantity);
            cartProducts.add(newCartItem);
        }
    }

    public void showCartItems(){
        for (CartItem cartProduct : cartProducts) {
            System.out.println(cartProduct);
        }
    }

    public void clearCart(){
        cartProducts.clear();
    }

}
