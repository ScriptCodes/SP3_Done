package org.example;

/**
 * @author Mads, Kevin, Daniel
 * Series class
 * with necessary attributes
 */

public class Series {
//Attributes
    private String title;
    private int year;
    private String[] genre;
    private double rating;
    private int seasons;
    private int endYear;

    //Constructor
    public Series(String title, int year, int endYear, String[] genres, double rating, int seasons) {
        this.title = title;
        this.year = year;
        this.endYear = endYear;
        this.genre = genres;
        this.rating = rating;
        this.seasons = seasons;
    }

    /**
     * Getter to get genre of the series
     * @return return the genre as a string
     */
    public String[] getGenres() {
        return genre;
    }
    /**
     * Getter to get title of movie
     * @return returns the title of the movie
     */
    //Region getters
    public String getTitle(){
        return title;
    }
    public int getEndYear(){
        return endYear;
    }
    public double getRating(){
        return rating;
    }

    public int getStartYear() {
        return year;
    }
    public int[] getSeasonsArray() {
        int[] seasonsArray = new int[seasons];
        for (int i = 0; i < seasons; i++) {
            seasonsArray[i] = i + 1;
        }
        return seasonsArray;
    }
    //Reion end
    /**
     * toString method
     * @return returns the toString of the series in a nice looking way
     */
    @Override
    public String toString() {
        String genreString = arrayToString(genre);
        return "Series:" +
                "Title: '" + title + '\'' +
                ", Year: '" + year +
                ", Genre: " + genreString +
                ", Rating: " + rating +
                ", Seasons: " + seasons;
    }
    /**
     * Helper method to convert array to string
     * @param array takes an array as parameter
     * @return returns a toString
     */
    private String arrayToString(String[] array) {
        if (array.length == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder(array[0]);
        for (int i = 1; i < array.length; i++) {
            result.append(", ").append(array[i]);
        }
        return result.toString();
    }
}