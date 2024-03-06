package im.malding.maldingreactors.client.ui.screen;

import im.malding.maldingreactors.content.handlers.ReactorScreenHandler;
import im.malding.maldingreactors.content.logic.HeatInformation;
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
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ReactorScreen extends BaseOwoHandledScreen<FlowLayout, ReactorScreenHandler> {

    private boolean isReactorActive;

    public static final Surface ON_OUTLINE = Surface.outline(Color.GREEN.argb());
    public static final Surface OFF_OUTLINE = Surface.outline(Color.RED.argb());

    public ReactorScreen(ReactorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, Text.empty());

        this.playerInventoryTitleY = 69420;
        this.isReactorActive = handler.reactorController.isReactorActive();
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
                                                                    new MaldingReactorPackets.ReactorToggle(handler.reactorController)
                                                            );
                                                        }).verticalSizing(Sizing.fixed(19))
                                                        .id("power_button")
                                        ).child(
                                                Containers.horizontalFlow(Sizing.fixed(30), Sizing.content())
                                                        .child(
                                                                Components.label(Text.of(isReactorActive ? "On" : "Off"))
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
                                        () -> handler.reactorController,
                                        be -> be.getFuelTank().getAmount(),
                                        be -> be.getFuelTank().getCapacity()
                                )
                        )
                        .child(
                                createTankLabel("Waste",
                                        () -> handler.reactorController,
                                        be -> be.getWasteTank().getAmount(),
                                        be -> be.getWasteTank().getCapacity()
                                )
                        )
                        .child(
                                createTankLabel("Energy",
                                        () -> handler.reactorController,
                                        be -> be.getEnergyStorage().getAmount(),
                                        be -> be.getEnergyStorage().getCapacity()
                                )
                        )
                        .child(
                                Components.label(Text.of("Heat Information"))
                                        .margins(Insets.of(3, 2, 0, 0))
                        ).child(
                                createBarLabel("Total Heat",
                                        handler.heatInfo::get,
                                        HeatInformation::totalHeat,
                                        BarRenderHelper.of(Color.BLUE)
                                )
                        )
                        .child(
                                createBarLabel("Average Heat",
                                        handler.heatInfo::get,
                                        HeatInformation::averageHeat,
                                        BarRenderHelper.of(Color.BLUE)
                                )
                        )
                        .child(
                                createBarLabel("Highest Heat",
                                        handler.heatInfo::get,
                                        HeatInformation::highestHeat,
                                        BarRenderHelper.of(Color.BLUE)
                                )
                        )
                        .gap(5)
                        .positioning(Positioning.relative(50, 50))
                        .surface(Surface.PANEL)
                        .padding(Insets.of(6))
        );

        rootComponent.surface(Surface.VANILLA_TRANSLUCENT);
    }

    private final Set<Consumer<FlowLayout>> onTickHandlers = new HashSet<>();

    @Override
    protected void handledScreenTick() {
        var rootComponent = uiAdapter.rootComponent;

        onTickHandlers.forEach(consumer -> consumer.accept(rootComponent));

        var controller = handler.reactorController;

        if(isReactorActive != controller.isReactorActive()){
            isReactorActive = controller.isReactorActive();

            var label = rootComponent.childById(LabelComponent.class, "power_label");

            label.text(Text.of(isReactorActive ? "On" : "Off"));
        }
    }

    public <T> Component createTankLabel(String name, Supplier<T> getter, Function<T, Number> amountGetter, Function<T, Number> capacityGetter){
        var barHelper = storageHigher(getter, amountGetter, capacityGetter);

        return createBarLabel(name, getter, amountGetter, barHelper);
    }

    public <T> Component createBarLabel(String name, Supplier<T> getter, Function<T, Number> amountGetter, BarRenderHelper helper){
        onTickHandlers.add((rootComponent) -> {
            rootComponent.childById(LabelComponent.class, name.toLowerCase() + "_label")
                    .text(Text.of(String.valueOf(amountGetter.apply(getter.get()))));
        });

        return Containers.horizontalFlow(Sizing.fixed(200), Sizing.content()) //100
                .child(
                        Components.label(Text.of(name + ": "))
                ).child(
                        Components.label(Text.of(""))
                                .id(name.toLowerCase() + "_label")
                ).surface((context, component) -> {
                    var bqColor = Color.ofDye(DyeColor.GRAY).argb();
                    context.drawGradientRect(component.x(), component.y(), component.width(), component.height(), bqColor, bqColor, bqColor, bqColor);

                    Pair<Color, Double> barRenderData = helper.getData();

                    int offset = MathHelper.floor((component.width() - (barRenderData.getRight() * component.width())));

                    ScissorStack.push(component.x() + 1, component.y() + 1, component.width() - offset - 2, component.height() - 2, context.getMatrixStack());

                    context.drawRectOutline(component.x() + 1, component.y() + 1, component.width() - 2, component.height() - 2, barRenderData.getLeft().argb());
                    context.drawRectOutline(component.x() + 2, component.y() + 2, component.width() - 4, component.height() - 4, barRenderData.getLeft().argb());

                    ScissorStack.pop();

                    context.drawRectOutline(component.x(), component.y(), component.width(), component.height(), Color.BLACK.argb());
                }).padding(Insets.of(4));
    }

    private <B> BarRenderHelper storageHigher(Supplier<B> getter, Function<B, Number> amountGetter, Function<B, Number> capacityGetter){
        return () -> {
            var capacity = capacityGetter.apply(getter.get()).longValue();
            var amount = amountGetter.apply(getter.get()).longValue();

            double percentage = amount / (double) capacity;

            Color color;

            var yellow = Color.ofDye(DyeColor.YELLOW);

            if(percentage > 0.66){
                var delta = (float) ((percentage - 0.66) / 0.33f);

                color = Color.GREEN.interpolate(yellow, 1.0f - Math.min(delta, 1.0f));
            } else if(percentage > 0.33){
                var delta = (float) ((percentage - 0.33) / 0.33f);

                color = yellow.interpolate(Color.RED, 1.0f - Math.min(delta, 1.0f));
            } else {
                color = Color.RED;
            }

            return new Pair<>(color, percentage);
        };
    }

    public interface BarRenderHelper {

        Pair<Color, Double> getData();

        static BarRenderHelper of(Color color){
            return () -> new Pair<>(color, 100D);
        }
    }
}
