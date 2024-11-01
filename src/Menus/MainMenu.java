package Menus;

import DataStructures.HashMap.Hashmap;
import DataStructures.Nodes.Pairs;
import Entities.Users;

public class MainMenu extends Menus {
    private boolean exit;
    public MainMenu(){
        exit = false;
        Users user = Login.getUser();

        while (!exit){
            switch (user.getUserTypes()){
                case TEACHER:
                    operations = new Hashmap<>(5);
                    operations.add(0, new Pairs<>("Votes", this::votes));
                    operations.add(1, new Pairs<>("Accounts", this::accounts));
                    operations.add(2, new Pairs<>("Polls", this::polls));
                    operations.add(3, new Pairs<>("Movies", this::movies));
                    operations.add(4, new Pairs<>("Exit", this::exit));
                    displayMenu(operations);
                    break;
                case COVER_TEACHER:
                    operations = new Hashmap<>(4);
                    operations.add(0, new Pairs<>("Votes", this::votes));
                    operations.add(1, new Pairs<>("Accounts", this::accounts));
                    operations.add(2, new Pairs<>("Polls", this::polls));
                    operations.add(3, new Pairs<>("Exit", super::exit));
                    displayMenu(operations);
                    break;
                case STUDENT:
                    operations = new Hashmap<>(2);
                    operations.add(0, new Pairs<>("Votes", this::votes));
                    operations.add(1, new Pairs<>("Exit", super::exit));
                    displayMenu(operations);
                    break;
            }
        }

    }

    private void movies(){
        Movies movies = new Movies();
        displayMenu(movies.getOperations());
    }

    private void votes(){
        Votes votes = new Votes();
        displayMenu(votes.getOperations());
    }

    private void accounts(){
        Accounts accounts = new Accounts();
        displayMenu(accounts.getOperations());
    }

    private void polls(){
        Polls polls = new Polls();
        displayMenu(polls.getOperations());
    }

    @Override
    protected void exit() {
        exit = true;
        super.exit();
    }
}
