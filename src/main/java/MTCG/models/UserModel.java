package MTCG.models;

import MTCG.dal.repository.UserRepository;
import MTCG.httpserver.server.Request;

public class UserModel {

    String username;
    String password;
    boolean isAdmin;
    int coinAmount;
    int ownedCardAmount;
    String name;
    String bio;
    String image;
    private int wins;
    private int losses;
    private int elo;

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

    private UserModel getUserFromBearerToken(Request request) {
        String authorizationHeader = request.getHeaderMap().getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String username = authorizationHeader.substring(7).split("-")[0];
            UserRepository userRepository = UserRepository.getInstance();
            return userRepository.getUserByUsername(username);
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public String getBio() {
        return this.bio;
    }

    public String getImage() {
        return this.image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getELO() {
        return elo;
    }

    public void setELO(int elo) {
        this.elo = elo;
    }
}
