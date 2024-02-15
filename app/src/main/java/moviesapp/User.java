package moviesapp;

import org.json.JSONArray;
import org.json.JSONObject;

public class User {
    private String username;
    private JSONArray favorites;

    public User(String username) {
        this.username = username;
        this.favorites = new JSONArray();
    }

    public User(JSONObject userJson) {
        this.username = userJson.getString("username");
        this.favorites = userJson.getJSONArray("favorites");
    }

    public String getUsername() {
        return username;
    }

    public JSONArray getFavorites() {
        return favorites;
    }

    public JSONObject toJson() {
        JSONObject userJson = new JSONObject();
        userJson.put("username", username);
        userJson.put("favorites", favorites);
        return userJson;
    }

    public void addFavorite(JSONObject movie) {
        favorites.put(movie);
    }

    public boolean isFavorite(JSONObject movie) {
        for (int i = 0; i < favorites.length(); i++) {
            JSONObject favorite = favorites.getJSONObject(i);
            if (favorite.getString("title").equals(movie.getString("title"))) {
                return true;
            }
        }
        return false;
    }
}
