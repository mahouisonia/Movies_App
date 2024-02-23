package moviesapp.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import moviesapp.App;
import moviesapp.UsersManager;

public class ViewFavoritesForm {
    private Stage primaryStage;
    private UsersManager usersManager;
    private App app;

    public ViewFavoritesForm(Stage primaryStage, UsersManager usersManager,App app) {
        this.primaryStage = primaryStage;
        this.usersManager = usersManager;
        this.app = app;
    }

    public Scene getScene() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        ListView<String> favoritesListView = new ListView<>();
        // Populate the ListView with favorites from the usersManager
        favoritesListView.getItems().addAll(String.valueOf(usersManager.getCurrentUser().getFavorites()));

        Button backToMainMenuButton = new Button("Back to Main Menu");
        backToMainMenuButton.setOnAction(e -> {
            // Show the main application interface
            app.showMainApplicationInterface();
        });

        layout.getChildren().addAll(favoritesListView, backToMainMenuButton);
        return new Scene(layout, 400, 400);
    }
}
