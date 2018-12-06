package upsd.json;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import upsd.domain.User;

import java.util.List;

public class UserJsonHelper {
    public JsonObject toJsonObject(User user) {
        return new JsonObject()
            .add("id", user.id())
            .add("name", user.name());
    }
    public JsonObject arrayFrom(List<User> users) {
        JsonArray usersArray = new JsonArray();
        users.stream()
            .map(this::toJsonObject)
            .forEach(usersArray::add);
        return new JsonObject().add("users", usersArray);
    }
}