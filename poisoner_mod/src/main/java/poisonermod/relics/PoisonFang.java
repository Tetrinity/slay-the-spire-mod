package poisonermod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import poisonermod.PoisonerMod;

import java.util.Iterator;

public class PoisonFang extends CustomRelic {
    public static final Logger logger = LogManager.getLogger(PoisonFang.class.getName());

    // ID, images, text.
    public static final String ID = PoisonerMod.makeID("PoisonFang");
    public static final String IMG = PoisonerMod.makePath(PoisonerMod.POISON_FANG);
    public static final String OUTLINE = PoisonerMod.makePath(PoisonerMod.POISON_FANG_OUTLINE);

    public PoisonFang() {
        super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.STARTER, LandingSound.MAGICAL);
    }

    // Flash at the start of Battle.
    @Override
    public void atBattleStartPreDraw() {
        flash();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type != DamageInfo.DamageType.NORMAL) { // avoid triggering off poison, thorns, etc.
            return;
        }

        this.flash();

        // cancel out unblocked damage and apply that much poison instead
        int unblockedDamage = damageAmount - target.currentBlock;

        logger.info("Poison Fang converting " + unblockedDamage + " unblocked damage to poison.");
        target.currentHealth += unblockedDamage;
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                        target,
                        AbstractDungeon.player,
                        new PoisonPower(target, AbstractDungeon.player, unblockedDamage),
                        unblockedDamage
                )
        );
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    // Which relic to return on making a copy of this relic.
    @Override
    public AbstractRelic makeCopy() {
        return new PoisonFang();
    }
}
