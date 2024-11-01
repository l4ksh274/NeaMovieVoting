package Menus;

import Algorithms.Dijkstra;
import DataStructures.HashMap.Hashmap;
import DataStructures.LinkedList.LinkedList;
import DataStructures.Nodes.Pairs;
import Entities.VoteOption;

public class Polls extends Menus{

    public Polls(){
        operations = new Hashmap<>(4);
        operations.add(0, new Pairs<>("Create a Poll", this::createPoll));
        operations.add(1, new Pairs<>("Get poll results", this::getPollResults));
        operations.add(2, new Pairs<>("Back", super::mainMenu));
    }

    private void createPoll() {
        if (database.getActivePoll() != null){
            System.out.println("There is already an active poll. Please wait for the poll to finish before creating a new one.");
            return;
        }
        int numberOfMovies = inputInt("How many movies do you want in the poll? ");
        int totalMovies = database.getNumberOfMovies();

        if (numberOfMovies > totalMovies || numberOfMovies < 1) {
            System.out.println("You can't choose this many movies");
            return;
        }
        if (numberOfMovies == totalMovies){
            database.createPoll(database.getAllMovies());
            return;
        }

        Dijkstra<String> dijkstra = new Dijkstra<>();

        String seedMovieID = database.getPollWinner()[1];
        if (seedMovieID == null){
            seedMovieID = database.getRandomMovie();
        }

        Hashmap<String, Float> movies = dijkstra.search(seedMovieID, numberOfMovies, database.createGraph(seedMovieID));

        if (movies.length() < numberOfMovies){
            database.fillMovies(movies, numberOfMovies);
        }

        LinkedList<VoteOption> voteOptions = new LinkedList<>();

        for (Pairs<String, Float> movie : movies){
            if (!movie.getKey().equals(seedMovieID)){
                 voteOptions.append(new VoteOption(movie.getKey()));
            }
        }
        database.createPoll(voteOptions);
    }

    private void getPollResults(){
        String[] winnerData = database.getPollWinner();

        System.out.printf("Movie Name - %s \nMovieID - %s \nNumber Of Votes %s \n", winnerData[0], winnerData[1], winnerData[2]);
    }
}
