package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mads, Kevin, Daniel
 * The following class handles all menus shown to the user
 */
public class Menu {
    private User loggedInUser;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<User> newUsersList = new ArrayList<>();
    TextUI ui = new TextUI();
    Login login = new Login();
    FileIO io = new FileIO();
    String uInputUsername;
    String uInputPassword;
    private boolean isAdmin = false;
    private boolean movieFound = false;
    private boolean seriesFound = false;
    User newUsers = new User(uInputUsername, uInputPassword, isAdmin);
    private ArrayList<Movie> movieList;
    private int currentIndex;
    Movie selectedMovie;
    Series selectedSeries;
    private boolean movieSelected = false;
    private ArrayList<Series> seriesList;
    private boolean seriesSelected = false;
    private int pageSize = 10;

    //Constructor
    public Menu() {
        currentIndex = 0;
        loggedInUser = null;

    }

    /**
     * The following method is responsible for running all our methods in one place
     * It is run in streaming class
     * It is also where a user gets to create a user by running the createuser() method from user class
     */
    public void loginMenu() {
        ui.displayMsg("1. Login\n2. Create new user");
        String loginOptions = ui.getInput("");
        switch (loginOptions) {
            case "1":
                ui.displayMsg("Please type your Username and password:");
                login.login(users);
                if (login.getLoggedInUser() != null) {
                    loggedInUser = login.getLoggedInUser();  // Set the loggedInUser
                    if (loggedInUser.getIsAdmin()) {
                        displayAdminOptions();
                    } else {
                        displayUserOptions();
                    }
                }
                break;
            case "2":
                ui.displayMsg("Please write your desired username!");
                uInputUsername = ui.getInput("");
                ui.displayMsg("Please write your desired password!");
                uInputPassword = ui.getInput("");
                newUsers.createUser(uInputUsername, uInputPassword, isAdmin);
                ui.displayMsg("\nYour account was successfully created!");

                // Automatically log in the newly created user
                loggedInUser = new User(uInputUsername, uInputPassword, isAdmin);
                if (newUsersList != null) {
                    newUsersList.add(loggedInUser);
                }
                displayUserOptions();
                break;
            default:
                ui.displayMsg("None of the options was chosen, try again");
                loginMenu();
        }
    }

    /**
     * The following method displays user options
     */
    public void displayUserOptions() {
        ui.displayMsg("What do you want to do?: ");
        ui.displayMsg("1.Watch movie" + "\n" + "2.Watch series" + "\n" + "3.Go to My Favorite" + "\n" + "4.Watch movies/series by genre"+"\n"+"5.Exit program");

        String options = ui.getInput("");

        switch (options) {
            case "1":
                displayMovies();
                break;
            case "2":
                displaySeries();
                break;
            case "3":
                displayFavorites();
                break;
            case "4":
                searchByGenreMovie();
                break;
            case "5":
                break;
            default:
                ui.displayMsg("None of the options was selected");

        }
    }

    /**
     * The following method displays the options for an admin user
     */
    public void displayAdminOptions() {
        ui.displayMsg("What do you want to do?: ");
        ui.displayMsg("1.Watch movie" + "\n" + "2.Watch series" + "\n" + "3.Go to My Favorite Movies" + "\n" + "4.Watch movies/series by genre" + "\n" + "5.Admin panel"+"\n"+"6.Exit program");

        String options = ui.getInput("");
        switch (options) {
            case "1":
                displayMovies();
                break;
            case "2":
                displaySeries();
                break;
            case "3":
                displayFavorites();
                break;
            case "4":
                genreMenu();
                break;
            case "5":
                adminPanel();
                break;
            case "6":
                break;
            default:
                ui.displayMsg("None of the options was selected");

        }
    }

