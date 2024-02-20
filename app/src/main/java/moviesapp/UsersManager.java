package moviesapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class UsersManager {
    private static final String USER_FILE = "users.json";
    private JSONArray users;
    private User currentUser;

    public UsersManager() {
        loadUsersData();
    }

    private void loadUsersData() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(USER_FILE)));
            JSONObject data = new JSONObject(content);
            users = data.getJSONArray("users");
        } catch (Exception e) {
            System.out.println("Failed to load users data. Starting with an empty set.");
            users = new JSONArray();
        }
    }

    public void saveUsersData() {
        try {
            JSONObject data = new JSONObject();
            data.put("users", users);
            Files.write(Paths.get(USER_FILE), data.toString().getBytes());
        } catch (Exception e) {
            System.out.println("Failed to save users data.");
        }
    }

    public boolean registerUser(String username, String password) {
        for (int i = 0; i < users.length(); i++) {
            JSONObject userJson = users.getJSONObject(i);
            if (userJson.getString("username").equals(username)) {
                System.out.println("Username already exists.");
                return false;
            }
        }
        JSONObject newUser = new JSONObject();
        newUser.put("username", username);
        newUser.put("password", password);
        newUser.put("favorites", new JSONArray());
        users.put(newUser);
        saveUsersData();
        return true;
    }

    public boolean resetPassword(String username, String password) {
        for (int i = 0; i < users.length(); i++) {
            JSONObject userJson = users.getJSONObject(i);
            if (userJson.getString("username").equals(username)) {
                userJson.put("password",password);
            }
        }
        return true;
    }

    public boolean loginUser(String username, String password) {
        for (int i = 0; i < users.length(); i++) {
            JSONObject userJson = users.getJSONObject(i);
            if (userJson.getString("username").equals(username) && userJson.getString("password").equals(password)) {
                currentUser = new User(userJson);
                return true;
            }
        }
        System.out.println("Login failed. Username or password is incorrect.");
        return false;
    }

    public boolean isUserExists(String username) {
        for (int i = 0; i < users.length(); i++) {
            JSONObject userJson = users.getJSONObject(i);
            if (userJson.getString("username").equals(username)) {
                return true;
            }
        }
        return false;
    }

    public String getpassword (String username) {
        for (int i = 0; i < users.length(); i++) {
            JSONObject userJson = users.getJSONObject(i);
            if (userJson.getString("username").equals(username)) {
                return userJson.getString("password");
            }
        }
        return null;
    }


    public User getCurrentUser() {
        return currentUser;
    }

    public void logoutUser() {
        currentUser = null;
    }

    // Optionally, implement methods to update user data like changing password, adding/removing favorites, etc.
}
