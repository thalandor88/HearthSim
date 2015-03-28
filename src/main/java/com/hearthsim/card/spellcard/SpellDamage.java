package com.hearthsim.card.spellcard;

import com.hearthsim.card.Deck;
import com.hearthsim.card.ImplementedCardList;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.event.effect.SpellEffectCharacterDamage;
import com.hearthsim.event.MinionFilter;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.json.JSONObject;

public class SpellDamage extends SpellCard {

    protected byte damage_;

    protected SpellEffectCharacterDamage effect;

    public SpellDamage() {
        super();
    }

    // damage is set during card import so we need to lazy load this for each card
    @Override
    protected SpellEffectCharacterDamage getEffect() {
        if (this.effect == null) {
            this.effect = new SpellEffectCharacterDamage(damage_);
        }
        return this.effect;
    }

    @Deprecated
    public SpellDamage(byte baseManaCost, byte damage, boolean hasBeenUsed) {
        super(baseManaCost, hasBeenUsed);
        damage_ = damage;
    }

    @Override
    public void initFromImplementedCard(ImplementedCardList.ImplementedCard implementedCard) {
        super.initFromImplementedCard(implementedCard);

        this.damage_ = (byte) implementedCard.spellEffect;
    }

    public byte getAttack() {
        return damage_;
    }

    @Override
    public boolean equals(Object other) {

        if (!super.equals(other)) {
            return false;
        }

        if (this.damage_ != ((SpellDamage)other).damage_) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + damage_;
        return result;
    }

    /**
     * Attack using this spell
     *
     * @param targetMinionPlayerSide
     * @param targetMinion The target minion
     * @param boardState The BoardState before this card has performed its action. It will be manipulated and returned.
     * @return The boardState is manipulated and returned
     */
    @Deprecated
    public final HearthTreeNode attack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode boardState) throws HSException {
        return this.getEffect().applyEffect(PlayerSide.CURRENT_PLAYER, this, targetMinionPlayerSide, targetMinion, boardState);
    }

    @Deprecated
    public final HearthTreeNode attack(PlayerSide targetMinionPlayerSide, Minion targetMinion, HearthTreeNode boardState,
                                 Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        return this.attack(targetMinionPlayerSide, targetMinion, boardState);
    }

    public HearthTreeNode effectAllUsingFilter(MinionFilter filter, HearthTreeNode boardState) throws HSException {
        if (boardState != null && filter != null) {
            for (BoardModel.CharacterLocation location : boardState.data_) {
                Minion character = boardState.data_.getCharacter(location);
                if(filter.targetMatches(PlayerSide.CURRENT_PLAYER, this, location.getPlayerSide(), character, boardState.data_)) {
                    boardState = this.attack(location.getPlayerSide(), character, boardState);
                }
            }
        }
        return boardState;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("type", "SpellDamage");
        return json;
    }
}
