package upsd.controllers;

import com.eclipsesource.json.JsonObject;
import upsd.json.UserJsonHelper;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.Response;
import upsd.domain.User;
import upsd.repositories.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UserControllerShould {

    private Request request;
    private Response response;
    private UserController userController;
    private final User USER = new User("1", "bob");
    private final User USER2 = new User("2", "bob");
    private final User USER3 = new User("3", "bob");

    private UserRepository userRepository;

    @Before
    public void setUp() {
        request = mock(Request.class);
        response = mock(Response.class);
        userRepository = mock(UserRepository.class);
        userController = new UserController(userRepository);
    }

    @Test
    public void
    return_404_response_type_when_user_not_found() {
        given(request.params(":params")).willReturn("12");
        given(userRepository.getById("12")).willReturn(Optional.empty());

        String actual = userController.getByName(request, this.response);

        verify(response).status(404);
        assertThat(actual, is(""));
    }

    private String jsonStringFor(User user) {
        return new JsonObject()
            .add("id", user.id())
            .add("name", user.name())
            .toString();
    }

    @Test
    public void
    return_200_and_all_users() {
        User[] users = {
            new User("1", "Sam"),
            new User("0", "Simion"),
            new User("2", "Solange"),
            new User("3", "Scott"),
            new User("4", "Sandro")
        };

        String jsonForAllUsers = new UserJsonHelper().arrayFrom(Arrays.asList(users)).toString();

        given(userRepository.getAll()).willReturn(Arrays.asList(users));

        String actual = userController.getAll(request, response);

        verify(response).status(200);
        assertThat(actual, is(jsonForAllUsers));
    }

    @Test
    public void return_user_for_supplied_id() {
        given(request.params(":id")).willReturn("1");
        given(userRepository.getById("1")).willReturn(Optional.of(USER));

        String actual = userController.getById(request, this.response);

        verify(response).type("application/json");

        assertThat(actual, is(jsonStringFor(USER)));
    }
    @Test
    public void return_users_for_supplied_name() {
        given(request.queryParams("name")).willReturn(USER.name());

        List<User> userByNameList = Arrays.asList(USER,USER2,USER3);

        given(userRepository.getAllByName(USER.name())).willReturn(userByNameList);

        String actual = userController.getAll(request, this.response);

        verify(response).type("application/json");
        String jsonForAllUsers = new UserJsonHelper().arrayFrom(userByNameList).toString();

        assertThat(actual, is(jsonForAllUsers));
    }

    @Test
    public void
    return_201_and_add_user() {
        User user =new User("12", "Elliott");

        given(request.body()).willReturn(jsonStringFor(user));
        given(userRepository.getById("12")).willReturn(Optional.of(user));

        userController.addUser(request, response);

        verify(response).status(201);
        verify(userRepository).add(user);

        Optional<User> actual = userRepository.getById("12");
        assertThat(actual, is(Optional.of(user)));
    }

    @Test
    public void
    return_200_and_delete_user() {
        String lukeId = "35";
        User luke = new User(lukeId, "Luke");

        given(request.body()).willReturn(jsonStringFor(luke));
        given(userRepository.getById(lukeId)).willReturn(Optional.empty());

        userController.deleteUserById(request,response);

        verify(response).status(200);
        verify(userRepository).delete(luke);

        Optional<User> actual = userRepository.getById(lukeId);
        assertThat(actual, is (Optional.empty()));
    }
}
