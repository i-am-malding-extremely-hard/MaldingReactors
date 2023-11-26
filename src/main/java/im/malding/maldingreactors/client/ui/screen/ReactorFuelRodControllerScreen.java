package im.malding.maldingreactors.client.ui.screen;

import im.malding.maldingreactors.client.MaldingTextures;
import im.malding.maldingreactors.content.handlers.ReactorFuelRodControllerScreenHandler;
import im.malding.maldingreactors.network.MaldingReactorNetworking;
import im.malding.maldingreactors.network.MaldingReactorPackets;
import io.wispforest.owo.ui.base.BaseOwoHandledScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.SliderComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class ReactorFuelRodControllerScreen extends BaseOwoHandledScreen<FlowLayout, ReactorFuelRodControllerScreenHandler> {

    private int currentRate;

    private final int originalRate;

    public ReactorFuelRodControllerScreen(ReactorFuelRodControllerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        currentRate = handler.reactorFuelRodController.reactionRate;
        originalRate = handler.reactorFuelRodController.reactionRate;

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
                                Components.label(title)
                                        .color(Color.ofArgb(4210752))
                                        .shadow(false)
                                        .margins(Insets.of(0, 0, 1, 0))
                        )
                        .child(
                                Components.discreteSlider(Sizing.fill(), 0.0, 100.0)
                                        .setFromDiscreteValue(originalRate)
                                        .snap(true)
                                        .configure((SliderComponent component) -> {
                                            //component.scrollStep(0.01);

                                            component.onChanged().subscribe(value -> {
                                                currentRate = (int) Math.round(value);
                                            });
                                        })
                        )
                        .gap(3)
                        //.horizontalAlignment(HorizontalAlignment.CENTER)
                        .surface(Surface.PANEL)
                        .padding(Insets.of(6))
                        .positioning(Positioning.relative(50, 50))
                        .zIndex(-1)
        );

        rootComponent.surface(Surface.VANILLA_TRANSLUCENT);
    }

    @Override
    public void close() {
        super.close();

        if(originalRate != currentRate){
            MaldingReactorNetworking.CHANNEL.clientHandle()
                    .send(
                            new MaldingReactorPackets.ControlRodAdjustment(
                                    handler.reactorFuelRodController.getPos(),
                                    currentRate
                            )
                    );
        }
    }
}
