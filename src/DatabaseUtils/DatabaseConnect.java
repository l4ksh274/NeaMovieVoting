package DatabaseUtils;

import DataStructures.AdjacencyList.AdjacencyList;
import DataStructures.HashMap.Hashmap;
import DataStructures.LinkedList.LinkedList;
import DataStructures.Nodes.Node;
import DataStructures.Nodes.Pairs;
import Entities.UserTypes;
import Entities.Users;
import Entities.VoteOption;
import Entities.VoteOptionManager;

import java.sql.*;
import java.util.Random;

public class DatabaseConnect {
    private Connection conn = null;

    public DatabaseConnect() {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");//Specify the SQLite Java driver
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/neamovievotingdb?createDatabaseIfNotExist=true", "root", "Ju1c3W31d!");//Specify the database, since relative in the main project folder
            conn.setAutoCommit(false);// Important as you want control of when data is written
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            System.out.println("Opened database successfully");



        } catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(1);
        }
    }

    public void close() {
        try {conn.close();} catch (SQLException e) {System.err.println(e.getClass().getName() + ": " + e.getMessage());}
    }

    // Executes required DDL and inserts constant values
    private void initDatabase(){
        try {
            Statement statement = conn.createStatement();

            statement.executeUpdate("CREATE TABLE Actors(" +
                    "                       ActorID Varchar(10) not null primary key," +
                    "                       ActorName Varchar(255)" +
                    "); ");

            statement.executeUpdate("CREATE TABLE Movie(" +
                     "                      MovieID varchar(10) not null primary key, " +
                     "                      MovieName Varchar(255)" +
                     "); ");
            statement.executeUpdate("CREATE TABLE Genre(" +
                    "                      GenreID Integer not null primary key auto_increment," +
                    "                      GenreName Varchar(255)" +
                    "); ");
            statement.executeUpdate("CREATE TABLE Polls(" +
                    "                      PollID Integer primary key auto_increment not null," +
                    "                      EndDate DateTime" +
                    "); ");
            statement.executeUpdate("CREATE TABLE Types(" +
                    "                      TypeID Integer not null primary key auto_increment," +
                    "                      TypeName VARCHAR(20)" +
                    "); ");
            statement.executeUpdate("CREATE TABLE Users(" +
                    "                      UserID Integer not null primary key auto_increment," +
                    "                      FirstName Varchar(255)," +
                    "                      Surname Varchar(255)," +
                    "                      Username Varchar(30)," +
                    "                      Password char(64)" +
                    "); ");
            statement.executeUpdate("CREATE TABLE Casts (" +
                    "                       MovieID VARCHAR (10) NOT NULL," +
                    "                       ActorID VARCHAR (10) NOT NULL," +
                    "                       primary key (MovieID, ActorID)," +
                    "                       FOREIGN KEY (MovieID) REFERENCES Movie(MovieID)," +
                    "                       FOREIGN KEY (ActorID) REFERENCES Actors(ActorID)" +
                    "); ");
            statement.executeUpdate("CREATE TABLE MovieGenre(" +
                    "                           MovieID VARCHAR(10) Not null," +
                    "                           GenreID Integer not null," +
                    "                           primary key (MovieID, GenreID)," +
                    "                           Foreign key(MovieID) references Movie(MovieID)," +
                    "                           Foreign key (GenreID) references Genre(GenreID)" +
                    "); ");
            statement.executeUpdate("CREATE TABLE PollOptions(" +
                    "                            OptionID integer not null primary key auto_increment ," +
                    "                            PollID integer," +
                    "                            MovieID Varchar(10)," +
                    "                            Foreign key (PollID) references Polls(PollID)," +
                    "                            foreign key (MovieID) references Movie(MovieID)" +
                    "); ");
            statement.executeUpdate("CREATE TABLE PollVotes(" +
                    "                          VoteID Integer not null primary key auto_increment ," +
                    "                          UserID Integer," +
                    "                          OptionID Integer," +
                    "                          Foreign key (UserID) references Users(UserID)," +
                    "                          Foreign key (OptionID) references PollOptions(OptionID)" +
                    "); ");
            statement.executeUpdate("CREATE TABLE RegistrationCodes(" +
                    "                                  RegistrationCode Integer Not null primary key," +
                    "                                  TypeID integer," +
                    "                                  Foreign Key (TypeID) References Types(TypeID)" +
                    "); ");
            statement.executeUpdate("CREATE TABLE UserTypes(" +
                    "                          UserID Integer not null," +
                    "                          TypeID Integer not null," +
                    "                          primary key(UserID, TypeID)," +
                    "                          Foreign Key (UserID) references Users(UserID)," +
                    "                          Foreign Key (TypeID) references Types(TypeID)" +
                    "); ");
            statement.execute("INSERT INTO Types (TypeID, TypeName)" +
                    "VALUES (1, 'Teacher'), (2, 'Cover Teacher'), (3, 'Student'); ");
            statement.execute("INSERT INTO RegistrationCodes (RegistrationCode, TypeID) " +
                    "VALUES (100000, 1); ");
            statement.executeUpdate("INSERT INTO Genre (GenreID, GenreName) " +
                    "VALUES (1, 'Action'), (2, 'Adventure'), (3, 'Comedy'), " +
                    "(4, 'Horror'), (5, 'Drama'), (6, 'Historical'), (7, 'Romance')");

            statement.close();
            conn.commit();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(1);
        }
    }

    public int[] useRegistrationCode(int code){
        int[] codeAndType = new int[]{-1};

        try{
            PreparedStatement statement = conn.prepareStatement("SELECT RegistrationCode, TypeID FROM RegistrationCodes WHERE RegistrationCode = ?");
            statement.setInt(1, code);

            ResultSet rs = statement.executeQuery();
            if (rs.next()){
               codeAndType = new int[]{rs.getInt(1), rs.getInt(2)};
            }
            rs.close();
            statement.close();
            removeRegistrationCode(code);

        }catch (SQLException e) {
            if (e.getErrorCode() == 1146){
                initDatabase();
                return useRegistrationCode(code);
            }

            System.err.println(e.getMessage());
            System.exit(1);
        }

        return codeAndType;
    }

    private void removeRegistrationCode(int code) {

        try{
            PreparedStatement statement = conn.prepareStatement("Delete from RegistrationCodes where RegistrationCode = ?");
            statement.setInt(1, code);
            statement.executeUpdate();
            statement.close();
            conn.commit();


        }catch (SQLException e){
            System.err.println(e.getMessage());
        }

    }

    public int genNewUserID() {
        try{
            PreparedStatement statement = conn.prepareStatement("SELECT max(UserID) as latest FROM Users limit 1;");
            int latest = 0;

            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                latest = rs.getInt("latest");
            }
            rs.close();
            statement.close();
            return latest + 1;

        }catch (SQLException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }

        return -1;

    }

    public void registerNewUser(Users user) {
        try{
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO Users (UserID, FirstName, Surname, Username, Password) " +
                         "values (?, ?, ?, ?, ?)");

            statement.setInt(1, user.getUserID());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getUsername());
            statement.setString(5, user.getPassword());

            statement.executeUpdate();
            statement.close();
            conn.commit();

            createUserType(user);

        }catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    private void createUserType(Users user){
        try{
            PreparedStatement statement = conn.prepareStatement("INSERT INTO UserTypes (userid, typeid) values (?, ?)");
            statement.setInt(1, user.getUserID());
            statement.setInt(2, user.getUserTypes().getID());
            statement.executeUpdate();
            statement.close();
            conn.commit();

        } catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }


    public boolean userNameExists(String username) {
        boolean exists;
        try{
            PreparedStatement statement = conn.prepareStatement("Select Username from Users where Username = ?");
            statement.setString(1, username);

            ResultSet rs = statement.executeQuery();
            exists = rs.next();
            rs.close();
            statement.close();

            return exists;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return true;
    }

    public Users getUserByLogin(String username, String password) {
        Users user = null;
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT FirstName, Surname, Users.UserID, UT.TypeID " +
                    "from Users " +
                    "JOIN UserTypes UT on Users.UserID = UT.UserID " +
                    "where (Username = ?) and (Password = ?)");


            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();

            if (rs.next()){
                user = new Users(rs.getString(1), rs.getString(2), rs.getInt(3), username, password, UserTypes.getTypeByID(rs.getInt(4)));
            }
            rs.close();
            statement.close();

        } catch (SQLException e) {
            if (e.getErrorCode() == 1146){
                initDatabase();
                return getUserByLogin(username, password);
            } else{
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
        return user;
    }

    public void displayUserHistory(String username) {
        try{
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT PollID, MovieName " +
                            "FROM PollVotes " +
                            "JOIN PollOptions PO on PO.OptionID = PollVotes.OptionID " +
                            "JOIN Movie M on M.MovieID = PO.MovieID " +
                            "WHERE PollVotes.UserID = (SELECT UserID FROM users where Username = ?) " +
                            "LIMIT 10");

            statement.setString(1, username);

            ResultSet rs = statement.executeQuery();
            if (!rs.next()){
                System.out.println("No available records");
            }else {
                rs.close(); // Sqlite doesn't support going back in result sets
                rs = statement.executeQuery();
                while(rs.next()){
                    System.out.printf("Poll #%d - %s \n", rs.getInt("PollID"), rs.getString("MovieName"));
                }
            }
            rs.close();
            statement.close();

        } catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public VoteOptionManager getActivePoll(){
        try{

            PreparedStatement statement = conn.prepareStatement(
                    "SELECT M.MovieID, M.MovieName, PollOptions.OptionID, P.PollID " +
                            "FROM PollOptions " +
                            "JOIN Polls P on P.PollID = PollOptions.PollID " +
                            "JOIN Movie M on M.MovieID = PollOptions.MovieID " +
                            "WHERE p.EndDate > now()");

            ResultSet rs = statement.executeQuery();

            VoteOptionManager voteOptionManager = null;
            if (rs.next()){
                voteOptionManager = new VoteOptionManager(rs.getInt("PollID"));
                rs.close();
                rs = statement.executeQuery();

                int i = 1;
                while(rs.next()){
                    voteOptionManager.addOption(i, new VoteOption(rs.getString("MovieID"), rs.getString("MovieName"), rs.getInt("OptionID")));
                    i++;
                }
            }


            rs.close();
            statement.close();

            return voteOptionManager;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public void registerVote(Users user, VoteOption option){
        try{
            VoteOptionManager voteOptionManager = getActivePoll();
            if (voteOptionManager == null){
                System.out.println("The poll has finished. Your vote hasn't been registered");
                return;
            } // Checks if the poll is still active

            PreparedStatement statement = conn.prepareStatement("INSERT INTO PollVotes (UserID, OptionID) values (?, ?)");
            statement.setInt(1, user.getUserID());
            statement.setInt(2, option.getOptionID());

            statement.executeUpdate();
            statement.close();
            conn.commit();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public boolean hasVotedMovie(Users user){
        boolean voted = true;
        try{
            int pollID = getActivePoll().getPollID();
            PreparedStatement statement = conn.prepareStatement(
                        "SELECT UserID FROM PollVotes " +
                            "join PollOptions PO on PollVotes.OptionID = PO.OptionID WHERE PollID = ? AND UserID = ?");
            statement.setInt(1, pollID);
            statement.setInt(2, user.getUserID());


            ResultSet rs = statement.executeQuery();

            voted = rs.next();

            rs.close();
            statement.close();

        } catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return voted;
    }

    public void deleteUser(int userID) {
        try{
            PreparedStatement userTypesStatement = conn.prepareStatement("DELETE FROM UserTypes WHERE (UserID = ?)");
            PreparedStatement pollVotesStatement = conn.prepareStatement("DELETE FROM PollVotes WHERE (UserID = ?)");
            PreparedStatement userStatement = conn.prepareStatement("DELETE FROM Users WHERE (UserID = ?)");

            userTypesStatement.setInt(1, userID);
            pollVotesStatement.setInt(1, userID);
            userStatement.setInt(1, userID);

            userTypesStatement.executeUpdate();
            pollVotesStatement.executeUpdate();
            userStatement.executeUpdate();

            userTypesStatement.close();
            pollVotesStatement.close();
            userStatement.close();

            conn.commit();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    public void resetPassword(int userID, String password) {
        try{
            PreparedStatement statement = conn.prepareStatement("UPDATE Users SET Password = ? WHERE UserID = ?");
            statement.setString(1, password);
            statement.setInt(2, userID);
            statement.executeUpdate();
            statement.close();
            conn.commit();

        }catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public LinkedList<Integer> createRegistrationCodes(int number, UserTypes userType) {
        LinkedList<Integer> regCodes = new LinkedList<>();
        try{
            Random random = new Random();
            int registrationCode;
            boolean created;

            for (int i = 0; i < number; i++){
                do{ // Checks if the RegistrationCode is being used
                    PreparedStatement stmtCodeIsUsed = conn.prepareStatement("SELECT RegistrationCode FROM RegistrationCodes where RegistrationCode = ?");
                    registrationCode  = random.nextInt(999999 - 100000) + 100000; // Generates a random 6 digit number
                    stmtCodeIsUsed.setInt(1, registrationCode);

                    ResultSet rs = stmtCodeIsUsed.executeQuery();
                    created = !rs.next();

                    rs.close();
                    stmtCodeIsUsed.close();

                }while (!created);
                PreparedStatement stmtInsertCode = conn.prepareStatement("INSERT INTO RegistrationCodes (RegistrationCode, TypeID) VALUES (?, ?)");

                stmtInsertCode.setInt(1, registrationCode);
                stmtInsertCode.setInt(2, userType.getID());
                stmtInsertCode.executeUpdate();
                stmtInsertCode.close();
                conn.commit();
                regCodes.append(registrationCode);


            }


        }catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return regCodes;
    }

    public int getNumberOfMovies() {
        int number = 0;
        try{
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT count(MovieID) FROM Movie");

            if (rs.next()){
                number = rs.getInt(1);
            }


            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return number;
    }

    public AdjacencyList<String> createGraph(String movieID){
        AdjacencyList<String> graph = new AdjacencyList<>();
        try {
            Hashmap<String, Float> weights = getWeights(movieID);
            addMoviesAsNodes(graph);

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT m1.MovieID as Start, m2.MovieID as End " +
                    "From Casts as c1 " +
                    "INNER JOIN Movie as m1 on c1.MovieID = m1.MovieID " +
                    "INNER JOIN  Casts as c2 on c2.ActorID = c1.ActorID " +
                    "INNER JOIN  Movie as m2 on c2.MovieID = m2.MovieID " +
                    "WHERE c1.MovieID != c2.MovieID " +
                    "GROUP BY m1.MovieID, m2.MovieID");

            while (rs.next()){
                String startMovieID = rs.getString("Start");
                String endMovieID = rs.getString("End");
                Float weight = weights.item( endMovieID.equals(movieID) ? startMovieID : endMovieID );

                graph.addLink(startMovieID, new Pairs<>(endMovieID, weight));

            }


            rs.close();
            statement.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return graph;
    }

    public String[] getPollWinner(){
        String[] pollResults = new String[3];
        try{
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT PO.MovieID, COUNT(PV.OptionID) AS NumberOfVotes " +
                    "FROM Polls P JOIN PollOptions PO ON P.PollID = PO.PollID " +
                    "JOIN PollVotes PV ON PO.OptionID = PV.OptionID " +
                    "WHERE P.EndDate = (SELECT MAX(EndDate) FROM Polls) " +
                    "GROUP BY P.PollID, PO.MovieID " +
                    "ORDER BY NumberOfVotes DESC LIMIT 1;");

            if (rs.next()){
                pollResults[0] = movieNameByID(rs.getString("MovieID"));
                pollResults[1] = rs.getString("MovieID");
                pollResults[2] = rs.getString("NumberOfVotes");
            }

            rs.close();
            statement.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return pollResults;
    }

    private String movieNameByID(String movieID) {
        String movieName = null;
        try{
            PreparedStatement statement = conn.prepareStatement("SELECT MovieName FROM Movie WHERE MovieID = ? LIMIT 1");
            statement.setString(1, movieID);
            ResultSet rs = statement.executeQuery();

            if (rs.next()){
                movieName = rs.getString("MovieName");
            }

            rs.close();
            statement.close();

        } catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return movieName;
    }

    private void addMoviesAsNodes(AdjacencyList<String> graph){
        try{
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT M1.MovieID, COUNT(DISTINCT M2.MovieID) AS NumberOfConnections " +
                            "FROM Casts AS C1 " +
                            "INNER JOIN Casts AS C2 ON C1.ActorID = C2.ActorID AND C1.MovieID != C2.MovieID " +
                            "INNER JOIN Movie AS M1 ON C1.MovieID = M1.MovieID " +
                            "INNER JOIN Movie AS M2 ON C2.MovieID = M2.MovieID " +
                            "GROUP BY M1.MovieID");

            while(rs.next()){
                graph.addNode(rs.getString("MovieID"), rs.getInt("NumberOfConnections"));
            }

            rs.close();
            statement.close();

        }catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private Hashmap<String, Float> getWeights (String sourceID){
        Hashmap<String, Float> weights = new Hashmap<>(getNumberOfMovies() - 1);
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "Select getSharedGenre.MovieID, 1 / (SharedActors + 0.5*(SharedGenres + 1)) as Weight From " +
                            "(SELECT M2.MovieID, COALESCE(COUNT(C1.ActorID), 0) as SharedActors " +
                            "FROM Movie M2 " +
                            "LEFT JOIN Casts C2 ON M2.MovieID = C2.MovieID " +
                            "LEFT JOIN Casts C1 ON C1.ActorID = C2.ActorID AND C1.MovieID = ? " +
                            "WHERE M2.MovieID != ? GROUP BY M2.MovieID) as getSharedActors " +
                            "JOIN  " +
                            "(SELECT M2.MovieID, (Select COUNT(Genre.GenreID) as genres from genre) - COALESCE(COUNT(MG1.GenreID), 0) as SharedGenres " +
                            "FROM Movie M2 LEFT JOIN MovieGenre MG2 ON M2.MovieID = MG2.MovieID     " +
                            "LEFT JOIN MovieGenre MG1 ON MG1.GenreID != MG2.GenreID AND MG1.MovieID = ?     " +
                            "WHERE M2.MovieID != ?     " +
                            "GROUP BY M2.MovieID)       " +
                            "as getSharedGenre on getSharedGenre.MovieID = getSharedActors.MovieID; "); // explained in documented design

            statement.setString(1, sourceID);
            statement.setString(2, sourceID);
            statement.setString(3, sourceID);
            statement.setString(4, sourceID);

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                weights.add(rs.getString("MovieID"), rs.getFloat("Weight"));
            }

            rs.close();
            statement.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return weights;

    }

    public LinkedList<VoteOption> getAllMovies(){
        LinkedList<VoteOption> movies = new LinkedList<>();
        try{
            PreparedStatement statement = conn.prepareStatement("SELECT MovieID FROM Movie");
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                movies.append(new VoteOption(rs.getString("MovieID")));
            }
            rs.close();
            statement.close();

        }catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return movies;

    }

    public void createPoll(LinkedList<VoteOption> voteOptions){
        try{
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO Polls (EndDate) VALUES (DATE_ADD(NOW(), INTERVAL 2 MINUTE))");

            PreparedStatement addOptions = conn.prepareStatement(
                    "INSERT INTO PollOptions (PollID, MovieID) " +
                    "VALUES ((SELECT PollID FROM Polls ORDER BY PolLID DESC LIMIT 1), ?)");

            for (Node<VoteOption> voteOption: voteOptions){
                addOptions.setString(1, voteOption.getData().getMovieID());
                addOptions.addBatch();
            }
            addOptions.executeBatch();
            statement.close();
            addOptions.close();
            conn.commit();

        } catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public String getRandomMovie(){
        String movieID = null;
        try{
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT MovieID FROM Movie ORDER BY RAND() LIMIT 1");

            if (rs.next()){
                movieID = rs.getString("MovieID");
            }

            rs.close();
            statement.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return movieID;
    }

    public void fillMovies(Hashmap<String , Float> visited, int numberOfMovies) {
        try{
            StringBuilder disallowed = new StringBuilder();
            for (Pairs<String, Float> movie : visited){
                disallowed.append(movie.getKey()).append(",");
            }

            PreparedStatement statement = conn.prepareStatement("SELECT MovieID FROM Movie WHERE MovieID not in (?) ORDER BY rand() LIMIT ?");
            statement.setString(1, disallowed.substring(0, disallowed.length() - 1));
            statement.setInt(2, numberOfMovies - visited.length());

            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                visited.add(rs.getString("MovieID"), 0f);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public int usernameToUserID(String username) {
        int userID = -1;
        try{
            PreparedStatement statement = conn.prepareStatement("SELECT UserID from Users where Username = ?");
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                userID = rs.getInt("UserID");
            }
            rs.close();
            statement.close();

        } catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return userID;
    }

    public LinkedList<Pairs<Integer, String>> getGenres() {
        LinkedList<Pairs<Integer, String>> genres = new LinkedList<>();
        try{
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT GenreName, GenreID FROM Genre");

            while(rs.next()){
                genres.append(new Pairs<>(rs.getInt("GenreID"), rs.getString("GenreName")));
            }

            statement.close();
            rs.close();

        } catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return genres;

    }

    public void addMovie(String movieID, String movieName){
        try{
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Movie (MovieID, MovieName) VALUES (?, ?)");
            statement.setString(1, movieID);
            statement.setString(2, movieName);
            statement.executeUpdate();
            statement.close();
            conn.commit();

        }catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    public void addMovieCast(String movieID, LinkedList<String> actors){
        try{
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Casts (MovieID, ActorID) VALUES (?, ?)");

            for (Node<String> actor : actors){
                statement.setString(1, movieID);
                statement.setString(2, actor.getData());
                statement.addBatch();
            }

            statement.executeBatch();
            statement.close();
            conn.commit();
        } catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }



    }

    public void addMovieGenre(String movieID, LinkedList<Integer> genres){
        try{
            PreparedStatement statement = conn.prepareStatement("INSERT INTO MovieGenre(MovieID, GenreID) VALUES (?, ?); ");

            for (Node<Integer> genre : genres){
                statement.setString(1, movieID);
                statement.setInt(2, genre.getData());
                statement.addBatch();
            }

            statement.executeBatch();
            statement.close();
            conn.commit();

        } catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public boolean actorNotExists(String actorID){
        boolean exists = false;

        try{
            PreparedStatement statement = conn.prepareStatement("SELECT actorID FROM actors WHERE actorID = ?");
            statement.setString(1, actorID);

            ResultSet rs = statement.executeQuery();

            exists = rs.next();

            rs.close();
            statement.close();

        } catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return !exists;
    }

    public void addActor(String actorID, String actorName) {
        try {
            
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Actors (ActorID, ActorName) VALUES (? , ?)");
            statement.setString(1, actorID);
            statement.setString(2, actorName);
            statement.executeUpdate();
            statement.close();
            conn.commit();
            
        }catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
        
    }

    public void removeMovie(String movieID) {
        try{
            PreparedStatement statement = conn.prepareStatement(
                "DELETE FROM PollVotes WHERE OptionID in (SELECT OptionID FROM PollOptions where MovieID = ?);");
            statement.setString(1, movieID);
            statement.executeUpdate();
            statement.close();

            statement = conn.prepareStatement("DELETE FROM PollOptions WHERE  MovieID = ?; ");
            statement.setString(1, movieID);
            statement.executeUpdate();
            statement.close();

            statement = conn.prepareStatement("DELETE FROM Casts WHERE  MovieID = ?; ");
            statement.setString(1, movieID);
            statement.executeUpdate();
            statement.close();

            statement = conn.prepareStatement("DELETE FROM MovieGenre WHERE  MovieID = ?; ");
            statement.setString(1, movieID);
            statement.executeUpdate();
            statement.close();

            statement = conn.prepareStatement("DELETE FROM Movie WHERE MovieID = ?;");
            statement.setString(1, movieID);
            statement.executeUpdate();
            statement.close();

            conn.commit();
            

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    public void removeActor(String actorID) {
        try{
            PreparedStatement statement = conn.prepareStatement("DELETE FROM Casts where ActorID = ?;");
            statement.setString(1, actorID);
            statement.executeUpdate();
            statement.close();

            statement = conn.prepareStatement("DELETE FROM Actors WHERE ActorID = ?;");
            statement.setString(1, actorID);
            statement.executeUpdate();
            statement.close();

            conn.commit();

        }catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public boolean movieExists(String movieID) {
        boolean exists = true;
        try{
            PreparedStatement statement = conn.prepareStatement("SELECT MovieID FROM Movie WHERE MovieID = ?");
            statement.setString(1, movieID);
            ResultSet rs = statement.executeQuery();

            exists = rs.next();

            rs.close();
            statement.close();

        }catch (SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return exists;

    }
}
