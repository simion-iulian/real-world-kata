package upsd.repositories;

import com.eclipsesource.json.JsonObject;
import upsd.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    private List<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }

    public void add(User userToAdd) {
        users.add(userToAdd);
    }

    public Optional<User> getBy(int id) {
        return users.stream()
                .filter(u -> u.id() == id)
                .findFirst();
    }

    public String getAll() {
        return multipleUsersToJsonObject(users).toString();
    }

    private JsonObject multipleUsersToJsonObject(List<User> users){
        JsonObject arrayOfObjects = new JsonObject();
        for (int i = 0; i < users.size(); i++) {
            arrayOfObjects.add("user"+i, jsonObjectFor(users.get(i)));
        }
        return arrayOfObjects;
    }

    private JsonObject jsonObjectFor(User user) {
        return new JsonObject()
            .add("id", user.id())
            .add("name", user.name());
    }
}
