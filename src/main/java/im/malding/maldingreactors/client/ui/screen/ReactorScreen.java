package im.malding.maldingreactors.client.ui.screen;

import im.malding.maldingreactors.content.handlers.ReactorScreenHandler;
import im.malding.maldingreactors.content.reactor.ReactorControllerBlockEntity;
import im.malding.maldingreactors.network.MaldingReactorNetworking;
import im.malding.maldingreactors.network.MaldingReactorPackets;
import io.wispforest.owo.ui.base.BaseOwoHandledScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import io.wispforest.owo.ui.util.ScissorStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class ReactorScreen extends BaseOwoHandledScreen<FlowLayout, ReactorScreenHandler> {

    private boolean isReactorActive;

    public static final Surface ON_OUTLINE = Surface.outline(Color.GREEN.argb());
    public static final Surface OFF_OUTLINE = Surface.outline(Color.RED.argb());

    public ReactorScreen(ReactorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, Text.empty());

        this.playerInventoryTitleY = 69420;
        this.isReactorActive = handler.reactorControllerBlockEntity.isReactorActive();
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.child(
                Containers.verticalFlow(Sizing.content(), Sizing.content())
                        .child(
                                Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                        .child(
                                                Components.button(Text.of("Power"), component -> {
                                                            MaldingReactorNetworking.CHANNEL.clientHandle().send(
                                                                    new MaldingReactorPackets.ReactorToggle(handler.reactorControllerBlockEntity)
                                                            );
                                                        }).verticalSizing(Sizing.fixed(19))
                                                        .id("power_button")
                                        ).child(
                                                Containers.horizontalFlow(Sizing.fixed(30), Sizing.content())
                                                        .child(
                                                                Components.label(Text.of("Off"))
                                                                        .margins(Insets.vertical(5))
                                                                        .id("power_label")
                                                        )
                                                        .horizontalAlignment(HorizontalAlignment.CENTER)
                                                        .surface((context, component) -> {
                                                            ((isReactorActive) ? ON_OUTLINE : OFF_OUTLINE)
                                                                    .draw(context, component);
                                                        })
                                        ).gap(3)
                        )
                        .child(
                                createTankLabel("Fuel",
                                        be -> be.getFuelTank().getAmount(),
                                        be -> be.getFuelTank().getCapacity()
                                )
                        )
                        .child(
                                createTankLabel("Waste",
                                        be -> be.getWasteTank().getAmount(),
                                        be -> be.getWasteTank().getCapacity()
                                )
                        )
                        .child(
                                createTankLabel("Energy",
                                        be -> be.getEnergyStorage().getAmount(),
                                        be -> be.getEnergyStorage().getCapacity()
                                )
                        )
                        .gap(5)
                        .positioning(Positioning.relative(50, 50))
                        .surface(Surface.PANEL)
                        .padding(Insets.of(6))
        );
    }

    private final Set<Consumer<FlowLayout>> onTickHandlers = new HashSet<>();

    @Override
    protected void handledScreenTick() {
        var rootComponent = uiAdapter.rootComponent;

        onTickHandlers.forEach(consumer -> consumer.accept(rootComponent));

        if(isReactorActive != handler.reactorControllerBlockEntity.isReactorActive()){
            isReactorActive = handler.reactorControllerBlockEntity.isReactorActive();

            var label = rootComponent.childById(LabelComponent.class, "power_label");

            label.text(Text.of(isReactorActive ? "On" : "Off"));
        }
    }

    public Component createTankLabel(String name, Function<ReactorControllerBlockEntity, Long> amountGetter, Function<ReactorControllerBlockEntity, Long> capacityGetter){
        onTickHandlers.add((rootComponent) -> {
            rootComponent.childById(LabelComponent.class, name.toLowerCase() + "_label")
                    .text(Text.of(String.valueOf(amountGetter.apply(handler.reactorControllerBlockEntity))));
        });

        return Containers.horizontalFlow(Sizing.fixed(100), Sizing.content())
                .child(
                        Components.label(Text.of(name + ": "))
                ).child(
                        Components.label(Text.of(""))
                                .id(name.toLowerCase() + "_label")
                ).surface((context, component) -> {
                    var capacity = capacityGetter.apply(handler.reactorControllerBlockEntity);
                    var amount = amountGetter.apply(handler.reactorControllerBlockEntity);

                    double percentage = amount / (double) capacity;

                    Color color;

                    if(percentage > 66){
                        color = Color.GREEN;
                    } else if(percentage > 33){
                        color = Color.ofDye(DyeColor.YELLOW);
                    } else {
                        color = Color.GREEN;
                    }

                    int offset = MathHelper.floor((component.width() - (percentage * component.width())));

                    ScissorStack.push(component.x() + offset, component.y(), component.width(), component.height(), context.getMatrixStack());

                    context.drawRectOutline(component.x(), component.y(), component.width(), component.height(), color.argb());

                    ScissorStack.pop();
                });
    }
}
