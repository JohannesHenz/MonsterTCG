import MTCG.dal.repository.repoUOWs.UserUOW;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserUOWTest {
    private Connection connectionMock;
    private PreparedStatement preparedStatementMock;
    private ResultSet resultSetMock;
    private UserUOW userUOW;

    @BeforeEach
    public void setUp() throws SQLException {
        connectionMock = Mockito.mock(Connection.class);
        preparedStatementMock = Mockito.mock(PreparedStatement.class);
        resultSetMock = Mockito.mock(ResultSet.class);
        userUOW = new UserUOW();
    }

    @Test
    public void testUserExists() throws SQLException {
        when(connectionMock.prepareStatement(Mockito.anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);

        assertTrue(userUOW.userExists("testUser"));
    }

    @Test
    public void testUserDoesNotExist() throws SQLException {
        when(connectionMock.prepareStatement(Mockito.anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(false);

        assertTrue(userUOW.userExists("testUser"));
    }

    @Test
    public void testSaveUser() throws SQLException {
        when(connectionMock.prepareStatement(Mockito.anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        assertFalse(userUOW.createUser("testUser", "testPassword", false, 0,0));
    }

    @Test
    public void testSaveUserFailed() throws SQLException {
        when(connectionMock.prepareStatement(Mockito.anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(0);

        assertFalse(userUOW.createUser("testUser", "testPassword", false, 0,0));
    }

    @Test
    public void testGetUser() throws SQLException {
        when(connectionMock.prepareStatement(Mockito.anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);

        assertNull(userUOW.getUser("testUser", "testPassword"));
    }
}