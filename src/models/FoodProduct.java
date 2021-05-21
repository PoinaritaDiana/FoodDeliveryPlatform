package models;

public class FoodProduct extends Product {
    private String category ;

    public FoodProduct(String productID,String name, String description, float price, float rating,
                       float preparationTime,String category, String restaurantId){
        super(productID,name,description,price, rating,preparationTime, restaurantId);
        this.category = category;
    }

    /*
    @Override
    public String toString() {
        return "Food Product no." + getProductId() + "\r\n" +
                "\t Product Name: " + getProductName() + "- price: " + getPrice() + ", rating=" + getRating() + "\r\n" +
                "\t Description='" + getDescription() + '\'' +  "\r\n" +
                "\t Preparation time=" + preparationTime + "minutes" +  "\r\n" +
                "\t Category: " + category +"\r\n";
    }
     */

    @Override
    public String toString(){
        return String.format("<html> %s <br> %s <br> Rating: %s <br> Price: %s <html>",
                getProductName(), getDescription(), String.format("%.02f", getRating()), String.format("%.02f", getPrice()));
    }

    public String[] getObjectData(){
        String[] objectData = {productName, description, String.valueOf(price), String.valueOf(rating), String.valueOf(preparationTime), category,  restaurantId};
        return objectData;
    }
}
