package classes;

public class BeverageProduct extends Product{
    private boolean alcoholicDrink;
    private String drinkType;

    public BeverageProduct(String name, String description, float price, float preparationTime,
                           boolean alcoholicDrink, String type){
        super(name,description,price,preparationTime);
        this.alcoholicDrink = alcoholicDrink;
        this.drinkType = type;
    }

    public boolean isAlcoholicDrink() {
        return alcoholicDrink;
    }

    public String getDrinkType() {
        return drinkType;
    }


    @Override
    public String toString() {
        return "Beverage Product{" +
                "Product Id=" + getProductId() +
                ", Product Name='" + getProductName() + '\'' +
                ", Price=" + getPrice() +
                ", Description='" + getDescription() + '\'' +
                ", Rating=" + getRating() +
                ", Alcoholic Drink=" + alcoholicDrink +
                ", Drink Type='" + drinkType + '\'' +
                '}';
    }
}
