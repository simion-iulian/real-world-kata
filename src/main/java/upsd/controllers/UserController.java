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
    private static final String JSON = "application/json";

    private static final int OK = 200;
    private static final int CREATED = 201;
    private static final int NOT_FOUND = 404;

    private final UserRepository userRepository;
    private final UserJsonHelper userJsonHelper = new UserJsonHelper();

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getAll(Request req, Response res) {
        String name = req.params(":name");

        List<User> users;

        if(name == null)
            users = userRepository.getAll();
        else
            users = userRepository.getAllByName(name);

        res.type(JSON);
        res.status(OK);
        return userJsonHelper.arrayFrom(users).toString();
    }

    public String addUser(Request req, Response res) {
        User user = userFromJson(req);
        userRepository.add(user);

        res.type(JSON);
        res.status(CREATED);

        JsonObject responseJson = new JsonObject().add("uri", "/users/" + user.id());
        return responseJson.toString();
    }

    private User userFromJson(Request req){
        JsonObject json = JsonObject.readFrom(req.body());

        String id = json.get("id").asString();
        String name = json.get("name").asString();

        return new User(id,name);
    }

    public String deleteUserById(Request req, Response res) {
        User user = userFromJson(req);

        userRepository.delete(user);

        res.type(JSON);
        res.status(OK);
        JsonObject responseJson = new JsonObject().add("uri", "/users/" + user.id());
        return responseJson.toString();
    }

    public String getByName(Request req, Response res) {
        String name = req.params(":name");

        Optional<User> user = userByIdOrName(name);

        return user
            .map(u -> userJson(res, u))
            .orElseGet(() -> emptyWith404(res));
    }

    public String getById(Request req, Response res) {
        String id = req.params(":id");

        Optional<User> user = userRepository.getById(id);

        return user
            .map(u -> userJson(res, u))
            .orElseGet(() -> emptyWith404(res));
    }

    private Optional<User> userByIdOrName(String params) {
        return (isNumeric(params))
             ? userRepository.getById(params)
             : userRepository.getByName(params);
    }

    private String emptyWith404(Response res) {
        res.status(NOT_FOUND);
        return "";
    }

    private String userJson(Response res, User userFound) {
        res.type(JSON);
        return userJsonHelper.toJsonObject(userFound).toString();
    }

    private  boolean isNumeric(String str)
    {
        try {
            Integer.parseInt(str);
        }
        catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}