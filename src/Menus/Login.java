package Menus;

import DataStructures.HashMap.Hashmap;
import DataStructures.Nodes.Pairs;
import Entities.UserTypes;
import Entities.Users;

import java.util.regex.Pattern;

public class Login extends Menus{

    private boolean successfulLogin;
    public Login(){
        successfulLogin = false;
        operations = new Hashmap<>(3);
        operations.add(0, new Pairs<>("Login", this::login));
        operations.add(1, new Pairs<>("Register", this::register));
        operations.add(2, new Pairs<>("Exit", super::exit));
        while(!successfulLogin){
            displayMenu(operations);
        }
        MainMenu mainMenu = new MainMenu();

    }

    private void login(){
        String username = inputStr("Username: ");
        String hashedPassword = Utils.bytesToHex(digest.digest(inputStr("Password: ").getBytes())); // Actual password isn't stored locally for slightly more security.

        setUser(database.getUserByLogin(username, hashedPassword));
        if (getUser() == null){
            System.out.println("Incorrect Username or Password. Please try again.");
            return;
        }

        System.out.println("\n\n\n\n\n\n\n\n\n\n\nLog in successful. Welcome " + getUser().getFirstName()); // To push password out of view
        successfulLogin = true;

    }

    private void register(){
        int code = inputInt("Enter Registration Code: ");
        int[] results = database.useRegistrationCode(code);

        if (results.length == 1){
            System.out.println("\nInvalid Registration Code \n");
            return;
        }

        UserTypes userType = UserTypes.getTypeByID(results[1]);

        String firstName = Utils.inputName("Enter First Name: ");
        String lastName = Utils.inputName("Enter Last Name: ");
        String username = createUserName();
        int userID = database.genNewUserID();
        String hashedPassword = Utils.bytesToHex(digest.digest(Utils.createPassword().getBytes()));

        setUser(new Users(firstName, lastName, userID, username, hashedPassword, userType));
        database.registerNewUser(getUser());

    }

    private String createUserName() {
        System.out.println("Usernames must be:" +
                            "\n- Unique" +
                            "\n- Between 3 - 30 characters" +
                            "\n- Made of only letters and numbers");

        Pattern pattern = Pattern.compile(
                "^\\w" + // A "word" character (Alphanumeric with _)
                "{5,30}$"); // Size check
        String input;

        do{
            input = inputStr("Please choose a username: ");
        } while (!pattern.matcher(input).matches() || database.userNameExists(input));

        return input;
    }
}
