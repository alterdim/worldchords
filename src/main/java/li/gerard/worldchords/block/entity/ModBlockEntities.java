package li.gerard.worldchords.block.entity;

import li.gerard.worldchords.WorldChords;
import li.gerard.worldchords.block.ModBlocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public class ModBlockEntities {
    // Create a Deferred Register to hold BlockEntityTypes which will all be registered under the "worldchords" namespace
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, WorldChords.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SculkAltarBlockEntity>> SCULK_ALTAR = BLOCK_ENTITIES.register("sculk_altar",
            () -> new BlockEntityType<>(SculkAltarBlockEntity::new, Set.of(ModBlocks.SCULK_ALTAR_BLOCK.get())));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SculkDevourerBlockEntity>> SCULK_DEVOURER = BLOCK_ENTITIES.register("sculk_devourer",
            () -> new BlockEntityType<>(SculkDevourerBlockEntity::new, Set.of(ModBlocks.SCULK_DEVOURER.get())));

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITIES.register(modEventBus);
    }
}
