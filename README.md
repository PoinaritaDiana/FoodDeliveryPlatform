# Food Delivery Platform - DeliverEAT

Objects that are defined on the platform:
- User -> Customer, DeliveryPerson
- Restaurant
- Menu
- Product -> FoodProduct, BeverageProduct
- Cart
- CartItem
- Order
- Services
- Comparator -> OrderComparator, ProductComparator, RestaurantComparator
- IDGenerator -> interface with generateID function implemented, which provides a Universally Unique Identifier for users, products and restaurants (as a String)


#### 1. USER, CUSTOMER and DELIVERYPERSON classes ####
The "User" abstract class stores basic information about each user (whether customer or courier), such as first name, last name, username, etc.\
DeliverEAT is a food delivery application that allows users to register and authenticate as:
- Customer -> Class that extends the User base class, representing the consumer/customer, having in addition the following fields:
```java
private Set <Order> ordersHistoryList = new TreeSet<>(new OrderComparator());
private Cart cart;
```
And the following functions:
```java
public void displayOrdersHistory()                  // show the list of all placed orders so far
public void addOrderHistoryList(Order newOrder)     // add new order to the list of placed orders
```

- Courier/DeliveryPerson -> Class that extends the user base class, representing the courier/ delivery person, having in addition the following fields:
```java
private String availabilityStatus;        // to check if this courier can take a new order or is in delivery
private String carRegistrationNumber;    
```
And the next function, which updates the status of the courier:
```java
public void updateAvailabilityStatus()
```


#### 2. RESTAURANT class ####
In the Restaurant class, information (including a menu) is stored about each restaurant available on the platform.\
A function defined in this class is the following, which updates the restaurant's rating when a new value is added :
```java
public void addRestaurantRating(float newRating)
```

#### 3. MENU class ####
The Menu class contains two lists, for food products, respectively beverage products, sorted alphabetically:
```java
private Set <FoodProduct> foodProductList= new TreeSet<>(new ProductComparator());
private Set <BeverageProduct>  beverageProductList= new TreeSet<>(new ProductComparator());
```
Functions available in this class are:
```java
public void showMenu()                                        // show/print/display the menu
public boolean searchProductInMenu(String productName)        // search a product in the menu by name
public Product searchProductInMenuByID(String productId)      // search a product in the menu by ID
public void displayProductInMenu(String productName)          // show product in the menu by name
```

#### 4. PRODUCT, FOODPRODUCT and BEVERAGEPRODUCT classes ####
In the abstract Product class, generic information about each product is stored, such as name, description, preparation time, rating, price, etc.\
There are two types of products on the platform:
- FoodProduct -> Class that extends the Product base class, having in addition a list of the categories to which it belongs
- BeverageProduct -> Class that extends the Product base class, having in addition the following fields, which can help make products easier to display based on user-selected filters:
```java
private boolean alcoholicDrink;
private String drinkType;
````



#### 5. CART class ####
The Cart class consists of a list of CartItem objects and a string, representing the id of the restaurant where the user wants to place the order (A user can only place an order at one restaurant at a time)
```java
private List<CartItem> cartProducts = new ArrayList<>();
private String productsFromRestaurantID = null;
```
In this class, the following functions are used to manage user's cart (before placing the order):
```java
// Add product to cart: If the product already exists in the cart, then only increase the quantity with the given number
// Also, update the restaurantID where the user wants to place the order
public void addProductToCart(Product product, int quantity, String restaurantId)

public boolean isEmpty()                          // check if cart is empty
public void showCartItems()                       // display products that are in the cart
public void clearCart()                           // empty the cart
public void removeCartItem(CartItem cartItem)     // remove product from cart
public float getCartTotalPrice()                  // get total price for the items in the cart
public long getTotalPreparationTime()             // get total preparation time for cart items (quantity does not matter)
```

#### 6. CARTITEM class ####
CartItem represents a product added to the user's cart with its quantity (for which the total price is calculated), having a function for changing the quantity of product in the cart, also updating the total price:
```java
private Product product;
private int productQuantity;
private float totalItemPrice;

