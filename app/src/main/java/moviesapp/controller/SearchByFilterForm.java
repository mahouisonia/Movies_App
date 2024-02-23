package moviesapp.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import moviesapp.SearchByFilter;

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

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        gridPane.add(resultArea, 0, 5, 2, 1);

        searchButton.setOnAction(e -> {
            String genreInput = genreField.getText();
            String[] genres = genreInput.split(",");
            Integer releaseYear = parseInputt(yearField.getText());
            Double minimumRating = parseInput(ratingField.getText());

            String result = SearchByFilter.searchMoviesByGenres(genres, releaseYear, minimumRating).toString();
            resultArea.setText(result);
        });

        return new Scene(gridPane, 400, 300);
    }

    private Integer parseInputt(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseInput(String input) {
        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
