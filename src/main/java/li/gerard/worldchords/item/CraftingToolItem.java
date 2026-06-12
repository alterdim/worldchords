package li.gerard.worldchords.item;

import li.gerard.worldchords.tier.Tier;
import li.gerard.worldchords.tier.Tiered;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.jspecify.annotations.Nullable;

/**
 * A GT-style crafting tool: used as an ingredient it stays in the crafting grid and
 * takes one point of damage per craft instead of being consumed, breaking on its
 * last use.
 */
public class CraftingToolItem extends Item implements Tiered {

    private final CraftingToolMaterial material;
    private final CraftingToolType toolType;

    public CraftingToolItem(CraftingToolMaterial material, CraftingToolType toolType, Properties properties) {
        super(properties);
        this.material = material;
        this.toolType = toolType;
    }

    public CraftingToolMaterial getMaterial() {
        return material;
    }

    public CraftingToolType getToolType() {
        return toolType;
    }

    public int getCraftingLevel() {
        return material.craftingLevel();
    }

    @Override
    public Tier getTier() {
        return material.tier();
    }

    @Override
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemInstance instance) {
        int damage = instance.getOrDefault(DataComponents.DAMAGE, 0) + 1;
        if (damage >= instance.getOrDefault(DataComponents.MAX_DAMAGE, 0)) {
            // last use: the tool breaks instead of staying in the grid
            return null;
        }
        if (instance instanceof ItemStack stack) {
            ItemStack remainder = stack.copyWithCount(1);
            remainder.set(DataComponents.DAMAGE, damage);
            return ItemStackTemplate.fromNonEmptyStack(remainder);
        }
        return new ItemStackTemplate(instance.typeHolder(), 1,
                DataComponentPatch.builder().set(DataComponents.DAMAGE, damage).build());
    }
}
