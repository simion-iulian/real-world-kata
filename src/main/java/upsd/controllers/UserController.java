package upsd.controllers;

import com.eclipsesource.json.JsonObject;
import spark.Request;
import spark.Response;
import upsd.domain.User;
import upsd.repositories.UserRepository;

import java.util.Optional;

public class UserController {
    private final UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getById(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        final Optional<User> idOptional = userRepository.getBy(id);

        if(idOptional.isPresent()){
            User userFound = idOptional.get();
            res.type("application/json");
            return jsonStringFor(userFound);
        } else {
            res.status(404);
            return "";
        }
    }

    private String jsonStringFor(User user) {
        return new JsonObject()
                .add("id", user.id())
                .add("name", user.name())
                .toString();
    }

    public String getAll(Request req, Response res) {
        throw new UnsupportedOperationException();
    }
}
