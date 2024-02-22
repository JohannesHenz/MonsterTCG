package MTCG.dal.repository.repoUOWs;

import MTCG.dal.uow.IUnitOfWork;

import java.sql.*;

public class UserUOW implements IUnitOfWork {

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
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "postgres");
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
        return commit();
    }

    @Override
    public boolean commit() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "postgres");
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