package services;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        // Get services
        Services services = Services.getServicesInstance();

        // Display Welcome Message
        services.displayWelcomeMessage();

        Scanner optionInput = new Scanner(System.in);
        System.out.println();

        // While user is logged in
        while(services.checkUserLoggedIn()) {
            // Show user-options
            services.displayUserOptions();

            //Get user input
            System.out.println("Your option: ");
            String userOption = optionInput.nextLine();

            // Calling the functionality corresponding to the user's option
            services.userInterface(userOption);

            System.out.println("Press enter to continue...");
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
