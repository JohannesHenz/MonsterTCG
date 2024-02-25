package MTCG.httpserver.server;

import MTCG.controllers.BattleController;
import MTCG.dal.repository.BattleRepository;
import MTCG.httpserver.utils.RequestHandler;
import MTCG.httpserver.utils.Router;
import MTCG.services.BattleService;
import MTCG.utils.BattlePool;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private Router router;
    private final BattlePool battlePool;
    private ExecutorService battleExecutor;
    private BattleRepository battleRepository;

    public Server(int port, Router router)  {
        this.port = port;
        this.executorService = Executors.newFixedThreadPool(10);
        this.router = router;
        this.battlePool =  BattlePool.getInstance();
        this.battleExecutor = Executors.newSingleThreadExecutor();
    }

    public void start() throws IOException {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        System.out.println("Start http-server...");
        System.out.println("http-server running at: http://localhost:" + this.port);
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            while (true) {
                final Socket clientSocket = serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler(clientSocket, this.router);
                executorService.submit(() -> {
                    requestHandler.run();
                   int battlePoolSize = battlePool.getPoolSize();

                    //synchronized(battlePool) {
                        if (battlePoolSize >= 2) {
                            String[] players = battlePool.getTwoPlayers();
                            System.out.println("There are two players in the battle pool: " + players[0] + " and " + players[1]);

                            BattleService battleService = new BattleService(battleRepository);
                            BattleController battleController = new BattleController(battlePool, battleService);
                            battleExecutor.submit(() -> {
                                battleService.battle(players[0], players[1]);
                            });
                        } else {
                            System.out.println("There are less than two players in the battle pool.");
                        }
                   // }
                });
            }
        }
    }
}