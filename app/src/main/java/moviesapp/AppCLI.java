package moviesapp;

import org.json.JSONObject;

import java.util.List;
import java.util.Scanner;

public class AppCLI {
    private static UsersManager usersManager = new UsersManager();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (usersManager.getCurrentUser() == null) {
                System.out.println("\nWelcome to the Movies App");
                System.out.println("Please select a command:");
                System.out.println("1: Register");
                System.out.println("2: Login");
                System.out.println("3: Forgot password");
                System.out.println("4: Quit");
                System.out.print("Enter your choice: ");
                String authChoice = scanner.nextLine();
                handleUserAuthentication(authChoice, scanner);
            } else {
                System.out.println("\nWelcome to the Movies App");
                System.out.println("Logged in as: " + usersManager.getCurrentUser().getUsername());
                System.out.println("Please select a command:");
                System.out.println("1: Search for a movie by name");
                System.out.println("2: Search for movies by filters");
                System.out.println("3: View favorites");
                System.out.println("4: Logout");
                System.out.println("5: Quit");
                System.out.print("Enter your choice: ");
                String input = scanner.nextLine();
                handleCommand(input, scanner);
            }
        }
    }

    private static void handleUserAuthentication(String choice, Scanner scanner) {
        switch (choice) {
            case "1": // Register
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                if (usersManager.registerUser(username, password)) {
                    System.out.println("Registration successful. Please log in.");
                } else {
                    System.out.println("Registration failed. Username might already exist.");
                }
                break;
            case "2": // Login
                System.out.print("Enter username: ");
                username = scanner.nextLine();
                System.out.print("Enter password: ");
                password = scanner.nextLine();
                if (usersManager.loginUser(username, password)) {
                    System.out.println("Login successful.");
                } else {
                    System.out.println("Login failed. Please check your username and password.");
                }
                break;

            case "3": // Forgot Password
                System.out.print("Enter your username: ");
                username = scanner.nextLine();
                if (usersManager.isUserExists(username)) {
                    System.out.print("Enter a new password : ");
                    password = scanner.nextLine();
                    if(usersManager.resetPassword(username,password)){
                       System.out.println("password reseted successfully");
                    }
                } else {
                    System.out.println("Username does not exist. Please enter a valid username.");
                }
                break;

            case "4": // Quit
                System.out.println("Quitting the application. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid command. Please enter a valid number.");
                break;
        }
    }

    private static void handleCommand(String command, Scanner scanner) {
        switch (command) {
            case "1":
                searchForMovieByName(scanner);
                break;
            case "2":
                searchForMoviesByFilters(scanner);
                break;
            case "3":
                viewFavorites();
                if (usersManager.getCurrentUser().getFavorites().isEmpty()) {
                    break;
                }
                System.out.print("Would you like to delete a movie from your favorites? (yes/no): ");
                String addToFavorites = scanner.nextLine().trim();
                if ("no".equalsIgnoreCase(addToFavorites)) {
                   break;
                }
                if("yes".equalsIgnoreCase(addToFavorites)){
                    System.out.println("Choose the movie to delete from favorite: ");
                    int fav_count = usersManager.getCurrentUser().getFavorites().length();
                    for(int i=0;i<fav_count;i++){
                        JSONObject favorite = usersManager.getCurrentUser().getFavorites().getJSONObject(i);
                        String movie = favorite.getString("title");
                        System.out.println(i + ") " + movie);
                    }
                    int movieToDelete = scanner.nextInt();
                    if (movieToDelete >= 0 && movieToDelete < fav_count) {
                        usersManager.getCurrentUser().getFavorites().remove(movieToDelete);
                        System.out.println("Movie removed from favorites successfully.");
                    } else {
                        System.out.println("Invalid movie number. Please enter a valid number.");
                    }
                    usersManager.saveUsersData();
                }
                break;
            case "4": // Logout
                usersManager.logoutUser();
                System.out.println("You have been logged out.");
                break;
            case "5": // Quit
                System.out.println("Quitting the application. Goodbye!");
                scanner.close();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid command. Please select a valid option.");
                break;
        }
    }

    private static void searchForMovieByName(Scanner scanner) {
        System.out.print("Enter the movie name: ");
        String movieName = scanner.nextLine();
        JSONObject movie = SearchMovie.makeRequest(movieName); // Assume this method exists and makes an API request
        if (movie != null) {
            displayMovieDetails(movie);
            if (!usersManager.getCurrentUser().isFavorite(movie)) {
                System.out.print("Would you like to add this movie to your favorites? (yes/no): ");
                String addToFavorites = scanner.nextLine().trim();
                if ("yes".equalsIgnoreCase(addToFavorites)) {
                    usersManager.getCurrentUser().addFavorite(movie);
                    usersManager.saveUsersData(); // Remember to save changes to the file
                    System.out.println("Movie added to favorites.");
                }
            } else {
                System.out.println("This movie is already in your favorites.");
            }
        }
        else {
            System.out.println("No results found for '" + movieName + "'.");
        }
    }

    private static void searchForMoviesByFilters(Scanner scanner) {
        System.out.println("Do you want to specify a genre for the movies? (yes/no): ");
        String genreDecision = scanner.nextLine().trim();
        String[] genres = {};
        String genreInput=null;
        if("yes".equalsIgnoreCase(genreDecision)){
            System.out.println("enter genres separated by commas (e.g., Action,Comedy):");
            genreInput = scanner.nextLine().trim();
            genres = genreInput.split(",");
        }

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

        Double minimumRating = null;
        System.out.print("Do you want to specify a minimum rating for the movies? (yes/no): ");
        String ratingDecision = scanner.nextLine().trim();
        if ("yes".equalsIgnoreCase(ratingDecision)) {
            System.out.print("Enter the minimum rating (1 to 10): ");
            String ratingInput = scanner.nextLine().trim();
            try {
                minimumRating = Double.parseDouble(ratingInput);
                if (minimumRating < 1 || minimumRating > 10) {
                    System.out.println("Rating must be between 1 and 10. Proceeding without rating filter.");
                    minimumRating = null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid rating format. Proceeding without rating filter.");
            }
        }

        // New section for actor name
        String actorName = null;
        System.out.print("Do you want to search for movies with a specific actor? (yes/no): ");
        String actorDecision = scanner.nextLine().trim();
        if ("yes".equalsIgnoreCase(actorDecision)) {
            System.out.print("Enter the actor's name: ");
            actorName = scanner.nextLine().trim();
        }

        String directorName = null;
        System.out.print("Do you want to search for movies with a specific director? (yes/no): ");
        String directorDecision = scanner.nextLine().trim();
        if ("yes".equalsIgnoreCase(directorDecision)) {
            System.out.print("Enter the director's name: ");
            directorName = scanner.nextLine().trim();
        }


        List<JSONObject> results = SearchByFilter.searchMoviesByGenres(genres, releaseYear, minimumRating, actorName, directorName);
        if (!results.isEmpty()) {
            System.out.println("Movies found:");
            for (JSONObject movie : results) {
                // Extract the title from each JSONObject and print it
                String title = movie.optString("title", "N/A");
                System.out.println(title);
            }
        } else {
            System.out.println("No results found for the specified filters!");
        }
    }


    private static void viewFavorites() {
        if (usersManager.getCurrentUser().getFavorites().isEmpty()) {
            System.out.println("You have no favorite movies");
        } else {
            System.out.println("Your favorites:");
            usersManager.getCurrentUser().getFavorites()
                    .forEach(favorite -> displayMovieDetails((JSONObject) favorite));
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
}
