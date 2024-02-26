package moviesapp.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import moviesapp.App;
import moviesapp.SearchMovie;
import moviesapp.UsersManager;
import org.json.JSONObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class SearchByNameForm {
    private Stage primaryStage;

    private Runnable backToMainInterface;

    private UsersManager usersManager; // Add this line at the beginning of your class

    public SearchByNameForm(Stage primaryStage, Runnable backToMainInterface, UsersManager usersManager) {
        this.primaryStage = primaryStage;
        this.backToMainInterface = backToMainInterface;
        this.usersManager = usersManager; // Initialize the usersManager
    }

    public Scene getScene() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Search for a Movie by Name");
        gridPane.add(sceneTitle, 0, 0, 2, 1);

        Label movieNameLabel = new Label("Movie Name:");
        gridPane.add(movieNameLabel, 0, 1);

        TextField movieNameTextField = new TextField();
        gridPane.add(movieNameTextField, 1, 1);

        Button searchButton = new Button("Search");
        gridPane.add(searchButton, 1, 2);

        VBox resultsBox = new VBox(10);
        resultsBox.setAlignment(Pos.CENTER_LEFT); // Ensure content aligns to the left

        searchButton.setOnAction(e -> {
            String movieName = movieNameTextField.getText();
            JSONObject movie = SearchMovie.makeRequest(movieName);
            if (movie != null) {
                displayMovieDetails(movie, resultsBox);
            } else {
                resultsBox.getChildren().clear(); // Clear previous results
                resultsBox.getChildren().add(new Text("No results found for '" + movieName + "'."));
            }
        });

        // Use a ScrollPane to allow vertical scrolling
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(resultsBox); // Add resultsBox to the scroll pane
        scrollPane.setFitToWidth(true); // Ensure the scroll pane fits the width of its content
        scrollPane.setPadding(new Insets(10)); // Optional padding




        Button backButton = new Button("Back");
        backButton.setOnAction(e -> backToMainInterface.run());

        HBox buttonBar = new HBox(backButton);
        buttonBar.setPadding(new Insets(10));
        buttonBar.setAlignment(Pos.CENTER_LEFT);

        // Existing code to set up the scene...
        BorderPane rootLayout = new BorderPane();
        rootLayout.setTop(gridPane); // Assuming gridPane is your GridPane setup
        rootLayout.setCenter(scrollPane); // Assuming scrollPane is your ScrollPane setup
        rootLayout.setBottom(buttonBar); // Add the button bar with the Back button at the bottom

        return new Scene(rootLayout, 600, 600);
    }


    private void displayMovieDetails(JSONObject movie, VBox container) {
        container.getChildren().clear(); // Clear previous results

        Text title = new Text("Title: " + movie.optString("title", "N/A"));
        Text overviewText = new Text("Overview: " + movie.optString("overview", "N/A"));
        overviewText.setWrappingWidth(400); // Set wrapping width for the overview text
        Text releaseDate = new Text("Release Date: " + movie.optString("release_date", "N/A"));
        Text voteAverage = new Text("Vote Average: " + movie.optDouble("vote_average", 0));
        Text popularity = new Text("Popularity: " + movie.optDouble("popularity", 0));

        String posterPath = movie.optString("poster_path", null);
        ImageView posterImageView = new ImageView();
        if (posterPath != null && !posterPath.isEmpty()) {
            String imageUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
            Image image = new Image(imageUrl, true); // true means the image is loaded in background
            posterImageView.setImage(image);
            posterImageView.setFitWidth(200); // Adjust the width as needed
            posterImageView.setPreserveRatio(true); // Preserve the aspect ratio
            posterImageView.setSmooth(true); // Apply a smoothing filter
        }

        VBox overviewContainer = new VBox(overviewText);
        overviewContainer.setAlignment(Pos.TOP_LEFT);

        // Check if the movie is already in favorites
        if (!usersManager.getCurrentUser().isFavorite(movie)) {
            Button addToFavoritesButton = new Button("Add to Favorites");
            addToFavoritesButton.setOnAction(e -> {
                usersManager.getCurrentUser().addFavorite(movie);
                usersManager.saveUsersData(); // Save changes
                addToFavoritesButton.setText("Added to Favorites");
                addToFavoritesButton.setDisable(true); // Disable the button after adding
            });

            container.getChildren().addAll(posterImageView, title, overviewContainer, releaseDate, voteAverage, popularity, addToFavoritesButton);
        } else {
            Text alreadyInFavoritesText = new Text("This movie is already in your favorites.");
            container.getChildren().addAll(posterImageView, title, overviewContainer, releaseDate, voteAverage, popularity, alreadyInFavoritesText);
        }

        container.setAlignment(Pos.TOP_LEFT);
    }

}
