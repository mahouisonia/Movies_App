package moviesapp.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import moviesapp.SearchByFilter;

import java.util.List;

public class SearchByFilterForm {
    private Stage primaryStage;

    public SearchByFilterForm(Stage primaryStage) {
        this.primaryStage = primaryStage;
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

        Button searchButton = new Button("Search");
        gridPane.add(searchButton, 1, 4);

        // Use a VBox inside a ScrollPane for results to enable vertical scrolling
        VBox resultBox = new VBox(5); // Spacing between elements
        ScrollPane scrollPane = new ScrollPane(resultBox);
        scrollPane.setFitToWidth(true);
        gridPane.add(scrollPane, 0, 5, 2, 1);

        searchButton.setOnAction(e -> {
            String genreInput = genreField.getText();
            String[] genres = genreInput.isEmpty() ? new String[0] : genreInput.split(",");
            Integer releaseYear = parseInputToInteger(yearField.getText());
            Double minimumRating = parseInputToDouble(ratingField.getText());

            List<String> results = SearchByFilter.searchMoviesByGenres(genres, releaseYear, minimumRating);
            resultBox.getChildren().clear(); // Clear previous results
            results.forEach(title -> resultBox.getChildren().add(new Text(title)));
        });

        return new Scene(gridPane, 400, 500); // Adjusted for potentially longer list of results
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