public void addQuantity(int quantity)
```

 
#### 7. ORDER class ####
This class contains data about each order placed, such as selected products, total price paid, the address to which it was delivered and the type of payment.\
This data will be sent as parameters to the constructor according to the data entered by the user at the time of placing the order.
```java
public Order(String clientId, String deliveryPersonId, List<CartItem> cartProducts, Float totalPrice, String payment, String deliveryAddress, float preparationTime) 
```

#### 8. SERVICES class ####
This class contains 3 lists in which are stored the restaurants available on the platform, the registered clients, respectively the couriers, as well as a User object, to take into account the currently logged in user.
```java
private List<Restaurant> restaurantList = new ArrayList<>();
private List<Customer> customersList = new ArrayList<>();
private List<DeliveryPerson> deliveryPeopleList = new ArrayList<>();
private User currentUser = null;                                        // currentUser == null <-> there is no user logged in
```
In the Services class are exposed and implemented all the functionalities of the application:
```java
// The function that manages all the facilities, based on the option introduced by the user, together with other auxiliary functions, such as:
public void userInterface(String userOption)

private boolean searchCustomer(String username)                              // search customer in the list by username
private boolean checkCustomerPassword(String username,String password)       // check the correct password entered for the given customer
private Customer getCustomer(String username)                                // get the customer with the given username
private boolean searchDeliveryPerson(String username)                        // search delivery person in the list by username
private boolean checkDeliveryPassword(String username,String password)       // check the correct password entered for the given delivery person  
private DeliveryPerson getDeliveryPerson(String username)                    // get the delivery person with the given username

public boolean checkUserLoggedIn()                                // check if any user is logged in
private void displayWelcomeMessage()                              // display a message that the user receives the first time he enters the application
private void displayCustomerOptions()                             // display application functionalities for a customer
private void displayDeliveryOptions()                             // display application functionalities for a delivery person
private CartItem getCartItemByProductID(String productId)         // get the product with the given ID from the logged-in user's cart
private Product getProductById(String productId)                  // get the product with the given ID from all restaurants
private Restaurant getRestaurantByName(String restaurantName)     // get restaurant by name

private String searchAvailableDeliveryPerson()                    // find available delivery person
```



#### 9. MAIN class ####
In the main class, the functions made available to the user are called through the single instance of the singleton Services class.

#### 10. COMPARATOR classes ####
- OrderComparator: Implementing the Comparator <> interface for Order objects, so that they are added in the ordersHistoryList of a Customer in descending order according to the date when the orders were placed
- ProductComparator: Implementing the Comparator <> interface for Product objects, so that they are stored in the restaurant menu in alphabetical order.
- RestaurantComparator: Implementing the Comparator <> interface for Restaurant objects, so that they are stored in the list in descending order of rating.


\
As a customer, you have the following options / facilities (which are implemented in the Services class):
```
1. Log in as another user                                public void userAuthenticationForm()                 
2. Register a new user                                   public void userRegistrationForm()
3. Log out                                               public void logOut()
4. See list of all restaurants                           public void showAllRestaurants()
5. Search restaurant by name                             public void searchRestaurant(String restaurantName)
6. Add rating to a specific restaurant                   public void addRestaurantRating(String restaurantId, float newRating)
7. See menu of a specific restaurant                     public void showRestaurantMenu(Restaurant restaurant)
8. Add rating to a specific product                      public void addRatingToProduct(Product product, float rating)
9. Search product by name in a specific restaurant       public void searchProductInRestaurant(Restaurant restaurant, String productName)
10. Search product by name in all restaurants            public void searchProductInRestaurants(String productName)
11. Place order                                          public void placeNewOrder()
12. See your order history                               public void showOrderHistory()
13. Add product to your cart                             public void addProductInMyCart(Product product, int quantity)
14. See your cart                                        public void showProductsMyCart()
15. Empty cart                                           public void clearMyCart()
16. Remove product from cart                             public void removeProductFromCart(CartItem cartItem)
17. Remove quantity of product from your cart            public void decreaseProductQuantity(CartItem cartItem, int quantity)
18. Show option menu                                     private void displayCustomerOptions()
19. Delete account                                       private void deleteAccount()
```
