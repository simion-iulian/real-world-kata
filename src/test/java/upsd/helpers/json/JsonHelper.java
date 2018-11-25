package upsd.helpers.json;

import com.eclipsesource.json.JsonObject;
import upsd.domain.User;

public class JsonHelper {
    public static JsonObject multipleUsersToJsonObject(User... users){
        JsonObject arrayOfObjects = new JsonObject();
        for (int i = 0; i < users.length; i++) {
            arrayOfObjects.add("user"+i, jsonObjectFor(users[i]));
        }
        return arrayOfObjects;
    }

    public static JsonObject jsonObjectFor(User user) {
        return new JsonObject()
            .add("id", user.id())
            .add("name", user.name());
    }
}
