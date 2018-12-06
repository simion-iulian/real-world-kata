package upsd.api;

import spark.Spark;
import upsd.controllers.UserController;
import upsd.repositories.UserRepository;

import static spark.Spark.*;
import static spark.Spark.get;
import static spark.route.HttpMethod.post;

class Routes {

    private UserController userController;

    Routes(UserRepository userRepository) {
        userController = new UserController(userRepository);
    }

    void setup() {
        get("/users/:id", (req, res) -> userController.getById(req, res));
        get("/users", (req,res) -> userController.getAll(req, res));
        post("/users", (req, res) -> userController.addUser(req,res));
    }
}
