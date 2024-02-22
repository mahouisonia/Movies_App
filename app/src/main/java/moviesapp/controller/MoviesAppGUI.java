package moviesapp.controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import moviesapp.UsersManager;
public class MoviesAppGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        UsersManager usersManager = new UsersManager();

        // Initial scene is the login screen
        Scene scene = new Scene(new VBox(), 300, 200); // Placeholder VBox, replace with actual layout
        primaryStage.setScene(scene);
        primaryStage.setTitle("Movies App");

        // Show the login form initially
        LoginForm loginForm = new LoginForm(usersManager, primaryStage);
        primaryStage.setScene(loginForm.getScene());

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
