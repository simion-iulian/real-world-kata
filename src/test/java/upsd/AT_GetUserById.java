package upsd;

import org.junit.BeforeClass;
import org.junit.Test;
import upsd.api.Server;
import upsd.domain.User;
import upsd.repositories.UserRepository;

import static io.restassured.RestAssured.get;
import static org.hamcrest.CoreMatchers.is;

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
}
