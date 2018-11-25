package upsd.repositories;

import org.junit.Test;
import upsd.domain.User;
import upsd.helpers.json.JsonHelper;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserRepositoryShould {

    @Test
    public void retrieve_added_user() {
        UserRepository userRepository = new UserRepository();
        User user = new User(1, "new user");
        userRepository.add(user);


        User userFound = userRepository.getBy(1).get();


        assertThat(userFound, is(user));
    }


    @Test
    public void retrieve_all_users() {
        UserRepository userRepository = new UserRepository();

        User[] users = {
            new User(1, "Sam"),
            new User(0, "Simion"),
            new User(2, "Solange"),
            new User(3, "Scott"),
            new User(4, "Sandro")
        };

        for(User user: users)
            userRepository.add(user);

        String allUsers = userRepository.getAll();

        assertThat(allUsers, is(JsonHelper.multipleUsersToJsonObject(users).toString()));
    }
}