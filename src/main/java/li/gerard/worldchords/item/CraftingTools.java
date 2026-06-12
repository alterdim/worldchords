package li.gerard.worldchords.item;

import li.gerard.worldchords.tier.Tier;

import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Declares the crafting tool materials and auto-registers every material/type
 * combination as {@code <material>_<type>} (e.g. {@code murky_siphon}). To add a
 * material, add a line to {@link #MATERIALS}; models and creative tab entries follow
 * automatically, only the texture and lang entries are manual.
 */
public final class CraftingTools {

    public static final List<CraftingToolMaterial> MATERIALS = List.of(
            new CraftingToolMaterial("murky", Tier.TIER_1, 0x40875A, 16, 1),
            new CraftingToolMaterial("berserkium", Tier.TIER_1, 0xC92241, 8, 1),
            new CraftingToolMaterial("sculk", Tier.TIER_2, 0x2BD6C8, 32, 2),
            new CraftingToolMaterial("deep", Tier.TIER_3, 0x5C7CFF, 64, 3),
            new CraftingToolMaterial("zenithian", Tier.TIER_4, 0xFFD24D, 128, 4));

    /** material name -> tool type -> registered item, in declaration order. */
    private static final Map<String, Map<CraftingToolType, DeferredItem<CraftingToolItem>>> TOOLS;

    static {
        var byMaterial = new LinkedHashMap<String, Map<CraftingToolType, DeferredItem<CraftingToolItem>>>();
        for (CraftingToolMaterial material : MATERIALS) {
            var byType = new EnumMap<CraftingToolType, DeferredItem<CraftingToolItem>>(CraftingToolType.class);
            for (CraftingToolType type : CraftingToolType.values()) {
                byType.put(type, ModItems.ITEMS.registerItem(
                        material.name() + "_" + type.id(),
                        properties -> new CraftingToolItem(material, type, properties),
                        properties -> properties.durability(material.durability())));
            }
            byMaterial.put(material.name(), Collections.unmodifiableMap(byType));
        }
        TOOLS = Collections.unmodifiableMap(byMaterial);
    }

    private CraftingTools() {}

    public static DeferredItem<CraftingToolItem> get(CraftingToolMaterial material, CraftingToolType type) {
        return TOOLS.get(material.name()).get(type);
    }

    public static List<DeferredItem<CraftingToolItem>> all() {
        return TOOLS.values().stream().flatMap(byType -> byType.values().stream()).toList();
    }

    /** All tools of the given type whose crafting level is at least {@code level}, in declaration order. */
    public static List<DeferredItem<CraftingToolItem>> withLevelAtLeast(CraftingToolType type, int level) {
        return MATERIALS.stream()
                .filter(material -> material.craftingLevel() >= level)
                .map(material -> get(material, type))
                .toList();
    }

    /** Forces class load so the static registrations run before the registry event. */
    public static void register() {}
}
