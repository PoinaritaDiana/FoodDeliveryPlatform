package models;

public class BeverageProduct extends Product{
    private boolean alcoholicDrink;
    private String drinkType;

    public BeverageProduct(String productID,String name, String description, float price, float rating, float preparationTime,
                           boolean alcoholicDrink, String type, String restaurantId){
        super(productID,name,description,price,rating,preparationTime,restaurantId);
        this.alcoholicDrink = alcoholicDrink;
        this.drinkType = type;
    }

    /*
    @Override
    public String toString() {
        return "Beverage Product no." + getProductId() + "\r\n" +
                "\t Product Name: " + getProductName() + "- price: " + getPrice() + ", rating=" + getRating() + "\r\n" +
                "\t Description='" + getDescription() + '\'' +  "\r\n" +
                "\t Alcoholic Drink=" + alcoholicDrink + "Drink Type='" + drinkType  +"\r\n";
    }
     */


    @Override
    public String toString(){
        return String.format("<html> %s <br> %s <br> Rating: %s <br> Price: %s <html>",
                getProductName(), getDescription(), String.format("%.02f", getRating()), String.format("%.02f", getPrice()));
    }

    public String[] getObjectData(){
        String[] objectData = {productName, description, String.valueOf(price), String.valueOf(rating),
                String.valueOf(preparationTime), String.valueOf(alcoholicDrink), drinkType, restaurantId};
        return objectData;
    }
}
