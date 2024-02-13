package MTCG.dal.dao;

public class UserStatsDAO {
    String Name;
    int Elo;
    int Wins;
    int Losses;

    public UserStatsDAO(String Name, int Elo, int Wins, int Losses) {
        this.Name = Name;
        this.Elo = Elo;
        this.Wins = Wins;
        this.Losses = Losses;
    }
}
