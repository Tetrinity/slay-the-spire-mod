package poisonermod;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModPanel;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;

import poisonermod.patches.*;
import poisonermod.relics.*;
import poisonermod.cards.*;
import poisonermod.characters.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class PoisonerMod
        implements EditCardsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber,
        EditCharactersSubscriber, PostInitializeSubscriber {
    private static final Logger logger = LogManager.getLogger(PoisonerMod.class.getName());

    private static final String MODNAME = "Poisoner Mod";
    private static final String AUTHOR = "Tetrinity";
    private static final String DESCRIPTION = "Adds The Poisoner as a playable character.";

    // =============== IMPUT TEXTURE LOCATION =================

    // Colors (RGB)
    // Character Color
    public static final Color POISONER_DARK_GREEN = CardHelper.getColor(64.0f, 100.0f, 20.0f);
        
    // Image folder name
    private static final String DEFAULT_MOD_ASSETS_FOLDER = "poisonerModResources/images";

    // Card backgrounds
    private static final String ATTACK_DEAFULT_GRAY = "512/bg_attack_default_gray.png";
    private static final String POWER_DEAFULT_GRAY = "512/bg_power_default_gray.png";
    private static final String SKILL_DEAFULT_GRAY = "512/bg_skill_default_gray.png";
    private static final String ENERGY_ORB_DEAFULT_GRAY = "512/card_default_gray_orb.png";
    private static final String CARD_ENERGY_ORB = "512/card_small_orb.png";

    private static final String ATTACK_DEAFULT_GRAY_PORTRAIT = "1024/bg_attack_default_gray.png";
    private static final String POWER_DEAFULT_GRAY_PORTRAIT = "1024/bg_power_default_gray.png";
    private static final String SKILL_DEAFULT_GRAY_PORTRAIT = "1024/bg_skill_default_gray.png";
    private static final String ENERGY_ORB_DEAFULT_GRAY_PORTRAIT = "1024/card_default_gray_orb.png";

    // Card images
    public static final String POISONER_STRIKE = "cards/Attack.png";
    public static final String DEFAULT_COMMON_SKILL = "cards/Skill.png";
    public static final String DEFAULT_COMMON_POWER = "cards/Power.png";
    public static final String DEFAULT_UNCOMMON_ATTACK = "cards/Attack.png";
    public static final String DEFAULT_UNCOMMON_SKILL = "cards/Skill.png";
    public static final String DEFAULT_UNCOMMON_POWER = "cards/Power.png";
    public static final String DEFAULT_RARE_ATTACK = "cards/Attack.png";
    public static final String DEFAULT_RARE_SKILL = "cards/Skill.png";
    public static final String DEFAULT_RARE_POWER = "cards/Power.png";

    // Power images
    public static final String COMMON_POWER = "powers/placeholder_power.png";
    public static final String UNCOMMON_POWER = "powers/placeholder_power.png";
    public static final String RARE_POWER = "powers/placeholder_power.png";

    // Relic images  
    public static final String POISON_FANG = "relics/placeholder_relic.png";
    public static final String POISON_FANG_OUTLINE = "relics/outline/placeholder_relic.png";
    
    // Character assets
    private static final String THE_DEFAULT_BUTTON = "charSelect/DefaultCharacterButton.png";
    private static final String THE_DEFAULT_PORTRAIT = "charSelect/DeafultCharacterPortraitBG.png";
    public static final String THE_DEFAULT_SHOULDER_1 = "char/defaultCharacter/shoulder.png";
    public static final String THE_DEFAULT_SHOULDER_2 = "char/defaultCharacter/shoulder2.png";
    public static final String THE_DEFAULT_CORPSE = "char/defaultCharacter/corpse.png";

    //Mod Badge
    public static final String BADGE_IMAGE = "Badge.png";

    // Animations atlas and JSON files
    public static final String THE_DEFAULT_SKELETON_ATLAS = "char/defaultCharacter/skeleton.atlas";
    public static final String THE_DEFAULT_SKELETON_JSON = "char/defaultCharacter/skeleton.json";

    // =============== /IMPUT TEXTURE LOCATION/ =================

    /**
     * Makes a full path for a resource path
     * 
     * @param resource the resource, must *NOT* have a leading "/"
     * @return the full path
     */
    public static final String makePath(String resource) {
        return DEFAULT_MOD_ASSETS_FOLDER + "/" + resource;
    }

    // =============== SUBSCRIBE, CREATE THE COLOR, INITIALIZE =================

    public PoisonerMod() {
        logger.info("Subscribing to basemod hooks");

        BaseMod.subscribe(this);

        logger.info("Done subscribing");

        logger.info("Creating color " + AbstractCardEnum.POISONER_DARK_GREEN.toString());

        BaseMod.addColor(AbstractCardEnum.POISONER_DARK_GREEN, POISONER_DARK_GREEN, POISONER_DARK_GREEN,
                POISONER_DARK_GREEN, POISONER_DARK_GREEN, POISONER_DARK_GREEN, POISONER_DARK_GREEN,
                POISONER_DARK_GREEN, makePath(ATTACK_DEAFULT_GRAY),
                makePath(SKILL_DEAFULT_GRAY), makePath(POWER_DEAFULT_GRAY),
                makePath(ENERGY_ORB_DEAFULT_GRAY), makePath(ATTACK_DEAFULT_GRAY_PORTRAIT),
                makePath(SKILL_DEAFULT_GRAY_PORTRAIT), makePath(POWER_DEAFULT_GRAY_PORTRAIT),
                makePath(ENERGY_ORB_DEAFULT_GRAY_PORTRAIT), makePath(CARD_ENERGY_ORB));

        logger.info("Done creating the color");
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        logger.info("========================= Initializing The Poisoner Mod. =========================");
        PoisonerMod defaultmod = new PoisonerMod();
        logger.info("========================= /The Poisoner Mod Initialized/ =========================");
    }

    // ============== /SUBSCRIBE, CREATE THE COLOR, INITIALIZE/ =================

    
    // =============== LOAD THE CHARACTER =================

    @Override
    public void receiveEditCharacters() {
        logger.info("begin editing characters. " + "Add " + ThePoisonerEnum.THE_POISONER.toString());

        BaseMod.addCharacter(new ThePoisoner("the Poisoner", ThePoisonerEnum.THE_POISONER),
                makePath(THE_DEFAULT_BUTTON), makePath(THE_DEFAULT_PORTRAIT), ThePoisonerEnum.THE_POISONER);

        logger.info("done editing characters");
    }

    // =============== /LOAD THE CHARACTER/ =================

    
    // =============== POST-INITIALIZE =================

    
    @Override
    public void receivePostInitialize() {

        logger.info("Load Badge Image and mod options");
        // Load the Mod Badge
        Texture badgeTexture = new Texture(makePath(BADGE_IMAGE));
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        settingsPanel.addUIElement(new ModLabel("PoisonerMod doesn't have any settings!", 400.0f, 700.0f,
                settingsPanel, (me) -> {
                }));
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        logger.info("Done loading badge Image and mod options");

       }

    // =============== / POST-INITIALIZE/ =================

    
    // ================ ADD RELICS ===================

    @Override
    public void receiveEditRelics() {
        logger.info("Add relics");

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(new PoisonFang(), AbstractCardEnum.POISONER_DARK_GREEN);

        logger.info("Done adding relics");
    }

    // ================ /ADD RELICS/ ===================

    
    
    // ================ ADD CARDS ===================

    @Override
    public void receiveEditCards() {
        logger.info("Add Variables");
        // Add the Custom Dynamic Variables
//        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        
        logger.info("Add Cards");
        // Add the cards
        BaseMod.addCard(new PoisonerStrike());

        logger.info("Making sure the cards are unlocked.");
        // Unlock the cards
        UnlockTracker.unlockCard(PoisonerStrike.ID);

        logger.info("Cards - added!");
    }

    // ================ /ADD CARDS/ ===================

    
    
    // ================ LOAD THE TEXT ===================

    @Override
    public void receiveEditStrings() {
        logger.info("Begin editing strings");

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                "poisonerModResources/localization/PoisonerMod-Card-Strings.json");

        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                "poisonerModResources/localization/DefaultMod-Power-Strings.json");

        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                "poisonerModResources/localization/PoisonerMod-Relic-Strings.json");

        logger.info("Done editing strings");
    }

    // ================ /LOAD THE TEXT/ ===================

    // ================ LOAD THE KEYWORDS ===================

    @Override
    public void receiveEditKeywords() {
        final String[] placeholder = { "keyword", "keywords" };
        BaseMod.addKeyword(placeholder, "Whenever you play a card, gain 1 dexterity this turn only.");

    }

    // ================ /LOAD THE KEYWORDS/ ===================    

    // this adds "ModName: " before the ID of any card/relic/power etc.
    // in order to avoid conflics if any other mod uses the same ID.
    public static String makeID(String idText) {
        return "ThePoisoner:" + idText;
    }

}
