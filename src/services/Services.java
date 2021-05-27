package services;

import auxiliar.RestaurantComparator;
import models.*;
import usersManagement.*;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


final public class Services {
    private static Services servicesInstance = null;
    private Database database = Database.getDatabaseInstance();
    private Audit audit = Audit.getAuditInstance();

    private List<Restaurant> restaurantList = new ArrayList<>();
    private List<Customer> customersList = new ArrayList<>();
    private List<DeliveryPerson> deliveryPeopleList = new ArrayList<>();

    // The current user that is logged in
    // If currentUser == null, then there is no user logged in
    private User currentUser = null;
    private Admin admin = null;


    // Get Services instance
    public static Services getServicesInstance(){
        if (servicesInstance == null)
            servicesInstance = new Services();
        return servicesInstance;
    }

    private Services(){
        try {
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public User getCurrentUser() {
        return currentUser;
    }

    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }


    // Check if user is logged in
    public boolean checkUserLoggedIn(){
        return (currentUser!=null);
    }

    // Check if admin is logged in
    public boolean checkAdminLoggedIn(){
        return (admin!=null);
    }

    // Display the message that the user receives the first time he enters the application
    public void displayWelcomeMessage() throws Exception {
        System.out.println("---------------------------------");
        System.out.println("WELCOME TO DeliverEAT");
        System.out.println("You have to log in first!");
        System.out.println("Press enter to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        adminAuthentication();
        if(admin==null) {
            // Redirecting to log in page
            userAuthenticationForm();
            if (currentUser == null) {
                System.out.println("You have to log in first!");
            }
        }
    }



    // ----- DISPLAY OPTIONS -----
    public void displayUserOptions(){
        if(currentUser instanceof Customer)
            displayCustomerOptions();
        else
            displayDeliveryOptions();
    }

    // Display application functionalities for a customer
    private void displayCustomerOptions(){
        audit.auditService("displayCustomerOptions");
        System.out.println();
        System.out.println("Food Delivery Platform Option Menu (choose number):");
        System.out.println("1. Log in as another user");
        System.out.println("2. Register a new user");
        System.out.println("3. Log out");
        System.out.println("4. See list of all restaurants");
        System.out.println("5. Search restaurant by name");
        System.out.println("6. Add rating to a specific restaurant");
        System.out.println("7. See menu of a specific restaurant");
        System.out.println("8. Add rating to a specific product");
        System.out.println("9. Search product by name in a specific restaurant");
        System.out.println("10. Search product by name in all restaurants");
        System.out.println("11. Place order");
        System.out.println("12. See your order history");
        System.out.println("13. Add product to your cart");
        System.out.println("14. See your cart");
        System.out.println("15. Empty cart");
        System.out.println("16. Remove product from cart");
        System.out.println("17. Remove quantity of product from your cart");
        System.out.println("18. Show option menu");
        System.out.println("19. Delete account");
        System.out.println("\n");
    }

    // Display application functionalities for a delivery person
    private void displayDeliveryOptions(){
        audit.auditService("displayDeliveryOptions");
        System.out.println();
        System.out.println("Food Delivery Platform Option Menu (choose number):");
        System.out.println("1. Log in as another user");
        System.out.println("2. Register a new user");
        System.out.println("3. Log out");
        System.out.println("4. Show option menu");
        System.out.println("5. Delete account");
        System.out.println("\n");
    }

    // Display application functionalities for admin
    public void displayAdminOptions(){
        System.out.println();
        System.out.println("Food Delivery Platform Option Menu (choose number):");
        System.out.println("1. Log in as another user");
        System.out.println("2. Register a new user");
        System.out.println("3. Log out");
        System.out.println("4. Show list of all restaurants");
        System.out.println("5. Add restaurant");
        System.out.println("6. Edit restaurant");
        System.out.println("7. Delete restaurant");
        System.out.println("8. Show list of all food products");
        System.out.println("9. Add food product");
        System.out.println("10. Edit food product");
        System.out.println("11. Delete food product");
        System.out.println("12. Show list of all beverage products");
        System.out.println("13. Add beverage product");
        System.out.println("14. Edit beverage product");
        System.out.println("15. Delete beverage product");
        System.out.println("16. Show option menu");
        System.out.println("\n");

    }


    // ----- INTERFACE -----
    public void userInterface(String userOption) throws Exception {
        if(admin!=null)
            adminInterface(userOption);
        else {
            if (currentUser instanceof Customer) {
                customerInterface(userOption);
            } else {
                deliveryPersonInterface(userOption);
            }
        }
    }

    private void customerInterface(String userOption) throws Exception {
        switch (userOption) {
            case "1": {
                logOut();
                userAuthenticationForm();
                if (currentUser != null) {
                    displayUserOptions();
                }
                else{
                    if(admin!=null)
                        displayAdminOptions();
                }
                break;
            }
            case "2": {
                logOut();
                userRegistrationForm();
                if (currentUser != null) {
                    displayUserOptions();
                }
                else{
                    if(admin!=null)
                        displayAdminOptions();
                }
                break;
            }
            case "3": {
                logOut();
                System.out.println("Thank you for using DeliverEAT");
                break;
            }
            case "4": {
                System.out.println("See our restaurant:");
                showAllRestaurants();
                break;
            }
            case "5": {
                System.out.println("Enter name of the restaurant:");
                Scanner nameInput = new Scanner(System.in);
                String restaurantName = nameInput.nextLine();
                searchRestaurantByName(restaurantName);
                break;
            }
            case "6": {
                System.out.println("Enter name of the restaurant to add rating:");
                Scanner nameInput = new Scanner(System.in);
                String restaurantName = nameInput.nextLine();
                Restaurant searchedRestaurant = getRestaurantByName(restaurantName);
                if (searchedRestaurant != null) {
                    System.out.println("Enter rating:");
                    Scanner ratingInput = new Scanner(System.in);
                    float newRating = Float.parseFloat(ratingInput.nextLine());
                    addRestaurantRating(searchedRestaurant.getRestaurantId(), newRating);
                } else
                    System.out.println(String.format("No result for \"%s\".Try again.", restaurantName));
                break;
            }
            case "7": {
                System.out.println("Enter name of the restaurant:");
                Scanner nameInput = new Scanner(System.in);
                String restaurantName = nameInput.nextLine();
                Restaurant searchedRestaurant = getRestaurantByName(restaurantName);
                if (searchedRestaurant != null)
                    showRestaurantMenu(searchedRestaurant);
                else
                    System.out.println(String.format("No result for \"%s\".Try again.", restaurantName));
                break;
            }
            case "8": {
                System.out.println("Enter productID to add rating: ");
                Scanner productIdInput = new Scanner(System.in);
                Product product = getProductById(productIdInput.nextLine());
                if (product != null) {
                    System.out.println("Enter rating:");
                    Scanner ratingInput = new Scanner(System.in);
                    float newRating = Float.parseFloat(ratingInput.nextLine());
                    addRatingToProduct(product, newRating);
                } else {
                    System.out.println("We're sorry. This product does not exist :(");
                }
                break;
            }
            case "9": {
                Scanner nameInput = new Scanner(System.in);
                System.out.println("Enter name of the product:");
                String productName = nameInput.nextLine();

                System.out.println("Enter name of the restaurant:");
                String restaurantName = nameInput.nextLine();
                Restaurant searchedRestaurant = getRestaurantByName(restaurantName);
                if(searchedRestaurant!=null)
                    searchProductInRestaurant(searchedRestaurant, productName);
                else
                    System.out.println("There is no " + restaurantName + " restaurant :(");
                break;
            }
            case "10": {
                System.out.println("Enter name of the product:");
                Scanner nameInput = new Scanner(System.in);
                String productName = nameInput.nextLine();
                searchProductInRestaurants(productName);
                break;
            }
            case "11": {
                placeNewOrder();
                break;
            }
            case "12": {
                showOrderHistory();
                break;
            }
            case "13": {
                System.out.println("Add product to your cart by productID: ");
                Scanner productIdInput = new Scanner(System.in);
                Product product = getProductById(productIdInput.nextLine());
                if (product != null) {
                    System.out.println("Add quantity for the selected product: ");
                    Scanner quantityInput = new Scanner(System.in);
                    int quantity = Integer.parseInt(quantityInput.nextLine());
                    if (quantity <= 0)
                        System.out.println("Quantity must be a positive integer!");
                    else
                        addProductInMyCart(product, quantity);
                } else {
                    System.out.println("We're sorry. This product does not exist :(");
                }
                break;
            }
            case "14": {
                showProductsMyCart();
                break;
            }
            case "15": {
                clearMyCart();
                break;
            }
            case "16": {
                System.out.println("Remove product from your cart by productID: ");
                Scanner productIdInput = new Scanner(System.in);
                CartItem cartItem = getCartItemByProductID(productIdInput.nextLine());
                if (cartItem != null)
                    removeProductFromCart(cartItem);
                else
                    System.out.println("There is no product in your cart with this ID");
                break;
            }
            case "17": {
                System.out.println("Remove product from your cart by productID: ");
                Scanner productIdInput = new Scanner(System.in);
                CartItem cartItem = getCartItemByProductID(productIdInput.nextLine());
                if (cartItem != null) {
                    int currentQuantity = cartItem.getProductQuantity();
                    System.out.println("Quantity you want to decrease for this product: ");
                    Scanner quantityInput = new Scanner(System.in);
                    int quantity = Integer.parseInt(quantityInput.nextLine());
                    if (currentQuantity - quantity < 0) {
                        System.out.println("The quantity of product in the cart is less than the value entered!");
                    } else {
                        if (currentQuantity == quantity)
                            removeProductFromCart(cartItem);
                        else
                            decreaseProductQuantity(cartItem, quantity);
                    }
                } else {
                    System.out.println("There is no product in your cart with this ID");
                }
                break;
            }
            case "18": {
                displayCustomerOptions();
                break;
            }
            case "19": {
                deleteAccount();
                break;
            }
            default: {
                System.out.println("No option with number " + userOption);
                break;
            }
        }
    }

    private void deliveryPersonInterface(String userOption) throws Exception{
        switch (userOption) {
            case "1": {
                logOut();
                userAuthenticationForm();
                if (currentUser != null) {
                    displayUserOptions();
                }
                else{
                    if(admin!=null)
                        displayAdminOptions();
                }
                break;
            }
            case "2": {
                logOut();
                userRegistrationForm();
                if (currentUser != null) {
                    displayUserOptions();
                }
                else{
                    if(admin!=null)
                        displayAdminOptions();
                }
                break;
            }
            case "3": {
                logOut();
                System.out.println("Thank you for using DeliverEAT");
                break;
            }
            case "4": {
                displayDeliveryOptions();
                break;
            }
            case "5": {
                deleteAccount();
                break;
            }
            default: {
                System.out.println("No option with number " + userOption);
                break;
            }
        }
    }

    private void adminInterface(String adminOption) throws Exception{
        switch (adminOption) {
            case "1": {
                logOut();
                userAuthenticationForm();
                if (currentUser != null) {
                    displayUserOptions();
                }
                else{
                    if(admin!=null)
                        displayAdminOptions();
                }
                break;
            }
            case "2": {
                logOut();
                userRegistrationForm();
                if (currentUser != null) {
                    displayUserOptions();
                }
                else{
                    if(admin!=null)
                        displayAdminOptions();
                }
                break;
            }
            case "3": {
                logOut();
                System.out.println("Thank you for using DeliverEAT");
                break;
            }
            case "4": {
                audit.auditService("showAllRestaurants");
                System.out.println("See our restaurant:");
                showAllRestaurants().forEach(restaurant -> System.out.println(restaurant));
                break;
            }
            case "5": {
                audit.auditService("addRestaurant");
                addRestaurant();
                break;
            }
            case "6": {
                audit.auditService("editRestaurant");
                editRestaurant();
                break;
            }
            case "7": {
                audit.auditService("deleteRestaurant");
                System.out.println("Id of restaurant to be deleted:");
                Scanner idInput = new Scanner(System.in);
                String restaurantId = idInput.nextLine();
                if(database.deleteRestaurant(restaurantId)) {
                    System.out.println("Restaurant deleted");
                    Iterator itr = restaurantList.iterator();
                    while (itr.hasNext()) {
                        if (((Restaurant) itr.next()).getRestaurantId().equals(restaurantId))
                            itr.remove();
                    }
                }
                break;
            }
            case "8": {
                audit.auditService("showFoodProducts");
                database.readFoodProduct().forEach(product -> System.out.println(product));
                break;
            }
            case "9": {
                audit.auditService("addFoodProduct");
                addFoodProduct();
                break;
            }
            case "10": {
                audit.auditService("editFoodProduct");
                editFoodProduct();
                break;
            }
            case "11": {
                audit.auditService("deleteFoodProduct");
                System.out.println("Id of food product to be deleted:");
                Scanner idInput = new Scanner(System.in);
                String productId = idInput.nextLine();
                if(database.deleteFoodProduct(productId))
                    System.out.println("Product deleted");
                break;
            }
            case "12": {
                audit.auditService("showBeverageProducts");
                database.readBeverageProduct().forEach(product -> System.out.println(product));
                break;
            }
            case "13": {
                audit.auditService("addBeverageProduct");
                addBeverageProduct();
                break;
            }
            case "14": {
                audit.auditService("editBeverageProduct");
                editBeverageProduct();
                break;
            }
            case "15": {
                audit.auditService("deleteBeverageProduct");
                System.out.println("Id of beverage product to be deleted:");
                Scanner idInput = new Scanner(System.in);
                String productId = idInput.nextLine();
                if(database.deleteBeverageProduct(productId))
                    System.out.println("Product deleted");
                break;
            }
            case "16": {
                audit.auditService("displayAdminOptions");
                displayAdminOptions();
                break;
            }
            default: {
                System.out.println("No option with number " + adminOption);
                break;
            }
        }
    }



    // ---- METHODS FOR ADMIN ----
    private void addRestaurant() throws Exception {
        List<String> restaurantData = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        System.out.println("ID:");
        restaurantData.add(input.nextLine());
        System.out.println("Name:");
        restaurantData.add(input.nextLine());
        System.out.println("Address :");
        restaurantData.add(input.nextLine());
        System.out.println("Type:");
        restaurantData.add(input.nextLine());
        System.out.println("Phone number: ");
        restaurantData.add(input.nextLine());
        System.out.println("Rating: ");
        restaurantData.add(input.nextLine());
        System.out.println("Delivery Price: ");
        restaurantData.add(input.nextLine());

        restaurantList.add(database.createRestaurant(restaurantData.toArray(new String[0])));
        Collections.sort(restaurantList, new RestaurantComparator());
    }

    private void addFoodProduct() throws Exception {
        List<String> productData = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        System.out.println("ID:");
        productData.add(input.nextLine());
        System.out.println("Name:");
        productData.add(input.nextLine());
        System.out.println("Description :");
        productData.add(input.nextLine());
        System.out.println("Price:");
        productData.add(input.nextLine());
        System.out.println("Rating: ");
        productData.add(input.nextLine());
        System.out.println("Preparation time: ");
        productData.add(input.nextLine());
        System.out.println("Category: ");
        productData.add(input.nextLine());
        System.out.println("Restaurant Id: ");
        String restaurantId = input.nextLine();
        productData.add(restaurantId);

        for(Restaurant restaurant: restaurantList){
            if(restaurant.getRestaurantId().equalsIgnoreCase(restaurantId))
                restaurant.getMenu().addFoodProductInMenu(database.createFoodProduct(productData.toArray(new String[0])));
        }
    }

    private void editFoodProduct() throws Exception {
        List<String> productData = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        System.out.println("ID of product to be updated:");
        String productId = input.nextLine();
        System.out.println("Name:");
        productData.add(input.nextLine());
        System.out.println("Description :");
        productData.add(input.nextLine());
        System.out.println("Price:");
        productData.add(input.nextLine());
        System.out.println("Rating: ");
        productData.add(input.nextLine());
        System.out.println("Preparation time: ");
        productData.add(input.nextLine());
        System.out.println("Category: ");
        productData.add(input.nextLine());
        System.out.println("Restaurant Id: ");
        productData.add(input.nextLine());

        if(database.updateFoodProduct(productId,productData.toArray(new String[0])))
            System.out.println("Product updated");
    }

    private void editBeverageProduct() throws Exception {
        List<String> productData = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        System.out.println("ID of product to be updated:");
        String productId = input.nextLine();
        System.out.println("Name:");
        productData.add(input.nextLine());
        System.out.println("Description :");
        productData.add(input.nextLine());
        System.out.println("Price:");
        productData.add(input.nextLine());
        System.out.println("Rating: ");
        productData.add(input.nextLine());
        System.out.println("Preparation time: ");
        productData.add(input.nextLine());
        System.out.println("Alcoholic Drink: ");
        productData.add(input.nextLine());
        System.out.println("Type: hot/cold? ");
        productData.add(input.nextLine());
        System.out.println("Restaurant Id: ");
        productData.add(input.nextLine());

        if(database.updateBeverageProduct(productId,productData.toArray(new String[0])))
            System.out.println("Product updated");
    }


    private void addBeverageProduct() throws Exception {
        List<String> productData = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        System.out.println("ID:");
        productData.add(input.nextLine());
        System.out.println("Name:");
        productData.add(input.nextLine());
        System.out.println("Description :");
        productData.add(input.nextLine());
        System.out.println("Price:");
        productData.add(input.nextLine());
        System.out.println("Rating: ");
        productData.add(input.nextLine());
        System.out.println("Preparation time: ");
        productData.add(input.nextLine());
        System.out.println("Alcoholic Drink: ");
        productData.add(input.nextLine());
        System.out.println("Type: hot/cold? ");
        productData.add(input.nextLine());
        System.out.println("Restaurant Id: ");
        String restaurantId = input.nextLine();
        productData.add(restaurantId);

        for(Restaurant restaurant: restaurantList){
            if(restaurant.getRestaurantId().equalsIgnoreCase(restaurantId))
                restaurant.getMenu().addBeverageProductInMenu(database.createBeverageProduct(productData.toArray(new String[0])));
        }
    }

    private void editRestaurant() throws Exception {
        List<String> restaurantData = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        String restaurantId;
        System.out.println("ID of restaurant to be updated:");
        restaurantId = input.nextLine();
        System.out.println("Name:");
        restaurantData.add(input.nextLine());
        System.out.println("Address :");
        restaurantData.add(input.nextLine());
        System.out.println("Type:");
        restaurantData.add(input.nextLine());
        System.out.println("Phone number: ");
        restaurantData.add(input.nextLine());
        System.out.println("Rating: ");
        restaurantData.add(input.nextLine());
        System.out.println("Delivery Price: ");
        restaurantData.add(input.nextLine());

        if(database.updateRestaurant(restaurantId,restaurantData.toArray(new String[0]))) {
            System.out.println("Restaurant updated");
            Collections.sort(restaurantList, new RestaurantComparator());
        }
    }




    // ----------- REGISTRATION, AUTHENTICATION AND LOGOUT ---------

    // Search customer in the list by username
    private boolean searchCustomer(String username){
        return getCustomer(username)!=null;
    }

    // Check the correct password entered for the given customer
    private boolean checkCustomerPassword(String username,String password){
        for(Customer customer: customersList)
            if(customer.getUsername().equals(username) && customer.getPassword().equals(password))
                return true;
        return false;
    }

    // Get the customer with the given username
    private Customer getCustomer(String username){
        Customer searchedCustomer = null;
        for(Customer customer: customersList)
            if (customer.getUsername().equals(username))
                searchedCustomer = customer;
        return searchedCustomer;
    }


    // Search delivery person in the list by username
    private boolean searchDeliveryPerson(String username){
        return getDeliveryPerson(username)!=null;
    }

    // Check the correct password entered for the given delivery person
    private boolean checkDeliveryPassword(String username,String password){
        for(DeliveryPerson deliveryPerson: deliveryPeopleList)
            if(deliveryPerson.getUsername().equals(username) && deliveryPerson.getPassword().equals(password))
                return true;
        return false;
    }

    // Get the delivery person with the given username
    private DeliveryPerson getDeliveryPerson(String username){
        DeliveryPerson searchedDeliveryPerson = null;
        for(DeliveryPerson deliveryPerson: deliveryPeopleList)
            if (deliveryPerson.getUsername().equals(username))
                searchedDeliveryPerson = deliveryPerson;
        return searchedDeliveryPerson;
    }


    // Log out function
    public void logOut(){
        audit.auditService("logOut");
        System.out.println("Logged out");
        currentUser = null;
        admin = null;
    }


    public void adminAuthentication() throws Exception {
        Scanner recordInput = new Scanner(System.in);
        System.out.println("LOG IN PAGE");

        while(true) {
            System.out.println("Log in as admin? Yes or No:");
            String adminLogIn = recordInput.nextLine();
            if ("yes".equalsIgnoreCase(adminLogIn)) {
                System.out.println("Username: ");
                String username = recordInput.nextLine();
                System.out.println("Password: ");
                String password = recordInput.nextLine();

                boolean adminUsername = username.equalsIgnoreCase(Admin.getUSERNAME());
                boolean adminPassword = password.equalsIgnoreCase(Admin.getPASSWORD());

                if (adminUsername && adminPassword) {
                    admin = Admin.getAdminInstance();
                    audit.auditService("userAuthentication");
                    break;
                } else {
                    if (!adminUsername) {
                        System.out.println("This username does not exist.");
                        System.out.println("Try again? Type YES or NO");
                        String answer = recordInput.nextLine();
                        if ("no".equalsIgnoreCase(answer)) {
                            System.out.println("Redirect to registration page? Type YES or NO");
                            String answerRedirect = recordInput.nextLine();
                            if ("yes".equalsIgnoreCase(answerRedirect)) {
                                userRegistrationForm();
                                break;
                            }
                            else {
                                System.out.println("Exit");
                                break;
                            }
                        }
                    }
                    else {
                        System.out.println("Invalid password. Try again:");
                    }
                }
            }
            else
                break;
        }
    }


    // Display authentication form for user
    public void userAuthenticationForm() throws Exception {
        Scanner recordInput = new Scanner(System.in);
        System.out.println("LOG IN PAGE");

        while(true) {
            System.out.println("Log in as a customer? Yes or No:");
            String customerLogIn = recordInput.nextLine();

            // Log in as a customer
            if ("yes".equalsIgnoreCase(customerLogIn)) {
                System.out.println("Username: ");
                String username = recordInput.nextLine();
                System.out.println("Password: ");
                String password = recordInput.nextLine();

                boolean existCustomer = searchCustomer(username);
                boolean correctPassword = checkCustomerPassword(username,password);

                // If username exists and password is correct, then log in customer
                if (existCustomer && correctPassword) {
                    currentUser = getCustomer(username);
                    System.out.println(String.format("Welcome back, %s %s", currentUser.getLastName(), currentUser.getFirstName()));
                    audit.auditService("userAuthentication");
                    break;
                } else {
                    // Username does not exist, need to register or try again
                    if (!existCustomer) {
                        System.out.println("This user does not exist.");
                        System.out.println("Try again? Type YES or NO");
                        String answer = recordInput.nextLine();
                        if("no".equalsIgnoreCase(answer)){
                            System.out.println("Redirect to registration page? Type YES or NO");
                            String answerRedirect = recordInput.nextLine();
                            if("yes".equalsIgnoreCase(answerRedirect))
                                userRegistrationForm();
                            else{
                                System.out.println("Exit");
                                break;
                            }
                        }
                    }
                    // Valid username, but incorrect password
                    else {
                        System.out.println("Invalid password. Try again:");
                    }
                }
            }
            // Log in not as a customer
            else {
                System.out.println("Log in as a delivery person? Yes or No:");
                String deliveryPersonLogIn = recordInput.nextLine();
                // Log in as a delivery person
                if ("yes".equalsIgnoreCase(deliveryPersonLogIn)) {
                    System.out.println("Username: ");
                    String username = recordInput.nextLine();
                    System.out.println("Password: ");
                    String password = recordInput.nextLine();

                    boolean existDelivery = searchDeliveryPerson(username);
                    boolean correctPassword = checkDeliveryPassword(username,password);

                    // If username exists and password is correct, then log in delivery person
                    if (existDelivery && correctPassword) {
                        currentUser = getDeliveryPerson(username);
                        System.out.println(String.format("Welcome back, %s %s", currentUser.getLastName(), currentUser.getFirstName()));
                        audit.auditService("userAuthentication");
                        break;
                    } else {
                        // Username does not exist, need to register or try again
                        if (!existDelivery) {
                            System.out.println("This user does not exist.");
                            System.out.println("Try again? Type YES or NO");
                            String answer = recordInput.nextLine();
                            if("no".equalsIgnoreCase(answer)){
                                System.out.println("Redirect to registration page? Type YES or NO");
                                String answerRedirect = recordInput.nextLine();
                                if("yes".equalsIgnoreCase(answerRedirect))
                                    userRegistrationForm();
                                else{
                                    System.out.println("Exit");
                                    break;
                                }
                            }
                        }
                        // Valid username, but incorrect password
                        else {
                            System.out.println("Invalid password. Try again:");
                        }
                    }
                }
                else{
                    System.out.println("Redirecting to Registration page...");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    userRegistrationForm();
                    break;
                }
            }
        }
    }


    // Display registration form for a new user
    public void userRegistrationForm() throws Exception {
        Scanner recordInput = new Scanner(System.in);
        while(true) {
            System.out.println("Create new account? Type YES to continue: ");
            String newAccount = recordInput.nextLine();

            if ("yes".equalsIgnoreCase(newAccount)) {
                System.out.println("Type 1 for Customer or 2 for Delivery Person:");
                int userType = Integer.parseInt(recordInput.nextLine());

                System.out.println("Last name: ");
                String lastName = recordInput.nextLine();
                System.out.println("First name: ");
                String firstName = recordInput.nextLine();
                System.out.println("Phone Number:");
                String phoneNumber = recordInput.nextLine();
                System.out.println("Email: ");
                String email = recordInput.nextLine();
                System.out.println("Username: ");
                String username = recordInput.nextLine();
                while (!(getCustomer(username)==null && getDeliveryPerson(username)==null)){
                    System.out.println("Username already exists. Try again");
                    System.out.println("Username: ");
                    username = recordInput.nextLine();
                }

                System.out.println("Password: ");
                String password = recordInput.nextLine();

                // Account for new customer
                if(userType == 1){
                    /*
                    Customer newCustomer = new Customer(User.generateID("C"),lastName, firstName, phoneNumber, email, username, password);
                    customersList.add(newCustomer);
                    Database.writeDataToCsv("customers.csv",newCustomer.getObjectData());
                    */
                    String[] customerData = {User.generateID("C"),lastName, firstName, phoneNumber, email, username, password};
                    customersList.add(database.createCustomer(customerData));
                    audit.auditService("userRegistration");

                    System.out.println("Redirecting to Log In page...");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    userAuthenticationForm();
                    break;
                }
                // Account for new delivery person
                else{
                    System.out.println("Car registration number: ");
                    String carRegistrationNumber = recordInput.nextLine();
                    /*
                    DeliveryPerson newDeliveryPerson = new DeliveryPerson(User.generateID("D"),lastName, firstName, phoneNumber, email,carRegistrationNumber, username, password);
                    deliveryPeopleList.add(newDeliveryPerson);
                    Database.writeDataToCsv("deliveryPeople.csv",newDeliveryPerson.getObjectData());
                    */
                    String[] deliveryPersonData = {User.generateID("D"),lastName, firstName, phoneNumber, email,carRegistrationNumber, username, password};
                    deliveryPeopleList.add(database.createDeliveryPerson(deliveryPersonData));
                    audit.auditService("userRegistration");

                    System.out.println("Redirecting to Log In page...");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    userAuthenticationForm();
                    break;
                }
            }
            else {
                System.out.println("Already have an account? Type YES to redirect to Log In Page: ");
                String redirectAuthentication = recordInput.nextLine();
                if ("yes".equalsIgnoreCase(redirectAuthentication)) {
                    System.out.println("Redirecting to Log In page...");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    userAuthenticationForm();
                    break;
                } else {
                    System.out.println("Invalid input! Type NO to exit or YES to continue");
                    String input = recordInput.nextLine();
                    if ("no".equalsIgnoreCase(input)){
                        System.out.println("Exit");
                        break;
                    }
                }
            }
        }
    }

    // Delete account
    public void deleteAccount() throws Exception {
        if (currentUser instanceof Customer) {
            customersList.remove(customersList.indexOf(currentUser));
            database.deleteCustomer(currentUser.getUserId());
        }
        else {
            deliveryPeopleList.remove(deliveryPeopleList.indexOf(currentUser));
            database.deleteDeliveryPerson(currentUser.getUserId());
        }
        System.out.println("Account deleted");
        audit.auditService("deleteAccount");
        currentUser = null;

        //Database.removeUserFromCsv(currentUser.getUserId());
    }




    // ------ AUXILIARY METHODS FOR GUI ------
    public boolean editCustomerAccount(String[] customerData){
        System.out.println("In SERVICES: " + Arrays.toString(customerData));
        try {
            audit.auditService("editCustomerAccount");
            currentUser.update(customerData);
            return database.updateCustomer(currentUser.getUserId(), customerData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registerCustomer(String [] userData){
        boolean check = true;
        if(getCustomer(userData[4])==null && getDeliveryPerson(userData[4])==null) {
            String[] customerData = {User.generateID("C"), userData[0], userData[1], userData[2], userData[3], userData[4], userData[5]};
            try {
                customersList.add(database.createCustomer(customerData));
            } catch (Exception e) {
                e.printStackTrace();
            }
            audit.auditService("userRegistration");
        }
        else
            check = false;
        return check;
    }

    public int logInUser(String username, String password){
        int check = 0;
        boolean existCustomer = searchCustomer(username);
        boolean correctPassword = checkCustomerPassword(username,password);
        if (existCustomer && correctPassword) {
            currentUser = getCustomer(username);
            audit.auditService("userAuthentication");
        } else {
            if (!existCustomer)
                check = 1;
            else
                check = 2; // Valid username, but incorrect password
        }
        return check;
    }




    // ---------- RESTAURANT FUNCTIONS ---------

    /*
    public void showAllRestaurants(){
        audit.auditService("showAllRestaurants");
        for(Restaurant restaurant:restaurantList)
            System.out.println(restaurant);
    }
    */

    public List<String> showAllRestaurants(){
        audit.auditService("showAllRestaurants");
        List<String> restaurants = new ArrayList<>();
        restaurantList.forEach(restaurant -> restaurants.add(restaurant.toString()));
        return restaurants;
    }

    public Restaurant searchRestaurantByName(String restaurantName){
        audit.auditService("searchRestaurantByName");
        for(Restaurant restaurant:restaurantList)
            if((restaurant.getRestaurantName()).equalsIgnoreCase(restaurantName)) {
                System.out.println(restaurant);
                return restaurant;
            }
        System.out.println(String.format("No result for \"%s\".Try again.",restaurantName));
        return null;
    }

    public void addRestaurantRating(String restaurantId, float newRating) throws Exception {
        audit.auditService("addRestaurantRating");
        for(Restaurant restaurant:restaurantList)
            if(restaurant.getRestaurantId().equals(restaurantId)) {
                restaurant.addRestaurantRating(newRating);
                System.out.println("Thank you for your appreciation!");
                database.updateRestaurant(restaurantId, restaurant.getData());
                //Database.updateCsv("restaurants.csv", restaurantId,restaurant.getRating());
                break;
            }
        Collections.sort(restaurantList, new RestaurantComparator());
    }

    public Restaurant getRestaurantByID(String restaurantId){
        Restaurant searchedRestaurant = null;
        for(Restaurant restaurant:restaurantList)
            if(restaurant.getRestaurantId().equals(restaurantId)) {
                searchedRestaurant = restaurant;
                break;
            }
        return searchedRestaurant;
    }

    private Restaurant getRestaurantByName(String restaurantName){
        Restaurant searchedRestauarant = null;
        for(Restaurant restaurant:restaurantList)
            if(restaurant.getRestaurantName().equalsIgnoreCase(restaurantName)) {
                searchedRestauarant = restaurant;
                break;
            }
        return searchedRestauarant;
    }



    //----------- CART FUNCTIONS -------------

    private CartItem getCartItemByProductID(String productId){
        CartItem searchedCartItem = null;
        for(CartItem cartItem: ((Customer)currentUser).getCart().getCartProducts())
            if(cartItem.getProduct().getProductId().equals(productId)){
                searchedCartItem = cartItem;
                break;
            }
        return searchedCartItem;
    }

    public boolean checkOrderRestaurant(Product product){
        String restaurantId = getRestaurantIdFromProduct(product);
        Cart userCart = ((Customer) currentUser).getCart();
        if(userCart.getProductsFromRestaurantID() != null && !userCart.getProductsFromRestaurantID().equals(restaurantId))
           return false;
        return true;
    }

    public void addProductInCart(Product product, int quantity){
        audit.auditService("addProductInCart");
        Cart userCart = ((Customer) currentUser).getCart();
        String restaurantId = getRestaurantIdFromProduct(product);
        if(checkOrderRestaurant(product)){
            userCart.addProductToCart(product, quantity, restaurantId);
        }
        else {
            clearMyCart();
            userCart.addProductToCart(product, quantity, restaurantId);
        }
    }

    public void addProductInMyCart(Product product, int quantity){
        audit.auditService("addProductInMyCart");
        Cart userCart = ((Customer) currentUser).getCart();
        String restaurantId = getRestaurantIdFromProduct(product);
        if(userCart.getProductsFromRestaurantID() == null || userCart.getProductsFromRestaurantID().equals(restaurantId)){
            userCart.addProductToCart(product, quantity, restaurantId);
            System.out.println("Product added in your cart");
        }
        else{
            System.out.println("You have already selected products from a different restaurant. If you continue, your cart and selections will be canceled.");
            System.out.println("Do you want to continue with the newly selected product?");
            Scanner recordInput = new Scanner(System.in);
            String answer = recordInput.nextLine();
            if("yes".equalsIgnoreCase(answer)){
                clearMyCart();
                userCart.addProductToCart(product, quantity,restaurantId);
                System.out.println("Product added in your cart");
            }
        }
    }

    public List<CartItem> showProductsMyCart(){
        audit.auditService("showProductsMyCart");
        return ((Customer) currentUser).getCart().showCartItems();
    }

    public void clearMyCart(){
        audit.auditService("clearMyCart");
        ((Customer) currentUser).getCart().clearCart();
        System.out.println("Empty cart!");
    }

    public void decreaseProductQuantity(CartItem cartItem, int quantity){
        audit.auditService("decreaseProductQuantity");
        cartItem.addQuantity(-quantity);
    }

    public void removeProductFromCart(CartItem cartItem){
        audit.auditService("removeProductFromCart");
        ((Customer) currentUser).getCart().removeCartItem(cartItem);
        System.out.println("Product removed from your cart");
    }

    public boolean removeProductFromCart(CartItem cartItem, int quantity) {
        audit.auditService("removeProductFromCart");
        int currentQuantity = cartItem.getProductQuantity();
        if (currentQuantity - quantity < 0) {
            System.out.println("The quantity of product in the cart is less than the value entered!");
            return false;
        } else {
            if (currentQuantity == quantity)
                removeProductFromCart(cartItem);
            else
                decreaseProductQuantity(cartItem, quantity);
        }
        return true;
    }




    //----------- PRODUCT AND MENU FUNCTIONS ----------

    // Get the product with the given id from all restaurants
    private Product getProductById(String productId){
        Product searchedProduct = null;
        for(Restaurant restaurant:restaurantList){
            Product productInMenu = restaurant.getMenu().searchProductInMenuByID(productId);
            if(productInMenu!=null) {
                searchedProduct = productInMenu;
                break;
            }
        }
        return searchedProduct;
    }

    // Add rating to a product
    public void addRatingToProduct(Product product, float rating) throws Exception {
        audit.auditService("addRatingToProduct");
        product.updateRating(rating);
        if(product.getProductId().charAt(0)=='F')
            database.updateFoodProduct(product.getProductId(), product.getObjectData());
        else
            database.updateBeverageProduct(product.getProductId(), product.getObjectData());

        //Database.updateCsv("products.csv", product.getProductId(),product.getRating());
    }

    public void showRestaurantMenu(Restaurant restaurant){
        audit.auditService("showRestaurantMenu");
        restaurant.getMenu().showMenu();
    }

    public Menu getRestaurantMenuById(String restaurantId){
        Restaurant searchedRestaurant = null;
        for(Restaurant restaurant:restaurantList)
            if(restaurant.getRestaurantId().equals(restaurantId)) {
                searchedRestaurant = restaurant;
                break;
            }
        if (searchedRestaurant!=null)
            return searchedRestaurant.getMenu();
        return null;
    }

    public void searchProductInRestaurant(Restaurant restaurant, String productName){
        audit.auditService("searchProductInRestaurant");
        if(restaurant.getMenu().searchProductInMenuByName(productName)!=null)
            restaurant.getMenu().displayProductInMenu(productName);
        else
            System.out.println(String.format("We're sorry! Product %s does not exist in %s restaurant",productName, restaurant.getRestaurantName()));
    }

    public void searchProductInRestaurants(String productName){
        audit.auditService("searchProductInRestaurants");
        boolean noRestaurant = true;
        for(Restaurant restaurant: restaurantList)
            if(restaurant.getMenu().searchProductInMenuByName(productName)!=null) {
                System.out.println(restaurant);
                noRestaurant = false;
            }
        if(noRestaurant == true)
            System.out.println("The product does not exist in any restaurant");
    }


    public String getRestaurantIdFromProduct(Product product){
        String restaurantID = null;
        for(Restaurant restaurant: restaurantList)
            if(restaurant.getMenu().searchProductInMenuByID(product.getProductId())==product){
                restaurantID = restaurant.getRestaurantId();
                break;
            }
        return restaurantID;
    }





    // -------------- ORDER FUNCTIONS ----------
    public String[] getCartPrice(){
        String[] price ={"0","0"};
        if(currentUser instanceof Customer) {
            Cart userCart = ((Customer) currentUser).getCart();
            if (!userCart.isEmpty()) {
                price[0] = String.valueOf(userCart.getCartTotalPrice());
                price[1] = String.valueOf(getRestaurantByID(userCart.getProductsFromRestaurantID()).getDeliveryPrice());
            }
        }
        return price;
    }


    public void showOrderHistory() {
        audit.auditService("showOrderHistory");
        if(currentUser instanceof Customer) {
            ((Customer) currentUser).displayOrdersHistory();
        }
    }

    public void placeNewOrder() throws Exception {
        audit.auditService("placeNewOrder");
        if(currentUser instanceof Customer) {
            Cart userCart = ((Customer) currentUser).getCart();

            if(!userCart.isEmpty()) {
                float totalPrice = userCart.getCartTotalPrice();
                float deliveryPrice =  getRestaurantByID(userCart.getProductsFromRestaurantID()).getDeliveryPrice();
                System.out.println("Products: " + totalPrice + " LEI");
                System.out.println("Delivery: " + deliveryPrice + " LEI");
                System.out.println("Total: " + (totalPrice+deliveryPrice) + " LEI");

                String deliveryPersonId = null;
                while (deliveryPersonId == null) {
                    System.out.println("Searching for an available delivery person. Please wait..");
                    deliveryPersonId = searchAvailableDeliveryPerson();
                }

                Scanner recordInput = new Scanner(System.in);
                System.out.println("Payment type: cash or card?");
                String userInput = recordInput.nextLine();
                String payment = userInput;

                System.out.println("Delivery Address: ");
                String deliveryAddress = recordInput.nextLine();

                long preparationTime = userCart.getTotalPreparationTime() + 10;

                String clientId = currentUser.getUserId();

                System.out.println("Do you want to complete the order placement?");
                Scanner checkOut = new Scanner(System.in);
                String checkOutInput = checkOut.nextLine();

                if("yes".equalsIgnoreCase(checkOutInput)) {
                    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    /*
                    Order newOrder = new Order(Order.generateID(),clientId, deliveryPersonId, totalPrice+deliveryPrice,
                            payment, deliveryAddress, preparationTime, LocalDateTime.now().format(dateFormat));
                    List<CartItem> cartProducts = userCart.getCartProducts();
                    for (CartItem cartItem : cartProducts)
                        cartItem.setOrderId(newOrder.getOrderId());
                    newOrder.setCartProducts(cartProducts);

                    // Write data in csv
                    Database.writeDataToCsv("orders.csv", newOrder.getObjectData());
                    cartProducts.stream().forEach(cartItem -> Database.writeDataToCsv("cartItems.csv", cartItem.getObjectData(newOrder.getOrderId())));
                    */
                    String [] orderData = {Order.generateID(),clientId, deliveryPersonId, String.valueOf(totalPrice+deliveryPrice),
                            payment, deliveryAddress, String.valueOf(preparationTime), LocalDateTime.now().format(dateFormat)};
                    Order newOrder = database.createOrder(orderData);

                    List<CartItem> cartProducts = userCart.getCartProducts();
                    for (CartItem cartItem : cartProducts)
                        cartItem.setOrderId(newOrder.getOrderId());

                    newOrder.setCartProducts(cartProducts);
                    for(CartItem item: cartProducts)
                        database.createCartItem(item.getObjectData());

                    userCart.clearCart();

                    // Wait preparation time
                    System.out.println("Please wait. Your order will be ready in about " + (preparationTime - 10) + " minutes");
                    try {
                        TimeUnit.SECONDS.sleep(preparationTime - 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Food is on the way! You will be happy in 10 minutes!");
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Food delivered! Enjoy!");

                    // Add order to history
                    addOrderHistory(newOrder);
                    // Update status for delivery person
                    for (DeliveryPerson deliveryPerson : deliveryPeopleList)
                        if (deliveryPerson.getUserId() == deliveryPersonId)
                            deliveryPerson.updateAvailabilityStatus();
                }
                else{
                    System.out.println("The order has not been placed.");
                }
            }
            else{
                System.out.println("Your carty is empty. Add an item before place order!");
            }
        }
    }


    public void placeNewOrder(String payment, String deliveryAddress) throws Exception {
        audit.auditService("placeNewOrder");
        Cart userCart = ((Customer) currentUser).getCart();
        float totalPrice = userCart.getCartTotalPrice();
        float deliveryPrice =  getRestaurantByID(userCart.getProductsFromRestaurantID()).getDeliveryPrice();

        String deliveryPersonId = null;
        while (deliveryPersonId == null) {
            JOptionPane.showMessageDialog(null, "Searching for an available delivery person. Please wait..",
                    "Place new order", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Searching for an available delivery person. Please wait..");
            deliveryPersonId = searchAvailableDeliveryPerson();
        }

        long preparationTime = userCart.getTotalPreparationTime() + 10;

        String clientId = currentUser.getUserId();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String [] orderData = {Order.generateID(),clientId, deliveryPersonId, String.valueOf(totalPrice+deliveryPrice),
                    payment, deliveryAddress, String.valueOf(preparationTime), LocalDateTime.now().format(dateFormat)};
        Order newOrder = database.createOrder(orderData);
        List<CartItem> cartProducts = userCart.getCartProducts();
        for (CartItem cartItem : cartProducts)
            cartItem.setOrderId(newOrder.getOrderId());

        newOrder.setCartProducts(cartProducts);
        for(CartItem item: cartProducts)
            database.createCartItem(item.getObjectData());

        userCart.clearCart();

        JOptionPane.showMessageDialog(null, "Please wait. Your order will be ready in about " + (preparationTime - 10) + " minutes",
                "Order status", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("Please wait. Your order will be ready in about " + (preparationTime - 10) + " minutes");

        String finalDeliveryPersonId = deliveryPersonId;
        Thread orderThread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(preparationTime - 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(null, "Food is on the way! You will be happy in 10 minutes!",
                    "Order status", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Food is on the way! You will be happy in 10 minutes!");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(null, "Food delivered! Enjoy!",
                    "Order status", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Food delivered! Enjoy!");

            addOrderHistory(newOrder);
            for (DeliveryPerson deliveryPerson : deliveryPeopleList)
                if (deliveryPerson.getUserId() == finalDeliveryPersonId)
                    deliveryPerson.updateAvailabilityStatus();
        });

        orderThread.start();
    }


    private void addOrderHistory(Order order){
        if(currentUser instanceof Customer) {
            ((Customer) currentUser).addOrderHistoryList(order);
        }
    }

    private String searchAvailableDeliveryPerson(){
        String deliveryPersonId = null;
        for(DeliveryPerson deliveryPerson: deliveryPeopleList) {
            String status = deliveryPerson.getStatus();
            if(status.equals("available")) {
                deliveryPersonId = deliveryPerson.getUserId();
                deliveryPerson.updateAvailabilityStatus();
                break;
            }
        }
        return deliveryPersonId;
    }




    // ------------ DATABASE/ INITIALIZATION --------------

    // ETAPA 3
    private void loadData() throws Exception {
        deliveryPeopleList = database.readDeliveryPerson();
        customersList = database.readCustomer();

        // Get last ID from deliveryPeople and last ID from customers and set next ID for new user
        int lastDeliveryPeopleID = Integer.parseInt(deliveryPeopleList.get(deliveryPeopleList.size()-1).getUserId().substring(1));
        int lastCustomersID = Integer.parseInt(customersList.get(customersList.size()-1).getUserId().substring(1));
        User.setLastUserId(Math.max(lastDeliveryPeopleID,lastCustomersID));
        User.setLastUserId(Math.max(lastDeliveryPeopleID,lastCustomersID));

        // Load products - grouping by restaurantId
        Map<String,List<BeverageProduct>> beverageProductsByRestaurant = database.readBeverageProduct().stream().collect(Collectors.groupingBy(object -> object.getRestaurantId()));
        Map<String,List<FoodProduct>> foodProductsByRestaurant = database.readFoodProduct().stream().collect(Collectors.groupingBy(object -> object.getRestaurantId()));

        restaurantList = database.readRestaurant();
        for(Restaurant restaurant: restaurantList)
            restaurant.setMenu(new Menu(foodProductsByRestaurant.get(restaurant.getRestaurantId()), beverageProductsByRestaurant.get(restaurant.getRestaurantId())));

        // Sort restaurants by rating
        Collections.sort(restaurantList, new RestaurantComparator());

        // Load cartItems - grouping by orderId
        Map<String,List<String[]>> cartItemsByOrder = database.readCartItem().stream().collect(Collectors.groupingBy(object -> object[object.length-1]));

        // Load orders - grouping by clientID
        Map<String,List<Order>> ordersByClient =  database.readOrder().stream().collect(Collectors.groupingBy(object -> object.getClientId()));

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Last id for orders
        int lastOrderId = 0;

        // Add cartItems in order, add order to customer orderHistoryList
            for(Map.Entry<String,List<Order>> entry: ordersByClient.entrySet()){
            String customerID = entry.getKey();

            for(Order order: entry.getValue()){
                String orderID = order.getOrderId();
                lastOrderId = Math.max(lastOrderId, Integer.parseInt(orderID.substring(3)));

                // cartItems list for current order
                List<CartItem> orderItems = new ArrayList<>();
                for(String[] cartItem: cartItemsByOrder.get(orderID))
                    orderItems.add(new CartItem(getProductById(cartItem[0]),Integer.parseInt(cartItem[1])));
                order.setCartProducts(orderItems);

                // Add current order to customer's list
                for(Customer customer: customersList)
                    if(customer.getUserId().equals(customerID))
                        customer.addOrderHistoryList(order);
            }
        }
        // Set next ID for orders
        Order.setLastOrderId(lastOrderId);
    }



    /*
    // ETAPA 2
    private void loadDataCSV() {
        String [] csvFiles = {"deliveryPeople.csv", "customers.csv","cartItems.csv", "orders.csv", "products.csv", "restaurants.csv"};

        // Load delivery People
        List<String []> data = database.readDataFromCsv(csvFiles[0]);
        deliveryPeopleList = data.stream()
                                 .map(object -> new DeliveryPerson(object[0],object[1], object[2], object[3], object[4],object[5],object[6],object[7]))
                                 .collect(Collectors.toList());

        // Load customers
        data = database.readDataFromCsv(csvFiles[1]);
        customersList = data.stream()
                            .map(object -> new Customer(object[0],object[1], object[2], object[3], object[4],object[5],object[6]))
                            .collect(Collectors.toList());

        // Get last ID from deliveryPeople and last ID from customers and set next ID for new user
        int lastDeliveryPeopleID = Integer.parseInt(deliveryPeopleList.get(deliveryPeopleList.size()-1).getUserId().substring(1));
        int lastCustomersID = Integer.parseInt(customersList.get(customersList.size()-1).getUserId().substring(1));
        User.setLastUserId(Math.max(lastDeliveryPeopleID,lastCustomersID));
        User.setLastUserId(Math.max(lastDeliveryPeopleID,lastCustomersID));


        // Load products - grouping by restaurantId
        data = database.readDataFromCsv(csvFiles[4]);
        Map<String,List<String[]>> productsByRestaurant = data.stream()
                .collect(Collectors.groupingBy(object -> object[object.length-1]));

        // Create menu (list of products) for each restaurant
        Map<String, List<Product>> menuByRestaurant = new HashMap<>();
        for(Map.Entry<String,List<String[]>> entry: productsByRestaurant.entrySet()){
            menuByRestaurant.put(entry.getKey(), new ArrayList<Product>());
            for(String[] product: entry.getValue()){
                if(product[0].charAt(0)=='F')
                    menuByRestaurant.get(entry.getKey()).add(new FoodProduct(product[0],product[1], product[2], Float.parseFloat(product[3]),
                            Float.parseFloat(product[4]), Float.parseFloat(product[5]),product[6]));
                else
                    menuByRestaurant.get(entry.getKey()).add(new BeverageProduct(product[0],product[1], product[2], Float.parseFloat(product[3]),
                            Float.parseFloat(product[4]), Float.parseFloat(product[5]),Boolean.parseBoolean(product[6]),product[7]));
            }
        }


        // Load restaurants
        data = database.readDataFromCsv(csvFiles[5]);
        for(String[] restaurant : data)
            restaurantList.add(new Restaurant(restaurant[0],restaurant[1], restaurant[2], restaurant[3], restaurant[4],
                    Float.parseFloat(restaurant[5]),Float.parseFloat(restaurant[6]), new Menu(menuByRestaurant.get(restaurant[0]))));

        // Sort restaurants by rating
        Collections.sort(restaurantList, new RestaurantComparator());



        // Load cartItems - grouping by orderId
        data = database.readDataFromCsv(csvFiles[2]);
        Map<String,List<String[]>> cartItemsByOrder = data.stream()
                        .collect(Collectors.groupingBy(object -> object[object.length-1]));


        // Load orders - grouping by clientID
        data = database.readDataFromCsv(csvFiles[3]);
        Map<String,List<String[]>> ordersByClient = data.stream()
                .collect(Collectors.groupingBy(object -> object[1]));


        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Last id for orders
        int lastOrderId = 0;

        // Add cartItems in order, add order to customer orderHistoryList
        for(Map.Entry<String,List<String[]>> entry: ordersByClient.entrySet()){
            String customerID = entry.getKey();

            for(String[] order: entry.getValue()){
                String orderID = order[0];
                lastOrderId = Math.max(lastOrderId, Integer.parseInt(orderID.substring(3)));

                // cartItems list for current order
                List<CartItem> orderItems = new ArrayList<>();
                for(String[] cartItem: cartItemsByOrder.get(orderID))
                    orderItems.add(new CartItem(getProductById(cartItem[0]),Integer.parseInt(cartItem[1])));

                // Add current order to customer's list
                for(Customer customer: customersList)
                    if(customer.getUserId().equals(customerID))
                        customer.addOrderHistoryList((new Order(order[0],order[1], order[2], Float.parseFloat(order[3]), order[4],order[5],
                                Float.parseFloat(order[6]), order[7],orderItems)));
            }
        }

        // Set next ID for orders
        Order.setLastOrderId(lastOrderId);
    }
    */


/*
    // ETAPA 1
    private void databaseInitialization(){
        //Customers
        Customer customer1 =  new Customer("Poinarita","Diana","0123456789",
                "poinaritadiana@fmi.ro", "dianap", "password123");
        Customer customer2 =  new Customer("Ionescu","Maria","0123456789",
                "ionescumaria@fmi.ro", "mariai", "password123");
        Customer customer3 =  new Customer("Popescu","Mircea","0123456789",
                "popescumircea@fmi.ro", "mirceap", "password123");
        customersList.add(customer1);
        customersList.add(customer2);
        customersList.add(customer3);


        //Delivery People
        DeliveryPerson dperson1= new DeliveryPerson("Mihai","Andrei","0123456789",
                "mihaiandrei@fmi.ro", "PH20PHP","andreim","password123");
        deliveryPeopleList.add(dperson1);


        //Products
        String [] categories = {"vegan", "post"};
        Product product1 =  new FoodProduct("Cartofi","cartofi prajiti", 5f, 10f,categories);
        Product product2 =  new FoodProduct("Paste","paste carbonara", 25f, 20f,categories);
        Product product3 =  new BeverageProduct("Apa","apa plata", 2f, 0f,false,"cold");
        Product product4 =  new BeverageProduct("Suc","suc de portocale", 5f, 0f,false, "cold");


        //Menu
        Product [] p1 = {product1};
        Product [] p2 = {product3};
        Product [] p3 = {product2, product4};
        Menu menu1= new Menu(p1);
        Menu menu2 = new Menu(p2);
        Menu menu3 = new Menu(p3);

        //Restaurants
        Restaurant restaurant1 = new Restaurant("Nume", "Bucuresti, nr.1", "traditional","0725181617", 0F, 5f,menu1);
        Restaurant restaurant2 = new Restaurant("NumeRestaurant", "Bucuresti, nr.2", "asiatic","0766721812", 1F, 10f, menu2);
        Restaurant restaurant3 = new Restaurant("Test", "Bucuresti, nr.3", "italian","0718371971", 2F, 3f,menu3);
        restaurantList.add(restaurant1);
        restaurantList.add(restaurant2);
        restaurantList.add(restaurant3);
        Collections.sort(restaurantList, new RestaurantComparator());
    }*/


    // Private methods for testing data
    private void testCustomersList(){
        System.out.println("Customers:");
        for(Customer customer: customersList)
            System.out.println(customer);
    }

    private void testDeliveryPeopleList(){
        System.out.println("Delivery People:");
        for(DeliveryPerson deliveryPerson: deliveryPeopleList)
            System.out.println(deliveryPerson);
    }

    private void testRestaurantsList(){
        System.out.println("Restaurants:");
        for(Restaurant restaurant: restaurantList)
            System.out.println(restaurant);
    }

    private void testData(){
        System.out.println("-----------TEST DATA----------");
        testCustomersList();
        testDeliveryPeopleList();
        testRestaurantsList();
    }

}
