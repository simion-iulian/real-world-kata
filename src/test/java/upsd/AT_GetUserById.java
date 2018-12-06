package upsd;

import com.eclipsesource.json.JsonObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import upsd.api.Server;
import upsd.domain.User;
import upsd.helpers.json.JsonHelper;
import upsd.repositories.UserRepository;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static upsd.helpers.json.JsonHelper.arrayOfUsers;

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

        JsonObject arrayOfUsers = arrayOfUsers(users);

        System.out.println(arrayOfUsers.toString());

        get("/users").
            then().
            statusCode(200).
            body(is(arrayOfUsers.toString()));
    }

    @Test
    public void
    return_201_and_add_a_user() {
        String jsonBody = JsonHelper.jsonObjectFor(new User(17, "Alex")).toString();

        given()
            .contentType("application/json")
            .body(jsonBody)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .contentType("application/json")
            .body("uri", equalTo("/users/17"));
    }
}
