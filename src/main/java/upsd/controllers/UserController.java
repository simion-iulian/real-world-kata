package upsd.controllers;

import com.eclipsesource.json.JsonObject;
import upsd.json.UserJsonHelper;
import spark.Request;
import spark.Response;
import upsd.domain.User;
import upsd.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserController {
    private static final String APPLICATION_JSON = "application/json";
    private final UserRepository userRepository;
    private final UserJsonHelper userJsonHelper = new UserJsonHelper();

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getAll(Request req, Response res) {
        List<User> users = userRepository.getAll();

        res.status(200);
        res.type(APPLICATION_JSON);
        return userJsonHelper.arrayFrom(users).toString();
    }

    public String addUser(Request req, Response res) {
        JsonObject userJson = JsonObject.readFrom(req.body());
        final int userId = userJson.get("id").asInt();
        final String userName = userJson.get("name").asString();

        userRepository.add(new User(userId, userName));

        res.status(201);
        res.type(APPLICATION_JSON);
        return new JsonObject().add("uri", "/users/"+userId).toString();
    }

    public String deleteUserById(Request req, Response res) {
        JsonObject userJson = JsonObject.readFrom(req.body());

        int userId = userJson.get("id").asInt();
        String userName = userJson.get("name").asString();

        User user = new User(userId, userName);
        userRepository.delete(user);

        res.type(APPLICATION_JSON);
        res.status(200);
        return new JsonObject().add("uri","/users/"+userId).toString();
    }

    public String getByName(Request req, Response res) {
        String name = req.params(":name");
        Optional<User> user = userRepository.getByName(name);
        return user.isPresent()
            ? userJson(res, user.get())
            : emptyWith404(res);
    }

    public String getById(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        Optional<User> user = userRepository.getById(id);
        return user.isPresent()
            ? userJson(res, user.get())
            : emptyWith404(res);
    }

    private String emptyWith404(Response res) {
        res.status(404);
        return "";
    }

    private String userJson(Response res, User userFound) {
        res.type(APPLICATION_JSON);
        return userJsonHelper.toJsonObject(userFound).toString();
    }

}