    /**
     * The following method handles genres
     */
    public void genreMenu() {
        ui.displayMsg("What do you want to do?: ");
        ui.displayMsg("1.Sort movie by genre" + "\n" + "2.Sort series by genre" + "\n");

        String genreOptions = ui.getInput("");

        switch (genreOptions) {
            case "1":
                searchByGenreMovie();
                break;

            case "2":
                searchByGenreSeries();
                break;

            default:
                ui.displayMsg("None of the options was selected");
        }
    }

    /**
     * The following method displays all movies
     * 10 movies at a time
     */
    public void displayMovies() {
        movieList = io.readMovieData();

        ui.displayMsg("----------------- All Movies -----------------");
        int pageSize = 10;

        for (int i = currentIndex; i < Math.min(currentIndex + pageSize, movieList.size()); i++) {
            System.out.println((i - currentIndex + 1) + ". " + movieList.get(i).toString());
        }

        displayNavigationOptionsMovie();
    }

    /**
     * The following method display series
     * 10 series at a time
     */
    public void displaySeries() {
        seriesList = io.readSeriesData();

        ui.displayMsg("----------------- All Series -----------------");


        for (int i = currentIndex; i < Math.min(currentIndex + pageSize, seriesList.size()); i++) {
            System.out.println((i - currentIndex + 1) + ". " + seriesList.get(i).toString());
        }

        displayNavigationOptionsSeries();
    }

    /**
     * Following method makes the user able to select a movie from search by genre
     *
     * @param matchingMovies takes Arraylist as parameter to save matching genre searched in an Arraylist
     */
    public void selectMovieFromSearch(ArrayList<Movie> matchingMovies) {

        while (true) {
            ui.displayMsg("Enter your choice");
            int movieIndex = ui.getIntInput("");
            if (movieIndex >= 1 && movieIndex <= matchingMovies.size()) {
                selectedMovie = matchingMovies.get(movieIndex - 1);
                ui.displayMsg("The movie you selected: " + selectedMovie.toString());
                currentlyPlaying();
                return;
            } else {
                ui.displayMsg("Invalid choice" + "\n" + "try again");
                break;
            }
        }
    }

    /**
     * The following method displays the ArrayList which the search by gets saved to
     *
     * @param matchingMovies takes ArrayList as to display the selected genres
     */
    public void displayMatchingMovies(ArrayList<Movie> matchingMovies) {
        ui.displayMsg("Movies sorted by genre");
        for (int i = 0; i < matchingMovies.size(); i++) {
            System.out.println((i + 1) + ". " + matchingMovies.get(i).toString());
        }
    }

    /**
     * 6
     * The following method searches through the genres of the movies
     * Strores them in a new Arraylist
     * Makes the user able to search movies by genre
     *
     * @return it returns an arraylist with the movies matching the input by the user
     */
    public ArrayList<Movie> searchByGenreMovie() {
        ArrayList<Movie> matchingMovie = io.readMovieData();
        ui.displayMsg("Please enter the genre you're looking for");
        ArrayList<Movie> matchingMovieByGenre = new ArrayList<>();
        String input = ui.getInput("");
        for (Movie movie : matchingMovie) {
            String[] movieGenres = movie.getGenre().split(", ");
            for (String genre : movieGenres) {
                if (input.equalsIgnoreCase(genre.trim())) {
                    matchingMovieByGenre.add(movie);
                    movieFound = true;
                    break;
                }
            }
        }
        if (movieFound) {
            displayMatchingMovies(matchingMovieByGenre);
            selectMovieFromSearch(matchingMovieByGenre);
        } else {
            ui.displayMsg("Sorry couldn't find a matching genre" + "\n" + "Try again");
            searchByGenreMovie();

        }
        return matchingMovieByGenre;
    }


    /**
     * Following method makes the user able to select a series from search by genre
     *
     * @param matchingSeries takes Arraylist as parameter to save matching genre searched in an Arraylist
     */
    public void selectseriesFromSearch(ArrayList<Series> matchingSeries) {

        while (true) {
            int seriesIndex = ui.getIntInput("Enter your choice");
            if (seriesIndex >= 1 && seriesIndex <= matchingSeries.size()) {
                selectedSeries = matchingSeries.get(seriesIndex - 1);
                ui.displayMsg("The movie you selected: " + selectedSeries.toString());
                currentlyWatchingSeries();
                return;
            } else {
                ui.displayMsg("Invalid choice" + "\n" + "try again");
                selectseriesFromSearch(matchingSeries);
                break;
            }
        }
    }

