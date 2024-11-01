package Menus;

import DataStructures.HashMap.Hashmap;
import DataStructures.Nodes.Pairs;
import DatabaseUtils.DatabaseConnect;
import Entities.UserTypes;
import Entities.Users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Menus {
    private Scanner sc = new Scanner(System.in);
    protected MessageDigest digest;
    protected Hashmap<Integer, Pairs<String, Runnable>> operations;

    // Static classes are used to ensure only one instance of them exist.
    protected static DatabaseConnect database = new DatabaseConnect();
    private static Users user;
    protected static UserTypes type;

    public Menus(){
        try{
            digest = MessageDigest.getInstance("SHA-256");
        }catch (NoSuchAlgorithmException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    protected String inputStr(String Prompt){
        return Utils.inputStr(Prompt);
    }

    protected int inputInt(String Prompt){
        return Utils.inputInt(Prompt);
    }

    protected void displayMenu(Hashmap<Integer, Pairs<String, Runnable>> options){

        for(Pairs<Integer, Pairs<String , Runnable>> pairs: options){
            System.out.printf("%d) %s\n", pairs.getKey() + 1, pairs.getValue().getKey());
        }
        int choice;
        do{
            choice = inputInt(String.format("\nPlease choose between 1 and %s - ", options.length())) - 1;
        } while(choice > options.length() - 1 || choice < 0);

         options.item(choice).getValue().run();
    }

    protected void exit(){
        System.out.println("Exiting...");
        database.close();
        System.exit(0);
    }

    public Hashmap<Integer, Pairs<String, Runnable>> getOperations() {
        return operations;
    }

    protected static Users getUser() {
        return user;
    }

    protected static void setUser(Users user) {
        Menus.user = user;
        Menus.type = user == null ? null : user.getUserTypes();
    }

    protected void mainMenu() { // Pushes older menu and input data out of view
        for(int i = 0; i < 25; i++){
            System.out.println();
        }
    }
}
