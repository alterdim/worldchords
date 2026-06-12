package li.gerard.worldchords.item;

/**
 * Simple per-material items (no durability, no crafting behavior). Each type has shared
 * grayscale art tinted with the material's tier color; only {@link #EXTRACT} has an
 * additional untinted overlay layer.
 */
public enum MaterialItemType {
    DUST("dust", "powder", false),
    INGOT("ingot", "ingot", false),
    EXTRACT("extract", "extract", true),
    ESSENCE("essence", "essence", false);

    private final String id;
    private final String texture;
    private final boolean hasOverlay;

    MaterialItemType(String id, String texture, boolean hasOverlay) {
        this.id = id;
        this.texture = texture;
        this.hasOverlay = hasOverlay;
    }

    /** Item name suffix: items register as {@code <material>_<id>}. */
    public String id() {
        return id;
    }

    /**
     * Texture name in {@code textures/item/}: used as-is for single-layer types,
     * with {@code _base}/{@code _overlay} suffixes when {@link #hasOverlay()}.
     */
    public String texture() {
        return texture;
    }

    public boolean hasOverlay() {
        return hasOverlay;
    }
}
