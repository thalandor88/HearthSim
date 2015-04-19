package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.basic.minion.RaidLeader;
import com.hearthsim.card.classic.minion.common.ScarletCrusader;
import com.hearthsim.card.curseofnaxxramas.minion.common.UnstableGhoul;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestUnstableGhoul {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new ScarletCrusader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());

        Card fb = new UnstableGhoul();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 8);
        waitingPlayer.setMana((byte) 8);
    }

    @Test
    public void test0() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board);

        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 8);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 1);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(3).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(3).getTotalAttack(), 7);

        assertTrue(waitingPlayer.getCharacter(1).getDivineShield());
    }

    @Test
    public void test1() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 3);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(3).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 1);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(3).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(3).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(3).getTotalAttack(), 7);

        assertTrue(waitingPlayer.getCharacter(1).getDivineShield());
    }

    @Test
    public void test2() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 3);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(3).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 1);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(3).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(3).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(3).getTotalAttack(), 7);

        assertTrue(waitingPlayer.getCharacter(1).getDivineShield());

        //attack the Ogre... deal 1 damage to all
        Minion attacker = currentPlayer.getCharacter(1);
        attacker.hasAttacked(false);
        ret = attacker.attack(PlayerSide.WAITING_PLAYER, 3, ret, false);

        assertFalse(ret == null);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 1);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 6);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 1);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 1);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 1);
        assertEquals(waitingPlayer.getCharacter(3).getHealth(), 4);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(3).getTotalAttack(), 7);

        assertFalse(waitingPlayer.getCharacter(1).getDivineShield());
    }

    @Test
    public void test3() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 3);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(3).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 1);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(3).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(3).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(3).getTotalAttack(), 7);

        assertTrue(waitingPlayer.getCharacter(1).getDivineShield());

        //Silence the Unstable Ghoul first, then attack with it
        Minion attacker = currentPlayer.getCharacter(1);
        attacker.silenced(PlayerSide.CURRENT_PLAYER, board);
        attacker.hasAttacked(false);
        attacker.attack(PlayerSide.WAITING_PLAYER, 3, ret, false);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 3);
        assertEquals(currentPlayer.getMana(), 6);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 1);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(3).getHealth(), 5);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(3).getTotalAttack(), 7);

        assertTrue(waitingPlayer.getCharacter(1).getDivineShield());
    }
}
