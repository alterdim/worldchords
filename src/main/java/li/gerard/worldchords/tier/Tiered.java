package li.gerard.worldchords.tier;

/**
 * Implemented by blocks and items that belong to a {@link Tier}. Tiered things get the
 * alt-tooltip automatically; tiered machine blocks additionally show their RF/SF levels.
 */
public interface Tiered {

    Tier getTier();
}
