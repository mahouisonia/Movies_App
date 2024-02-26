package moviesapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import moviesapp.controller.*;
import javafx.geometry.Pos;


public class App extends Application {
    private Stage primaryStage;
    private UsersManager usersManager = new UsersManager();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginForm();
    }

    private void showLoginForm() {
        // Pass a Runnable that calls showMainApplicationInterface() to the LoginForm
        LoginForm loginForm = new LoginForm(usersManager, primaryStage, this::showMainApplicationInterface);
        primaryStage.setScene(loginForm.getScene());
        primaryStage.setTitle("Movies App");
        primaryStage.show();
    }

    public void showMainApplicationInterface() {
        VBox layout = new VBox(20); // Set spacing between elements
        layout.setAlignment(Pos.CENTER); // Center the VBox contents

        Button searchByNameButton = new Button("Search for a movie by name");
        Button searchByFiltersButton = new Button("Search for movies by filters");
        Button viewFavoritesButton = new Button("View favorites");
        Button logoutButton = new Button("Logout");

        // Set a preferred width for the buttons
        double buttonWidth = 300; // You can adjust this value to fit your design
        searchByNameButton.setPrefWidth(buttonWidth);
        searchByFiltersButton.setPrefWidth(buttonWidth);
        viewFavoritesButton.setPrefWidth(buttonWidth);
        logoutButton.setPrefWidth(buttonWidth);

        // Optionally, center the button text if desired
        searchByNameButton.setAlignment(Pos.CENTER);
        searchByFiltersButton.setAlignment(Pos.CENTER);
        viewFavoritesButton.setAlignment(Pos.CENTER);
        logoutButton.setAlignment(Pos.CENTER);

        // Configure the buttons' actions
        searchByNameButton.setOnAction(e -> {
            SearchByNameForm searchByNameForm = new SearchByNameForm(primaryStage, this::showMainApplicationInterface, usersManager);
            primaryStage.setScene(searchByNameForm.getScene());
        });

        searchByFiltersButton.setOnAction(e -> {
            SearchByFilterForm searchByFilterForm = new SearchByFilterForm(primaryStage, usersManager, this::showMainApplicationInterface);
            primaryStage.setScene(searchByFilterForm.getScene());
        });

        viewFavoritesButton.setOnAction(e -> {
            ViewFavoritesForm favoritesForm = new ViewFavoritesForm(primaryStage, usersManager, this::showMainApplicationInterface);
            primaryStage.setScene(favoritesForm.getScene());
        });

        logoutButton.setOnAction(e -> {
            usersManager.logoutUser(); // Log out the current user
            showLoginForm(); // Show the login form again
        });

        // Add the buttons to the VBox
        layout.getChildren().addAll(searchByNameButton, searchByFiltersButton, viewFavoritesButton, logoutButton);

        // Create a new scene with the layout, and set it on the primary stage
        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
    }



    public static void main(String[] args) {
        launch(args);
    }
}
