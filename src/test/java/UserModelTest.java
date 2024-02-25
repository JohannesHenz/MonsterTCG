import MTCG.models.UserModel;
import MTCG.httpserver.server.HeaderMap;
import MTCG.httpserver.server.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserModelTest {
    private UserModel userModel;
    private Request mockRequest;

    @BeforeEach
    public void setUp() {
        userModel = new UserModel("testUser", "testPassword", false);
        mockRequest = mock(Request.class);
    }

    @Test
    public void testGetUsername() {
        assertEquals("testUser", userModel.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("testPassword", userModel.getPassword());
    }

    @Test
    public void testGetAdminStatus() {
        assertFalse(userModel.getAdminStatus());
    }

    @Test
    public void testGetCoinAmount() {
        assertEquals(20, userModel.getCoinAmount());
    }

    @Test
    public void testGetOwnedCardAmount() {
        assertEquals(0, userModel.getOwnedCardAmount());
    }

    @Test
    public void testGetUserFromBearerToken() {
        HeaderMap mockHeaderMap = mock(HeaderMap.class);
        when(mockRequest.getHeaderMap()).thenReturn(mockHeaderMap);
        when(mockHeaderMap.getHeader("Authorization")).thenReturn("Bearer testUser-testToken");

        UserModel result = userModel.getUserFromBearerToken(mockRequest);

        assertEquals("testUser", result.getUsername());
    }
}