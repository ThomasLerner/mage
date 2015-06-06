/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.abilities.effects.common;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.effects.OneShotEffect;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.filter.common.FilterControlledPermanent;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.common.TargetControlledPermanent;
import mage.util.CardUtil;

/**
 *
 * @author Plopmans
 */
public class ReturnToHandChosenControlledPermanentEffect extends OneShotEffect {
    
    private final FilterControlledPermanent filter;
    private int number;
    private int minNumber;
    
    public ReturnToHandChosenControlledPermanentEffect(FilterControlledPermanent filter) {
        this(filter, 1);
    }
    public ReturnToHandChosenControlledPermanentEffect(FilterControlledPermanent filter, int number) {
        super(Outcome.ReturnToHand);
        this.filter = filter;
        this.number = number;
        this.minNumber = number;
        this.staticText = getText();
    }
    
        public ReturnToHandChosenControlledPermanentEffect(FilterControlledPermanent filter, int minNumber, int maxNumber) {
        super(Outcome.ReturnToHand);
        this.filter = filter;
        this.number = maxNumber;
        this.minNumber = minNumber;
        this.staticText = getText();
    }

    public ReturnToHandChosenControlledPermanentEffect(final ReturnToHandChosenControlledPermanentEffect effect) {
        super(effect);
        this.filter = effect.filter;
        this.number = effect.number;
        this.minNumber = this.minNumber;
    }
    


    @Override
    public ReturnToHandChosenControlledPermanentEffect copy() {
        return new ReturnToHandChosenControlledPermanentEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player player = game.getPlayer(source.getControllerId());
        if (player != null) {
            TargetControlledPermanent target = new TargetControlledPermanent(minNumber, number, filter, true);
            if (player.choose(this.outcome, target, source.getSourceId(), game)) {
                for (UUID targetCreatureId : target.getTargets()) {
                    Permanent permanent = game.getPermanent(targetCreatureId);
                    if (permanent != null) {
                        player.moveCardToHandWithInfo(permanent, source.getSourceId(), game, Zone.BATTLEFIELD);
                    }
                }
            }
            return true;
        }
        return false;
    }

    private String getText() {
        StringBuilder sb = new StringBuilder("return ");
        if (!filter.getMessage().startsWith("another")) {
            sb.append(CardUtil.numberToText(number, "a"));
        }
        sb.append(" ").append(filter.getMessage());
        if (number > 1) {
            sb.append(" to their owner's hand");
        } else {
            sb.append(" to its owner's hand");
        }
        return sb.toString();
    }
}
