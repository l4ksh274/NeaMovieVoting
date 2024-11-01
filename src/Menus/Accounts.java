package Menus;

import DataStructures.HashMap.Hashmap;
import DataStructures.LinkedList.LinkedList;
import DataStructures.Nodes.Node;
import DataStructures.Nodes.Pairs;
import Entities.UserTypes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Accounts extends Menus{

    public Accounts(){
        switch (type){
            case TEACHER:
                operations = new Hashmap<>(4);
                operations.add(0, new Pairs<>("Register New Accounts", this::registerNewUser));
                operations.add(1, new Pairs<>("Reset User Password", this::resetPassword));
                operations.add(2, new Pairs<>("Delete Account", this::deleteUser));
                operations.add(3, new Pairs<>("Back", super::mainMenu));
                break;
            case COVER_TEACHER:
                operations = new Hashmap<>(3);
                operations.add(0, new Pairs<>("Register New Accounts", this::registerNewUser));
                operations.add(1, new Pairs<>("Reset User Password", this::resetPassword));
                operations.add(2, new Pairs<>("Back", super::mainMenu));
                break;
        }
    }

    private void deleteUser() {
        String username = inputStr("Please enter the username of the account you want to delete: ");
        int userID = database.usernameToUserID(username);
        if (userID == -1){
            System.out.println("User does not exist. ");
        }else {
            database.deleteUser(userID);
            System.out.println("Deleted user successfully");
        }
    }

    private void resetPassword() {
        String username = inputStr("Please enter the Username");
        int userID = database.usernameToUserID(username);

        if (userID == -1){
            System.out.println("This user does not exist. ");
        }else {
            database.resetPassword(userID, Utils.bytesToHex(digest.digest(Utils.createPassword().getBytes())));
            System.out.println("Password changed successfully");
        }
    }

    private void registerNewUser() {
        int teacherNo, coverTeacherNo, studentNo;
        teacherNo = coverTeacherNo = 0;

        do{
            System.out.println("Please answer a number greater than or equal to 0");
            if (getUser().getUserTypes() == UserTypes.TEACHER){
                teacherNo = inputInt("How many teacher codes do you want to generate? : ");
                coverTeacherNo = inputInt("How many cover teacher codes do you want to generate? : ");
            }

            studentNo = inputInt("How many student codes do you want to generate? : ");
        } while (teacherNo < 0 || coverTeacherNo < 0 || studentNo < 0);

        saveInFile("RegistrationCodes/Teacher.txt", database.createRegistrationCodes(teacherNo, UserTypes.TEACHER));
        saveInFile("RegistrationCodes/CoverTeacher.txt", database.createRegistrationCodes(coverTeacherNo, UserTypes.COVER_TEACHER));
        saveInFile("RegistrationCodes/Student.txt", database.createRegistrationCodes(studentNo, UserTypes.STUDENT));
    }

    private void saveInFile(String fileName, LinkedList<Integer> linkedList){
        try {
            File file = new File(fileName);
            file.getParentFile().mkdirs();

            FileWriter fileWriter = new FileWriter(file, false);
            BufferedWriter br = new BufferedWriter(fileWriter);

            for (Node<Integer> pairs : linkedList){
                br.write(pairs.getData().toString());
                br.newLine();
            }
            br.close();

        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
