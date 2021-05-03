package services;

import auxiliar.RestaurantComparator;
import classes.*;
import usersManagement.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


final class Services {
    private static Services servicesInstance = null;

    private List<Restaurant> restaurantList = new ArrayList<>();
    private List<Customer> customersList = new ArrayList<>();
    private List<DeliveryPerson> deliveryPeopleList = new ArrayList<>();

    // The current user that is logged in
    // If currentUser == null, then there is no user logged in
    private User currentUser = null;

    Database database = Database.getDatabaseInstance();
    Audit audit = Audit.getAuditInstance();


    // Get Services instance (singleton)
    public static Services getServicesInstance(){
        if (servicesInstance == null)
            servicesInstance = new Services();
        return servicesInstance;
    }


    // Private constructor
    private Services(){
        // Load data (for testing functionality only)
        loadData();
        // Test data
        testData();
    }

    // Check if user is logged in
    public boolean checkUserLoggedIn(){
        return (currentUser!=null);
    }

    // Display the message that the user receives the first time he enters the application
    public void displayWelcomeMessage(){
        System.out.println("---------------------------------");
        System.out.println("WELCOME TO DeliverEAT");
        System.out.println("You have to log in first!");
        System.out.println("Press enter to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Redirecting to log in page
        userAuthenticationForm();

        if(currentUser!=null) {
            // Test registration of new user and currentUser
            System.out.println("CURRENT USER: " + currentUser);
        }
        else{
            System.out.println("You have to log in first!");
        }
    }

    public void displayUserOptions(){
        if(currentUser instanceof Customer)
            // Print available options for customer
            displayCustomerOptions();
        else
            displayDeliveryOptions();
    }

    public void userInterface(String userOption) {
        if (currentUser instanceof Customer) {
            switch (userOption) {
                case "1": {
                    logOut();
                    userAuthenticationForm();
                    if (currentUser != null) {
                        if(currentUser instanceof Customer)
                            displayCustomerOptions();
                        else
                            displayDeliveryOptions();
                    }
                    break;
                }
                case "2": {
                    logOut();
                    userRegistrationForm();
                    if (currentUser != null) {
                        if(currentUser instanceof Customer)
                            displayCustomerOptions();
                        else
                            displayDeliveryOptions();
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
                    showAllRestaurants();
                    break;
                }
                case "5": {
                    audit.auditService("searchRestaurant");
                    System.out.println("Enter name of the restaurant:");
                    Scanner nameInput = new Scanner(System.in);
                    String restaurantName = nameInput.nextLine();
                    searchRestaurant(restaurantName);
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
                        audit.auditService("addRestaurantRating");
                    } else
                        System.out.println(String.format("No result for \"%s\".Try again.", restaurantName));
                    break;
                }
                case "7": {
                    audit.auditService("showRestaurantMenu");
                    System.out.println("Enter name of the restaurant:");
                    Scanner nameInput = new Scanner(System.in);
                    String restaurantName = nameInput.nextLine();
                    // Return restaurant with this name
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
                        audit.auditService("addRatingToProduct");
                    } else {
                        System.out.println("We're sorry. This product does not exist :(");
                    }
                    break;
                }
                case "9": {
                    audit.auditService("searchProductInRestaurant");
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
                    audit.auditService("searchProductInRestaurants");
                    System.out.println("Enter name of the product:");
                    Scanner nameInput = new Scanner(System.in);
                    String productName = nameInput.nextLine();
                    searchProductInRestaurants(productName);
                    break;
                }
                case "11": {
                    placeNewOrder();
                    audit.auditService("placeNewOrder");
                    break;
                }
                case "12": {
                    audit.auditService("showOrderHistory");
                    showOrderHistory();
                    break;
                }
                case "13": {
                    audit.auditService("addProductInMyCart");
                    System.out.println("Add product to your cart by productID: ");
                    Scanner productIdInput = new Scanner(System.in);
                    // Get product by id
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
                    audit.auditService("showProductsMyCart");
                    showProductsMyCart();
                    break;
                }
                case "15": {
                    audit.auditService("clearMyCart");
                    clearMyCart();
                    break;
                }
                case "16": {
                    audit.auditService("removeProductFromCart");
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
                    audit.auditService("decreaseProductQuantity");
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
                    audit.auditService("displayCustomerOptions");
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
        } else {
            switch (userOption) {
                case "1": {
                    logOut();
                    userAuthenticationForm();
                    if (currentUser != null)
                        displayCustomerOptions();
                    break;
                }
                case "2": {
                    logOut();
                    userRegistrationForm();
                    if (currentUser != null)
                        displayCustomerOptions();
                    break;
                }
                case "3": {
                    logOut();
                    System.out.println("Thank you for using DeliverEAT");
                    break;
                }
                case "4": {
                    audit.auditService("displayDeliveryOptions");
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
    }





    //------------- AUXILIARY FUNCTIONS -------------

    // Display application functionalities for a customer
    private void displayCustomerOptions(){
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
        System.out.println();
        System.out.println("Food Delivery Platform Option Menu (choose number):");
        System.out.println("1. Log in as another user");
        System.out.println("2. Register a new user");
        System.out.println("3. Log out");
        System.out.println("4. Show option menu");
        System.out.println("5. Delete account");
        System.out.println("\n");
    }

    // Get the product with the given id from the logged in user cart
    private CartItem getCartItemByProductID(String productId){
        CartItem searchedCartItem = null;
        for(CartItem cartItem: ((Customer)currentUser).getCart().getCartProducts())
            if(cartItem.getProduct().getProductId().equals(productId)){
                searchedCartItem = cartItem;
                break;
            }
        return searchedCartItem;
    }

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

    // Get restaurant by name
    private Restaurant getRestaurantByName(String restaurantName){
        Restaurant searchedRestauarant = null;
        for(Restaurant restaurant:restaurantList)
            if(restaurant.getRestaurantName().equals(restaurantName)) {
                searchedRestauarant = restaurant;
                break;
            }
        return searchedRestauarant;
    }





    // ----------- REGISTRATION, AUTHENTICATION AND LOGOUT ---------

    // Search customer in the list by username
    private boolean searchCustomer(String username){
        for(Customer customer: customersList)
            if(customer.getUsername().equals(username))
                return true;
        return false;
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
        for(DeliveryPerson deliveryPerson: deliveryPeopleList)
            if(deliveryPerson.getUsername().equals(username))
                return true;
        return false;
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
    }

    // Display authentication form for user
    public void userAuthenticationForm(){
        Scanner recordInput = new Scanner(System.in);
        System.out.println("LOG IN PAGE");

        while(true) {
            System.out.println("Log in as a customer? Yes or No:");
            String customerLogIn = recordInput.nextLine();

            // Log in as a customer
            if ("yes".equalsIgnoreCase(customerLogIn)) {
                // Get username and password
                System.out.println("Username: ");
                String username = recordInput.nextLine();
                System.out.println("Password: ");
                String password = recordInput.nextLine();

                // Check username and password
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
                    // Get username and password
                    System.out.println("Username: ");
                    String username = recordInput.nextLine();
                    System.out.println("Password: ");
                    String password = recordInput.nextLine();

                    // Check username and password
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
    public void userRegistrationForm(){
        Scanner recordInput = new Scanner(System.in);
        while(true) {
            System.out.println("Create new account? Type YES to continue: ");
            String newAccount = recordInput.nextLine();

            // New account
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
                    Customer newCustomer = new Customer(User.generateID("C"),lastName, firstName, phoneNumber, email, username, password);
                    customersList.add(newCustomer);

                    audit.auditService("userRegistration");
                    Database.writeDataToCsv("customers.csv",newCustomer.getObjectData());

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
                    DeliveryPerson newDeliveryPerson = new DeliveryPerson(User.generateID("D"),lastName, firstName, phoneNumber, email,carRegistrationNumber, username, password);
                    deliveryPeopleList.add(newDeliveryPerson);

                    audit.auditService("userRegistration");
                    Database.writeDataToCsv("deliveryPeople.csv",newDeliveryPerson.getObjectData());

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
    private void deleteAccount(){
        if (currentUser instanceof Customer)
            customersList.remove(customersList.indexOf(currentUser));
        else
            deliveryPeopleList.remove(deliveryPeopleList.indexOf(currentUser));
        System.out.println("Account deleted");
        audit.auditService("deleteAccount");
        Database.removeUserFromCsv(currentUser.getUserId());
        currentUser = null;
    }





    // ---------- RESTAURANT FUNCTIONS ---------
    // Display list of all restaurants
    public void showAllRestaurants(){
        for(Restaurant restaurant:restaurantList)
            System.out.println(restaurant);
    }

    // Search for a restaurant
    public void searchRestaurant(String restaurantName){
        boolean found = false;
        for(Restaurant restaurant:restaurantList)
            if(restaurant.getRestaurantName().equals(restaurantName)) {
                System.out.println(restaurant);
                found = true;
            }
        if(found==false)
            System.out.println(String.format("No result for \"%s\".Try again.",restaurantName));
    }

    // Add rating to a restaurant
    public void addRestaurantRating(String restaurantId, float newRating){
        for(Restaurant restaurant:restaurantList)
            if(restaurant.getRestaurantId().equals(restaurantId)) {
                restaurant.addRestaurantRating(newRating);
                System.out.println("Thank you for your appreciation!");
                Database.updateCsv("restaurants.csv", restaurantId,restaurant.getRating());
                break;
            }

        Collections.sort(restaurantList, new RestaurantComparator());
    }

    // Get a restaurant by Id
    public Restaurant getRestaurantByID(String restaurantId){
        Restaurant searchedRestaurant = null;
        for(Restaurant restaurant:restaurantList)
            if(restaurant.getRestaurantId().equals(restaurantId)) {
                searchedRestaurant = restaurant;
                break;
            }
        return searchedRestaurant;
    }





    //----------- CART FUNCTIONS -------------
    // Add product to cart
    public void addProductInMyCart(Product product, int quantity){
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

    // Show products in cart
    public void showProductsMyCart(){
        ((Customer) currentUser).getCart().showCartItems();
    }

    // Empty cart
    public void clearMyCart(){
        ((Customer) currentUser).getCart().clearCart();
        System.out.println("Empty cart!");
    }

    // Change product quantity in cart
    public void decreaseProductQuantity(CartItem cartItem, int quantity){
        cartItem.addQuantity(-quantity);
    }

    // Remove product from cart
    public void removeProductFromCart(CartItem cartItem){
        ((Customer) currentUser).getCart().removeCartItem(cartItem);
        System.out.println("Product removed from your cart");
    }





    //----------- PRODUCT AND MENU FUNCTIONS ----------
    // Add rating to a product
    public void addRatingToProduct(Product product, float rating){
        product.updateRating(rating);
        Database.updateCsv("products.csv", product.getProductId(),product.getRating());
    }

    // Show menu for given restaurant
    public void showRestaurantMenu(Restaurant restaurant){
        restaurant.getMenu().showMenu();
    }

    // Search product in restaurant
    public void searchProductInRestaurant(Restaurant restaurant, String productName){
        boolean found = restaurant.getMenu().searchProductInMenu(productName);
        if(found==true)
            restaurant.getMenu().displayProductInMenu(productName);
        else
            System.out.println(String.format("We're sorry! Product %s does not exist in %s restaurant",productName, restaurant.getRestaurantName()));
    }


    // Display restaurants which prepares the given product
    public void searchProductInRestaurants(String productName){
        boolean noRestaurant = true;
        for(Restaurant restaurant: restaurantList)
            if(restaurant.getMenu().searchProductInMenu(productName)) {
                System.out.println(restaurant);
                noRestaurant = false;
            }
        if(noRestaurant == true)
            System.out.println("The product does not exist in any restaurant");
    }


    // Get restaurant id where you can find given product
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
    // Display orders history
    public void showOrderHistory() {
        if(currentUser instanceof Customer) {
            ((Customer) currentUser).displayOrdersHistory();
        }
    }

    // Add new order
    public void placeNewOrder(){
        if(currentUser instanceof Customer) {
            Cart userCart = ((Customer) currentUser).getCart();

            // Test if cart is empty
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

                List<CartItem> cartProducts = userCart.getCartProducts();
                String clientId = currentUser.getUserId();

                System.out.println("Do you want to complete the order placement?");
                Scanner checkOut = new Scanner(System.in);
                String checkOutInput = checkOut.nextLine();

                if("yes".equalsIgnoreCase(checkOutInput)) {
                    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    Order newOrder = new Order(Order.generateID(),clientId, deliveryPersonId, totalPrice+deliveryPrice,
                            payment, deliveryAddress, preparationTime, LocalDateTime.now().format(dateFormat), cartProducts);

                    // Write data in csv
                    Database.writeDataToCsv("orders.csv", newOrder.getObjectData());
                    cartProducts.stream().forEach(cartItem -> Database.writeDataToCsv("cartItems.csv", cartItem.getObjectData(newOrder.getOrderId())));


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

    // Add order to order history
    private void addOrderHistory(Order order){
        if(currentUser instanceof Customer) {
            ((Customer) currentUser).addOrderHistoryList(order);
        }
    }

    // Find available delivery person: returns ID of available delivery person
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
    private void loadData() {
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


        // LOad orders - grouping by clientID
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


/*    private void databaseInitialization(){
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
    // Show customers
    private void testCustomersList(){
        System.out.println("Customers:");
        for(Customer customer: customersList)
            System.out.println(customer);
    }

    // Show delivery people
    private void testDeliveryPeopleList(){
        System.out.println("Delivery People:");
        for(DeliveryPerson deliveryPerson: deliveryPeopleList)
            System.out.println(deliveryPerson);
    }

    // Show restaurants
    private void testRestaurantsList(){
        //Restaurants should be sorted in descending order according to their rating
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
