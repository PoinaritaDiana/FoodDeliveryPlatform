package auxiliar;

import classes.Order;

import java.util.Comparator;

public class OrderComparator implements Comparator<Order> {
    @Override
    public int compare(Order order1, Order order2){
        return order1.getCreateTime().compareTo(order2.getCreateTime());
    }
}
