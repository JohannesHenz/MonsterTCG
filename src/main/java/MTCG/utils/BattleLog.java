package MTCG.utils;

import MTCG.models.CardModel;

import java.util.ArrayList;
import java.util.List;

public class BattleLog {
    private List<String> log;

    public BattleLog() {
        this.log = new ArrayList<>();
    }

    public void logPlayerEntry(String player) {
        log.add("Player: " + player + " is waiting for an opponent");
    }

    public void logBattleStart(String player1, String player2) {
        log.add("Battle between " + player1 + " and " + player2 + " has begun!");
    }

    public void logRoundResult(CardModel winnerCard, CardModel loserCard) {
        log.add(winnerCard.getName() + " has defeated " + loserCard.getName());
    }

    public void logRoundDraw() {
        log.add("The round ended in a draw");
    }

    public void logDeckSizeAndDrawCounter(int deckSizePlayer1, int deckSizePlayer2, int drawCounter) {
        log.add("Player 1 deck size: " + deckSizePlayer1 + ", Player 2 deck size: " + deckSizePlayer2 + ", Draw counter: " + drawCounter);
    }

    public void logBattleWinner(String winner) {
        log.add("The Winner is: " + winner);
    }

    public void printLog() {
        for (String logEntry : log) {
            System.out.println(logEntry);
        }
    }
}