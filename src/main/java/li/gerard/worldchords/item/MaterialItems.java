package li.gerard.worldchords.item;

import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Auto-registers a {@link MaterialItem} of every {@link MaterialItemType} for each
 * material in {@link CraftingTools#MATERIALS}, named {@code <material>_<type>}
 * (e.g. {@code murky_dust}). Models and creative tab entries follow automatically;
 * only the lang entries are manual.
 */
public final class MaterialItems {

    /** material name -> item type -> registered item, in declaration order. */
    private static final Map<String, Map<MaterialItemType, DeferredItem<MaterialItem>>> ITEMS;

    static {
        var byMaterial = new LinkedHashMap<String, Map<MaterialItemType, DeferredItem<MaterialItem>>>();
        for (CraftingToolMaterial material : CraftingTools.MATERIALS) {
            var byType = new EnumMap<MaterialItemType, DeferredItem<MaterialItem>>(MaterialItemType.class);
            for (MaterialItemType type : MaterialItemType.values()) {
                byType.put(type, ModItems.ITEMS.registerItem(
                        material.name() + "_" + type.id(),
                        properties -> new MaterialItem(material, properties)));
            }
            byMaterial.put(material.name(), Collections.unmodifiableMap(byType));
        }
        ITEMS = Collections.unmodifiableMap(byMaterial);
    }

    private MaterialItems() {}

    public static DeferredItem<MaterialItem> get(CraftingToolMaterial material, MaterialItemType type) {
        return ITEMS.get(material.name()).get(type);
    }

    public static DeferredItem<MaterialItem> get(String materialName, MaterialItemType type) {
        return ITEMS.get(materialName).get(type);
    }

    public static List<DeferredItem<MaterialItem>> all() {
        return ITEMS.values().stream().flatMap(byType -> byType.values().stream()).toList();
    }

    /** Forces class load so the static registrations run before the registry event. */
    public static void register() {}
}
