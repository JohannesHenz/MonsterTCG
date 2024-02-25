// BattleUOW.java
package MTCG.dal.repository.repoUOWs;

import MTCG.dal.repository.StatRepository;
import MTCG.models.CardModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BattleUOW {
    private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/mtcg";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";


    // In BattleService.java
    public void updateStatsAfterBattle(String winner, String loser) {
        StatRepository statRepository = new StatRepository();
        statRepository.updateStatsAfterWin(winner);
        statRepository.updateStatsAfterLoss(loser);
    }
}
