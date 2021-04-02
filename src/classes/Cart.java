package classes;

import java.util.HashSet;
import java.util.Set;

public final class Cart {
    private static Cart userCart;
    private Set<CartItem> cartProducts = new HashSet<>();

    private Cart(){}

    public static Cart getUserCart(){
        if(userCart == null)
            userCart = new Cart();
        return userCart;
    }
}
