package Menus;

import DataStructures.HashMap.Hashmap;
import DataStructures.Nodes.Node;
import DataStructures.Nodes.Pairs;
import Entities.UserTypes;
import Entities.VoteOption;
import Entities.VoteOptionManager;

public class Votes extends Menus{
    private Hashmap<Integer, Pairs<String, Runnable>> operations;

    public Votes(){
        switch (type){
            case TEACHER:
                operations = new Hashmap<>(4);
                operations.add(0, new Pairs<>("See Voting history", this::voteHistory));
                operations.add(1, new Pairs<>("View Current Options", this::viewCurrentOptions));
                operations.add(2, new Pairs<>("See Option Information", this::seeOptionInfo));
                operations.add(3, new Pairs<>("Back", super::mainMenu));
                break;
            case COVER_TEACHER:
                operations = new Hashmap<>(3);
                operations.add(0, new Pairs<>("View Current Options", this::viewCurrentOptions));
                operations.add(1, new Pairs<>("See Option Information", this::seeOptionInfo));
                operations.add(2, new Pairs<>("Back", super::mainMenu));
                break;
            case STUDENT:
                operations = new Hashmap<>(5);
                operations.add(0, new Pairs<>("Vote", this::vote));
                operations.add(1, new Pairs<>("See Voting history", this::voteHistory));
                operations.add(2, new Pairs<>("View Current Options", this::viewCurrentOptions));
                operations.add(3, new Pairs<>("See Option Information", this::seeOptionInfo));
                operations.add(4, new Pairs<>("Back", super::mainMenu));
                break;
        }

    }

    private void vote() {
        VoteOptionManager voteOptionManager = database.getActivePoll();
        if (voteOptionManager == null){
            System.out.println("There is no active poll. Please wait until one starts.");
            return;
        }
        if (database.hasVotedMovie(getUser())){
            System.out.println("You have already voted on this poll. Please wait for the next poll.");
            return;
        }
        viewCurrentOptions();
        int choice;

        do{
            choice = inputInt(String.format("Please choose a movie 1 - %d", voteOptionManager.getAvailableOptions().getSize()));
        } while (choice <= 0 || choice > voteOptionManager.getAvailableOptions().getSize());

        database.registerVote(getUser(), voteOptionManager.getAvailableOptions().get(choice - 1).getData().getValue());

    }

    private void seeOptionInfo() {
        VoteOptionManager voteOptionManager = database.getActivePoll();
        if (voteOptionManager == null){
            System.out.println("There is no active poll. Please wait until one starts.");
            return;
        }

        for (Node<Pairs<Integer, VoteOption>> optionPairs : voteOptionManager.getAvailableOptions()){
            System.out.printf("%s - https://www.imdb.com/title/tt%s/ \n", optionPairs.getData().getValue().getMovieName(), optionPairs.getData().getValue().getMovieID());
        }

        System.out.println("\n");

    }

    private void viewCurrentOptions() {
        VoteOptionManager voteOptionManager = database.getActivePoll();
        if (voteOptionManager == null){
            System.out.println("There is no active poll. Please wait until one starts.");
            return;
        }
        for (Node<Pairs<Integer, VoteOption>> optionPairs : voteOptionManager.getAvailableOptions()){
            System.out.printf("%d) %s\n", optionPairs.getData().getKey(), optionPairs.getData().getValue().getMovieName());
        }

        System.out.println("\n");

    }

    private void voteHistory() {
        if (type == UserTypes.STUDENT){
            database.displayUserHistory(getUser().getUsername());
        } else{
            String username = inputStr("Enter Username for the student");
            database.displayUserHistory(username);
        }
        System.out.println("\n");

    }

    public Hashmap<Integer, Pairs<String, Runnable>> getOperations() {
        return operations;
    }
}
