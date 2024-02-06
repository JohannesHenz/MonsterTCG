package MTCG.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataBaseHelper {

    public void initializeDB() {
        String connectionString = "jdbc:postgresql://localhost:5432/mtcg";
        String user = "postgres";
        String password = "postgres";
        String databaseName = "mtcg";
        try {
            Connection connection = DriverManager.getConnection(connectionString, user, password);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT 1 AS isExists FROM pg_database WHERE datname='" + databaseName + "'");

            // Create DB if not exists
            if (resultSet.next() && resultSet.getInt("isExists") == 0) {
                statement.execute("CREATE DATABASE " + databaseName);
            }

            resultSet.close();
            statement.close();
            connection.close();

            // Establish connection to the created/newly verified database
            connection = DriverManager.getConnection(connectionString+databaseName, user, password);


            String sql = "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, username VARCHAR(50) NOT NULL, password VARCHAR(50) NOT NULL, coins INT NOT NULL, elo INT NOT NULL)";
            statement.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS decks (id SERIAL PRIMARY KEY, name VARCHAR(50) NOT NULL, user_id INT NOT NULL, FOREIGN KEY (user_id) REFERENCES users(id))";
            statement.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS cards (id SERIAL PRIMARY KEY, name VARCHAR(50) NOT NULL, damage INT NOT NULL, type VARCHAR(50) NOT NULL, user_id INT NOT NULL, FOREIGN KEY (user_id) REFERENCES users(id))";
            statement.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS packages (id SERIAL PRIMARY KEY, name VARCHAR(50) NOT NULL, user_id INT NOT NULL, FOREIGN KEY (user_id) REFERENCES users(id))";
            statement.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS package_cards (id SERIAL PRIMARY KEY, package_id INT NOT NULL, card_id INT NOT NULL, FOREIGN KEY (package_id) REFERENCES packages(id), FOREIGN KEY (card_id) REFERENCES cards(id))";
            statement.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS trades (id SERIAL PRIMARY KEY, card_id INT NOT NULL, user_id INT NOT NULL, FOREIGN KEY (card_id) REFERENCES cards(id), FOREIGN KEY (user_id) REFERENCES users(id))";
            statement.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS battles (id SERIAL PRIMARY KEY, user1_id INT NOT NULL, user2_id INT NOT NULL, winner_id INT NOT NULL, FOREIGN KEY (user1_id) REFERENCES users(id), FOREIGN KEY (user2_id) REFERENCES users(id), FOREIGN KEY (winner_id) REFERENCES users(id))";
            statement.executeUpdate(sql);
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
