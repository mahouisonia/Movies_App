package moviesapp;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppCLI {
    private static final List<JSONObject> favorites = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWelcome to the Movies App");
            System.out.println("Please select a command:");
            System.out.println("1: Search for a movie by name");
            System.out.println("2: Search for movies by genres");
            System.out.println("3: View favorites");
            System.out.println("4: Quit");
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine();
            int command;
            try {
                command = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (command) {
                case 1:
                    System.out.print("Enter the movie name: ");
                    String movieName = scanner.nextLine();
                    JSONObject movie = SearchMovie.makeRequest(movieName);
                    if (movie != null) {
                        displayMovieDetails(movie);
                        if (!isFavorite(movie)) {
                            System.out.print("Would you like to add this movie to your favorites? (yes/no): ");
                            String addToFavorites = scanner.nextLine().trim();
                            while (!addToFavorites.equalsIgnoreCase("yes") && !addToFavorites.equalsIgnoreCase("no")) {
                                System.out.println("Invalid input. Please type 'yes' or 'no'.");
                                System.out.print("Would you like to add this movie to your favorites? (yes/no): ");
                                addToFavorites = scanner.nextLine().trim();
                            }

                            if ("yes".equalsIgnoreCase(addToFavorites)) {
                                favorites.add(movie);
                                System.out.println("Movie added to favorites.");
                            }
                        } else {
                            System.out.println("This movie is already in your favorites.");
                        }

                    } else {
                        System.out.println("No results found for '" + movieName + "'.");
                    }
                    break;
                case 2:
                    System.out.println("Enter genres separated by commas (e.g., Action,Comedy): ");
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
                case 3:
                    if (favorites.isEmpty()) {
                        System.out.println("Your favorites list is empty.");
                    } else {
                        System.out.println("Your favorites:");
                        favorites.forEach(AppCLI::displayMovieDetails);
                    }
                    break;
                case 4:
                    System.out.println("Quitting the application. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid command. Please select 1, 2, 3, or 4.");
            }
        }
    }

    private static void displayMovieDetails(JSONObject movie) {
        System.out.println("-------------------------------------------------");
        System.out.println("Title: " + movie.optString("title", "N/A"));
        System.out.println("Overview: " + movie.optString("overview", "N/A"));
        System.out.println("Release Date: " + movie.optString("release_date", "N/A"));
        System.out.println("Vote Average: " + movie.optDouble("vote_average", 0));
        System.out.println("Popularity: " + movie.optDouble("popularity", 0));
        System.out.println("-------------------------------------------------\n");
    }

    private static boolean isFavorite(JSONObject movie) {
        for (JSONObject favorite : favorites) {
            if (favorite.optString("title").equals(movie.optString("title"))) {
                return true; // Movie is already in favorites
            }
        }
        return false; // Movie is not in favorites
    }
}
