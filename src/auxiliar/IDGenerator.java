package auxiliar;

import java.util.UUID;

// IDGenerator interface in which the generateID function is implemented,
// which provides a Universally Unique Identifier for users, products and restaurants (as a String)

public interface IDGenerator {
    default String generateID (){
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }
}
