package moviesapp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SearchMovie {

    public static JSONArray makeRequest(String movieName) {
        OkHttpClient client = new OkHttpClient();
        JSONArray results = null;

        String encodedMovieName = URLEncoder.encode(movieName, StandardCharsets.UTF_8);
        String url = "https://api.themoviedb.org/3/search/movie?query=" + encodedMovieName + "&include_adult=false&language=en-US&page=1";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                // Note: Replace 'YOUR_ACCESS_TOKEN' with your actual TMDB API v4 access token
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyYjk2YTUzY2ZhNmI1ZTViNDJmYTcwMzQ2M2U1YTcyYyIsInN1YiI6IjY1YjdiNGM2OGNmY2M3MDE3Y2U2ODJkMiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.KKauoZO7myG0aSb77vxBl2kOnAREFmBMXyEqXVfWVbY")
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
                results = jsonObject.getJSONArray("results");
            } else {
                System.out.println("Request was not successful");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public static void main(String[] args){
        // Example usage
        JSONArray searchResults = makeRequest("harry potter");
        if (searchResults != null) {
            System.out.println(searchResults.toString());
        } else {
            System.out.println("No results found.");
        }
    }
}
