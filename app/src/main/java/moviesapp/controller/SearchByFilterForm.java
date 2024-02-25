package moviesapp.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import moviesapp.SearchByFilter;
import moviesapp.UsersManager;
import org.json.JSONObject;

import java.util.List;

public class SearchByFilterForm {
    private Stage primaryStage;
    private UsersManager usersManager; // For managing favorites
    private Runnable backToMainInterface; // For back navigation

    public SearchByFilterForm(Stage primaryStage, UsersManager usersManager, Runnable backToMainInterface) {
        this.primaryStage = primaryStage;
        this.usersManager = usersManager;
        this.backToMainInterface = backToMainInterface;
    }

    public Scene getScene() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Search for movies by filters");
        gridPane.add(sceneTitle, 0, 0, 2, 1);

        Label genreLabel = new Label("Enter genre(s) separated by commas:");
        gridPane.add(genreLabel, 0, 1);

        TextField genreField = new TextField();
        gridPane.add(genreField, 1, 1);

        Label yearLabel = new Label("Enter release year (optional):");
        gridPane.add(yearLabel, 0, 2);

        TextField yearField = new TextField();
        gridPane.add(yearField, 1, 2);

        Label ratingLabel = new Label("Enter minimum rating (optional):");
        gridPane.add(ratingLabel, 0, 3);

        TextField ratingField = new TextField();
        gridPane.add(ratingField, 1, 3);

        Label actorLabel = new Label("Enter actor's name (optional):");
        gridPane.add(actorLabel, 0, 4);

        TextField actorField = new TextField();
        gridPane.add(actorField, 1, 4);

        Label directorLabel = new Label("Enter director's name (optional):");
        gridPane.add(directorLabel, 0, 5); // Note the row index is now 5

        TextField directorField = new TextField();
        gridPane.add(directorField, 1, 5);

        Button searchButton = new Button("Search");
        gridPane.add(searchButton, 1, 6); // Adjusted for the new row

        VBox resultsBox = new VBox(10);
        resultsBox.setAlignment(Pos.TOP_LEFT);

        searchButton.setOnAction(e -> {
            // Capture input from all fields, including the new directorField
            String genreInput = genreField.getText();
            String[] genres = genreInput.isEmpty() ? new String[0] : genreInput.split(",");
            Integer releaseYear = parseInputToInteger(yearField.getText());
            Double minimumRating = parseInputToDouble(ratingField.getText());
            String actorName = actorField.getText().trim();
            String directorName = directorField.getText().trim(); // New director name

            // Adjusted method call to include directorName
            List<JSONObject> results = SearchByFilter.searchMoviesByGenres(genres, releaseYear, minimumRating, actorName, directorName);
            resultsBox.getChildren().clear(); // Clear previous results
            for (JSONObject movie : results) {
                resultsBox.getChildren().add(createMovieDisplay(movie));
            }
        });

        ScrollPane scrollPane = new ScrollPane(resultsBox);
        scrollPane.setFitToWidth(true);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> backToMainInterface.run());
        HBox bottomBar = new HBox(backButton);
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setPadding(new Insets(10));

        BorderPane rootLayout = new BorderPane();
        rootLayout.setTop(gridPane);
        rootLayout.setCenter(scrollPane);
        rootLayout.setBottom(bottomBar);

        return new Scene(rootLayout, 800, 800);
    }

    private HBox createMovieDisplay(JSONObject movie) {
        HBox movieBox = new HBox(10);
        movieBox.setPadding(new Insets(10));
        movieBox.setAlignment(Pos.CENTER_LEFT);

        ImageView posterImageView = new ImageView();
        String posterPath = movie.optString("poster_path", null);
        if (posterPath != null && !posterPath.isEmpty()) {
            Image image = new Image("https://image.tmdb.org/t/p/w500" + posterPath, true);
            posterImageView.setImage(image);
            posterImageView.setFitHeight(400); // Adjust the height as needed
            posterImageView.setPreserveRatio(true);
            posterImageView.setSmooth(true);
        }

        VBox textInfo = new VBox(5);
        textInfo.setAlignment(Pos.TOP_LEFT);

        Text title = new Text("Title: " + movie.optString("title", "N/A"));
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Text overviewLabel = new Text("Overview:");
        overviewLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Text overviewDesc = new Text(movie.optString("overview", "N/A"));
        overviewDesc.setWrappingWidth(400); // Adjust the wrapping width as needed

        Text releaseDateLabel = new Text("Release Date:");
        releaseDateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Text releaseDateDesc = new Text(movie.optString("release_date", "N/A"));

        Text voteAverageLabel = new Text("Vote Average:");
        voteAverageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Text voteAverageDesc = new Text(String.valueOf(movie.optDouble("vote_average", 0)));

        Text popularityLabel = new Text("Popularity:");
        popularityLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Text popularityDesc = new Text(String.valueOf(movie.optDouble("popularity", 0)));

        Button addToFavoritesButton = new Button("Add to Favorites");
        addToFavoritesButton.setOnAction(e -> {
            if (!usersManager.getCurrentUser().isFavorite(movie)) {
                usersManager.getCurrentUser().addFavorite(movie);
                usersManager.saveUsersData();
                addToFavoritesButton.setText("Added to Favorites");
                addToFavoritesButton.setDisable(true);
            }
        });

        // Adding labels and descriptions separately
        textInfo.getChildren().addAll(title, overviewLabel, overviewDesc, releaseDateLabel, releaseDateDesc, voteAverageLabel, voteAverageDesc, popularityLabel, popularityDesc, addToFavoritesButton);

        movieBox.getChildren().addAll(posterImageView, textInfo);
        return movieBox;
    }




    private Integer parseInputToInteger(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseInputToDouble(String input) {
        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
