package upsd.controllers;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import upsd.domain.User;

import java.util.List;

public class UserJsonHelper {

    public JsonObject objectFor(User user) {
        return new JsonObject()
            .add("id", user.id())
            .add("name", user.name());
    }

    public JsonObject arrayFrom(List<User> users) {
        JsonArray usersArray = new JsonArray();
        for (User user : users) {
            usersArray.add(objectFor(user));
        }
        return new JsonObject().add("users", usersArray);
    }
}