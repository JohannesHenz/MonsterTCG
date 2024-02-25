import MTCG.dal.repository.BattleRepository;
import MTCG.models.CardModel;
import MTCG.services.BattleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class BattleServiceTest {
    private BattleService battleService;

    @BeforeEach
    public void setUp() {
        battleService = new BattleService(new BattleRepository());
    }



    @Test
    public void testMonsterVsMonster() {
        CardModel monster1 = new CardModel(UUID.randomUUID(), "FireElf", 10);
       CardModel monster2 = new CardModel(UUID.randomUUID(), "Dragon", 70);
        CardModel winnerCard = battleService.monsterVsMonster(monster1, monster2);
        assertEquals(monster1, winnerCard);
    }

    @Test
    public void testSpellVsSpell() {
        CardModel spell1 = new CardModel(UUID.randomUUID(), "RegularSpell", 15);
       CardModel spell2 = new CardModel(UUID.randomUUID(), "WaterSpell", 10);
        CardModel winnerCard = battleService.spellVsSpell(spell1, spell2);
        assertEquals(spell1, winnerCard);
    }

    @Test
    public void testMonsterVsSpell() {
        CardModel monster = new CardModel(UUID.randomUUID(), "Knight", 35);
        CardModel spell = new CardModel(UUID.randomUUID(), "WaterSpell", 10);
        CardModel winnerCard = battleService.monsterVsSpell(monster, spell);
        assertEquals(spell, winnerCard);
    }


}