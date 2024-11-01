package Entities;

public class Users {
    private String firstName, lastName, username, password;
    private Integer userID;
    private UserTypes userTypes;

    public Users(String firstName, String lastName, Integer UserID, String Username, String Password, UserTypes userType){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = UserID;
        this.username = Username;
        this.password = Password;
        this.userTypes = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getUserID() {
        return userID;
    }

    public UserTypes getUserTypes() {
        return userTypes;
    }
}
