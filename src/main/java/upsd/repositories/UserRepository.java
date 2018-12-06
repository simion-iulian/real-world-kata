package upsd.repositories;

import upsd.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class UserRepository {

    private List<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }

    public void add(User userToAdd) {
        users.add(userToAdd);
    }

    public Optional<User> getById(String id) {
        Predicate<User> byID = u -> u.id().equals(id);

        return firstUser(byID);
    }

    public List<User> getAll() {
        return users;
    }

    public void delete(User user) {
        users.remove(user);
    }

    public Optional<User> getByName(String name) {
        Predicate<User> byName = u -> u.name().equals(name);
        return firstUser(byName);
    }

    private Optional<User> firstUser(Predicate<User> byName) {
        return users.stream()
            .filter(byName)
            .findFirst();
    }
}
