package services;

import classes.Restaurant;
import usersManagement.Customer;
import usersManagement.DeliveryPerson;
import usersManagement.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Services {
    private List<Restaurant> restaurantList = new ArrayList<>();
    private List<Customer> customersList = new ArrayList<>();
    private List<DeliveryPerson> deliveryPeopleList = new ArrayList<>();
    // The current user that is logged in
    // If currentUser == null, then there is no user logged in
    private User currentUser;


    // Display list of all restaurants
    public void showRestaurants(){
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
                break;
            }
        if(found==false)
            System.out.println(String.format("No result for \"%s\".Try again.",restaurantName));
    }


    private boolean searchCustomer(String username){
        for(Customer customer: customersList)
            if(customer.getUsername() == username)
                return true;
        return false;
    }

    private boolean checkCustomerPassword(String username,String password){
        for(Customer customer: customersList)
            if(customer.getUsername() == username && customer.getPassword() == password)
                return true;
        return false;
    }

    private Customer getCustomer(String username){
        Customer searchedCustomer = null;
        for(Customer customer: customersList)
            if (customer.getUsername() == username)
                searchedCustomer = customer;

        return searchedCustomer;
    }


    private boolean searchDeliveryPerson(String username){
        for(DeliveryPerson deliveryPerson: deliveryPeopleList)
            if(deliveryPerson.getUsername() == username)
                return true;
        return false;
    }

    private boolean checkDeliveryPassword(String username,String password){
        for(DeliveryPerson deliveryPerson: deliveryPeopleList)
            if(deliveryPerson.getUsername() == username && deliveryPerson.getPassword() == password)
                return true;
        return false;
    }

    private DeliveryPerson getDeliveryPerson(String username){
        DeliveryPerson searchedDeliveryPerson = null;
        for(DeliveryPerson deliveryPerson: deliveryPeopleList)
            if (deliveryPerson.getUsername() == username)
                searchedDeliveryPerson = deliveryPerson;
        return searchedDeliveryPerson;
    }


    public void logOut(){
        System.out.println("Logged out");
        currentUser=null;
    }


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



    public void userRegistrationForm(){
        while(true) {
            Scanner recordInput = new Scanner(System.in);
            System.out.println("Create new account? Type YES to continue: ");
            String newAccount = recordInput.nextLine();
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
                //System.out.println("Type password again: ");
                //String passwordCheck = recordInput.nextLine();

                if(userType == 1){
                    Customer newCustomer = new Customer(lastName, firstName, phoneNumber, email, username, password);
                    customersList.add(newCustomer);
                    System.out.println("Redirecting to Log In page...");
                    userAuthenticationForm();
                }
                else{
                    DeliveryPerson newDeliveryPerson = new DeliveryPerson(lastName, firstName, phoneNumber, email, username, password);
                    deliveryPeopleList.add(newDeliveryPerson);
                    System.out.println("Redirecting to Log In page...");
                    userAuthenticationForm();
                }
            }
            else {
                System.out.println("Already have an account? Type YES to redirect to Log In: ");
                String redirectAuthentication = recordInput.nextLine();
                if ("yes".equalsIgnoreCase(newAccount)) {
                    System.out.println("Redirecting to Log In....");
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




}
