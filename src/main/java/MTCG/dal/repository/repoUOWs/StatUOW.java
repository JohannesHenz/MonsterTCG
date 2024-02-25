package MTCG.dal.repository.repoUOWs;

import MTCG.models.UserModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatUOW {

    String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/mtcg";
    String USER = "postgres";
    String PASSWORD ="postgres";

    public UserModel getUserStats(String username) {
        String query = "SELECT * FROM Stats WHERE \"User\" = ?";
        UserModel userStats = null;
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int wins = resultSet.getInt("Wins");
                    int losses = resultSet.getInt("Losses");
                    int elo = resultSet.getInt("ELO");
                    userStats = new UserModel(username, null, false);
                    userStats.setWins(wins);
                    userStats.setLosses(losses);
                    userStats.setELO(elo);
                } else {
                    String insertQuery = "INSERT INTO Stats (\"User\", Wins, Losses, ELO) VALUES (?, 0, 0, 0)";
                    try (PreparedStatement insertPreparedStatement = connection.prepareStatement(insertQuery)) {
                        insertPreparedStatement.setString(1, username);
                        insertPreparedStatement.executeUpdate();
                    }
                    userStats = new UserModel(username, null, false);
                    userStats.setWins(0);
                    userStats.setLosses(0);
                    userStats.setELO(100);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userStats;
    }

    public List<UserModel> getScoreboard() {
        String query = "SELECT * FROM Scoreboard";
        List<UserModel> scoreboard = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String username = resultSet.getString("User");
                int elo = resultSet.getInt("ELO");
                UserModel user = new UserModel(username, null, false);
                user.setELO(elo);
                scoreboard.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scoreboard;
    }

    // In StatsUOW.java
    public void updateStatsAfterWin(String winner) {
        String sql = "UPDATE Stats SET Wins = Wins + 1, ELO = ELO + 3 WHERE \"User\" = ?";
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, winner);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStatsAfterLoss(String loser) {
        String sql = "UPDATE Stats SET Losses = Losses + 1, ELO = ELO - 5 WHERE \"User\" = ?";
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, loser);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}