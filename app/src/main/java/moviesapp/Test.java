package moviesapp;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {
    private static final List<JSONObject> favorites = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWelcome to the Movies App");
            System.out.println("Please select a command:");
            System.out.println("1: Search for a movie by name");
            System.out.println("2: Search for movies by filters");
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

                    System.out.print("Do you want to add a specific release year to your search? (yes/no): ");
                    String yearDecision = scanner.nextLine().trim();

                    Integer releaseYear = null;
                    if ("yes".equalsIgnoreCase(yearDecision)) {
                        System.out.print("Enter the release year: ");
                        String yearInput = scanner.nextLine().trim();
                        try {
                            releaseYear = Integer.parseInt(yearInput);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid year format. Proceeding without year filter.");
                        }
                    }

                    Double minimumRating = null; // Variable to hold the minimum rating
                    System.out.print("Do you want to specify a minimum rating for the movies? (yes/no): ");
                    String ratingDecision = scanner.nextLine().trim();
                    if ("yes".equalsIgnoreCase(ratingDecision)) {
                        System.out.print("Enter the minimum rating (1 to 10): ");
                        String ratingInput = scanner.nextLine().trim();
                        try {
                            minimumRating = Double.parseDouble(ratingInput);
                            if (minimumRating < 1 || minimumRating > 10) {
                                System.out.println("Rating must be between 1 and 10. Proceeding without rating filter.");
                                minimumRating = null; // Ignore invalid input
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid rating format. Proceeding without rating filter.");
                        }
                    }

                    // Assuming you modify the searchMoviesByGenres method to accept a minimum rating as an argument
                    List<String> movieTitles = SearchByFilter.searchMoviesByGenres(genres, releaseYear, minimumRating);
                    if (!movieTitles.isEmpty()) {
                        System.out.println("Movies found: ");
                        movieTitles.forEach(System.out::println);
                    } else {
                        System.out.println("No results found for genres: " + genreInput +
                                (releaseYear != null ? " in " + releaseYear : "") +
                                (minimumRating != null ? " with minimum rating " + minimumRating : ""));
                    }
                    break;

                case 3:
                    if (favorites.isEmpty()) {
                        System.out.println("Your favorites list is empty.");
                    } else {
                        System.out.println("Your favorites:");
                        for (JSONObject fav : favorites) {
                            displayMovieDetails(fav);
                        }
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
                return true;
            }
        }
        return false;
    }


}
