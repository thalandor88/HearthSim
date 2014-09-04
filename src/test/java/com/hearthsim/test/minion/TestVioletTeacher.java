package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.concrete.VioletTeacher;
import com.hearthsim.card.spellcard.concrete.HolySmite;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.boardstate.BoardState;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestVioletTeacher {

	private HearthTreeNode board;
	private Deck deck;

	@Before
	public void setup() {
		board = new HearthTreeNode(new BoardState());

		Minion minion0_0 = new BoulderfistOgre();
		Minion minion0_1 = new RaidLeader();
		Minion minion1_0 = new BoulderfistOgre();
		Minion minion1_1 = new RaidLeader();
		
		board.data_.placeCard_hand_p0(minion0_0);
		board.data_.placeCard_hand_p0(minion0_1);
				
		board.data_.placeCard_hand_p1(minion1_0);
		board.data_.placeCard_hand_p1(minion1_1);

		Card cards[] = new Card[10];
		for (int index = 0; index < 10; ++index) {
			cards[index] = new TheCoin();
		}
	
		deck = new Deck(cards);

		Card fb = new VioletTeacher();
		board.data_.placeCard_hand_p0(fb);

		board.data_.setMana_p0((byte)8);
		board.data_.setMana_p1((byte)8);
		
		board.data_.setMaxMana_p0((byte)8);
		board.data_.setMaxMana_p1((byte)8);
		
		HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
		try {
			tmpBoard.data_.getCard_hand_p0(0).useOn(0, tmpBoard.data_.getHero_p0(), tmpBoard, deck, null);
			tmpBoard.data_.getCard_hand_p0(0).useOn(0, tmpBoard.data_.getHero_p0(), tmpBoard, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
		try {
			board.data_.getCard_hand_p0(0).useOn(0, board.data_.getHero_p0(), board, deck, null);
			board.data_.getCard_hand_p0(0).useOn(0, board.data_.getHero_p0(), board, deck, null);
		} catch (HSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board.data_.resetMana();
		board.data_.resetMinions();
		
	}
	
	

	@Test
	public void test0() throws HSException {
		
		//null case
		Minion target = board.data_.getCharacter(1, 0);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(1, target, board, deck, null);
		
		assertTrue(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 1);
		assertEquals(board.data_.getNumMinions_p0(), 2);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 8);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 7);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 7);

		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 7);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 7);
	}
	
	@Test
	public void test1() throws HSException {
		
		//null case
		Minion target = board.data_.getCharacter(0, 2);
		Card theCard = board.data_.getCard_hand_p0(0);
		HearthTreeNode ret = theCard.useOn(0, target, board, deck, null);
		
		assertFalse(ret == null);
		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 3);
		assertEquals(board.data_.getNumMinions_p1(), 2);
		assertEquals(board.data_.getMana_p0(), 4);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 7);
		assertEquals(board.data_.getMinion_p0(2).getHealth(), 5);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p1(1).getHealth(), 7);

		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 7);
		assertEquals(board.data_.getMinion_p0(2).getTotalAttack(), 4);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p1(1).getTotalAttack(), 7);
		
		
		
		//----------------------------------------------------------
		// Use a spell now... p0 should get a Violet Apprentice
		Card cardToUse = new HolySmite();
		board.data_.placeCard_hand_p0(cardToUse);
		target = board.data_.getCharacter(1, 1);
		ret = cardToUse.useOn(1, target, ret, deck, null);

		assertEquals(board.data_.getNumCards_hand(), 0);
		assertEquals(board.data_.getNumMinions_p0(), 4);
		assertEquals(board.data_.getNumMinions_p1(), 1);
		assertEquals(board.data_.getMana_p0(), 3);
		assertEquals(board.data_.getMana_p1(), 8);
		assertEquals(board.data_.getHero_p0().getHealth(), 30);
		assertEquals(board.data_.getHero_p1().getHealth(), 30);
		assertEquals(board.data_.getMinion_p0(0).getHealth(), 2);
		assertEquals(board.data_.getMinion_p0(1).getHealth(), 7);
		assertEquals(board.data_.getMinion_p0(2).getHealth(), 5);
		assertEquals(board.data_.getMinion_p0(3).getHealth(), 1);
		assertEquals(board.data_.getMinion_p1(0).getHealth(), 7);

		assertEquals(board.data_.getMinion_p0(0).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p0(1).getTotalAttack(), 7);
		assertEquals(board.data_.getMinion_p0(2).getTotalAttack(), 4);
		assertEquals(board.data_.getMinion_p0(3).getTotalAttack(), 2);
		assertEquals(board.data_.getMinion_p1(0).getTotalAttack(), 6);
		
		//----------------------------------------------------------
		// flipped, p1 should not get a Violet Apprentice
		HearthTreeNode flp = new HearthTreeNode(board.data_.flipPlayers());
		Card cardToUse2 = new HolySmite();
		flp.data_.placeCard_hand_p0(cardToUse2);

		assertEquals(flp.data_.getNumCards_hand(), 1);

		target = flp.data_.getCharacter(1, 1);
		ret = cardToUse2.useOn(1, target, flp, deck, null);

		assertEquals(ret.data_.getNumCards_hand(), 0);
		assertEquals(ret.data_.getNumMinions_p0(), 1);
		assertEquals(ret.data_.getNumMinions_p1(), 3);
		assertEquals(ret.data_.getMana_p0(), 7);
		assertEquals(ret.data_.getMana_p1(), 3);
		assertEquals(ret.data_.getHero_p0().getHealth(), 30);
		assertEquals(ret.data_.getHero_p1().getHealth(), 30);
		assertEquals(ret.data_.getMinion_p0(0).getHealth(), 7);
		assertEquals(ret.data_.getMinion_p1(0).getHealth(), 7);
		assertEquals(ret.data_.getMinion_p1(1).getHealth(), 5);
		assertEquals(ret.data_.getMinion_p1(2).getHealth(), 1);

		assertEquals(ret.data_.getMinion_p0(0).getTotalAttack(), 6);
		assertEquals(ret.data_.getMinion_p1(0).getTotalAttack(), 6);
		assertEquals(ret.data_.getMinion_p1(1).getTotalAttack(), 3);
		assertEquals(ret.data_.getMinion_p1(2).getTotalAttack(), 1);
		
	}

}