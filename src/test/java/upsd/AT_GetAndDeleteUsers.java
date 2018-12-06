package upsd;

import com.eclipsesource.json.JsonObject;
import org.junit.BeforeClass;
import org.junit.Test;
import upsd.api.Server;
import upsd.json.UserJsonHelper;
import upsd.domain.User;
import upsd.repositories.UserRepository;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class AT_GetAndDeleteUsers {
    private static UserRepository userRepository;
    private final String APPLICATION_JSON = "application/json";

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
                .contentType(APPLICATION_JSON)
                .body("id", is(1))
                .body("name", is("Sam"));
    }
    @Test
    public void return_200_and_user_found_for_specified_name() {
        User sandro = new User(4, "Sandro");
        userRepository.add(sandro);

        get("/users/"+sandro.name())
            .then()
                .statusCode(200)
                .contentType(APPLICATION_JSON)
                .body("id", is(sandro.id()))
                .body("name", is(sandro.name()));
    }

    @Test
    public void return_404_and_user_not_found_for_specified_id() {
        get("/users/12").then()
          .statusCode(404);
    }

    @Test
    public void return_200_and_all_users_id_and_names() {
        User[] users = {
            new User(0, "Simion"),
            new User(1, "Sam"),
            new User(2, "Solange"),
            new User(3, "Scott"),
            new User(4, "Sandro")
        };

        for(User user: users)
            userRepository.add(user);

        JsonObject arrayOfUsers = new UserJsonHelper().arrayFrom(userRepository.getAll());


        get("/users").
            then().
            statusCode(200).
            body(equalTo(arrayOfUsers.toString()));
    }

    @Test
    public void
    return_201_and_add_a_user() {
        String userToAdd = new UserJsonHelper().toJsonObject(new User(17, "Alex")).toString();

        given()
            .contentType(APPLICATION_JSON)
            .body(userToAdd)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .contentType(APPLICATION_JSON)
            .body("uri", equalTo("/users/17"));
    }

    @Test
    public void
    return_200_and_delete_a_user() {
        String userToDelete = new UserJsonHelper().toJsonObject(new User(26, "Erik")).toString();

        given()
            .contentType(APPLICATION_JSON)
            .body(userToDelete)
        .when()
        .delete("/users")
        .then()
            .statusCode(200)
            .contentType(APPLICATION_JSON)
            .body("uri", equalTo("/users/26"));

    }
}
