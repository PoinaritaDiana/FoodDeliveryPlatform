package auxiliar;

import java.util.UUID;

public interface IDGenerator {
    default String generateID (){
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }
}
