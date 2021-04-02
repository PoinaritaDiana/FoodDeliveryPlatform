package classes;

import java.util.Objects;

/** Class for DerliveryPerson **/

public class DeliveryPerson extends User {
    /** Perosnal info about delivery person  **/
    private String firstName;
    private String lastName;

    /** The current order - if is null then delivery person is available **/
    private Order currentOrder;

    /** Constructors - new delivery person **/
    public DeliveryPerson(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.currentOrder = null;
    }

    /** Getters **/
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    /** Setters **/
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryPerson that = (DeliveryPerson) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(currentOrder, that.currentOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, currentOrder);
    }
}
