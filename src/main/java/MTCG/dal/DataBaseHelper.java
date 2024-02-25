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
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", USER, PASSWORD);
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

    public void createDatabase(Connection connection, String databaseName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE DATABASE " + databaseName);
        }
    }

    public void createTablesIfNotExist(Statement statement) throws SQLException {

        String createCardTable = "CREATE TABLE IF NOT EXISTS Card (" +
                "Id VARCHAR (100) PRIMARY KEY NOT NULL," +
                "Name VARCHAR(50) NOT NULL," +
                "Damage FLOAT NOT NULL," +
                "UNIQUE (Id)" +
                ");";
        statement.execute(createCardTable);

        String createUserTable = "CREATE TABLE IF NOT EXISTS \"User\" (" +
                "Username VARCHAR(50) PRIMARY KEY NOT NULL," +
                "Password VARCHAR(50) NOT NULL," +
                "IsAdmin BOOLEAN NOT NULL," +
                "CoinAmount INT NOT NULL," +
                "OwnedCardAmount INT NOT NULL," +
                "Name VARCHAR(50)," +
                "Bio VARCHAR(50)," +
                "Image VARCHAR(50)" +
                ");";
        statement.execute(createUserTable);



        String createDeckTable = "CREATE TABLE IF NOT EXISTS Deck (" +
                "Owner VARCHAR(50) NOT NULL," +
                "Card1 VARCHAR(50)," +
                "Card2 VARCHAR(50)," +
                "Card3 VARCHAR(50)," +
                "Card4 VARCHAR(50)," +
                "FOREIGN KEY (Owner) REFERENCES  \"User\"(Username)," +
                "FOREIGN KEY (Card1) REFERENCES Card(Id)," +
                "FOREIGN KEY (Card2) REFERENCES Card(Id)," +
                "FOREIGN KEY (Card3) REFERENCES Card(Id)," +
                "FOREIGN KEY (Card4) REFERENCES Card(Id)" +
                ");";
        statement.execute(createDeckTable);


        String createTradingDealTable = "CREATE TABLE IF NOT EXISTS TradingDeal (" +
                "TradeId VARCHAR(100) PRIMARY KEY NOT NULL," +
                "FromPlayer VARCHAR(50) NOT NULL," +
                "ToPlayer VARCHAR(50) NOT NULL," +
                "FOREIGN KEY (FromPlayer) REFERENCES \"User\"(Username)," +
                "FOREIGN KEY (ToPlayer) REFERENCES \"User\"(Username)" +
                ");";
        statement.execute(createTradingDealTable);

        String createStatsTable = "CREATE TABLE IF NOT EXISTS Stats (" +
                " \"User\" VARCHAR(50) NOT NULL UNIQUE," +
                "ELO INT NOT NULL," +
                "Wins INT NOT NULL," +
                "Losses INT NOT NULL," +
                "FOREIGN KEY (\"User\") REFERENCES  \"User\"(Username)" +
                ");";
        statement.execute(createStatsTable);

        String createStackTable = "CREATE TABLE IF NOT EXISTS Stack (" +
                "Owner VARCHAR(50) NOT NULL," +
                "CardId VARCHAR(100) NOT NULL," +
                "FOREIGN KEY (Owner) REFERENCES  \"User\"(Username)," +
                "FOREIGN KEY (CardId) REFERENCES Card(Id)," +
                "PRIMARY KEY (Owner, CardId)" + // Changed primary key to combination of Owner and CardId
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
                "FOREIGN KEY (Card1) REFERENCES Card(Id)," +
                "FOREIGN KEY (Card2) REFERENCES Card(Id)," +
                "FOREIGN KEY (Card3) REFERENCES Card(Id)," +
                "FOREIGN KEY (Card4) REFERENCES Card(Id)," +
                "FOREIGN KEY (Card5) REFERENCES Card(Id)" +
                ");";
        statement.execute(createPackageTable);

        String createScoreboardTable = "CREATE TABLE IF NOT EXISTS Scoreboard (" +
                " \"User\" VARCHAR(50) NOT NULL PRIMARY KEY," +
                "ELO INT NOT NULL," +
                "FOREIGN KEY (\"User\") REFERENCES  \"User\"(Username)" +
                ");";
        statement.execute(createScoreboardTable);
    }
    }