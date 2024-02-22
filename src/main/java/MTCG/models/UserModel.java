package MTCG.models;

public class UserModel {

    String username;
    String password;
    boolean isAdmin;
    int coinAmount;
    int ownedCardAmount;

    public UserModel(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.coinAmount = 20;
        this.ownedCardAmount = 0;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean getAdminStatus() {
        return this.isAdmin;
    }

    public int getCoinAmount() {
        return this.coinAmount;
    }

    public int getOwnedCardAmount() {
        return this.ownedCardAmount;
    }
}
