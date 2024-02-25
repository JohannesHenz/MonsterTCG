package MTCG.dal.repository;

import MTCG.dal.repository.repoUOWs.UserUOW;
import MTCG.models.UserModel;

public class UserRepository {

    private static UserRepository instance;
    private UserUOW userUOW;

    private UserRepository() {
        this.userUOW = new UserUOW();
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public boolean userExists(String username) {
        return userUOW.userExists(username);
    }

    public void save(UserModel user) {
        userUOW.createUser(user.getUsername(), user.getPassword(), user.getAdminStatus(), user.getCoinAmount(), user.getOwnedCardAmount());
    }

    public UserModel getUser(String username, String password) {
        return userUOW.getUser(username, password);
    }

    public UserModel getUserByUsername(String username) {
        return userUOW.getUserByUsername(username);
    }

    public boolean buyPackage(UserModel user) {
        return userUOW.buyPackage(user);
    }

    public boolean updateUser(String username, String name, String bio, String image) {
        return userUOW.updateUser(username, name, bio, image);
    }

    public UserModel getUserData(String username) {
        return userUOW.getUserData(username);
    }

    public boolean decreaseCoins(String username, int amount) {
        return userUOW.decreaseCoins(username, amount);
    }
}

