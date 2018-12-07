package upsd;

import upsd.api.Server;
import upsd.domain.User;
import upsd.repositories.UserRepository;

public class App {

    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        userRepository.add(new User("1","Sam"));
        userRepository.add(new User("2","Sandro"));
        userRepository.add(new User("3","Sandro"));
        new Server(userRepository).startOn(3000);
    }
}
