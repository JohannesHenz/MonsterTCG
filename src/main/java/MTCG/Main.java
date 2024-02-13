package MTCG;

import MTCG.dal.DataBaseHelper;
import MTCG.httpserver.server.Server;
import MTCG.httpserver.utils.Router;
public class Main {
    public static void main(String[] args) {


        Router router = new Router();

        int port = 10001;
        Server server = new Server(port, router);
        DataBaseHelper db = new DataBaseHelper();

        try {
            db.initializeDB();
            server.start();


        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
