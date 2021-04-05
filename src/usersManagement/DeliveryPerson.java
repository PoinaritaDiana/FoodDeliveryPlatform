package usersManagement;

import usersManagement.User;

public class DeliveryPerson extends User {
    private String availabilityStatus;

    public DeliveryPerson(String lastName, String firstName, String phoneNumber, String email, String username, String password) {
        super(lastName, firstName, phoneNumber, email, username, password);
        this.availabilityStatus = "available";
    }

    public void updateAvailabilityStatus(){
        if(this.availabilityStatus == "available")
            this.availabilityStatus = "busy";
        else
            this.availabilityStatus = "available";
    }

    public String getStatus() {
        return availabilityStatus;
    }
}
