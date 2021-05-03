package usersManagement;

// Abstract class for users, which can be of two types: customers and delivery person
abstract public class User{
    private static int lastUserId;
    protected String userId;
    protected String lastName;
    protected String firstName;
    protected String phoneNumber;
    protected String email;
    protected String username;
    protected String password;

    public User(String userID,String lastName, String firstName, String phoneNumber, String email, String username, String password) {
        this.userId = userID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    @Override
    public String toString(){
        return String.format("User %d : %s %s",userId,lastName, firstName);
    }


    public static void setLastUserId(int userID){
        lastUserId = userID;
    }

    public static String generateID(String type){
        lastUserId +=1;
        return String.format(type + String.valueOf(lastUserId));
    }
}
