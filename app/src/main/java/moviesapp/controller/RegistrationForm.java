package moviesapp.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import moviesapp.UsersManager;

public class RegistrationForm {
    private UsersManager usersManager;
    private Stage primaryStage;
    private Runnable onSuccess; // Runnable for post-registration success action

    // Constructor now accepts a Runnable for the onSuccess action
    public RegistrationForm(UsersManager usersManager, Stage primaryStage, Runnable onSuccess) {
        this.usersManager = usersManager;
        this.primaryStage = primaryStage;
        this.onSuccess = onSuccess;
    }

    public Scene getScene() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Registration");
        gridPane.add(sceneTitle, 0, 0, 2, 1);

        Label userName = new Label("Username:");
        gridPane.add(userName, 0, 1);

        TextField userTextField = new TextField();
        gridPane.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        gridPane.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        gridPane.add(pwBox, 1, 2);

        Button btn = new Button("Register");
        gridPane.add(btn, 1, 3);

        final Text actiontarget = new Text();
        gridPane.add(actiontarget, 1, 4);

        btn.setOnAction(e -> {
            if (usersManager.registerUser(userTextField.getText(), pwBox.getText())) {
                actiontarget.setText("Registration successful.");
                onSuccess.run(); // Navigate to the main interface upon successful registration
            } else {
                actiontarget.setText("Registration failed. Username might already exist.");
            }
        });

        Button loginScreenButton = new Button("Go to Login");
        gridPane.add(loginScreenButton, 1, 5);
        loginScreenButton.setOnAction(e -> {
            LoginForm loginForm = new LoginForm(usersManager, primaryStage, onSuccess); // Use onSuccess to maintain consistent behavior
            primaryStage.setScene(loginForm.getScene());
        });

        return new Scene(gridPane, 500, 500);
    }
}
