package services;

import auxiliar.RestaurantComparator;
import classes.*;
import usersManagement.Customer;
import usersManagement.DeliveryPerson;
import usersManagement.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Services {
    private List<Restaurant> restaurantList = new ArrayList<>();
    private List<Customer> customersList = new ArrayList<>();
    private List<DeliveryPerson> deliveryPeopleList = new ArrayList<>();
    // The current user that is logged in
    // If currentUser == null, then there is no user logged in
    private User currentUser;



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
        System.out.println("Logged out");
        currentUser = null;
    }


    // Display authentication form for user
    public void userAuthenticationForm() throws InterruptedException {
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
                    System.out.println("Exit");
                    break;
                }
            }
        }
    }


    // Display registration form for a new user
    public void userRegistrationForm() throws InterruptedException {
        Scanner recordInput = new Scanner(System.in);
        while(true) {
            System.out.println("Create new account? Type YES to continue: ");
            String newAccount = recordInput.nextLine();

            // New account
            if ("yes".equalsIgnoreCase(newAccount)) {
                System.out.println("Type 1 for Customer or 2 for Delivery Person:");
                int userType = recordInput.nextInt();

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
                System.out.println("Password: ");
                String password = recordInput.nextLine();

                // Account for new customer
                if(userType == 1){
                    Customer newCustomer = new Customer(lastName, firstName, phoneNumber, email, username, password);
                    customersList.add(newCustomer);
                    System.out.println("Redirecting to Log In page...");
                    Thread.sleep(3000);
                    userAuthenticationForm();
                }
                // Account for new delivery person
                else{
                    DeliveryPerson newDeliveryPerson = new DeliveryPerson(lastName, firstName, phoneNumber, email, username, password);
                    deliveryPeopleList.add(newDeliveryPerson);
                    System.out.println("Redirecting to Log In page...");
                    Thread.sleep(3000);
                    userAuthenticationForm();
                }
            }
            else {
                System.out.println("Already have an account? Type YES to redirect to Log In Page: ");
                String redirectAuthentication = recordInput.nextLine();
                if ("yes".equalsIgnoreCase(newAccount)) {
                    System.out.println("Redirecting to Log In page...");
                    Thread.sleep(3000);
                    userAuthenticationForm();
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
                break;
            }
    }



    //----------- CART FUNCTIONS -------------
    // Add product to cart
    public void addProductInMyCart(Product product, int quantity){
        ((Customer) currentUser).getCart().addProductToCart(product,quantity);
    }

    // Show products in cart
    public void showProductsMyCart(){
        ((Customer) currentUser).getCart().showCartItems();
    }

    // Empty cart
    public void clearMyCart(){
        ((Customer) currentUser).getCart().clearCart();
    }

    // Change product quantity in cart
    public void decreaseProductQuantity(CartItem cartItem, int quantity){
        cartItem.addQuantity(quantity);
    }

    // Remove product from cart
    public void removeProductFromCart(CartItem cartItem){
        ((Customer) currentUser).getCart().removeCartItem(cartItem);
    }



    //----------- PRODUCT AND MENU FUNCTIONS ----------
    // Add rating to a product
    public void addRatingToProduct(Product product, float rating){
        product.updateRating(rating);
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
        for(Restaurant restaurant: restaurantList)
            if(restaurant.getMenu().searchProductInMenu(productName))
                System.out.println(restaurant);

    }



    // -------------- ORDER FUNCTIONS ----------
    // Display orders history
    public void showOrderHistory() {
        if(currentUser instanceof Customer) {
            ((Customer) currentUser).displayOrdersHistory();
        }
    }


    // Add new order
    public void placeNewOrder() throws InterruptedException {
        if(currentUser instanceof Customer) {
            Cart userCart = ((Customer) currentUser).getCart();
            float totalPrice = userCart.getCartTotalPrice() + 5;
            List<CartItem> cartProducts = userCart.getCartProducts();
            String deliveryPersonId = null;
            while (deliveryPersonId == null) {
                System.out.println("Searching for an available delivery person. Please wait..");
                deliveryPersonId = searchAvailableDeliveryPerson();
            }
            String clientId = currentUser.getUserId();

            Scanner recordInput = new Scanner(System.in);
            System.out.println("Payment type: cash or card?");
            String userInput = recordInput.nextLine();
            String payment= userInput;

            System.out.println("Delivery Address: ");
            String deliveryAddress = recordInput.nextLine();
            long preparationTime = userCart.getTotalPreparationTime() + 10;;

            Order newOrder = new Order(clientId,deliveryPersonId,cartProducts,totalPrice,payment,deliveryAddress, preparationTime);

            // Asteptare perioada de preparare
            System.out.println("Please wait. Food in progress....");
            TimeUnit.SECONDS.sleep(preparationTime-10);
            System.out.println("Food is on the way");
            TimeUnit.SECONDS.sleep(10);
            System.out.println("Food deliverd");

            addOrderHistory(newOrder);
            for(DeliveryPerson deliveryPerson: deliveryPeopleList)
                if(deliveryPerson.getUserId()==deliveryPersonId)
                    deliveryPerson.updateAvailabilityStatus();
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



    // ------------ ADD DATA --------------
    private void databaseInitialization(){
        //Customers
        //Delivery People
        //Products
        //Menu
        //Restaurants
    }


}
