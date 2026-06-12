package li.gerard.worldchords.item;

import li.gerard.worldchords.tier.Tier;
import li.gerard.worldchords.tier.Tiered;

import net.minecraft.world.item.Item;

/** A plain per-material item (dust, ingot, extract, essence) carrying its material. */
public class MaterialItem extends Item implements Tiered {

    private final CraftingToolMaterial material;

    public MaterialItem(CraftingToolMaterial material, Properties properties) {
        super(properties);
        this.material = material;
    }

    public CraftingToolMaterial getMaterial() {
        return material;
    }

    @Override
    public Tier getTier() {
        return material.tier();
    }
}
