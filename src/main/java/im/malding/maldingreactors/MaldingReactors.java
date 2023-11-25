package im.malding.maldingreactors;

import im.malding.maldingreactors.content.MaldingBlockEntities;
import im.malding.maldingreactors.content.MaldingBlocks;
import im.malding.maldingreactors.content.MaldingFluids;
import im.malding.maldingreactors.content.MaldingItems;
import im.malding.maldingreactors.content.handlers.MaldingScreenHandlers;
import im.malding.maldingreactors.data.MediumLinksLoader;
import im.malding.maldingreactors.data.MediumPropertiesLoader;
import im.malding.maldingreactors.network.MaldingReactorNetworking;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.network.OwoNetChannel;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
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

    @Override
    public void onInitialize() {
        MaldingReactorNetworking.init();

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
