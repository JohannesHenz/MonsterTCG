// BattleRepository.java
package MTCG.dal.repository;

import MTCG.dal.repository.repoUOWs.BattleUOW;
import MTCG.models.CardModel;

import java.util.List;

public class BattleRepository {
    private BattleUOW battleUOW = new BattleUOW();



    public void updateStatsAfterBattle(String winner, String loser) {
        battleUOW.updateStatsAfterBattle(winner, loser);
    }
}