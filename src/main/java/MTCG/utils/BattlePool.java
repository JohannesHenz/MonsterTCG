package MTCG.utils;

import java.util.concurrent.*;

public class BattlePool {
    private static BattlePool instance = null;
    private final ConcurrentLinkedQueue<String> pool = new ConcurrentLinkedQueue<>();

    private BattlePool() {}

    public static synchronized BattlePool getInstance() {
        if (instance == null) {
            instance = new BattlePool();
        }
        return instance;
    }

    public synchronized void addPlayer(String player) {
        pool.add(player);
        System.out.println("Current number of players in the BattlePool: " + pool.size());
    }

    public boolean hasTwoPlayers() {
        return pool.size() > 1;
    }

    public boolean hasPlayers() {
        return pool.size() > 0;
    }

    public String[] getTwoPlayers() {
        return new String[]{pool.poll(), pool.poll()};
    }

    public int getPoolSize() {
        return pool.size();
    }
}