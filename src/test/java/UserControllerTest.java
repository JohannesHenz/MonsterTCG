import MTCG.controllers.UserController;
import MTCG.httpserver.http.HttpStatus;
import MTCG.httpserver.http.Method;
import MTCG.httpserver.server.HeaderMap;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.models.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private Request mockRequest;
    private UserModel mockUser;

    @BeforeEach
    public void setUp() {
        this.mockUser = new UserModel("testUser", "testPassword", false);
        HeaderMap headerMap = new HeaderMap();
        headerMap.setHeader("Authorization", "Bearer " + mockUser.getUsername() + "-token");

        mockRequest = mock(Request.class);
        when(mockRequest.getHeaderMap()).thenReturn(headerMap);


        userController = new UserController();
        mockRequest = mock(Request.class);
        mockUser = mock(UserModel.class);
    }


    @Test
    public void testGetLoggedInUsers() {
        // Add a user to the loggedInUsers list
        userController.addLoggedInUser(mockUser);

        // Call the method to test
        List<String> loggedInUsers = userController.getLoggedInUsers();

        // Assert the loggedInUsers list contains the user
        assertTrue(loggedInUsers.contains(mockUser.getUsername()));
    }

    @Test
    public void testAddLoggedInUser() {
        // Call the method to test
        userController.addLoggedInUser(mockUser);

        // Assert the loggedInUsers list contains the user
        assertTrue(userController.getLoggedInUsers().contains(mockUser.getUsername()));
    }


    @Test
    public void testLogoutAllUsers() {
        // Add a user to the loggedInUsers list
        userController.addLoggedInUser(mockUser);

        // Call the method to test
        userController.logoutAllUsers();

        // Assert the loggedInUsers list is empty
        assertTrue(userController.getLoggedInUsers().isEmpty());
    }
}