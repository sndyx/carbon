package moe.sndy.carbon.data.items;

public class ItemConstants {

    public static final String STAT_TYPES = "damage-all|damage-fire|damage-ice|damage-bleed|damage-steal|damage-ultimate|defense-all|defense-fire|defense-ice|defense-bleed|defense-gilded|defense-ultimate|skill-stength|skill-agility|skill-wisdom|stat-health|stat-mana|requirement-strength|requirement-agility|requirement-wisdom";
    public static final String CONSUMABLE_TYPES = "boost-health|boost-mana|effect-hpincrease|effect-manaincrease"; // TODO: Make all consumable effects

    public static char getRarityFormatting(String rarity) {
        switch (rarity) {
            case "common":
                return '7';
            case "uncommon":
                return 'a';
            case "rare":
                return '9';
            case "epic":
                return 'c';
            case "legendary":
                return 'e';
            case "divine":
                return 'd';
            default:
                return '7';
        }
    }

}
