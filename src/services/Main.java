package services;

import java.util.Scanner;

public class Main {
    // Application functionality testing - first phase
    private static void testFirstPhase(){
        Services services = Services.getServicesInstance();
        Scanner optionInput = new Scanner(System.in);
        System.out.println();

        while(services.checkUserLoggedIn()) {
            System.out.println("Your option: ");
            String userOption = optionInput.nextLine();
            services.userInterface(userOption);
        }
    }


    public static void main(String[] args){
        testFirstPhase();
    }
}
