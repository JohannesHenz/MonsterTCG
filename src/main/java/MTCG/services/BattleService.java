// BattleService.java
package MTCG.services;

import MTCG.dal.repository.BattleRepository;
import MTCG.dal.repository.CardRepository;
import MTCG.httpserver.http.ContentType;
import MTCG.httpserver.http.HttpStatus;
import MTCG.httpserver.server.Response;
import MTCG.models.CardModel;
import MTCG.models.MonsterCardModel;
import MTCG.models.SpellCardModel;
import MTCG.models.enums.ElementTypes;
import MTCG.models.enums.CardNames;
import MTCG.utils.BattleLog;
import java.util.Random;
import java.util.List;

public class BattleService {
    private BattleLog battleLog;
    private Random random;
    private BattleRepository battleRepository;
    CardModel winnerCard = null;


    public BattleService(BattleRepository battleRepository) {
        this.battleRepository = battleRepository;
        this.battleLog = new BattleLog();
        this.random = new Random();
    }

    public Response battle(String player1, String player2) {
        System.out.println("Battle between " + player1 + " and " + player2 + " has begun!");
        battleLog.logBattleStart(player1, player2);
        CardRepository cardRepository = new CardRepository();
        List<CardModel> deck1 = cardRepository.getDeck(player1);
        List<CardModel> deck2 = cardRepository.getDeck(player2);
        String winner = "";
        int draws = 0;
        int round = 1;
        while (!deck1.isEmpty() && !deck2.isEmpty() && round < 100) {
            System.out.println("Deck1 size of Player 1's Deck: " + deck1.size());
            System.out.println("Deck1 size of Player 2's Deck: " + deck2.size());
            CardModel card1 = deck1.remove(random.nextInt(deck1.size()));
            CardModel card2 = deck2.remove(random.nextInt(deck2.size()));
            System.out.println("Card1: " + card1.getName());
            System.out.println("Card2: " + card2.getName());

            if (card1.getName().contains("Spell") && card2.getName().contains("Spell")) {
                System.out.println("Round Number: " + round + " In the Spell vs Spell section");
                winnerCard = spellVsSpell(card1, card2);
                if (winnerCard != null) {
                    System.out.println("Spell vs Spell. Winner: " + winnerCard.getName());
                } else {
                    System.out.println("Spell vs Spell. It's a draw");
                }
                round++;
            } else if (!card1.getName().contains("Spell") && !card2.getName().contains("Spell")) {
                System.out.println("Round Number: " + round + " In the Monster vs Monster section");
                winnerCard = monsterVsMonster(card1, card2);
                if (winnerCard != null) {
                    System.out.println("Monster vs Monster. Winner: " + winnerCard.getName());
                } else {
                    System.out.println("Monster vs Monster. It's a draw");
                }
                round++;
            } else if (!card1.getName().contains("Spell") && card2.getName().contains("Spell")) {
                System.out.println("Round Number: " + round + " In the Monster vs Spell section");
                winnerCard = monsterVsSpell(card1, card2);
                if (winnerCard != null) {
                    System.out.println("Monster vs Spell. Winner: " + winnerCard.getName());
                } else {
                    System.out.println("Monster vs Spell. It's a draw");
                }
                round++;
            } else if (card1.getName().contains("Spell") && !card2.getName().contains("Spell")) {
                System.out.println("Round Number: " + round + " In the Spell vs Monster section");
                winnerCard = monsterVsSpell(card2, card1);
                if (winnerCard != null) {
                    System.out.println("Spell vs Monster. Winner: " + winnerCard.getName());
                } else {
                    System.out.println("Spell vs Monster. It's a draw");
                }
                round++;
            }
            if (winnerCard == card1) {
                deck1.add(card1);
                deck1.add(card2);
                System.out.println("Round Number: " + round);
                battleLog.logRoundResult(winnerCard, card2);
                battleLog.logDeckSizeAndDrawCounter(deck1.size(), deck2.size(), draws);
            } else if (winnerCard == card2) {
                deck2.add(card1);
                deck2.add(card2);
                System.out.println("Round Number: " + round);
                battleLog.logRoundResult(winnerCard, card1);
                battleLog.logDeckSizeAndDrawCounter(deck1.size(), deck2.size(), draws);
            } else if (winnerCard == null) {
                System.out.println("DRAW?!");
                System.out.println("Round Number: " + round);
                deck1.add(card1);
                deck2.add(card2);
                draws++;
                battleLog.logDeckSizeAndDrawCounter(deck1.size(), deck2.size(), draws);
            }
            else {
                System.out.println("No clue mate");
            }
        }
        if (round < 100 && deck1.isEmpty()) {
            winner = player2;
                battleRepository.updateStatsAfterBattle(player2, player1); //Player 1 hat gewonnen
               //battleLog.logBattleWinner(player1);
            return new Response(HttpStatus.OK, ContentType.JSON, "{'message': 'The winner is " + winner + "'}");
            } else if (round < 100 && deck2.isEmpty()) {
            winner = player1;
                battleRepository.updateStatsAfterBattle(player1, player2); //Player 2 hat gewonnen
            return new Response(HttpStatus.OK, ContentType.JSON, "{'message': 'The winner is " + winner + "'}");
              // battleLog.logBattleWinner(player2);
            } else {
               // battleLog.logBattleWinner("Draw");
                // battleLog.printLog();
            System.out.println("The battle ended in a draw");
             return new Response(HttpStatus.OK, ContentType.JSON, "{'message': 'The battle ended in a draw'}");
        }
        //battleLog.printLog();

    }


