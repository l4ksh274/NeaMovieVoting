package Menus;

import DataStructures.HashMap.Hashmap;
import DataStructures.LinkedList.LinkedList;
import DataStructures.Nodes.Pairs;

public class Movies extends Menus{

    public Movies(){
        operations = new Hashmap<>(5);
        operations.add(0, new Pairs<>("Add Movie", this::addMovie));
        operations.add(1, new Pairs<>("Add Actor", this::addActor));
        operations.add(2, new Pairs<>("Remove Movie", this::removeMovie));
        operations.add(3, new Pairs<>("Remove Actor", this::removeActor));
        operations.add(4, new Pairs<>("Back", super::mainMenu));
    }

    private void addMovie(){
        String movieID = Utils.inputID("Enter MovieID: ");

        if (database.movieExists(movieID)){
            System.out.println("This movie already exists. ");
            return;
        }

        String movieName = inputStr("Enter Movie Name: ");

        LinkedList<String> actors = new LinkedList<>();
        int actorCount = inputInt("How many actors do you want to add? ");
        for (int i = 0; i < actorCount; i++){
            String actorID = Utils.inputID("Enter the IMDbID for the actor: ");
            if (database.actorNotExists(actorID)){
                int choice;

                do{
                    choice = inputInt("Actor doesn't exist. Would you like to add him/her? \n 1 - Yes \n 2 - No");

                    switch (choice){
                        case 1:
                            addActor(actorID);
                            break;
                        case 2:
                            actorID = Utils.inputID("Enter the IMDbID for the actor: ");
                            break;
                        default:
                            System.out.println("Please choose a valid response");
                    }

                }while (choice < 1 || choice > 2 || database.actorNotExists(actorID));
            }
            actors.append(actorID);
        }

        LinkedList<Pairs<Integer, String>> allGenres = database.getGenres(); // Implement display menu for ts
        LinkedList<Integer> chosenGenres = new LinkedList<>();
        int numberOfGenres = inputInt("How many genres do you want to add? ");

        for (int i = 0; i < numberOfGenres; i++){
            for (int j = 0; j < allGenres.getSize(); j++){
                Pairs<Integer, String> genre = allGenres.get(j).getData();
                System.out.printf("%d) %s \n", genre.getKey(), genre.getValue());
            }
            int choice;
            do{
                choice = inputInt(String.format("\nPlease choose between 1 and %s - ", allGenres.getSize()));
            } while(choice > allGenres.getSize()|| choice < 0);

            chosenGenres.append(choice);
        }

        database.addMovie(movieID, movieName);
        database.addMovieCast(movieID, actors);
        database.addMovieGenre(movieID, chosenGenres);
    }

    private void addActor(String actorID){
        String actorName = Utils.inputName("Enter actor name: ");
        database.addActor(actorID, actorName);
    }

    private void addActor(){
        String actorName = Utils.inputName("Enter actor name: ");
        String actorID = Utils.inputID("Enter the IMDbID of the actor: ");
        if (database.actorNotExists(actorID)){
            database.addActor(actorID, actorName);
        }
    }

    private void removeMovie(){
        if (database.getActivePoll() != null){
            System.out.println("There is an active poll. Please wait for the poll to end before deleting any movies as this may affect the results. ");
            return;
        }
        String movieID = Utils.inputID("Enter IMDbID of the movie you want to remove: ");
        database.removeMovie(movieID);
    }

    private void removeActor(){
        if (database.getActivePoll() != null){
            System.out.println("There is an active poll. Please wait for the poll to end before deleting any actors as this may affect the results. ");
            return;
        }
        String actorID = Utils.inputID("Enter IMDbID of the actor you want to remove: ");
        database.removeActor(actorID);
    }
}
