package MTCG.dal.repository;

import MTCG.dal.repository.repoUOWs.StatUOW;
import MTCG.models.UserModel;

import java.util.List;

public class StatRepository {

    private StatUOW statUOW;

    public StatRepository() {
        this.statUOW = new StatUOW();
    }

    public UserModel getUserStats(String username) {
        return statUOW.getUserStats(username);
    }

    public List<UserModel> getScoreboard() {
        return statUOW.getScoreboard();
    }

    // In StatRepository.java
    public void updateStatsAfterWin(String winner) {
        statUOW.updateStatsAfterWin(winner);
    }

    public void updateStatsAfterLoss(String loser) {
        statUOW.updateStatsAfterLoss(loser);
    }
}