    private CardModel monsterVsMonster(CardModel monster1, CardModel monster2) {
        System.out.println("Monster vs Monster Fight has begun between " + monster1.getName() + " and " + monster2.getName());

        if (monster1.getName().equals("Dragon") && monster2.getName().equals("Goblin")) {
            return monster1;
        } else if (monster1.getName().equals("Goblin") && monster2.getName().equals("Dragon")) {
            return monster2;
        } else if (monster1.getName().equals("FireElf") && monster2.getName().equals("Dragon")) {
            return monster1;
        } else if (monster1.getName().equals("Dragon") && monster2.getName().equals("FireElf")) {
            return monster2;

        }
        if (monster1.getDamage() > monster2.getDamage()) {
            System.out.println("Monster: " + monster1.getName() + " wins");
            return monster1;
        } else if (monster1.getDamage() < monster2.getDamage()) {
            System.out.println("Monster: " + monster2.getName() + " wins");
            return monster2;
        } else {
            System.out.println("Draw");
            return null;
        }
    }

    private CardModel spellVsSpell(CardModel spell1, CardModel spell2) {
        System.out.println("Spell vs Spell Fight has begun between " + spell1.getName() + " and " + spell2.getName());
        double spell1Damage = spell1.getDamage();
        double spell2Damage = spell2.getDamage();

        switch (spell1.getElementType()) {
            case "Water":
                if (spell2.getElementType().equals("Fire")) {
                    spell1Damage *= 2;
                } else if (spell2.getElementType().equals("Regular")) {
                    spell1Damage /= 2;
                }
                break;
            case "Fire":
                if (spell2.getElementType().equals("Regular")) {
                    spell1Damage *= 2;
                } else if (spell2.getElementType().equals("Water")) {
                    spell1Damage /= 2;
                }
                break;
            case "Regular":
                if (spell2.getElementType().equals("Water")) {
                    spell1Damage *= 2;
                } else if (spell2.getElementType().equals("Fire")) {
                    spell1Damage /= 2;
                }
                break;
        }

        switch (spell2.getElementType()) {
            case "Water":
                if (spell1.getElementType().equals("Fire")) {
                    spell2Damage *= 2;
                } else if (spell1.getElementType().equals("Regular")) {
                spell2Damage /= 2;
            }
            break;
            case "Fire":
                if (spell1.getElementType().equals("Regular")) {
                    spell2Damage *= 2;
                } else if (spell1.getElementType().equals("Water")) {
                    spell2Damage /= 2;
                }
                break;
            case "Regular":
                if (spell1.getElementType().equals("Water")) {
                    spell2Damage *= 2;
                } else if (spell1.getElementType().equals("Fire")) {
                    spell2Damage /= 2;
                }
                break;
        }

        if (spell1Damage > spell2Damage) {
            System.out.println("Spell: " + spell1.getName() + " wins");
            return spell1;
        } else if (spell1Damage < spell2Damage) {
            System.out.println("Spell: " + spell2.getName() + " wins");
            return spell2;
        } else {
            // In case of a draw, return null
            System.out.println("Draw");
            return null;
        }
    }

    private CardModel monsterVsSpell(CardModel monster, CardModel spell) {
        System.out.println("Monster vs Spell Fight has begun between " + monster.getName() + " and " + spell.getName());

        double spellDamage = spell.getDamage();
        double monsterDamage = monster.getDamage();


         if (monster.getName().equals("Knight") && spell.getName().equals("WaterSpell")) {
            return spell;
        } else if (monster.getName().equals("Kraken")) {
             return monster;
         } else {

             switch (spell.getElementType()) {
                 case "Water":
                     if (monster.getElementType().equals("Fire")) {
                         spellDamage *= 2;
                     } else if (monster.getElementType().equals("Regular")) {
                         spellDamage /= 2;
                     }
                     break;
                 case "Fire":
                     if (monster.getElementType().equals("Regular")) {
                         spellDamage *= 2;
                     } else if (monster.getElementType().equals("Water")) {
                         spellDamage /= 2;
                     }
                     break;
                 case "Regular":
                     if (monster.getElementType().equals("Water")) {
                         spellDamage *= 2;
                     } else if (monster.getElementType().equals("Fire")) {
                         spellDamage /= 2;
                     }
                     break;
             }

             switch (monster.getElementType()) {
                 case "Water":
                     if (spell.getElementType().equals("Fire")) {
                         monsterDamage *= 2;
                     } else if (monster.getElementType().equals("Regular")) {
                         monsterDamage /= 2;
                     }
                     break;
                 case "Fire":
                     if (monster.getElementType().equals("Regular")) {
                         monsterDamage *= 2;
                     } else if (monster.getElementType().equals("Water")) {
                         monsterDamage /= 2;
                     }
                     break;
                 case "Regular":
                     if (monster.getElementType().equals("Water")) {
                         monsterDamage *= 2;
                     } else if (monster.getElementType().equals("Fire")) {
                         monsterDamage /= 2;
                     }
                     break;
             }

            if (monsterDamage > spellDamage) {
                System.out.println("Monster: " + monster.getName() + " wins");
                return monster;
            } else if (monsterDamage < spellDamage) {
                System.out.println("Spell: " + spell.getName() + " wins");
                return spell;
            } else {
                System.out.println("Monster Damage: " + monsterDamage);
                System.out.println("Spell Damage: " + spellDamage);// In case of a draw, return null
                System.out.println("Draw");
                return null; // It's a draw
            }
        }
    }
}