package classes;

public class BeverageProduct extends Product{
    private boolean alcoholicDrink;
    private String drinkType;

    public BeverageProduct(String productID,String name, String description, float price, float rating, float preparationTime,
                           boolean alcoholicDrink, String type){
        super(productID,name,description,price,rating,preparationTime);
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
        return "Beverage Product no." + getProductId() + "\r\n" +
                "\t Product Name: " + getProductName() + "- price: " + getPrice() + ", rating=" + getRating() + "\r\n" +
                "\t Description='" + getDescription() + '\'' +  "\r\n" +
                "\t Alcoholic Drink=" + alcoholicDrink + "Drink Type='" + drinkType  +"\r\n";
    }
}
