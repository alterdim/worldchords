package li.gerard.worldchords.tier;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * Machine and item tiers. Each tier fixes how much RF and Sculk Force can flow in or
 * out per tick, and carries a color used everywhere the tier is shown (tooltips, GUI
 * titles). Adding a tier is one line here plus a {@code worldchords.tier.<id>} lang entry.
 */
public enum Tier {
    MURKY("murky", 0x8AA45B, 64, 32),
    SCULK("sculk", 0x2BD6C8, 256, 128),
    DEEP("deep", 0x5C7CFF, 1024, 512),
    ZENITHIAN("zenithian", 0xFFD24D, 4096, 2048);

    private final String id;
    private final int color;
    private final int rfTransfer;
    private final int sfTransfer;

    Tier(String id, int color, int rfTransfer, int sfTransfer) {
        this.id = id;
        this.color = color;
        this.rfTransfer = rfTransfer;
        this.sfTransfer = sfTransfer;
    }

    public String id() {
        return id;
    }

    /** RGB color used to render this tier's name and values. */
    public int color() {
        return color;
    }

    /** Max RF per tick a machine of this tier can input or output. */
    public int rfTransfer() {
        return rfTransfer;
    }

    /** Max Sculk Force per tick a machine of this tier can input or output. */
    public int sfTransfer() {
        return sfTransfer;
    }

    /** The tier's name, already colored. */
    public MutableComponent displayName() {
        return Component.translatable("worldchords.tier." + id).withStyle(s -> s.withColor(color));
    }
}
