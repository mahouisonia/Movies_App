package moviesapp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
public class Try {
    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void main(String[] args) {
        Try tryObj = new Try();
        String url = "https://api.themoviedb.org/3/movie/560?api_key=2b96a53cfa6b5e5b42fa703463e5a72c";  // Replace with your actual URL

        try {
            String result = tryObj.run(url);
            System.out.println("Response: " + result);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}