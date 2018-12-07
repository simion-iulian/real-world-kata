package upsd.repositories;

import upsd.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserRepository {
    private List<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }
    public Optional<User> getByName(String name) {
        Predicate<User> byName = u -> u.name().equals(name);
        return firstUser(byName);
    }
    public Optional<User> getById(String id) {
        Predicate<User> byID = u -> u.id().equals(id);
        return firstUser(byID);
    }
    public List<User> getAll() {
        return users;
    }
    public List<User> getAllByName(String name) {
        List<User> list = new ArrayList<>();
        for (User user : users) {
            if (user.name().equals(name)) {
                list.add(user);
            }
        }
        return list;
    }
    public void add(User userToAdd) {
        users.add(userToAdd);
    }
    public void delete(User user) {
        users.remove(user);
    }

    private Optional<User> firstUser(Predicate<User> byName) {
        return users.stream()
            .filter(byName)
            .findFirst();
    }
}
