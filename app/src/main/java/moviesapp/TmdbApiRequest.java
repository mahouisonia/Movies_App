package moviesapp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class TmdbApiRequest {

    public static JSONArray makeRequest() {
        OkHttpClient client = new OkHttpClient();
        JSONArray results = null;

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc")
                .get()
                .addHeader("accept", "application/json")
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
        System.out.println(makeRequest());
    }
}
