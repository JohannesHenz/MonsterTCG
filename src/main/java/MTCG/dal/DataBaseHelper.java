package MTCG.dal;

import java.sql.*;
import java.util.Arrays;
import java.util.stream.Collectors;

import MTCG.models.enums.CardNames;
import MTCG.models.enums.CardTypes;

public class DataBaseHelper {

    private static final String DATABASE_NAME = "mtcg";
    private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/mtcg";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public void initializeDB() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT 1 AS isExists FROM pg_database WHERE datname=?")) {

            statement.setString(1, DATABASE_NAME);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!(resultSet.next() && resultSet.getInt("isExists") == 1)) {
                    createDatabase(connection, DATABASE_NAME);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            System.out.println("Creating tables...");
            createTablesIfNotExist(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createDatabase(Connection connection, String databaseName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE DATABASE IF NOT EXISTS " + databaseName);
        }
    }

    private void createTablesIfNotExist(Statement statement) throws SQLException {

        /*
        try {
            // Drop the type if it exists
            statement.execute("DROP TYPE IF EXISTS CardTypes CASCADE");
        } catch (SQLException ignored) {
            // Ignore exception if the type does not exist
        }

        // Create the type
        String enumValues = Arrays.stream(CardTypes.values())
                .map(Enum::name)
                .collect(Collectors.joining("', '", "'", "'"));
        statement.execute("CREATE TYPE CardTypes AS ENUM (" + enumValues + ")");

        try {
            // Drop the type if it exists
            statement.execute("DROP TYPE IF EXISTS CardNames CASCADE");
        } catch (SQLException ignored) {
            // Ignore exception if the type does not exist
        }

        // Create the type
        enumValues = Arrays.stream(CardNames.values())
                .map(Enum::name)
                .collect(Collectors.joining("', '", "'", "'"));
        statement.execute("CREATE TYPE CardNames AS ENUM (" + enumValues + ")");


*/

        //TODO: Check if the types already exist
        //statement.execute("CREATE TYPE CardTypes AS ENUM ('MONSTER', 'SPELL')");
        //statement.execute("CREATE TYPE CardNames AS ENUM ('WATERGOBLIN, FIREGOBLIN, REGULARGOBLIN, WATERTROLL, FIRETROLL, REGULARTROLL, WATERELF, FIREELF, REGULARELF, WATERSPELL, FIRESPELL, REGULARSPELL, KNIGHT, DRAGON, ORK, KRAKEN')");
        statement.execute("CREATE TABLE IF NOT EXISTS UserCredentials (username VARCHAR(50) PRIMARY KEY NOT NULL, password VARCHAR(50) NOT NULL)");
        statement.execute("CREATE TABLE IF NOT EXISTS UserData (name VARCHAR(50) PRIMARY KEY NOT NULL, bio VARCHAR(200), image VARCHAR(200))");
        statement.execute("CREATE TABLE IF NOT EXISTS UserStats (name VARCHAR(50) PRIMARY KEY NOT NULL, elo INT NOT NULL, wins INT NOT NULL, losses INT NOT NULL)");
        statement.execute("CREATE TABLE IF NOT EXISTS Card (id uuid PRIMARY KEY, name VARCHAR(50) NOT NULL, damage float NOT NULL)");
        statement.execute("CREATE TABLE IF NOT EXISTS TradingDeal (id uuid PRIMARY KEY, cardToTrade uuid NOT NULL, type VARCHAR(50) NOT NULL, minimumDamage float NOT NULL)");
    }
}