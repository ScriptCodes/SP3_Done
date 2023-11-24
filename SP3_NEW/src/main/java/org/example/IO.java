package org.example;

import java.util.ArrayList;

/**
 * @author Mads, Kevin, Daniel
 * Interface for reeading data, save data and remove data from files
 */
public interface IO {
    /**
     *The following methods reads, save and overwrites data
     *
     */

    ArrayList<User> readUserData();
    void saveUserData(ArrayList<User> newUsersList);
    ArrayList<Movie> readMovieData();
    ArrayList<Series> readSeriesData();
    void removeMovieByTitle(String title);
    void removeSeriesByTitle(String title);
    void addMovie(Movie movie);
    void addSeries(Series series);
    void saveMovieData(ArrayList<Movie> movies);
    void saveSeriesData(ArrayList<Series> series);
    String arrayToString(String[] array);
    String formatSeasons(int[] seasons);
    int parseIntSafe(String s);
    void createTextFile(User user);
}
