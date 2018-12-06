package upsd.controllers;

import com.eclipsesource.json.JsonObject;
import spark.Request;
import spark.Response;
import upsd.domain.User;
import upsd.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserController {
    public static final String APPLICATION_JSON = "application/json";
    private final UserRepository userRepository;
    private final UserJsonHelper userJsonHelper = new UserJsonHelper();

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getById(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));

        final Optional<User> idOptional = userRepository.getBy(id);


        if(idOptional.isPresent()){
            User userFound = idOptional.get();
            res.type(APPLICATION_JSON);
            return userJsonHelper.objectFor(userFound).toString();
        } else {
            res.status(404);
            return "";
        }
    }


    public String getAll(Request req, Response res) {
        res.status(200);
        List<User> users = userRepository.getAll();
        res.type(APPLICATION_JSON);
        return userJsonHelper.arrayFrom(users).toString();
    }

    public String addUser(Request req, Response res) {
        JsonObject userJson = JsonObject.readFrom(req.body());
        final int userId = userJson.get("id").asInt();
        final String userName = userJson.get("name").asString();

        final User user = new User(userId, userName);
        System.out.println(req.body());
        res.status(201);
        res.type(APPLICATION_JSON);
        userRepository.add(user);
        return new JsonObject().add("uri", "/users/"+userId).toString();
    }

    public String deleteUser(Request req, Response res) {
        throw new UnsupportedOperationException();
    }
}
