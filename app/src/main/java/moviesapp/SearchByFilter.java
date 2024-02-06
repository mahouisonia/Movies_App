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

    static {
        genreMap.put("Action", 28);
        genreMap.put("Aventure", 12);
        genreMap.put("Animation", 16);
        genreMap.put("Comédie", 35);
        genreMap.put("Crime", 80);
        genreMap.put("Documentaire", 99);
        genreMap.put("Drame", 18);
        genreMap.put("Famille", 10751);
        genreMap.put("Fantaisie", 14);
        genreMap.put("Histoire", 36);
        genreMap.put("Horreur", 27);
        genreMap.put("Musique", 10402);
        genreMap.put("Mystère", 9648);
        genreMap.put("Romance", 10749);
        genreMap.put("Science-Fiction", 878);
        genreMap.put("Téléfilm", 10770);
        genreMap.put("Thriller", 53);
        genreMap.put("Guerre", 10752);
        genreMap.put("Western", 37);
    }

    public static List<String> searchMoviesByGenres(String[] genres) {
        OkHttpClient client = new OkHttpClient();
        List<String> movieTitles = new ArrayList<>();

        String genreIds = Arrays.stream(genres)
                .map(genre -> genreMap.getOrDefault(genre, -1).toString())
                .filter(id -> !id.equals("-1"))
                .collect(Collectors.joining(","));

        if (genreIds.isEmpty()) {
            System.out.println("Genres non trouvés");
            return movieTitles;
        }

        String url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&with_genres=" + genreIds;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyYjk2YTUzY2ZhNmI1ZTViNDJmYTcwMzQ2M2U1YTcyYyIsInN1YiI6IjY1YjdiNGM2OGNmY2M3MDE3Y2U2ODJkMiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.KKauoZO7myG0aSb77vxBl2kOnAREFmBMXyEqXVfWVbY") // Replace YOUR_ACCESS_TOKEN with your actual access token
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
        List<String> movieTitles = searchMoviesByGenres(new String[]{"Action", "Science-Fiction"});
        if (!movieTitles.isEmpty()) {
            System.out.println(movieTitles);
        } else {
            System.out.println("Aucun résultat trouvé.");
        }
    }
}
