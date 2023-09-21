package i.malding.hard.maldingreactors;

import i.malding.hard.maldingreactors.content.MaldingBlockEntities;
import i.malding.hard.maldingreactors.content.MaldingBlocks;
import i.malding.hard.maldingreactors.content.MaldingFluids;
import i.malding.hard.maldingreactors.content.MaldingItems;
import i.malding.hard.maldingreactors.content.handlers.MaldingScreenHandlers;
import i.malding.hard.maldingreactors.data.MediumLinksLoader;
import i.malding.hard.maldingreactors.data.MediumPropertiesLoader;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.network.OwoNetChannel;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;
import team.reborn.energy.api.EnergyStorage;

import java.util.Locale;

public class MaldingReactors implements ModInitializer {

    public static final String MOD_ID = "malding-reactors";

    public static final OwoItemGroup GROUP = OwoItemGroup.builder(new Identifier(MOD_ID, "main"), () -> Icon.of(new ItemStack(MaldingBlocks.REACTOR_CASING)))
            .initializer(group -> {
                group.addTab(Icon.of(MaldingBlocks.COPIUM_ORE), "reactor_parts", null, true);
                group.addTab(Icon.of(MaldingItems.COPIUM_INGOT), "resources", null, false);
            }).build();

    public static final OwoNetChannel CHANNEL = OwoNetChannel.create(id("main"));

    @Override
    public void onInitialize() {
        FieldRegistrationHandler.register(MaldingBlocks.class, MaldingReactors.MOD_ID, false);
        FieldRegistrationHandler.register(MaldingBlockEntities.class, MaldingReactors.MOD_ID, false);
        FieldRegistrationHandler.register(MaldingItems.class, MaldingReactors.MOD_ID, false);
        FieldRegistrationHandler.register(MaldingScreenHandlers.class, MaldingReactors.MOD_ID, false);
        FieldRegistrationHandler.processSimple(MaldingFluids.class, false);

        //FieldRegistrationHandler.processSimple(MaldingFeatures.class, false);
        GROUP.initialize();

        registerNetworkPackets();
        registerEnergyStuff();

        var helper = ResourceManagerHelper.get(ResourceType.SERVER_DATA);

        helper.registerReloadListener(new MediumPropertiesLoader());
        helper.registerReloadListener(new MediumLinksLoader());

        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {

        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {

        });
    }

    public void registerEnergyStuff() {
        EnergyStorage.SIDED.registerSelf(MaldingBlockEntities.REACTOR_POWER_PORT);
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path.toLowerCase(Locale.ROOT).replace(' ', '_'));
    }

    public static void registerNetworkPackets() {
        //MAIN.registerClientbound(MultiBlockUpdatePacket.class, MultiBlockUpdatePacket::setControllersMultipartState);
    }
}
