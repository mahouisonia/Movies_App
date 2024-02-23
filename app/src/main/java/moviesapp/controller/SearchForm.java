package moviesapp.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import moviesapp.App;
import moviesapp.SearchMovie;
import org.json.JSONObject;

public class SearchForm {
    private Stage primaryStage;
    private App app;

    public SearchForm(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene getScene() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Search for a Movie by Name");
        TextField searchField = new TextField();
        Button searchButton = new Button("Search");

        searchButton.setOnAction(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                JSONObject movie = SearchMovie.makeRequest(query);
                if (movie != null) {
                    // Display movie details here (you can create a new scene or dialog for this)
                    // For now, let's just print the movie details to the console
                    System.out.println("Movie found:");
                    System.out.println(movie.toString());
                } else {
                    System.out.println("No results found for '" + query + "'.");
                }
            } else {
                System.out.println("Please enter a movie name.");
            }
        });

        Button backToMainMenuButton = new Button("Back to Main Menu");
        backToMainMenuButton.setOnAction(e -> {
            app.showMainApplicationInterface();
        });

        layout.getChildren().addAll(titleLabel, searchField, searchButton, backToMainMenuButton);
        return new Scene(layout, 400, 400);
    }
}
