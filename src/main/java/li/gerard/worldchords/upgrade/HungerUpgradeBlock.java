package li.gerard.worldchords.upgrade;

/**
 * Each connected hunger upgrade lets the machine work one extra time per base interval
 * (a devourer with one upgrade devours twice as often, etc.).
 */
public class HungerUpgradeBlock extends UpgradeBlock {

    public HungerUpgradeBlock(Properties properties) {
        super(properties);
    }
}
