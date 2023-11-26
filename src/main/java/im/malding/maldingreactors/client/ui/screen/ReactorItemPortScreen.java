package im.malding.maldingreactors.client.ui.screen;

import im.malding.maldingreactors.MaldingReactors;
import im.malding.maldingreactors.content.handlers.ReactorItemPortScreenHandler;
import im.malding.maldingreactors.network.MaldingReactorNetworking;
import im.malding.maldingreactors.network.MaldingReactorPackets;
import io.wispforest.owo.client.screens.SlotGenerator;
import io.wispforest.owo.ui.base.BaseOwoHandledScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import io.wispforest.owo.ui.util.ScissorStack;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReactorItemPortScreen extends BaseOwoHandledScreen<FlowLayout, ReactorItemPortScreenHandler> {

    public static final Identifier SLOT_TEXTURE = MaldingReactors.id("textures/gui/slot.png");

    public final Text playerInvText;

    public ReactorItemPortScreen(ReactorItemPortScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        playerInvText = inventory.getDisplayName();

        this.playerInventoryTitleY = 69420;
        this.titleY = 69420;
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.child(
                Containers.verticalFlow(Sizing.fixed(176), Sizing.content())
                        .child(
                                Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                        .child(
                                                Components.label(title)
                                                        .color(Color.ofArgb(4210752))
                                                        .shadow(false)
                                                        .margins(Insets.of(0, 0, 1, 0))
                                        )
                                        .child(
                                                Containers.horizontalFlow(Sizing.fill(57), Sizing.content())
                                                        .child(
                                                                Components.button(Text.of("\uD83D\uDDD1"), component -> { //🗑️
                                                                    MaldingReactorNetworking.CHANNEL.clientHandle()
                                                                            .send(new MaldingReactorPackets.ItemPortExportWaste(handler.reactorItemPort.getPos()));
                                                                }).sizing(Sizing.fixed(22), Sizing.fixed(16))
                                                                        .tooltip(Text.of("Export Waste"))
                                                                        .margins(Insets.of(1))
                                                        )
                                                        .child(
                                                                Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                                        .child(
                                                                                Components.button(Text.of("\uD83E\uDDBE⚙"), component -> { //🦾⚙
                                                                                    MaldingReactorNetworking.CHANNEL.clientHandle()
                                                                                            .send(new MaldingReactorPackets.ToggleItemPortExport(handler.reactorItemPort));
                                                                                }).sizing(Sizing.fixed(22), Sizing.fixed(16))
                                                                                        .tooltip(Text.of("Toggle Auto Waste Export"))
                                                                        ).surface(
                                                                                (context, component) -> {
                                                                                    Color color = handler.reactorItemPort.autoExportWaste
                                                                                            ? Color.GREEN
                                                                                            : Color.RED;

                                                                                    Surface.outline(color.argb())
                                                                                            .draw(context, component);
                                                                                }
                                                                        ).padding(Insets.of(1))
                                                        ).gap(3)
                                                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                                        )
                                        .gap(3)
                        )
                        .child(
                                Containers.horizontalFlow(Sizing.fill(), Sizing.content())
                                        .child(
                                                Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                        .child(Components.label(Text.of("Fuel Slot")))
                                                        .child(createSlotComponent(0))
                                                        .gap(2)
                                                        .verticalAlignment(VerticalAlignment.CENTER)
                                                        .horizontalAlignment(HorizontalAlignment.CENTER)
                                                        .surface(Surface.outline(Color.BLACK.argb()))
                                                        .padding(Insets.of(3))

                                        ).child(
                                                Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                        .child(Components.label(Text.of("Waste Slot")))
                                                        .child(createSlotComponent(1))
                                                        .gap(2)
                                                        .verticalAlignment(VerticalAlignment.CENTER)
                                                        .horizontalAlignment(HorizontalAlignment.CENTER)
                                                        .surface(Surface.outline(Color.BLACK.argb()))
                                                        .padding(Insets.of(3))
                                        ).gap(4)
                                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        ).child(
                                playerInv(2)
                        )
                        .gap(3)
                        //.horizontalAlignment(HorizontalAlignment.CENTER)
                        .surface(Surface.PANEL)
                        .padding(Insets.of(6))
                        .positioning(Positioning.relative(50, 50))
//                        .zIndex(-1)
        );

        rootComponent.surface(Surface.VANILLA_TRANSLUCENT);
    }

    private Component createSlotComponent(int index){
        return Containers.verticalFlow(Sizing.content(), Sizing.content())
                .child(
                        this.slotAsComponent(index)
                                .margins(Insets.of(1))
                )
                .surface((context, component) -> {
                    var child = component.children().get(0);

                    context.drawTexture(SLOT_TEXTURE, child.x() - 1, child.y() - 1, 0,0, 0, 18, 18, 18, 18);
                });
    }

    private FlowLayout playerInv(int startId) {
        FlowLayout layout = Containers.verticalFlow(Sizing.content(), Sizing.content());

        layout.child(
                Components.label(playerInvText)
                        .color(Color.ofArgb(4210752))
                        .shadow(false)
                        .margins(Insets.of(0, 2, 1, 0))
        );

        Surface slotSurface = (context, component) -> {
            context.recordQuads();

            component.children().forEach(child -> {
                context.drawTexture(SLOT_TEXTURE, child.x() - 1, child.y() - 1, 0, 0, 0, 18, 18, 18, 18);
            });

            context.submitQuads();
        };

        int bottomStart = startId + 27;

        //--

        var topLayout = Containers.verticalFlow(Sizing.content(), Sizing.content());

        for (int row = 0; row < 3; row++) {
            int rowStart = startId + (row * 9);
            int rowEnd = rowStart + 9;

            var topRowLayout = Containers.horizontalFlow(Sizing.content(), Sizing.content());

            for (int i = rowStart; i < rowEnd; i++) {
                topRowLayout.child(
                        this.slotAsComponent(i)
                                .margins(Insets.of(1))
                );
            }

            topLayout.child(topRowLayout.surface(slotSurface));
        }

        layout.child(topLayout);

        //--

        var bottomLayout = Containers.horizontalFlow(Sizing.content(), Sizing.content());

        for (int i = bottomStart; i < bottomStart + 9; i++) {
            bottomLayout.child(
                    this.slotAsComponent(i)
                            .margins(Insets.of(1))
            );
        }

        layout.child(
                bottomLayout
                        .surface(slotSurface)
                        .margins(Insets.top(4))
                );
                //.padding(Insets.of(3));

        //--

        return layout;
    }
}
