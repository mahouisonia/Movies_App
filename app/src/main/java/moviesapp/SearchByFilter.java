package moviesapp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchByFilter {

    private static final Map<String, Integer> genreMap = new HashMap<>();
    private static final String API_KEY = "f1777d5efc77db9afe972a45e8263775"; // Make sure to use your actual API key

    static {
        genreMap.put("action", 28);
        genreMap.put("aventure", 12);
        genreMap.put("animation", 16);
        genreMap.put("comédie", 35);
        genreMap.put("crime", 80);
        genreMap.put("documentaire", 99);
        genreMap.put("drame", 18);
        genreMap.put("famille", 10751);
        genreMap.put("fantaisie", 14);
        genreMap.put("histoire", 36);
        genreMap.put("horreur", 27);
        genreMap.put("musique", 10402);
        genreMap.put("mystère", 9648);
        genreMap.put("romance", 10749);
        genreMap.put("science-fiction", 878);
        genreMap.put("téléfilm", 10770);
        genreMap.put("thriller", 53);
        genreMap.put("guerre", 10752);
        genreMap.put("western", 37);
    }

    private static String findActorIdByName(String actorName) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.themoviedb.org/3/search/person?api_key=" + API_KEY +
                "&language=en-US&page=1&include_adult=false&query=" + actorName.replace(" ", "%20");

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
                JSONArray results = jsonObject.getJSONArray("results");
                if (results.length() > 0) {
                    // Use getInt to get the actor's ID as an integer
                    int actorId = results.getJSONObject(0).getInt("id");
                    // Return the ID as a string
                    return String.valueOf(actorId);
                }
            }
        }
        return null; // Return null if the actor is not found or in case of failure
    }



    public static List<String> searchMoviesByGenres(String[] genres, Integer releaseYear, Double minimumRating, String actorName) {
        List<String> movieTitles = new ArrayList<>();
        try {
            String actorId = actorName != null ? findActorIdByName(actorName) : null;
            if (actorName != null && actorId == null) {
                System.out.println("Actor not found");
                return movieTitles;
            }

            OkHttpClient client = new OkHttpClient();
            String genreIds = Arrays.stream(genres)
                    .map(String::toLowerCase)
                    .map(genre -> genreMap.getOrDefault(genre, -1).toString())
                    .filter(id -> !id.equals("-1"))
                    .collect(Collectors.joining(","));

            if (genreIds.isEmpty()) {
                System.out.println("Genres not found");
                return movieTitles;
            }

            String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY +
                    "&include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&with_genres=" + genreIds;

            if (releaseYear != null) {
                url += "&primary_release_year=" + releaseYear;
            }

            if (minimumRating != null) {
                url += "&vote_average.gte=" + minimumRating;
            }

            if (actorId != null) {
                url += "&with_cast=" + actorId;
            }

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("accept", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONArray results = jsonObject.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject movie = results.getJSONObject(i);
                        movieTitles.add(movie.getString("title"));
                    }
                } else {
                    System.out.println("Request failed");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while searching for movies: " + e.getMessage());
        }
        return movieTitles;
    }


    // Overloaded methods for backward compatibility and varied use cases
    public static List<String> searchMoviesByGenres(String[] genres) {
        return searchMoviesByGenres(genres, null, null, null); // Updated to match the new method signature
    }

    public static List<String> searchMoviesByGenres(String[] genres, Integer releaseYear) {
        return searchMoviesByGenres(genres, releaseYear, null, null); // Updated to match the new method signature
    }

    public static List<String> searchMoviesByGenres(String[] genres, Integer releaseYear, Double minimumRating) {
        return searchMoviesByGenres(genres, releaseYear, minimumRating, null); // This matches the full method signature
    }



    // Example usage in the main method for testing
    public static void main(String[] args) {
        // Corrected method calls without actor name
        List<String> moviesByGenres = searchMoviesByGenres(new String[]{"Action", "science-fiction"});
        System.out.println("Movies by Genres: " + moviesByGenres);

        List<String> moviesByGenresAndYear = searchMoviesByGenres(new String[]{"Action", "science-fiction"}, 2012);
        System.out.println("Movies by Genres and Year: " + moviesByGenresAndYear);

        // If you want to include the actor filter, specify the actor's name in the call, like so:
        // Assuming there's an actor you want to search for:
        String actorName = "Brad Pitt"; // Example actor name
        List<String> moviesByGenresYearAndRating = searchMoviesByGenres(new String[]{"Action", "science-fiction"}, 2012, 8.0, actorName);
        System.out.println("Movies by Genres, Year, Minimum Rating, and Actor: " + moviesByGenresYearAndRating);
    }

}
