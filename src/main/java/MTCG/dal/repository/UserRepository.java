package MTCG.dal.repository;

import MTCG.dal.repository.repoUOWs.UserUOW;
import MTCG.models.UserModel;

public class UserRepository {

    public boolean userExists(String username) {
        // Implement your logic to check if a user with the given username exists in the database
        // Return true if the user exists, false otherwise
        return false;
    }
    public void save(UserModel user) {
        String sql = "INSERT INTO \"User\" (Username, Password, IsAdmin, CoinAmount, OwnedCardAmount) VALUES (?, ?, ?, ?, ?)";

        UserUOW userUOW = new UserUOW(sql, user.getUsername(), user.getPassword(), user.getAdminStatus(), user.getCoinAmount(), user.getOwnedCardAmount());
        userUOW.commit();
    }

}
