package Entities;

public class VoteOption {

    private String movieID, movieName;
    private int optionID;

    public VoteOption(String movieID, String movieName, int optionID){
        this.movieID = movieID;
        this.movieName = movieName;
        this.optionID = optionID;
    }

    public VoteOption(String movieID){
        this.movieID = movieID;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getOptionID() {
        return optionID;
    }

    public void setOptionID(int optionID) {
        this.optionID = optionID;
    }
}
