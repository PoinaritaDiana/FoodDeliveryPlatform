package services;

import models.*;
import usersManagement.Customer;
import usersManagement.DeliveryPerson;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public final class Database implements AutoCloseable{
    private static Database database = null;
    private final Connection connection;


    public static Database getDatabaseInstance(){
        if (database == null) {
            try {
                database = new Database();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return database;
    }


    private Database() throws Exception {
        connection = DriverManager.getConnection("jdbc:derby:deliverEatDB;create=true");
        createTables();
    }


    private void insertData(String tableName) throws Exception {
        switch(tableName) {
            case "restaurants":
                for (String[] restaurant : readDataFromCsv("restaurants.csv"))
                    createRestaurant(restaurant);
                break;
            case "customers":
                for (String[] customer : readDataFromCsv("customers.csv"))
                    createCustomer(customer);
                break;
            case "delivery_people":
                for (String[] deliveryPerson : readDataFromCsv("deliveryPeople.csv"))
                    createDeliveryPerson(deliveryPerson);
                break;
            case "food_products":
                for (String[] product : readDataFromCsv("products.csv")) {
                    if (product[0].charAt(0) == 'F')
                        createFoodProduct(product);
                }
                break;
            case "beverage_products":
                for (String[] product : readDataFromCsv("products.csv")) {
                    if (product[0].charAt(0) == 'B')
                        createBeverageProduct(product);
                }
                break;
        }
    }


    private void createTables() throws Exception {
        ResultSet results = connection.getMetaData().getTables(null, null, null, new String[]{"TABLE"});
        List<String> databaseTablesName = new ArrayList<>();
        while(results.next())
            databaseTablesName.add(results.getString("TABLE_NAME").toLowerCase());

        if (databaseTablesName.size() != 7){
            HashMap<String, String> tableStatements = new HashMap<>();
            tableStatements.put("restaurants", "CREATE TABLE restaurants (id varchar(36) primary key, name varchar(50), address varchar(200), " +
                    "type varchar(30), phonenumber varchar(11), rating float(2), deliveryPrice float(2))");
            tableStatements.put("orders", "CREATE TABLE orders (id varchar(36) primary key, clientId varchar(50), deliveryPersonId varchar(50), " +
                    "totalPrice float(2), payment varchar(10), deliveryAddress varchar(200), preparationTime float(2), createTime varchar(100))");
            tableStatements.put("customers", "CREATE TABLE customers (id varchar(36) primary key, lastName varchar(50), firstName varchar(50), " +
                    "phoneNumber varchar(11), email varchar(30), username varchar(20), password varchar(30))");
            tableStatements.put("delivery_people", "CREATE TABLE delivery_people (id varchar(36) primary key, lastName varchar(50), firstName varchar(50), " +
                    "phoneNumber varchar(11), email varchar(30), car varchar(10), username varchar(20), password varchar(30))");
            tableStatements.put("food_products", "CREATE TABLE food_products (id varchar(36) primary key, name varchar(50), description varchar(200), " +
                    "price float(2), rating float(2), preparationTime float(2), category varchar(100), restaurantId varchar(36))");
            tableStatements.put("beverage_products", "CREATE TABLE beverage_products (id varchar(36) primary key, name varchar(50), description varchar(200), " +
                    "price float(2), rating float(2), preparationTime float(50), alcoholic varchar(50), type varchar(50), restaurantId varchar(36))");
            tableStatements.put("cart_items", "CREATE TABLE cart_items (productId varchar(36), productQuantity int, orderId varchar(36))");

            for(Map.Entry<String, String> table : tableStatements.entrySet()) {
                boolean found = databaseTablesName.contains(table.getKey());
                if (!found) {
                    connection.createStatement().execute(table.getValue());
                    insertData(table.getKey());
                }
            }
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }



    // ETAPA 3
    // -------- OPERATII DE TIP CRUD --------

    // ------ Customer ------
    public Customer createCustomer(String[] object) throws Exception {
        Customer customer = new Customer(object[0],object[1], object[2], object[3], object[4],object[5],object[6]);
        PreparedStatement statement = connection.prepareStatement("INSERT INTO customers VALUES (?,?,?,?,?,?,?)");
        for(int parameterIndex = 1; parameterIndex<=7; parameterIndex++ )
            statement.setString(parameterIndex,object[parameterIndex-1]);
        if(statement.executeUpdate() == 1){
            return customer;
        }
        return null;
    }

    public List<Customer> readCustomer() throws Exception {
        ResultSet results = connection.createStatement().executeQuery("SELECT * FROM customers");
        List<Customer> customers = new ArrayList<>();
        while(results.next())
            customers.add(new Customer(results.getString(1),results.getString(2),
                    results.getString(3),results.getString(4),results.getString(5),
                    results.getString(6),results.getString(7)));
        return customers;
    }

    public boolean updateCustomer(String id, String[] object) throws Exception {
        System.out.println("In database: " + Arrays.toString(object));
        PreparedStatement statement = connection.prepareStatement("UPDATE customers SET lastName = ?, firstName = ?, phoneNumber = ?, email = ?, username = ?, password = ? WHERE id = ?");
        for(int parameterIndex = 1; parameterIndex<=6; parameterIndex++ )
            statement.setString(parameterIndex,object[parameterIndex-1]);
        statement.setString(7,id);
        return statement.executeUpdate() == 1;
    }

    public boolean deleteCustomer(String id) throws Exception {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM customers WHERE id = ?");
        statement.setString(1, id);
        return statement.executeUpdate() == 1;
    }



    // ------ Delivery Person ------
    public DeliveryPerson createDeliveryPerson(String[] object) throws Exception {
        DeliveryPerson deliveryPerson = new DeliveryPerson(object[0],object[1], object[2], object[3], object[4],object[5],object[6],object[7]);
        PreparedStatement statement = connection.prepareStatement("INSERT INTO delivery_people VALUES (?,?,?,?,?,?,?,?)");
        for(int parameterIndex = 1; parameterIndex<=8; parameterIndex++ )
            statement.setString(parameterIndex,object[parameterIndex-1]);
        if(statement.executeUpdate() == 1)
            return deliveryPerson;
        return null;
    }

    public List<DeliveryPerson> readDeliveryPerson() throws Exception {
        ResultSet results = connection.createStatement().executeQuery("SELECT * FROM delivery_people");
        List<DeliveryPerson> deliveryPeople = new ArrayList<>();
        while(results.next())
            deliveryPeople.add(new DeliveryPerson(results.getString(1),results.getString(2),
                    results.getString(3),results.getString(4),results.getString(5),
                    results.getString(6),results.getString(7),results.getString(8)));
        return deliveryPeople;
    }

    public boolean updateDeliveryPerson(String id, String[] object) throws Exception {
        PreparedStatement statement = connection.prepareStatement("UPDATE delivery_people SET lastName = ?, firstName = ?, phoneNumber = ?, email = ?, car = ? ,username = ?, password = ? WHERE id = ?");
        for(int parameterIndex = 1; parameterIndex<=7; parameterIndex++ )
            statement.setString(parameterIndex,object[parameterIndex-1]);
        statement.setString(8,id);
        return statement.executeUpdate() == 1;
    }

    public boolean deleteDeliveryPerson(String id) throws Exception {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM delivery_people WHERE id = ?");
        statement.setString(1, id);
        return statement.executeUpdate() == 1;
    }



    // ------ Restaurant ------
    public Restaurant createRestaurant(String[] restaurant) throws Exception {
        Restaurant newRestaurant = new Restaurant(restaurant[0],restaurant[1], restaurant[2], restaurant[3], restaurant[4],
                Float.parseFloat(restaurant[5]),Float.parseFloat(restaurant[6]));
        PreparedStatement statement = connection.prepareStatement("INSERT INTO restaurants VALUES (?,?,?,?,?,?,?)");

        for(int parameterIndex = 1; parameterIndex<=5; parameterIndex++ )
            statement.setString(parameterIndex,restaurant[parameterIndex-1]);
        statement.setFloat(6,Float.parseFloat(restaurant[5]));
        statement.setFloat(7,Float.parseFloat(restaurant[6]));
        if(statement.executeUpdate() == 1)
            return newRestaurant;
        return null;
    }

    public List<Restaurant> readRestaurant() throws Exception {
        List<Restaurant> restaurants = new ArrayList<>();
        ResultSet results = connection.createStatement().executeQuery("SELECT * FROM restaurants");
        while(results.next())
            restaurants.add(new Restaurant(results.getString(1),results.getString(2),
                    results.getString(3),results.getString(4),results.getString(5),
                    results.getFloat(6),results.getFloat(7)));
        return restaurants;
    }

    public boolean updateRestaurant(String id, String[] object) throws Exception {
        PreparedStatement statement = connection.prepareStatement("UPDATE restaurants SET name = ?, address = ?, type = ?, phonenumber = ?, rating = ?, deliveryPrice = ? WHERE id = ?");
        for(int parameterIndex = 1; parameterIndex<=4; parameterIndex++ )
            statement.setString(parameterIndex,object[parameterIndex-1]);
        statement.setFloat(5,Float.parseFloat(object[4]));
        statement.setFloat(6,Float.parseFloat(object[5]));
        statement.setString(7,id);
        return statement.executeUpdate() == 1;
    }

    public boolean deleteRestaurant(String id) throws Exception {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM restaurants WHERE id = ?");
        statement.setString(1, id);
        return statement.executeUpdate() == 1;
    }



    // ------ Order ------
    public Order createOrder(String[] object) throws Exception {
        Order newOrder = new Order(object[0],object[1], object[2], Float.parseFloat(object[3]), object[4],
                object[5],Float.parseFloat(object[6]), object[7]);
        PreparedStatement statement = connection.prepareStatement("INSERT INTO orders VALUES (?,?,?,?,?,?,?,?)");

        for(int parameterIndex = 1; parameterIndex<=8; parameterIndex++ )
            if(parameterIndex == 4 ||  parameterIndex == 7 )
                statement.setFloat(parameterIndex,Float.parseFloat(object[parameterIndex-1]));
            else
                statement.setString(parameterIndex,object[parameterIndex-1]);

        if(statement.executeUpdate() == 1)
            return newOrder;
        return null;
    }

    public List<Order> readOrder() throws Exception {
        ResultSet results = connection.createStatement().executeQuery("SELECT * FROM orders");
        List<Order> orders = new ArrayList<>();
        while(results.next())
            orders.add(new Order(results.getString(1),results.getString(2),
                    results.getString(3),results.getFloat(4),results.getString(5),
                    results.getString(6),results.getFloat(7), results.getString(8)));
        return orders;
    }

    public boolean updateOrder(String id, String[] object) throws Exception {
        PreparedStatement statement = connection.prepareStatement("UPDATE orders SET clientId = ?, deliveryPersonId = ?, " +
                "totalPrice = ?, payment = ?,  deliveryAddress = ?, preparationTime = ?, createTime = ? WHERE id = ?");

        for(int parameterIndex = 1; parameterIndex<=7; parameterIndex++ )
            if(parameterIndex == 3 ||  parameterIndex == 6 )
                statement.setFloat(parameterIndex,Float.parseFloat(object[parameterIndex-1]));
            else
                statement.setString(parameterIndex,object[parameterIndex-1]);
        statement.setString(8,id);
        return statement.executeUpdate() == 1;
    }

    public boolean deleteOrder(String id) throws Exception {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM orders WHERE id = ?");
        statement.setString(1, id);
        return statement.executeUpdate() == 1;
    }



    // ------ Cart Item ------
    public List<String[]> readCartItem() throws Exception {
        ResultSet results = connection.createStatement().executeQuery("SELECT * FROM cart_items");
        List<String[]> cartItems = new ArrayList<>();
        while(results.next()) {
            String [] cartItem = {results.getString(1), String.valueOf(results.getInt(2)), results.getString(3)};
            cartItems.add(cartItem);
        }
        return cartItems;
    }

    public void createCartItem(String[] object) throws Exception {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO cart_items VALUES (?,?,?)");
        statement.setString(1,object[0]);
        statement.setInt(2,Integer.parseInt(object[1]));
        statement.setString(3,object[2]);
        statement.executeUpdate();
    }

    public boolean deleteCartItem(String orderId) throws Exception {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM cart_items WHERE orderId = ?");
        statement.setString(1, orderId);
        return statement.executeUpdate() == 1;
    }



    // ------ Food Product ------
    public FoodProduct createFoodProduct(String[] object) throws Exception {
        FoodProduct newFoodProduct = new FoodProduct(object[0],object[1], object[2],
                Float.parseFloat(object[3]), Float.parseFloat(object[4]), Float.parseFloat(object[5]),
                object[6], object[7]);
        PreparedStatement statement = connection.prepareStatement("INSERT INTO food_products VALUES (?,?,?,?,?,?,?,?)");

        for(int parameterIndex = 1; parameterIndex<=8; parameterIndex++ )
            if(parameterIndex == 4 ||  parameterIndex == 5 || parameterIndex == 6 )
                statement.setFloat(parameterIndex,Float.parseFloat(object[parameterIndex-1]));
            else
                statement.setString(parameterIndex,object[parameterIndex-1]);

        if(statement.executeUpdate() == 1)
            return newFoodProduct;
        return null;
    }

    public List<FoodProduct> readFoodProduct() throws Exception {
        List<FoodProduct> foodProducts = new ArrayList<>();
        ResultSet results = connection.createStatement().executeQuery("SELECT * FROM food_products");
        while(results.next())
            foodProducts.add(new FoodProduct(results.getString(1),results.getString(2),
                    results.getString(3),results.getFloat(4),results.getFloat(5),
                    results.getFloat(6),results.getString(7),results.getString(8)));
        return foodProducts;
    }

    public boolean updateFoodProduct(String id, String[] object) throws Exception {
        PreparedStatement statement = connection.prepareStatement("UPDATE food_products SET name = ?, description = ?, price = ?, " +
                "rating = ?, preparationTime = ?, category = ?, restaurantId = ? WHERE id = ?");
        for(int parameterIndex = 1; parameterIndex<=7; parameterIndex++ )
            if(parameterIndex == 3 ||  parameterIndex == 4 || parameterIndex == 5 )
                statement.setFloat(parameterIndex,Float.parseFloat(object[parameterIndex-1]));
            else
                statement.setString(parameterIndex,object[parameterIndex-1]);
        statement.setString(8,id);
        return statement.executeUpdate() == 1;
    }

    public boolean deleteFoodProduct(String id) throws Exception {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM food_products WHERE id = ?");
        statement.setString(1, id);
        return statement.executeUpdate() == 1;
    }



    // ------ Beverage Product ------
    public BeverageProduct createBeverageProduct(String[] object) throws Exception {
        BeverageProduct newBeverageProduct = new BeverageProduct(object[0],object[1], object[2],
                Float.parseFloat(object[3]), Float.parseFloat(object[4]), Float.parseFloat(object[5]),
                Boolean.parseBoolean(object[6]), object[7], object[8]);
        PreparedStatement statement = connection.prepareStatement("INSERT INTO beverage_products VALUES (?,?,?,?,?,?,?,?,?)");

        for(int parameterIndex = 1; parameterIndex<=9; parameterIndex++ )
            if(parameterIndex == 4 ||  parameterIndex == 5 || parameterIndex == 6 )
                statement.setFloat(parameterIndex,Float.parseFloat(object[parameterIndex-1]));
            else
                statement.setString(parameterIndex,object[parameterIndex-1]);

        if(statement.executeUpdate() == 1)
            return newBeverageProduct;
        return null;
    }

    public List<BeverageProduct> readBeverageProduct() throws Exception {
        List<BeverageProduct> beverageProducts = new ArrayList<>();
        ResultSet results = connection.createStatement().executeQuery("SELECT * FROM beverage_products");
        while(results.next())
            beverageProducts.add(new BeverageProduct(results.getString(1),results.getString(2),
                    results.getString(3),results.getFloat(4),results.getFloat(5),
                    results.getFloat(6),Boolean.valueOf(results.getString(7)),results.getString(8),
                    results.getString(9)));
        return beverageProducts;
    }

    public boolean updateBeverageProduct(String id, String[] object) throws Exception {
        PreparedStatement statement = connection.prepareStatement("UPDATE beverage_products SET name = ?, description = ?, price = ?, " +
                "rating = ?, preparationTime = ?, alcoholic = ?, type = ?, restaurantId = ? WHERE id = ?");
        for(int parameterIndex = 1; parameterIndex<=8; parameterIndex++ )
            if(parameterIndex == 3 ||  parameterIndex == 4 || parameterIndex == 5 )
                statement.setFloat(parameterIndex,Float.parseFloat(object[parameterIndex-1]));
            else
                statement.setString(parameterIndex,object[parameterIndex-1]);

        statement.setString(9,id);
        return statement.executeUpdate() == 1;
    }

    public boolean deleteBeverageProduct(String id) throws Exception {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM beverage_products WHERE id = ?");
        statement.setString(1, id);
        return statement.executeUpdate() == 1;
    }





    // ETAPA 2
    // ------------- Read/Write/Update CSV methods ------------------
    public List<String []> readDataFromCsv(String csvFile){
        try {
            // List of lines / arrays with the data from the file
            List<String []> data = Files.readAllLines(Paths.get("data/"+csvFile)).stream()
                                    //.filter(Objects::nonNull)
                                    .map(line -> line.split(","))
                                    .collect(Collectors.toList());
            return data;
        } catch (IOException e) {
            System.out.println("The file does not exist.");
        }
        return null;
    }


    public static void writeDataToCsv(String csvFile, String[] data){
        try {
            File file = new File ("data/"+csvFile);
            if (!file.exists())
                file.createNewFile();

            FileWriter csvWriter = new FileWriter(file, true);
            csvWriter.write(String.join(",", data));
            csvWriter.write("\n");
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void removeUserFromCsv(String userID){
        String csvFile= "customers.csv";
        if(userID.charAt(0)=='D')
            csvFile="deliveryPeople.csv";
        try {
            // Get users except user with userID
            List<String []> data = Files.readAllLines(Paths.get("data/"+csvFile)).stream()
                    .map(line -> line.split(","))
                    .filter(line -> !line[0].equals(userID))
                    .collect(Collectors.toList());

            FileWriter csvWriter = new FileWriter("data/"+csvFile, false);
            data.stream().forEach(line -> {
                try {
                    csvWriter.write(String.join(",", line));
                    csvWriter.write("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void updateCsv(String csvFile, String id, float rating){
        try {
            // Get data from file
            List<String []> data = Files.readAllLines(Paths.get("data/"+csvFile)).stream()
                    .map(line -> line.split(","))
                    .collect(Collectors.toList());

            // Delete file
            File file= new File ("data/"+csvFile);
            file.delete();

            // Write updated data to file
            for(String[] line: data) {
                if (line[0].equals(id))
                    line[5]= String.valueOf(rating);
                Database.writeDataToCsv(csvFile,line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
