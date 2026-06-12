package li.gerard.worldchords.item;

/**
 * The crafting tool shapes. Every {@link CraftingToolMaterial} gets one item per type,
 * so adding a type here auto-creates it for all declared materials.
 */
public enum CraftingToolType {
    SIPHON("siphon"),
    NEEDLE("needle"),
    RESONATOR("resonator");

    private final String id;

    CraftingToolType(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