    /**
     * The following method displays the ArrayList which the search by gets saved to
     *
     * @param matchingSeries takes ArrayList as to display the selected genres
     */
    public void displayMatchingSeries(ArrayList<Series> matchingSeries) {
        ui.displayMsg("Series sorted by genre");
        for (int i = 0; i < matchingSeries.size(); i++) {
            System.out.println((i + 1) + ". " + matchingSeries.get(i).toString());
        }
    }

    /**
     * The following method searches through the genres of the series
     * Strores them in a new Arraylist
     * Makes the user able to search series by genre
     *
     * @return it returns an arraylist with the series matching the input by the user
     */

    public ArrayList<Series> searchByGenreSeries() {
        ArrayList<Series> matchingSeries = io.readSeriesData();
        ui.displayMsg("Please enter the genre you're looking for");
        ArrayList<Series> matchingSeriesByGenre = new ArrayList<>();
        String input = ui.getInput("");
        for (Series series : matchingSeries) {
            String[] seriesGenres = series.getGenres();
            for (String genre : seriesGenres) {
                if (input.equalsIgnoreCase(genre.trim())) {
                    matchingSeriesByGenre.add(series);
                    seriesFound = true;
                    break;
                }
            }
        }
        if (seriesFound) {
            displayMatchingSeries(matchingSeriesByGenre);
            selectseriesFromSearch(matchingSeriesByGenre);

        } else {
            ui.displayMsg("Sorry couldn't find a matching genre" + "\n" + "Try again");
            searchByGenreSeries();
        }
        return matchingSeries;
    }

    /**
     * The following method handles the navigation feature to "scroll" through the pages of movies
     */
    private void displayNavigationOptionsMovie() {
        ui.displayMsg("N. Next 10 movies");
        ui.displayMsg("P. Previous 10 movies");
        ui.displayMsg("S. Select a movie");
        ui.displayMsg("E. Exit");

        String choice = ui.getInput("Enter your choice: ");

        switch (choice.toUpperCase()) {
            case "N":
                currentIndex += 10;
                break;
            case "P":
                currentIndex = Math.max(0, currentIndex - 10);
                break;
            case "S":
                selectMovie();
                movieSelected = true;
                return;
            case "E":
                if (login.getLoggedInUser() != null) {
                    if (login.getLoggedInUser().getIsAdmin()) {
                        displayAdminOptions();
                    } else {
                        displayUserOptions();
                    }
                }
                break;
            default:
                ui.displayMsg("Invalid choice. Please enter a valid option.");
                return;
        }

        if (!movieSelected) {
            displayMovies();
        }
    }

    /**
     * The following method handles the navigation feature to "scroll" through the pages of series
     */
    private void displayNavigationOptionsSeries() {
        ui.displayMsg("N. Next 10 series");
        ui.displayMsg("P. Previous 10 series");
        ui.displayMsg("S. Select a series");
        ui.displayMsg("E. Exit");

        String choice = ui.getInput("Enter your choice: ");

        switch (choice.toUpperCase()) {
            case "N":
                currentIndex += 10;
                break;
            case "P":
                currentIndex = Math.max(0, currentIndex - 10);
                break;
            case "S":
                selectSeries();
                seriesSelected = true;
                return;
            case "E":
                if (login.getLoggedInUser() != null) {
                    if (login.getLoggedInUser().getIsAdmin()) {
                        displayAdminOptions();
                    } else {
                        displayUserOptions();
                    }
                }
                break;
            default:
                ui.displayMsg("Invalid choice. Please enter a valid option.");
                return;
        }

        if (!seriesSelected) {
            displaySeries();
        }
    }

