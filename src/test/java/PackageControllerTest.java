import MTCG.controllers.PackageController;
import MTCG.httpserver.http.ContentType;
import MTCG.httpserver.http.HttpStatus;
import MTCG.httpserver.http.Method;
import MTCG.httpserver.server.HeaderMap;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.services.PackageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PackageControllerTest {
    private PackageController packageController;
    private PackageService mockPackageService;
    private Request mockRequest;
    private Response mockResponse;

    @BeforeEach
    public void setUp() {
        mockPackageService = mock(PackageService.class);
        mockRequest = mock(Request.class);
        packageController = new PackageController();
        mockResponse = mock(Response.class);

        HeaderMap mockHeaderMap = mock(HeaderMap.class);

        when(mockRequest.getHeaderMap()).thenReturn(mockHeaderMap);

        when(mockHeaderMap.getHeader("Authorization")).thenReturn("Bearer admin");

        when(mockRequest.getServiceRoute()).thenReturn("/packages");

    }

    @Test
    public void testHandleRequestWithInvalidMethod() {
        when(mockRequest.getMethod()).thenReturn(Method.GET);

        Response response = packageController.handleRequest(mockRequest);

        assertNull(mockResponse.getMessage());
    }

    @Test
    public void testHandleRequestWithValidMethodButInvalidUser() {
        when(mockRequest.getMethod()).thenReturn(Method.POST);
        when(mockRequest.getHeaderMap().getHeader("Authorization")).thenReturn(null);

        Response response = packageController.handleRequest(mockRequest);

        assertEquals(401, response.getStatus());
        assertEquals("Unauthorized", response.getMessage());
    }

    @Test
    public void testHandleRequestWithValidMethodAndValidUser() {
        when(mockRequest.getMethod()).thenReturn(Method.POST);
        when(mockRequest.getHeaderMap().getHeader("Authorization")).thenReturn("Bearer admin");
        when(mockPackageService.createPackage(mockRequest)).thenReturn(new Response(HttpStatus.OK, ContentType.JSON, "Package created successfully"));

        Response response = packageController.handleRequest(mockRequest);

        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getMessage());
    }
}