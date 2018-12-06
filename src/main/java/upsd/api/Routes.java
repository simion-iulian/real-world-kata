package upsd.api;

import upsd.controllers.UserController;
import upsd.repositories.UserRepository;

import static spark.Spark.*;
import static spark.Spark.get;

class Routes {

    private UserController userController;

    Routes(UserRepository userRepository) {
        userController = new UserController(userRepository);
    }

    void setup() {
        get("/users/:params", (req, res) -> userController.getBy(req, res));
        get("/users", (req,res) -> userController.getAll(req, res));
        post("/users", (req, res) -> userController.addUser(req,res));
        delete("/users", (req, res) -> userController.deleteUserById(req,res));
        internalServerError((req, res) -> {
            res.type("application/json");
            return "{\"message\":\"500 error - something is wrong\"}";
        });
    }
}
