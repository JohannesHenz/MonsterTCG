package MTCG.dal.repository.repoUOWs;

import MTCG.dal.uow.IUnitOfWork;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class UserUOW implements IUnitOfWork {

    private String query;
    private String username;
    private String password;

    private boolean isAdmin;

    private int coinAmount;

    private int ownedCardAmount;

    public UserUOW (String query, String username, String password, boolean isAdmin, int coinAmount, int ownedCardAmount)
    {
        this.query = query;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.coinAmount = coinAmount;
        this.ownedCardAmount = ownedCardAmount;
    }


    @Override
    public boolean commit() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "postgres");
            // Disable auto-commit mode
            conn.setAutoCommit(false);

            // Create a statement
            pstmt = conn.prepareStatement(query);

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setBoolean(3, isAdmin);
            pstmt.setInt(4, coinAmount);
            pstmt.setInt(5, ownedCardAmount);

            // Execute the SQL query
            pstmt.executeUpdate();

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

    @Override
    public void rollback() {

    }

    @Override
    public void beginTransaction() {

    }
}
