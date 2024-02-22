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

public class LoginForm {
    private UsersManager usersManager;
    private Stage primaryStage;

    public LoginForm(UsersManager usersManager, Stage primaryStage) {
        this.usersManager = usersManager;
        this.primaryStage = primaryStage;
    }

    public Scene getScene() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Login");
        gridPane.add(sceneTitle, 0, 0, 2, 1);

        Label userName = new Label("Username:");
        gridPane.add(userName, 0, 1);

        TextField userTextField = new TextField();
        gridPane.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        gridPane.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        gridPane.add(pwBox, 1, 2);

        Button btn = new Button("Login");
        gridPane.add(btn, 1, 3);

        final Text actiontarget = new Text();
        gridPane.add(actiontarget, 1, 4);

        btn.setOnAction(e -> {
            if (usersManager.loginUser(userTextField.getText(), pwBox.getText())) {
                actiontarget.setText("Login successful.");
                // Here, transition to the main application scene after successful login
            } else {
                actiontarget.setText("Login failed. Please check your username and password.");
            }
        });

        Button registrationScreenButton = new Button("Go to Register");
        gridPane.add(registrationScreenButton, 1, 5);
        registrationScreenButton.setOnAction(e -> {
            RegistrationForm registrationForm = new RegistrationForm(usersManager, primaryStage);
            primaryStage.setScene(registrationForm.getScene());
        });

        return new Scene(gridPane, 350, 275);
    }
}
