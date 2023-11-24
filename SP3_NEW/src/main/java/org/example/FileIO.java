package org.example;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author Mads, Kevin, Daniel
 * The following class reads all data from the provided data files
 */

public class FileIO implements IO {
    TextUI ui = new TextUI();
    Movie movie;
    File file;
    ArrayList<Movie> movies = new ArrayList<>();
    ArrayList<Series> series = new ArrayList<>();

    /**
     * The following methods reads from data files
     *
     * @return list of users created in the database
     * @throws FileNotFoundException in case the file is not read properly
     */

    public ArrayList<User> readUserData() {

        ArrayList<User> users = new ArrayList<>(); //Creates a new ArrayList for users

        file = new File("SP3_NEW/src/main/java/org/example/database.txt");

        try {
            Scanner scan = new Scanner(file);
            scan.nextLine();
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] userData = line.split(",");

                String username = userData[0].trim();
                String password = userData[1].trim();
                boolean isAdmin = Boolean.parseBoolean(userData[2].trim());

                User user = new User(username, password, isAdmin);
                users.add(user);//Adds user to ArrayList
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        }
        return users;
    }

    /**
     * The following method allows to save user data created
     *
     * @param newUsersList
     * @throws IOException in case of saving user data doesn't save properly
     */
    public void saveUserData(ArrayList<User> newUsersList) {
        try {
            Path filePath = Path.of("SP3_NEW/src/main/java/org/example/database.txt");

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            List<String> existingUsers = Files.readAllLines(filePath);

            for (User user : newUsersList) {
                existingUsers.add(user.toString());
            }

            Files.write(filePath, existingUsers);
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    /**
     * The following method reads data from the data file
     * splits the different parts the file is seperated into
     *
     * @return list of all movies
     * @throws IOException in case there is an error while reading from file
     */
    public ArrayList<Movie> readMovieData() {


        File movieFile = new File("SP3_NEW/src/main/java/org/example/100bedstefilm.txt");


        try (BufferedReader br = new BufferedReader(new FileReader(movieFile))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] parts = line.split(";");
                String title = parts[0].trim();
                int year = Integer.parseInt(parts[1].trim());
                String genres = parts[2].trim();
                double rating = Double.parseDouble(parts[3].replace(",", ".").trim());
                movie = new Movie(title, year, genres, rating);
                movies.add(movie);
            }

        } catch (IOException e) {
            System.out.println("Error reading/adding movies from text file" + e.getMessage());

        }
        return movies;
    }

    /**
     * The following method reads data from the data file
     * * splits the different parts the file is seperated into
     *
     * @return list of all series
     * @throws IOException in case there is an error while reading from file
     */

    public ArrayList<Series> readSeriesData() {

        File seriesFile = new File("SP3_NEW/src/main/java/org/example/100bedsteserier.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(seriesFile))) {
            String line;
            while ((line = br.readLine()) != null) {


                String[] parts = line.split(";");

                if (parts.length >= 5) {
                    String title = parts[0].trim();
                    String[] years = parts[1].split("-");
                    int startYear, endYear;
                    if (years.length == 2) {
                        startYear = parseIntSafe(years[0].trim());
                        endYear = parseIntSafe(years[1].trim());
                    } else if (years.length == 1 && !years[0].isEmpty()) {
                        startYear = parseIntSafe(years[0].trim());
                        endYear = startYear;
                    } else {
                        System.out.println("Error reading years for series: " + title);
                        System.out.println("Problematic line: " + line);
                        continue;
                    }
                    String[] genres = parts[2].split(", ");
                    double rating = Double.parseDouble(parts[3].replace(",", ".").trim());
                    String[] seasonParts = parts[4].split(",");
                    int totalSeasons = 0;

                    for (String seasonPart : seasonParts) {
                        String[] seasonInfo = seasonPart.split("-");
                        if (seasonInfo.length != 2) {
                            System.out.println("Error reading season info for series: " + title);
                            continue;
                        }
                        int startSeason = Integer.parseInt(seasonInfo[0].trim());
                        int endSeason = Integer.parseInt(seasonInfo[0].trim());
                        totalSeasons += endSeason - startSeason + 1;
                    }
                    Series serie = new Series(title, startYear, endYear, genres, rating, totalSeasons);
                    series.add(serie);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading/adding series from text file: " + e.getMessage());
        }

        return series;
    }

    /**
     * The following method makes the admin able to remove a movie by title
     *
     * @param title takes title as parameter to delete that title
     */
    public void removeMovieByTitle(String title) {
        ArrayList<Movie> movies = readMovieData();

        movies.removeIf(movie -> movie.getTitle().equalsIgnoreCase(title));

        saveMovieData(movies);
    }

    /**
     * The following method makes the admin able to remove a series by title
     *
     * @param title takes the title as parameter to delete that title
     */
    public void removeSeriesByTitle(String title) {

        ArrayList<Series> existingSeries = readSeriesData();

        existingSeries.removeIf(serie -> serie.getTitle().equalsIgnoreCase(title));

        saveSeriesData(existingSeries);
    }

    /**
     * The following method makes the admin able to add a movie
     *
     * @param movie takes movie as parameter to add as a movie
     */
    public void addMovie(Movie movie) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("SP3_NEW/src/main/java/org/example/100bedstefilm.txt", true))) {
            String movieData = String.format("%s; %d; %s; %.1f;", movie.getTitle(), movie.getYear(), String.join(", ", movie.getGenre()), movie.getRating());//Creates a string movieData and uses "String.format". This formats the title,year,genres and rating.
            writer.write(movieData);//Writes the formatted string to the file
            writer.newLine();//Writes on a new line, so that the next movie gets written on a new line
        } catch (IOException e) {
            System.out.println("Error adding movie: " + e.getMessage());
        }
    }

    /**
     * The following method makes the admin able to add a series
     *
     * @param series takes series as parameter to add as a series
     */
    public void addSeries(Series series) {
        ArrayList<Series> existingSeries = readSeriesData();
        existingSeries.add(series);
        saveSeriesData(existingSeries);
    }
    /**
     * The following method updates and saves the file that stores movies
     *
     * @param movies takes a movie as parameter to store that movie
     */
    public void saveMovieData(ArrayList<Movie> movies) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("SP3_NEW/src/main/java/org/example/100bedstefilm.txt"))) {
            for (Movie movie : movies) {
                String movieData = String.format("%s; %d; %s; %.1f;", movie.getTitle(), movie.getYear(), String.join(", ", movie.getGenre()), movie.getRating());
                writer.write(movieData);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving movie data " + e.getMessage());
        }
    }
    /**
     * The following method updates and saves the file that stores series
     *
     * @param series takes a series as parameter to store that series
     */
    public void saveSeriesData(ArrayList<Series> series) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("SP3_NEW/src/main/java/org/example/100bedsteserier.txt"))) {
            for (Series serie : series) {
                String genres = arrayToString(serie.getGenres());
                String seasons = formatSeasons(serie.getSeasonsArray());
                String seriesData = String.format("%s; %d-%s; %s; %.1f; %s;", serie.getTitle(), serie.getStartYear(), (serie.getEndYear() == 0) ? "-" : String.valueOf(serie.getEndYear()), Arrays.toString(serie.getGenres()), serie.getRating(), seasons);
                writer.write(seriesData);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving series data " + e.getMessage());
        }
    }
    /**
     * Following method converts Array to a string
     *
     * @param array takes an Array as parameter to make that Array to a String
     * @return return a String
     */
    public String arrayToString(String[] array) {
        return "" + String.join(", ", array) + "";
    }
    /**
     * Following method makes the admin able to add seasons
     *
     * @param seasons
     * @return
     */
    public String formatSeasons(int[] seasons) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < seasons.length; i++) {
            result.append(String.format("%d-%d", i + 1, seasons[i]));
            if (i < seasons.length - 1) {
                result.append(", ");
            }
        }
        return result.toString();
    }

    public int parseIntSafe(String s) {
        return s.isEmpty() ? 0 : Integer.parseInt(s);
    }
    /**
     * The following method creates a txt file to save a users favorite movies or series
     * @param user Takes a user as parameter to ensure that the movies or series added to favorites is bound to the specific user
     */
    public void createTextFile(User user) {
        try {
            String fileName = "SP3_NEW/src/main/java/org/example/favorites/" + user.getUsername() + ".txt";
            Path filePath = Path.of(fileName);

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                ui.displayMsg("User file created: " + fileName);
            } else {
                ui.displayMsg("User file already exists: " + fileName);
            }
        } catch (IOException e) {
            System.out.println("Error creating user file: " + e.getMessage());
        }
    }
}
