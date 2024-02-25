package MTCG.dal.repository.repoUOWs;

import MTCG.dal.uow.IUnitOfWork;
import MTCG.models.UserModel;

import java.sql.*;

public class UserUOW implements IUnitOfWork {

    String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/mtcg";

    String USER = "postgres";

    String PASSWORD ="postgres";

    private String query;
    private String username;
    private String password;
    private boolean isAdmin;
    private int coinAmount;
    private int ownedCardAmount;

    public UserUOW() {
    }

    public boolean userExists(String username) {
        this.username = username;
        this.query = "SELECT * FROM \"User\" WHERE Username = ?";
        boolean exists = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
            // Create a statement
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            // Execute the SQL query
            rs = pstmt.executeQuery();
            // If the ResultSet has a next row, set exists to true
            if (rs.next()) {
                exists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return exists;
    }

    public boolean createUser(String username, String password, boolean isAdmin, int coinAmount, int ownedCardAmount) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.coinAmount = coinAmount;
        this.ownedCardAmount = ownedCardAmount;
        this.query = "INSERT INTO \"User\" (Username, Password, IsAdmin, CoinAmount, OwnedCardAmount) VALUES (?, ?, ?, ?, ?)";
        boolean userCreated = commit();

        if (userCreated) {
            // If the user was created successfully, insert a record into the Stats table with initial ELO set to 100
            this.query = "INSERT INTO Stats (\"User\", ELO, Wins, Losses) VALUES (?, 100, 0, 0)";
            return commit();
        }

        return false;
    }

    public UserModel getUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.query = "SELECT * FROM \"user\" WHERE Username = ? AND Password = ?";
        UserModel user = null;
        if (commit()) {
            user = new UserModel(username, password, isAdmin);
        }
        return user;
    }

    public UserModel getUserByUsername(String username) {
        this.username = username;
        this.query = "SELECT * FROM \"User\" WHERE Username = ?";
        UserModel user = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
            // Create a statement
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            // Execute the SQL query
            rs = pstmt.executeQuery();
            // If the ResultSet has a next row, create a new UserModel with the retrieved data
            if (rs.next()) {
                String password = rs.getString("Password");
                boolean isAdmin = rs.getBoolean("IsAdmin");
                user = new UserModel(username, password, isAdmin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return user;
    }

    public boolean buyPackage(UserModel user) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD)) {
            connection.setAutoCommit(false);

            // Fetch the current coin amount from the database
            String selectCoinsSql = "SELECT CoinAmount FROM \"User\" WHERE Username = ?";
            PreparedStatement selectCoinsStmt = connection.prepareStatement(selectCoinsSql);
            selectCoinsStmt.setString(1, user.getUsername());
            ResultSet rs1 = selectCoinsStmt.executeQuery();

            if (!rs1.next()) {
                return false;
            }

            int currentCoinAmount = rs1.getInt("CoinAmount");

            // Check if the user has enough coins
            if (currentCoinAmount < 5) {
                return false;
            }


            String selectPackageSql = "SELECT * FROM Package WHERE IsOwned = false ORDER BY Id LIMIT 1";
            PreparedStatement selectPackageStmt = connection.prepareStatement(selectPackageSql);
            ResultSet rs = selectPackageStmt.executeQuery();

            if (!rs.next()) {
                return false;
            }

            String packageId = rs.getString("Id");

            String updatePackageSql = "UPDATE Package SET IsOwned = true, Owner = ? WHERE Id = ?";
            PreparedStatement updatePackageStmt = connection.prepareStatement(updatePackageSql);
            updatePackageStmt.setString(1, user.getUsername());
            updatePackageStmt.setString(2, packageId);
            updatePackageStmt.executeUpdate();

            for (int i = 1; i <= 5; i++) {
                String cardId = rs.getString("Card" + i);
                String insertStackSql = "INSERT INTO Stack (Owner, CardId) VALUES (?, ?)";
                PreparedStatement insertStackStmt = connection.prepareStatement(insertStackSql);
                insertStackStmt.setString(1, user.getUsername());
                insertStackStmt.setString(2, cardId);
                insertStackStmt.executeUpdate();
            }

            String updateCoinsSql = "UPDATE \"User\" SET CoinAmount = CoinAmount - 5 WHERE Username = ?";
            PreparedStatement updateCoinsStmt = connection.prepareStatement(updateCoinsSql);
            updateCoinsStmt.setString(1, user.getUsername());
            updateCoinsStmt.executeUpdate();

            String updateOwnedCardAmountSql = "UPDATE \"User\" SET OwnedCardAmount = OwnedCardAmount + 5 WHERE Username = ?";
            PreparedStatement updateOwnedCardAmountStmt = connection.prepareStatement(updateOwnedCardAmountSql);
            updateOwnedCardAmountStmt.setString(1, user.getUsername());
            updateOwnedCardAmountStmt.executeUpdate();

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateUser(String username, String name, String bio, String image) {
        String query = "UPDATE \"User\" SET Name = ?, Bio = ?, Image = ? WHERE Username = ?";
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, bio);
            preparedStatement.setString(3, image);
            preparedStatement.setString(4, username);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public UserModel getUserData(String username) {
        this.username = username;
        this.query = "SELECT Name, Bio, Image FROM \"User\" WHERE Username = ?";
        UserModel user = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("Name");
                String bio = rs.getString("Bio");
                String image = rs.getString("Image");
                user = new UserModel(username, null, false);
                user.setName(name);
                user.setBio(bio);
                user.setImage(image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    @Override
    public boolean commit() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
            // Disable auto-commit mode
            conn.setAutoCommit(false);

            // Create a statement
            pstmt = conn.prepareStatement(query);

            // Set the parameters in the prepared statement
            pstmt.setString(1, username);
            if (password != null) {
                pstmt.setString(2, password);
                pstmt.setBoolean(3, isAdmin);
                pstmt.setInt(4, coinAmount);
                pstmt.setInt(5, ownedCardAmount);
            }

            // Execute the SQL query
            if (query.startsWith("SELECT")) {
                rs = pstmt.executeQuery();
                // If the ResultSet has a next row, return true
                if (rs.next()) {
                    return true;
                }
            } else {
                pstmt.executeUpdate();
            }

            // Commit the transaction
            conn.commit();

            // If all steps are successful, return true
            return true;
        } catch (Exception e) {
            // If any step fails, rollback the transaction
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            // Close resources
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}