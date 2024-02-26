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

    private static String findDirectorIdByName(String directorName) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.themoviedb.org/3/search/person?api_key=" + API_KEY +
                "&language=en-US&page=1&include_adult=false&query=" + directorName.replace(" ", "%20");

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
                if (!results.isEmpty()) {
                    // Assuming the first result is the correct director
                    int directorId = results.getJSONObject(0).getInt("id");
                    return String.valueOf(directorId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if the director is not found or in case of an error
    }




    public static List<JSONObject> searchMoviesByGenres(String[] genres, Integer releaseYear, Double minimumRating, String actorName, String directorName) {
        List<JSONObject> movies = new ArrayList<>();
        try {
            String actorId = actorName != null ? findActorIdByName(actorName) : null;
            String directorId = directorName != null ? findDirectorIdByName(directorName) : null;
//            if (actorName != null && actorId == null) {
//                System.out.println("Actor not found");
//                return movies;
//            }

            OkHttpClient client = new OkHttpClient();
            String genreIds = "";
            if (genres != null && genres.length > 0) {
                genreIds = Arrays.stream(genres)
                        .map(String::toLowerCase)
                        .map(genre -> genreMap.getOrDefault(genre, -1).toString())
                        .filter(id -> !id.equals("-1"))
                        .collect(Collectors.joining(","));
            }

            String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY +
                    "&include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc";

            if (!genreIds.isEmpty()) {
                url += "&with_genres=" + genreIds;
            }

            if (releaseYear != null) {
                url += "&primary_release_year=" + releaseYear;
            }

            if (minimumRating != null) {
                url += "&vote_average.gte=" + minimumRating;
            }

            if (actorId != null) {
                url += "&with_cast=" + actorId;
            }

            // Build the URL with additional director criteria
            if (directorId != null) {
                url += "&with_crew=" + directorId;
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
                        movies.add(results.getJSONObject(i));
                    }
                } else {
                    System.out.println("Request failed");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while searching for movies: " + e.getMessage());
        }
        return movies;
    }



    // Overloaded methods for backward compatibility and varied use cases
    public static List<JSONObject> searchMoviesByGenres(String[] genres) {
        // Adding null for both actorName and directorName
        return searchMoviesByGenres(genres, null, null, null, null);
    }

    public static List<JSONObject> searchMoviesByGenres(String[] genres, Integer releaseYear) {
        // Adding null for both actorName and directorName
        return searchMoviesByGenres(genres, releaseYear, null, null, null);
    }

    public static List<JSONObject> searchMoviesByGenres(String[] genres, Integer releaseYear, Double minimumRating) {
        // Adding null for both actorName and directorName
        return searchMoviesByGenres(genres, releaseYear, minimumRating, null, null);
    }






}
