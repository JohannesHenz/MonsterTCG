package MTCG.dal.repository.repoUOWs;

import MTCG.dal.uow.IUnitOfWork;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class UserUOW implements IUnitOfWork {

    private String query;

    UserUOW (String query)
    {
        this.query = query;
    }


    @Override
    public boolean commit() {
        Connection conn = null;
        Statement stmt = null;
        try {
            // Establish a connection to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
            // Disable auto-commit mode
            conn.setAutoCommit(false);

            // Create a statement
            stmt = conn.createStatement();

            // Execute the SQL query
            stmt.executeUpdate(query);

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
            if (stmt != null) {
                try {
                    stmt.close();
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