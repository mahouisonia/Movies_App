package moviesapp.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import moviesapp.UsersManager;
import org.json.JSONObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONArray;
import org.json.JSONObject;

public class ViewFavoritesForm {
    private Stage primaryStage;
    private UsersManager usersManager;
    private Runnable backToMainInterface; // Runnable for back navigation

    private VBox layout;


    public ViewFavoritesForm(Stage primaryStage, UsersManager usersManager, Runnable backToMainInterface) {
        this.primaryStage = primaryStage;
        this.usersManager = usersManager;
        this.backToMainInterface = backToMainInterface; // Initialize the runnable for back navigation
    }

    public Scene getScene() {
        layout = new VBox(10); // Initialize the class member
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        Text header = new Text("Your Favorites");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 20)); // Customize the font as needed

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Disable horizontal scrollbar

        JSONArray favorites = usersManager.getCurrentUser().getFavorites(); // Adjusted to JSONArray
        if (favorites.isEmpty()) {
            layout.getChildren().add(new Text("No favorites added yet."));
        } else {
            layout.getChildren().add(header); // Add the header to the layout
            for (int i = 0; i < favorites.length(); i++) {
                JSONObject movie = favorites.getJSONObject(i); // Directly get the JSONObject
                layout.getChildren().add(createMovieDisplay(movie));
            }
        }

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> backToMainInterface.run());

        HBox buttonBar = new HBox(backButton);
        buttonBar.setPadding(new Insets(10));
        buttonBar.setAlignment(Pos.CENTER_LEFT);

        BorderPane rootLayout = new BorderPane();
        rootLayout.setCenter(scrollPane);
        rootLayout.setBottom(buttonBar); // Add the button bar with the Back button at the bottom

        return new Scene(rootLayout, 500, 500);
    }

    private VBox createMovieDisplay(JSONObject movie) {
        VBox movieBox = new VBox(5);
        movieBox.setPadding(new Insets(10));
        movieBox.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("Title: " + movie.optString("title", "N/A"));
        Text overview = new Text("Overview: " + movie.optString("overview", "N/A"));
        overview.setWrappingWidth(450); // Ensure the overview text wraps

        String posterPath = movie.optString("poster_path", null);
        ImageView posterImageView = new ImageView();
        if (posterPath != null && !posterPath.isEmpty()) {
            String imageUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
            Image image = new Image(imageUrl, true); // true means the image is loaded in background
            posterImageView.setImage(image);
            posterImageView.setFitWidth(100); // Adjust as needed
            posterImageView.setPreserveRatio(true);
            posterImageView.setSmooth(true);
        }

        Button deleteButton = new Button("Delete from Favorites");
        deleteButton.setOnAction(e -> {
            // Assuming you use the title as a unique identifier for simplicity
            String movieTitle = movie.getString("title");

            // Remove the movie from favorites by finding it in the JSONArray
            JSONArray favorites = usersManager.getCurrentUser().getFavorites();
            for (int i = 0; i < favorites.length(); i++) {
                JSONObject favorite = favorites.getJSONObject(i);
                if (favorite.getString("title").equals(movieTitle)) {
                    favorites.remove(i); // Remove the matched movie
                    break; // Exit the loop after removing the movie
                }
            }

            usersManager.saveUsersData(); // Save the changes
            refreshFavoritesDisplay(); // Refresh the display
        });


        movieBox.getChildren().addAll(posterImageView, title, overview, deleteButton);
        return movieBox;
    }

    private void refreshFavoritesDisplay() {
        layout.getChildren().clear(); // Clear the existing content

        JSONArray favorites = usersManager.getCurrentUser().getFavorites();
        if (favorites.isEmpty()) {
            layout.getChildren().add(new Text("No favorites added yet."));
        } else {
            for (int i = 0; i < favorites.length(); i++) {
                JSONObject movie = favorites.getJSONObject(i);
                layout.getChildren().add(createMovieDisplay(movie));
            }
        }
    }



}
