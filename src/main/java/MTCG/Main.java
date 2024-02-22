package MTCG;

import MTCG.controllers.*;
import MTCG.controllers.routes.SessionsRouteController;
import MTCG.dal.DataBaseHelper;
import MTCG.httpserver.server.Server;
import MTCG.httpserver.utils.Router;
import MTCG.services.UserService;

public class Main {
    public static void main(String[] args) {


        Router router = new Router();

        int port = 10001;
        Server server = new Server(port, router);
        DataBaseHelper db = new DataBaseHelper();
        router.addService("/users", new UserController());
        router.addService("/users/{username}", new UserController());
        router.addService("/sessions", new UserController());
        router.addService("/packages", new PackageController());
        router.addService("/transactions/packages", new PackageController());
        router.addService("/cards", new CardController());
        router.addService("/deck", new CardController());
        router.addService("/stats", new StatController());
        router.addService("/scoreboard", new StatController());
        router.addService("/battles", new BattleController());
        router.addService("/tradings", new TradingController());

        try {
            db.initializeDB();
            server.start();


        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
