package MTCG.dal.repository;

import MTCG.dal.repository.repoUOWs.UserUOW;
import MTCG.models.UserModel;

public class UserRepository {

    public boolean userExists(String username) {
        UserUOW userUOW = new UserUOW();
        return userUOW.userExists(username);
    }

    public void save(UserModel user) {
        UserUOW userUOW = new UserUOW();
        userUOW.createUser(user.getUsername(), user.getPassword(), user.getAdminStatus(), user.getCoinAmount(), user.getOwnedCardAmount());
    }

    // rest of the code...
}

