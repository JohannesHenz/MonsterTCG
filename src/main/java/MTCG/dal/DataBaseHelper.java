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
       // Class.forName("org.postgresql.Driver");
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

        String createCardTable = "CREATE TABLE IF NOT EXISTS Card (" +
                "CardName VARCHAR(50) PRIMARY KEY NOT NULL," +
                "Damage INT NOT NULL," +
                "ElementType VARCHAR(50) NOT NULL," +
                "UNIQUE (CardName)" +
                ");";
        statement.execute(createCardTable);

        String createUserTable = "CREATE TABLE IF NOT EXISTS \"User\" (" +
                "Username VARCHAR(50) PRIMARY KEY NOT NULL," +
                "Password VARCHAR(50) NOT NULL," +
                "IsAdmin BOOLEAN NOT NULL," +
                "CoinAmount INT NOT NULL," +
                "OwnedCardAmount INT NOT NULL" +
                ");";
        statement.execute(createUserTable);



        String createDeckTable = "CREATE TABLE IF NOT EXISTS Deck (" +
                "Owner VARCHAR(50) NOT NULL," +
                "Card1 VARCHAR(50)," +
                "Card2 VARCHAR(50)," +
                "Card3 VARCHAR(50)," +
                "Card4 VARCHAR(50)," +
                "FOREIGN KEY (Owner) REFERENCES  \"User\"(Username)," +
                "FOREIGN KEY (Card1) REFERENCES Card(CardName)," +
                "FOREIGN KEY (Card2) REFERENCES Card(CardName)," +
                "FOREIGN KEY (Card3) REFERENCES Card(CardName)," +
                "FOREIGN KEY (Card4) REFERENCES Card(CardName)" +
                ");";
        statement.execute(createDeckTable);
/*

        String createDeckTable = "CREATE TABLE IF NOT EXISTS Deck (" +
                "DeckId SERIAL PRIMARY KEY," +
                "Owner VARCHAR(50) NOT NULL," +
                "FOREIGN KEY (Owner) REFERENCES  \"User\"(Username)" +
                ");";

        String createDeckCardTable = "CREATE TABLE IF NOT EXISTS DeckCard(" +
                "DeckId INT NOT NULL," +
                "CardName VARCHAR(50) NOT NULL," +
                "FOREIGN KEY (DeckId) REFERENCES Deck(DeckId)," +
                "FOREIGN KEY (CardName) REFERENCES Card(CardName)" +
                ");";
        statement.execute(createDeckTable);
        statement.execute(createDeckCardTable);



 */
        String createTradingDealTable = "CREATE TABLE IF NOT EXISTS TradingDeal (" +
                "Id VARCHAR(50) PRIMARY KEY NOT NULL," +
                "FromPlayer VARCHAR(50) NOT NULL," +
                "ToPlayer VARCHAR(50) NOT NULL," +
                "FOREIGN KEY (FromPlayer) REFERENCES \"User\"(Username)," +
                "FOREIGN KEY (ToPlayer) REFERENCES \"User\"(Username)" +
                ");";
        statement.execute(createTradingDealTable);

        String createStatsTable = "CREATE TABLE IF NOT EXISTS Stats (" +
                " \"User\" VARCHAR(50) NOT NULL," +
                "ELO INT NOT NULL," +
                "Wins INT NOT NULL," +
                "Losses INT NOT NULL," +
                "FOREIGN KEY (\"User\") REFERENCES  \"User\"(Username)" +
                ");";
        statement.execute(createStatsTable);

        String createStackTable = "CREATE TABLE IF NOT EXISTS Stack (" +
                "Owner VARCHAR(50) NOT NULL," +
                "Card1 VARCHAR(50)," +
                "FOREIGN KEY (Owner) REFERENCES  \"User\"(Username)," +
                "FOREIGN KEY (Card1) REFERENCES Card(CardName)" +
                ");";
        statement.execute(createStackTable);

        String createPackageTable = "CREATE TABLE IF NOT EXISTS Package (" +
                "Id VARCHAR(50) PRIMARY KEY NOT NULL," +
                "Card1 VARCHAR(50)," +
                "Card2 VARCHAR(50)," +
                "Card3 VARCHAR(50)," +
                "Card4 VARCHAR(50)," +
                "Card5 VARCHAR(50)," +
                "IsOwned BOOLEAN NOT NULL," +
                "Owner VARCHAR(50)," +
                "FOREIGN KEY (Owner) REFERENCES  \"User\"(Username)," +
                "FOREIGN KEY (Card1) REFERENCES Card(CardName)," +
                "FOREIGN KEY (Card2) REFERENCES Card(CardName)," +
                "FOREIGN KEY (Card3) REFERENCES Card(CardName)," +
                "FOREIGN KEY (Card4) REFERENCES Card(CardName)," +
                "FOREIGN KEY (Card5) REFERENCES Card(CardName)" +
                ");";
        statement.execute(createPackageTable);
    }
    }