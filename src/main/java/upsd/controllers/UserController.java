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

        res.type(APPLICATION_JSON);
        res.status(200);
        return userJsonHelper.arrayFrom(users).toString();
    }

    public String addUser(Request req, Response res) {
        JsonObject json = JsonObject.readFrom(req.body());

        String id = json.get("id").asString();
        String name = json.get("name").asString();

        userRepository.add(new User(id, name));

        res.type(APPLICATION_JSON);
        res.status(201);

        JsonObject responseJson = new JsonObject().add("uri", "/users/" + id);
        return responseJson.toString();
    }

    public String deleteUserById(Request req, Response res) {
        JsonObject userJson = JsonObject.readFrom(req.body());

        String userId = userJson.get("id").asString();
        String userName = userJson.get("name").asString();

        userRepository.delete(new User(userId, userName));

        res.type(APPLICATION_JSON);
        res.status(200);
        return new JsonObject().add("uri","/users/"+userId).toString();
    }

    public String getBy(Request req, Response res) {
        String params = req.params(":params");

        Optional<User>
            user = (isNumeric(params))
                 ? userRepository.getById(params)
                 : userRepository.getByName(params);

        return user
            .map(u -> userJson(res, u))
            .orElseGet(() -> emptyWith404(res));
    }

    private String emptyWith404(Response res) {
        res.status(404);
        return "";
    }

    private String userJson(Response res, User userFound) {
        res.type(APPLICATION_JSON);
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