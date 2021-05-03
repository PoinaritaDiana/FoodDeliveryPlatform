package usersManagement;

import usersManagement.User;

public class DeliveryPerson extends User {
    private String availabilityStatus;
    private String carRegistrationNumber;

    public DeliveryPerson(String userID,String lastName, String firstName, String phoneNumber, String email, String carRegistrationNumber, String username, String password) {
        super(userID,lastName, firstName, phoneNumber, email, username, password);
        this.availabilityStatus = "available";
        this.carRegistrationNumber = carRegistrationNumber;
    }

    // Updates courier status
    public void updateAvailabilityStatus(){
        if(this.availabilityStatus == "available")
            this.availabilityStatus = "on delivery";
        else
            this.availabilityStatus = "available";
    }

    // Get current status
    public String getStatus() {
        return availabilityStatus;
    }

    @Override
    public String toString(){
        return String.format("Delivery person %s : %s %s",getUserId(), getLastName(), getFirstName());
    }

    public String[] getObjectData(){
        String[] objectData = {userId,lastName, firstName, phoneNumber, email, carRegistrationNumber, username, password};
        return objectData;
    }
}
