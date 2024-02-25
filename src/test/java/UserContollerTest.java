import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import MTCG.controllers.UserController;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;

public class UserControllerTest {

    private UserController userController;

    @Before
    public void setUp() {
        userController = new UserController();
    }

    @Test
    public void testHandleRequest() {
        Request request = new Request();
        // set up the request object as needed

        Response response = userController.handleRequest(request);

        // assert statements to check if the response is as expected
        // for example, if you expect the status code to be 200:
        assertEquals(200, response.getStatusCode());
        // you can add more assertions to check the content type, body, etc.
    }
}