    /**
     * The following method makes the user able to choose a movie from the display lsit
     */
    private void selectMovie() {
        int pageSize = 10;

        while (true) {
            int movieIndex = ui.getIntInput("Enter the number of the movie you want to select:");

            if (movieIndex >= 1 && movieIndex <= pageSize && currentIndex + movieIndex - 1 < movieList.size()) {
                selectedMovie = movieList.get(currentIndex + movieIndex - 1);
                ui.displayMsg("Selected Movie: " + selectedMovie.toString());
                currentlyPlaying();
                return;
            } else {
                ui.displayMsg("Invalid movie number. Please enter a valid number.");
                selectMovie();
            }
            break;
        }
    }


    /**
     * The following method makes the user able to choose a series from the display list
     */
    private void selectSeries() {
        int pageSize = 10;

        while (true) {
            int seriesIndex = ui.getIntInput("Enter the number of the series you want to select:");

            if (seriesIndex >= 1 && seriesIndex <= pageSize && currentIndex + seriesIndex - 1 < seriesList.size()) {
                selectedSeries = seriesList.get(currentIndex + seriesIndex - 1);
                ui.displayMsg("Selected Series: " + selectedSeries.toString());
                currentlyWatchingSeries();
                return;
            } else {
                ui.displayMsg("Invalid series number. Please enter a valid number.");
                selectSeries();
                break;
            }
        }
    }


    /**
     * The following methods displays the current series which the user has chosen to watch
     */
    private void currentlyWatchingSeries() {
        System.out.println("-------------------------------------");
        System.out.println("You are now watching:\n" + selectedSeries);
        ui.displayMsg("Do you want to add it to your Favorites?: Yes/No");
        String input = ui.getInput("").toLowerCase();


        switch (input) {
            case "yes":
                FavoriteMenu();
                break;
            case "no":
                if (login.getLoggedInUser() != null) {
                    if (login.getLoggedInUser().getIsAdmin()) {
                        displayAdminOptions();
                    } else {
                        displayUserOptions();
                    }
                }
                break;
            default:
                System.out.println("Invalid option, please insert Yes or No");

        }
    }

    /**
     * The following method display the current playing movie
     */
    private void currentlyPlaying() {
        System.out.println("-------------------------------------");
        System.out.println("You are now watching:\n" + selectedMovie);
        ui.displayMsg("Do you want to add it to your Favorites?: Yes/No");
        String input = ui.getInput("").toLowerCase();

        switch (input) {
            case "yes":
                FavoriteMenu();
                break;
            case "no":
                if (login.getLoggedInUser() != null) {
                    if (login.getLoggedInUser().getIsAdmin()) {
                        displayAdminOptions();
                    } else {
                        displayUserOptions();
                    }
                }
                break;
            default:
                System.out.println("Invalid option, please insert Yes or No");

        }
    }

    /**
     * The following method handles the users input in saving a movie to the exact users favortie list
     */
    private void FavoriteMenu() {
        if (loggedInUser == null) {
            ui.displayMsg("User not logged in. Please log in before adding favorites.");
            return;
        }

        ui.displayMsg("1. Add movie/series to favorites\n2. Exit to main menu");
        String favInput = ui.getInput("");

        switch (favInput) {
            case "1":
                if (selectedMovie != null) {
                    loggedInUser.addToFavorites(selectedMovie.getTitle());
                    ui.displayMsg("Added movie to favorites: " + selectedMovie.getTitle());
                    if (login.getLoggedInUser() != null) {
                        if (login.getLoggedInUser().getIsAdmin()) {
                            displayAdminOptions();
                        } else {
                            displayUserOptions();
                        }
                    }
                } else if (selectedSeries != null) {
                    loggedInUser.addToFavorites(selectedSeries.getTitle());
                    ui.displayMsg("Added series to favorites: " + selectedSeries.getTitle());
                    if (login.getLoggedInUser() != null) {
                        if (login.getLoggedInUser().getIsAdmin()) {
                            displayAdminOptions();
                        } else {
                            displayUserOptions();
                        }
                    } else {
                        ui.displayMsg("No movie or series selected.");
                    }
                }
                break;
            case "2":
                if (login.getLoggedInUser() != null) {
                    if (login.getLoggedInUser().getIsAdmin()) {
                        displayAdminOptions();
                    } else {
                        displayUserOptions();
                    }
                }
                break;
            default:
                ui.displayMsg("None of the options was selected");
        }
    }

