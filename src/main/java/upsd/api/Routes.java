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
        get("/users/name/:name", (req, res) -> userController.getByName(req, res));
        get("/users/:id", (req, res) -> userController.getById(req, res));
        get("/users", (req,res) -> userController.getAll(req, res));
        post("/users", (req, res) -> userController.addUser(req,res));
        delete("/users", (req, res) -> userController.deleteUserById(req,res));
    }
}
