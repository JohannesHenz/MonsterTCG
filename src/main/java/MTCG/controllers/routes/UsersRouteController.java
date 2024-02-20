package MTCG.controllers.routes;

import MTCG.services.UserService;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
public class UsersRouteController {

    public final UserService userService;

    public UsersRouteController(UserService userService){
        this.userService = userService;
    }


}