    /**
     * The following method handles the options an admin user has
     * removing a movie or series from the data file
     * I.e. if netflix removed a movie from their website
     */
    public void adminPanel() {
        ui.displayMsg("1.Add movie/series " + "\n" + "2.Remove movie/series " + "\n" + "3.Exit to main menu");
        String inputAdmin = ui.getInput("");
        switch (inputAdmin) {
            case "1":
                ui.displayMsg("Enter the details for the new movie/series:");
                ui.displayMsg("Title:");

                String title = ui.getInput("");

                ui.displayMsg("Year:");
                int year = Integer.parseInt(ui.getInput(""));

                ui.displayMsg("Genre:");
                String genre = ui.getInput("");

                ui.displayMsg("Rating:");
                double rating = Double.parseDouble(ui.getInput(""));

                if (title != null && !title.isEmpty() && genre != null && !genre.isEmpty()) {
                    ui.displayMsg("Is it a movie or series? (Enter 'movie' or 'series')");
                    if (ui.getInput("Is it a movie or series? (Enter 'movie' or 'series'):").equalsIgnoreCase("movie")) {
                        Movie newMovie = new Movie(title, year, genre, rating);
                        io.addMovie(newMovie);
                    } else {
                        ui.displayMsg("Series Start Year:");
                        int startYear = Integer.parseInt(ui.getInput(""));

                        ui.displayMsg("Series End Year:");
                        int endYear = Integer.parseInt(ui.getInput(""));

                        ui.displayMsg("Number of Seasons:");
                        int seasons = Integer.parseInt(ui.getInput(""));

                        Series newSeries = new Series(title, startYear, endYear, genre.split(", "), rating, seasons);
                        io.addSeries(newSeries);
                    }
                    ui.displayMsg("Media added successfully!");
                } else {
                    ui.displayMsg("Invalid details entered.");
                }
                adminPanel();
                break;

            case "2":
                ui.displayMsg("Enter the title of the movie/series to remove:");
                String mediaTitle = ui.getInput("");

                if (mediaTitle != null && !mediaTitle.isEmpty()) {
                    io.removeSeriesByTitle(mediaTitle);
                    io.removeMovieByTitle(mediaTitle);

                    ui.displayMsg("Media removed successfully!");
                } else {
                    ui.displayMsg("Invalid title entered.");
                }
                adminPanel();
                break;

            case "3":
                if (login.getLoggedInUser() != null) {
                    loggedInUser = login.getLoggedInUser();
                    if (loggedInUser.getIsAdmin()) {
                        displayAdminOptions();
                    } else {
                        displayUserOptions();
                    }
                }
                break;
            default:
                ui.displayMsg("None of the options was selected");
        }
    }

    /**
     * Following method displays favorite movies
     */
    public void displayFavorites() {
        if (loggedInUser != null) {
            List<String> favorites = loggedInUser.getFavorites();
            ui.displayMsg("----------------- My Favorites -----------------");
            for (String favorite : favorites) {
                System.out.println(favorite);
            }
            ui.displayMsg("_________________________________________________");
            ui.displayMsg("--------Redirecting you back to main menu--------");
            if (login.getLoggedInUser() != null) {
                loggedInUser = login.getLoggedInUser();
                if (loggedInUser.getIsAdmin()) {
                    displayAdminOptions();
                } else {
                    displayUserOptions();
                }

            }
        } else {
            ui.displayMsg("User not logged in.");
        }
    }


}