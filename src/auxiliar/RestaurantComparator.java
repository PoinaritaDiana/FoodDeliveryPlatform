package auxiliar;

import classes.Restaurant;

import java.util.Comparator;

public class RestaurantComparator implements Comparator<Restaurant> {
    @Override
    public int compare(Restaurant r1, Restaurant r2){
        return -Float.compare(r1.getRating(),r2.getRating());
    }
}
