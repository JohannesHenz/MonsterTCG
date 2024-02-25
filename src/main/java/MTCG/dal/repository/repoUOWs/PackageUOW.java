package MTCG.dal.repository.repoUOWs;

import MTCG.models.CardModel;
import MTCG.models.PackageModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PackageUOW {

    private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/mtcg";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public boolean createPackage(PackageModel packageModel) {
        if (packageModel.getCards().size() < 5) {
            return false;
        }
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD)) {
            connection.setAutoCommit(false);

            String insertCardSql = "INSERT INTO Card (Id, Name, Damage) VALUES (?, ?, ?)";
            PreparedStatement insertCardStmt = connection.prepareStatement(insertCardSql);
            for (CardModel card : packageModel.getCards()) {
                insertCardStmt.setString(1, card.getId().toString());
                insertCardStmt.setString(2, card.getName());
                insertCardStmt.setDouble(3, card.getDamage());
                insertCardStmt.addBatch();
            }

            String insertPackageSql = "INSERT INTO Package (Id, Card1, Card2, Card3, Card4, Card5, IsOwned) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertPackageStmt = connection.prepareStatement(insertPackageSql);
            insertPackageStmt.setString(1, packageModel.getPackageId());
            for (int i = 0; i < 5; i++) {
                insertPackageStmt.setString(i + 2, packageModel.getCards().get(i).getId().toString());
            }
            insertPackageStmt.setBoolean(7, false);

            insertCardStmt.executeBatch();
            insertPackageStmt.executeUpdate();

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}