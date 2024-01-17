package MTCG.controllers.routes;

import MTCG.models.UserModel;
import MTCG.services.UserService;
import MTCG.httpserver.http.Method;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
public class UsersRouteController {

    public final UserService userService;

    public UsersRouteController(UserService userService){
        this.userService = userService;
    }

    public Response handleUserRegistration(Request request){
        //TODO
    }

    public Response handleUserLogin(Response response){
        //TODO
    }
}
