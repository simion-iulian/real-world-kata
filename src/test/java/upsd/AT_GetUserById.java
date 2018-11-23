package upsd;

import org.junit.BeforeClass;
import org.junit.Test;
import upsd.api.Server;
import upsd.domain.User;
import upsd.repositories.UserRepository;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;

public class AT_GetUserById {

    private static UserRepository userRepository;

    @BeforeClass
    public static void setUp() {
        userRepository = new UserRepository();
        new Server(userRepository).startOn(8080);
    }

    @Test
    public void return_200_and_user_found_for_specified_id() {
        userRepository.add(new User(1, "sam"));


        get("/users/1").then()
                .statusCode(200)
                .contentType("application/json")
                .body("id", is(1))
                .body("name", is("sam"));
    }

    @Test
    public void return_404_and_user_not_found_for_specified_id() {
        get("/users/12").then()
          .statusCode(404);
    }
    @Test
    public void return_200_and_all_users_id_and_names() {
        userRepository.add(new User(1, "sam"));
        userRepository.add(new User(2, "simion"));
        userRepository.add(new User(3, "solange"));
        userRepository.add(new User(4, "scott"));
        userRepository.add(new User(5, "andre"));

        get("/users/all/").
            then().
            statusCode(200).
            body("", hasItems("sam","simion","solange","scott","andre"));
    }
}
