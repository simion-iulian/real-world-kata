package upsd.helpers.json;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import upsd.domain.User;

public class JsonHelper {
    public static JsonObject arrayOfUsers(User... users){
        JsonArray usersArray = new JsonArray();
        for (int i = 0; i < users.length; i++) {
            usersArray.add(jsonObjectFor(users[i]));
        }
        return new JsonObject().add("users",usersArray);
    }

    public static JsonObject jsonObjectFor(User user) {
        return new JsonObject()
            .add("id", user.id())
            .add("name", user.name());
    }
}
