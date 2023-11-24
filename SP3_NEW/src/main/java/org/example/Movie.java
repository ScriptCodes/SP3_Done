package org.example;

/**
 * @author Mads, Kevin, Daniel
 * Movie class
 * with necessary attributes
 */
public class Movie {
    private String title;
    private int year;
    private String genre;
    private double rating;

    //constructor
    public Movie(String title,int year,String genres,double rating){
        this.title = title;
        this.year = year;
        this.genre = genres;
        this.rating = rating;
    }
    /**
     * getter to get genre of the film
     * @return returns the genre of the movie as a string
     */
    String getGenre(){
        return genre;
    }
    /**
     * Getter to get title of movie
     * @return returns the title of the movie
     */
    public String getTitle(){return title;}

    public int getYear(){
        return year;
    }
    public double getRating(){
        return rating;
    }
    /**
     * toString method
     * @return returns the toString of the movie in a nice looking way
     */
    @Override
    public String toString() {
        return "Movie: " +
                "Title:'" + title + '\'' +
                ", Year:'" + year +
                ", Genere:'" + genre + '\'' +
                ", Rating:" + rating;

    }
}
