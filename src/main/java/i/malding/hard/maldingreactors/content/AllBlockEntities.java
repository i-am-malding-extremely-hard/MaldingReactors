package i.malding.hard.maldingreactors.content;

import i.malding.hard.maldingreactors.content.reactor.ReactorControllerBlockEntity;
import i.malding.hard.maldingreactors.content.reactor.ReactorItemPort;
import i.malding.hard.maldingreactors.content.reactor.ReactorItemPortBlockEntity;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class AllBlockEntities implements AutoRegistryContainer<BlockEntityType<?>> {

    public static BlockEntityType<ReactorControllerBlockEntity> REACTOR_CONTROLLER = FabricBlockEntityTypeBuilder.create(ReactorControllerBlockEntity::new, AllBlocks.REACTOR_CONTROLLER).build();
    public static BlockEntityType<ReactorItemPortBlockEntity> REACTOR_ITEM_PORT = FabricBlockEntityTypeBuilder.create(ReactorItemPortBlockEntity::new, AllBlocks.REACTOR_ITEM_PORT).build();

    @Override
    public Registry<BlockEntityType<?>> getRegistry() {
        return Registry.BLOCK_ENTITY_TYPE;
    }

    @Override
    public Class<BlockEntityType<?>> getTargetFieldType() {
        return (Class<BlockEntityType<?>>) (Object) BlockEntityType.class;
    }
}
