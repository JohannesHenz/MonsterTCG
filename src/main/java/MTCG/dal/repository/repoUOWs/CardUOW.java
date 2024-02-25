package MTCG.dal.repository.repoUOWs;

import MTCG.models.CardModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardUOW {

    private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/mtcg";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public boolean createCard(CardModel card) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD)) {
            String insertCardSql = "INSERT INTO Card (Id, Name, Damage) VALUES (?, ?, ?)";
            PreparedStatement insertCardStmt = connection.prepareStatement(insertCardSql);
            insertCardStmt.setString(1, card.getId().toString());
            insertCardStmt.setString(2, card.getName());
            insertCardStmt.setDouble(3, card.getDamage());
            insertCardStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CardModel> getCards(String username) {
        List<CardModel> cards = new ArrayList<>();
        String query = "SELECT Card.Id, Card.Name, Card.Damage FROM Card JOIN Stack ON Card.Id = Stack.CardId WHERE Stack.Owner = ?";
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                UUID Id = UUID.fromString(rs.getString("Id"));
                String Name = rs.getString("Name");
                double Damage = rs.getDouble("Damage");
                cards.add(new CardModel(Id, Name, Damage));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    public List<CardModel> getDeck(String username) {
        List<CardModel> cards = new ArrayList<>();
        String query = "SELECT Card.Id, Card.Name, Card.Damage FROM Card WHERE Card.Id IN (SELECT Card1 FROM Deck WHERE Owner = ? UNION SELECT Card2 FROM Deck WHERE Owner = ? UNION SELECT Card3 FROM Deck WHERE Owner = ? UNION SELECT Card4 FROM Deck WHERE Owner = ?)";
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, username);
            pstmt.setString(3, username);
            pstmt.setString(4, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                UUID Id = UUID.fromString(rs.getString("Id"));
                String Name = rs.getString("Name");
                double Damage = rs.getDouble("Damage");
                cards.add(new CardModel(Id, Name, Damage));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    public boolean executeDelete(String sql, String username) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean executeInsert(String sql, String username, List<String> cardIds) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            for (int i = 0; i < cardIds.size(); i++) {
                preparedStatement.setString(i + 2, cardIds.get(i));
            }

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean upgradeCard(String username, String cardId, double amount) {
        String sql = "UPDATE Card SET Damage = Damage + ? WHERE Id = ? AND Id IN (SELECT CardId FROM Stack WHERE Owner = ?)";
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDouble(1, amount);
            preparedStatement.setString(2, cardId);
            preparedStatement.setString(3, username);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



}