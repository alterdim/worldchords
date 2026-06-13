package li.gerard.worldchords.block;

import li.gerard.worldchords.item.CraftingToolMaterial;

/**
 * Declares an ore for a material: the block registers as {@code <material>_ore},
 * rendered with the shared {@code ore_base}/{@code ore_overlay} textures tinted by the
 * material's color, and generates naturally with these parameters.
 *
 * @param veinSize      max blocks per vein
 * @param veinsPerChunk placement attempts per chunk
 * @param minY          lowest Y the ore generates at (inclusive)
 * @param maxY          highest Y the ore generates at (inclusive)
 */
public record OreDeclaration(CraftingToolMaterial material, int veinSize, int veinsPerChunk, int minY, int maxY) {

    /** The ore block's registry name. */
    public String name() {
        return material.name() + "_ore";
    }
}
