package services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Database {
    private static Database database = null;

    // Get Audit instance
    public static Database getDatabaseInstance(){
        if (database == null)
            database = new Database();
        return database;
    }

    private Database(){}

    protected List<String []> readDataFromCsv(String csvFile){
        try {
            // List of lines / arrays with the data from the file
            List<String []> data = Files.readAllLines(Paths.get("data/"+csvFile)).stream()
                                    //.filter(Objects::nonNull)
                                    .map(line -> line.split(","))
                                    .collect(Collectors.toList());
            return data;
        } catch (IOException e) {
            System.out.println("The file does not exist.");
        }
        return null;
    }


    protected static void writeDataToCsv(String csvFile, String[] data){
        try {
            File file = new File ("data/"+csvFile);
            if (!file.exists())
                file.createNewFile();

            FileWriter csvWriter = new FileWriter(file, true);
            csvWriter.write(String.join(",", data));
            csvWriter.write("\n");
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected static void removeUserFromCsv(String userID){
        String csvFile= "customers.csv";
        if(userID.charAt(0)=='D')
            csvFile="deliveryPeople.csv";

        try {
            // Get users except user with userID
            List<String []> data = Files.readAllLines(Paths.get("data/"+csvFile)).stream()
                    .map(line -> line.split(","))
                    .filter(line -> !line[0].equals(userID))
                    .collect(Collectors.toList());

            FileWriter csvWriter = new FileWriter("data/"+csvFile, false);
            data.stream().forEach(line -> {
                try {
                    csvWriter.write(String.join(",", line));
                    csvWriter.write("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    protected static void updateCsv(String csvFile, String id, float rating){
        try {
            // Get data from file
            List<String []> data = Files.readAllLines(Paths.get("data/"+csvFile)).stream()
                    .map(line -> line.split(","))
                    .collect(Collectors.toList());

            // Delete file
            File file= new File ("data/"+csvFile);
            file.delete();

            // Write updated data to file
            for(String[] line: data) {
                if (line[0].equals(id))
                    line[5]= String.valueOf(rating);
                Database.writeDataToCsv(csvFile,line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
