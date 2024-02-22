package moviesapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import moviesapp.controller.LoginForm;
import moviesapp.controller.RegistrationForm; // Make sure to import the RegistrationForm if you're going to use it

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
        VBox layout = new VBox(10);
        Button searchByNameButton = new Button("Search for a movie by name");
        Button searchByFiltersButton = new Button("Search for movies by filters");
        Button viewFavoritesButton = new Button("View favorites");
        Button logoutButton = new Button("Logout");

        // Configure the buttons' actions
        searchByNameButton.setOnAction(e -> {
            // Placeholder for showing the SearchByNameForm
        });

        searchByFiltersButton.setOnAction(e -> {
            // Placeholder for showing the SearchByFiltersForm
        });

        viewFavoritesButton.setOnAction(e -> {
            // Placeholder for showing the FavoritesForm
        });

        logoutButton.setOnAction(e -> {
            usersManager.logoutUser(); // Log out the current user
            showLoginForm(); // Show the login form again
        });

        layout.getChildren().addAll(searchByNameButton, searchByFiltersButton, viewFavoritesButton, logoutButton);
        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
