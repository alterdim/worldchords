package li.gerard.worldchords.tier;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * Machine and item tiers. Each tier fixes how much RF and Sculk Force can flow in or
 * out per tick. Colors belong to materials ({@code CraftingToolMaterial}), not tiers.
 * Adding a tier is one line here plus a {@code worldchords.tier.<id>} lang entry.
 */
public enum Tier {
    TIER_1("tier_1", 64, 32),
    TIER_2("tier_2", 256, 128),
    TIER_3("tier_3", 1024, 512),
    TIER_4("tier_4", 4096, 2048);

    private final String id;
    private final int rfTransfer;
    private final int sfTransfer;

    Tier(String id, int rfTransfer, int sfTransfer) {
        this.id = id;
        this.rfTransfer = rfTransfer;
        this.sfTransfer = sfTransfer;
    }

    public String id() {
        return id;
    }

    /** Max RF per tick a machine of this tier can input or output. */
    public int rfTransfer() {
        return rfTransfer;
    }

    /** Max Sculk Force per tick a machine of this tier can input or output. */
    public int sfTransfer() {
        return sfTransfer;
    }

    /** The tier's name, uncolored; callers may style it with a material color. */
    public MutableComponent displayName() {
        return Component.translatable("worldchords.tier." + id);
    }
}
