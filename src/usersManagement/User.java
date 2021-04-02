package usersManagement;

import auxiliar.IDGenerator;

abstract public class User implements IDGenerator {
    private String userId;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;

    public User(String lastName, String firstName, String phoneNumber, String email, String username, String password) {
        this.userId = generateID();
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString(){
        return String.format("User %d is %s %s",userId,lastName, firstName);
    }
}
