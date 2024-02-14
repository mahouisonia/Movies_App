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
    private static final String API_KEY = "f1777d5efc77db9afe972a45e8263775";

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

    // Original method signature, kept for compatibility
    public static List<String> searchMoviesByGenres(String[] genres) {
        return searchMoviesByGenres(genres, null); // Call the overloaded method without release year
    }

    // Overloaded method to include optional release year
    public static List<String> searchMoviesByGenres(String[] genres, Integer releaseYear) {
        OkHttpClient client = new OkHttpClient();
        List<String> movieTitles = new ArrayList<>();

        String genreIds = Arrays.stream(genres)
                .map(String::toLowerCase)
                .map(genre -> genreMap.getOrDefault(genre, -1).toString())
                .filter(id -> !id.equals("-1"))
                .collect(Collectors.joining(","));

        if (genreIds.isEmpty()) {
            System.out.println("Genres non trouvés");
            return movieTitles;
        }

        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY +
                "&include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&with_genres=" + genreIds;

        // Add the release year to the URL if provided
        if (releaseYear != null) {
            url += "&primary_release_year=" + releaseYear;
        }

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
                JSONArray results = jsonObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject movie = results.getJSONObject(i);
                    movieTitles.add(movie.getString("title"));
                }
            } else {
                System.out.println("La requête n'a pas réussi");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movieTitles;
    }

    public static void main(String[] args) {
        // Example usage without release year
        List<String> movieTitles = searchMoviesByGenres(new String[]{"Action", "science-fiction"});
        System.out.println("Movies by Genres: " + movieTitles);

        // Example usage with release year
        List<String> moviesByGenresAndYear = searchMoviesByGenres(new String[]{"Action", "science-fiction"}, 2012);
        System.out.println("Movies by Genres and Year: " + moviesByGenresAndYear);
    }
}
