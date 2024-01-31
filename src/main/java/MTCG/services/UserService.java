package MTCG.services;

import MTCG.dal.repository.UserRepository;
import MTCG.models.UserCredentialsModel;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(String username, String password) {
        boolean userExists = userRepository.userExists(username);

        if (userExists) {
            throw new IllegalArgumentException("User already exists");
        }

        UserCredentialsModel newUser = new UserCredentialsModel(username, password);
        userRepository.save(newUser);
    }


}