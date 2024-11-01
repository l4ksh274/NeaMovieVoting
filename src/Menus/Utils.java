package Menus;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Utils {

    private static final Scanner scanner = new Scanner(System.in);

    public static String createPassword() { // TODO add confirm password
        Pattern passwordRegex = Pattern.compile(
                "^(?=.*?[A-Z])" + // A Capital
                        "(?=.*?[a-z])" + // A lower case
                        "(?=.*?[0-9])" + // A digit
                        "(?=.*?[_#?!@$%^&*-])" + // An allowed symbol
                        ".{8,30}$"); // Size check
        String input, confirmInput;
        do{
            do{
                System.out.println("The following requirements must be met:\n" +
                        "- 8 - 30 Characters long\n" +
                        "- 1 Capital Letter\n" +
                        "- 1 Lower case Letter\n" +
                        "- 1 Number\n" +
                        "- 1 of the following symbols _#?!@$%^&*-\n");
                input = inputStr("Please choose a password: ");
            } while (!passwordRegex.matcher(input).matches());

            confirmInput = inputStr("Please confirm the password: ");
            if (!confirmInput.equals(input)){
                System.out.println("Passwords are different. Please try again.\n");
            }

        } while (!confirmInput.equals(input));

        return input;
    }

    public static String inputName(String s){
        Pattern regex = Pattern.compile("^[0-9]+");
        String name;
        do{
            System.out.println(s);
            name = scanner.nextLine();
        }while (regex.matcher(name).matches());

        return name;

    }

    public static String inputID(String s){
        Pattern regex = Pattern.compile("^([0-9]){6,9}$");
        String id;

        do{
            System.out.println(s);
            id = scanner.nextLine();
        } while (!regex.matcher(id).matches());

        return id;
    }

    public static String inputStr(String s) {
        System.out.println(s);
        return scanner.nextLine();
    }

    public static int inputInt(String s){ // Uses parse int to automatically deal with "\n"
        while (true){
            System.out.println(s);
            try{
                return  Integer.parseInt(scanner.nextLine());
            } catch (Exception e){
                System.out.println("\n This input isn't allowed here. Please enter an integer value (whole number) \n");
            }
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
