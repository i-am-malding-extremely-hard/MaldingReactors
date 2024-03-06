package im.malding.maldingreactors.content;

import im.malding.maldingreactors.content.reactor.*;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class MaldingBlockEntities implements AutoRegistryContainer<BlockEntityType<?>> {

    public static final BlockEntityType<ReactorBaseBlockEntity> REACTOR_CASING = FabricBlockEntityTypeBuilder.create((pos, state) -> new ReactorBaseBlockEntity(MaldingBlockEntities.REACTOR_CASING, pos, state), MaldingBlocks.REACTOR_CASING).build();
    public static final BlockEntityType<ReactorBaseBlockEntity> REACTOR_GLASS = FabricBlockEntityTypeBuilder.create((pos, state) -> new ReactorBaseBlockEntity(MaldingBlockEntities.REACTOR_GLASS, pos, state), MaldingBlocks.REACTOR_GLASS).build();

    public static final BlockEntityType<ReactorControllerBlockEntity> REACTOR_CONTROLLER = FabricBlockEntityTypeBuilder.create(ReactorControllerBlockEntity::new, MaldingBlocks.REACTOR_CONTROLLER).build();

    public static final BlockEntityType<ReactorItemPortBlockEntity> REACTOR_ITEM_PORT = FabricBlockEntityTypeBuilder.create(ReactorItemPortBlockEntity::new, MaldingBlocks.REACTOR_ITEM_PORT).build();
    public static final BlockEntityType<ReactorPowerPortBlockEntity> REACTOR_POWER_PORT = FabricBlockEntityTypeBuilder.create(ReactorPowerPortBlockEntity::new, MaldingBlocks.REACTOR_POWER_PORT).build();

    public static final BlockEntityType<ReactorBaseBlockEntity> REACTOR_FUEL_ROD = FabricBlockEntityTypeBuilder.create((pos, state) -> new ReactorBaseBlockEntity(MaldingBlockEntities.REACTOR_FUEL_ROD, pos, state), MaldingBlocks.REACTOR_FUEL_ROD).build();
    public static final BlockEntityType<ReactorFuelRodControllerBlockEntity> REACTOR_FUEL_ROD_CONTROLLER = FabricBlockEntityTypeBuilder.create(ReactorFuelRodControllerBlockEntity::new, MaldingBlocks.REACTOR_FUEL_ROD_CONTROLLER).build();


    @Override
    public Registry<BlockEntityType<?>> getRegistry() {
        return Registries.BLOCK_ENTITY_TYPE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<BlockEntityType<?>> getTargetFieldType() {
        return (Class<BlockEntityType<?>>) (Object) BlockEntityType.class;
    }
}
