package moviesapp;

import org.json.JSONObject;
import java.util.List;
import java.util.Scanner;

public class AppCLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Movies App");
        System.out.println("Please select a command:");
        System.out.println("1: Search for a movie by name");
        System.out.println("2: Search for movies by genres");

        int command = Integer.parseInt(scanner.nextLine()); // Read the command

        switch (command) {
            case 1:
                // Search for a movie by name
                System.out.print("Enter the movie name: ");
                String movieName = scanner.nextLine();
                JSONObject movie = SearchMovie.makeRequest(movieName);
                if (movie != null) {
                    // Display movie details
                    System.out.println("Movie found: ");
                    System.out.println("Title: " + movie.optString("title", "N/A"));
                    System.out.println("Overview: " + movie.optString("overview", "N/A"));
                    System.out.println("Release Date: " + movie.optString("release_date", "N/A"));
                    System.out.println("Vote Average: " + movie.optDouble("vote_average", 0));
                    System.out.println("Popularity: " + movie.optDouble("popularity", 0));
                } else {
                    System.out.println("No results found for '" + movieName + "'.");
                }
                break;
            case 2:
                // Search for movies by genres
                System.out.println("Enter genres separated by commas (e.g., Action,Comédie): ");
                String genreInput = scanner.nextLine();
                String[] genres = genreInput.split(",");
                List<String> movieTitles = SearchByFilter.searchMoviesByGenres(genres);
                if (!movieTitles.isEmpty()) {
                    System.out.println("Movies found: ");
                    movieTitles.forEach(System.out::println);
                } else {
                    System.out.println("No results found for genres: " + genreInput);
                }
                break;
            default:
                System.out.println("Invalid command. Please select 1 or 2.");
                break;
        }

        scanner.close();
    }
}
