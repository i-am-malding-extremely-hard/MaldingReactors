package im.malding.maldingreactors.network;

import im.malding.maldingreactors.MaldingReactors;
import io.wispforest.owo.network.OwoNetChannel;

public class MaldingReactorNetworking {
    public static final OwoNetChannel CHANNEL = OwoNetChannel.create(MaldingReactors.id("main"));

    public static void init(){
        CHANNEL.registerServerbound(MaldingReactorPackets.ReactorToggle.class, MaldingReactorPackets.ReactorToggle::tryActivation);
        CHANNEL.registerServerbound(MaldingReactorPackets.ControlRodAdjustment.class, MaldingReactorPackets.ControlRodAdjustment::adjustRate);

        CHANNEL.registerServerbound(MaldingReactorPackets.ItemPortExportWaste.class, MaldingReactorPackets.ItemPortExportWaste::exportWaste);
        CHANNEL.registerServerbound(MaldingReactorPackets.ToggleItemPortExport.class, MaldingReactorPackets.ToggleItemPortExport::toggleExport);
    }
}
