package moviesapp.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import moviesapp.UsersManager;
import org.json.JSONObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONArray;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ViewSuggestedForm {
    private Stage primaryStage;
    private UsersManager usersManager;
    private String movieId;
    private Runnable backToMainInterface;

    public ViewSuggestedForm(Stage primaryStage, UsersManager usersManager, String movieId, Runnable backToMainInterface) {
        this.primaryStage = primaryStage;
        this.usersManager = usersManager;
        this.movieId = movieId;
        this.backToMainInterface = backToMainInterface;
    }

    public Scene getScene() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        // Fetch similar movies based on movieId
        JSONArray similarMovies = fetchSimilarMovies(movieId);

        // Display each similar movie
        for (int i = 0; i < similarMovies.length(); i++) {
            JSONObject movie = similarMovies.getJSONObject(i);
            layout.getChildren().add(createMovieDisplay(movie));
        }


        Button backButton = new Button("Back");
        backButton.setOnAction(e -> backToMainInterface.run());

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        BorderPane rootLayout = new BorderPane();
        rootLayout.setCenter(scrollPane);
        rootLayout.setBottom(new HBox(backButton));

        return new Scene(rootLayout, 600, 600);
    }

    private JSONArray fetchSimilarMovies(String movieId) {
        OkHttpClient client = new OkHttpClient();
        String apiKey = "f1777d5efc77db9afe972a45e8263775";
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/recommendations?api_key=" + apiKey + "&language=en-US&page=1";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
                return jsonObject.getJSONArray("results");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray(); // Return an empty array if there's an issue with the request
    }

    private VBox createMovieDisplay(JSONObject movie) {
        VBox movieBox = new VBox(10);
        movieBox.setPadding(new Insets(10));
        movieBox.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("Title: " + movie.optString("title", "N/A"));
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Text overview = new Text("Overview: " + movie.optString("overview", "N/A"));
        overview.setWrappingWidth(450); // Adjust the wrapping width as needed
        overview.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        String posterPath = movie.optString("poster_path", null);
        ImageView posterImageView = new ImageView();
        if (posterPath != null && !posterPath.isEmpty()) {
            String imageUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
            Image image = new Image(imageUrl, true); // Load image in the background
            posterImageView.setImage(image);
            posterImageView.setFitHeight(200); // Adjust the height as needed
            posterImageView.setPreserveRatio(true);
            posterImageView.setSmooth(true);
        }

        Button addToFavoritesButton = new Button("Add to Favorites");
        addToFavoritesButton.setOnAction(e -> {
            usersManager.getCurrentUser().addFavorite(movie);
            usersManager.saveUsersData(); // Save changes
            addToFavoritesButton.setText("Added to Favorites");
            addToFavoritesButton.setDisable(true); // Disable the button after adding
        });

        movieBox.getChildren().addAll(posterImageView, title, overview, addToFavoritesButton);
        return movieBox;
    }

}
