package li.gerard.worldchords.capability;

import li.gerard.worldchords.WorldChords;
import li.gerard.worldchords.block.entity.ModBlockEntities;
import li.gerard.worldchords.block.entity.SculkForceMachineBlockEntity;

import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import org.jspecify.annotations.Nullable;

@EventBusSubscriber(modid = WorldChords.MODID)
public final class ModCapabilities {

    /**
     * Sculk force is an energy-like resource. It reuses NeoForge's {@link EnergyHandler}
     * interface (transactions, sided views, {@code EnergyHandlerUtil.move} all work)
     */
    public static final class SculkForce {
        public static final BlockCapability<EnergyHandler, @Nullable Direction> BLOCK = BlockCapability.createSided(
                Identifier.fromNamespaceAndPath(WorldChords.MODID, "sculk_force_handler"), EnergyHandler.class);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(SculkForce.BLOCK, ModBlockEntities.SCULK_ALTAR.get(),
                SculkForceMachineBlockEntity::getSculkForceHandler);
        event.registerBlockEntity(SculkForce.BLOCK, ModBlockEntities.SCULK_DEVOURER.get(),
                SculkForceMachineBlockEntity::getSculkForceHandler);
    }
}
