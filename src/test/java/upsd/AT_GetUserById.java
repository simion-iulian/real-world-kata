package upsd;

import com.eclipsesource.json.JsonObject;
import org.junit.BeforeClass;
import org.junit.Test;
import upsd.api.Server;
import upsd.domain.User;
import upsd.repositories.UserRepository;

import static io.restassured.RestAssured.get;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static upsd.helpers.json.JsonHelper.multipleUsersToJsonObject;

public class AT_GetUserById {

    private static UserRepository userRepository;

    @BeforeClass
    public static void setUp() {
        userRepository = new UserRepository();
        new Server(userRepository).startOn(8080);
    }

    @Test
    public void return_200_and_user_found_for_specified_id() {
        userRepository.add(new User(1, "Sam"));


        get("/users/1").then()
                .statusCode(200)
                .contentType("application/json")
                .body("id", is(1))
                .body("name", is("Sam"));
    }

    @Test
    public void return_404_and_user_not_found_for_specified_id() {
        get("/users/12").then()
          .statusCode(404);
    }

    @Test
    public void return_200_and_all_users_id_and_names() {
        User[] users = {
            new User(1, "Sam"),
            new User(0, "Simion"),
            new User(2, "Solange"),
            new User(3, "Scott"),
            new User(4, "Sandro")
        };

        for(User user: users)
            userRepository.add(user);

        JsonObject arrayOfUsers = multipleUsersToJsonObject(users);

        get("/users").
            then().
            statusCode(200).
            body(is(arrayOfUsers.toString()));
    }
}
