package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.basic.minion.DreadInfernal;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionMock;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestDreadInfernal {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private static final byte mana = 2;
    private static final byte attack0 = 5;
    private static final byte health0 = 3;
    private static final byte health1 = 7;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Minion minion0_0 = new MinionMock("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion0_1 = new MinionMock("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);
        Minion minion1_0 = new MinionMock("" + 0, mana, attack0, health0, attack0, health0, health0);
        Minion minion1_1 = new MinionMock("" + 0, mana, attack0, (byte)(health1 - 1), attack0, health1, health1);

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_0);
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, minion0_1);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_0);
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, minion1_1);

        Minion fb = new DreadInfernal();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 7);
        waitingPlayer.setMana((byte) 4);
    }

    @Test
    public void test0() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board);

        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), health0);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), health1 - 1);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), health0);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), health1 - 1);
    }

    @Test
    public void test2() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board);

        assertNotNull(ret);
        currentPlayer = ret.data_.getCurrentPlayer();
        waitingPlayer = ret.data_.getWaitingPlayer();

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 1);
        assertEquals(waitingPlayer.getMana(), 4);
        assertEquals(currentPlayer.getHero().getHealth(), 29);
        assertEquals(waitingPlayer.getHero().getHealth(), 29);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), health0 - 1);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 6);
        assertEquals(currentPlayer.getCharacter(3).getHealth(), health1 - 1 - 1);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), health0 - 1);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), health1 - 1 - 1);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), attack0);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 6);
        assertEquals(currentPlayer.getCharacter(3).getTotalAttack(), attack0);
    }
}
