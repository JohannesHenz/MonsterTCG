package MTCG.models;

public class UserStatsModel {
    private String Name;
    private int Elo;
    private int Wins;
    private int Losses;

    // Getters and Setters

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getElo() {
        return Elo;
    }

    public void setElo(int elo) {
        Elo = elo;
    }

    public int getWins() {
        return Wins;
    }

    public void setWins(int wins) {
        Wins = wins;
    }

    public int getLosses() {
        return Losses;
    }

    public void setLosses(int losses) {
        Losses = losses;
    }
}
