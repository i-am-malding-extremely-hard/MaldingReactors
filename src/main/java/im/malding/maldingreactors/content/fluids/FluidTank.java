package im.malding.maldingreactors.content.fluids;

import im.malding.maldingreactors.util.TransferAPIEndecs;
import io.wispforest.owo.serialization.Endec;
import io.wispforest.owo.serialization.impl.KeyedEndec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class FluidTank extends SingleVariantStorage<FluidVariant> {

    public static final KeyedEndec<FluidVariant> FLUID_VARIANT_KEY = TransferAPIEndecs.FLUID_VARIANT_ENDEC.keyed("variant", FluidVariant.blank());
    public static final KeyedEndec<Long> AMOUNT_KEY = Endec.LONG.keyed("amount", 0L);

    public static final String DEFAULT_KEY = "tank";

    private final long capacity;

    public FluidTank(long capacity) {
        this.capacity = capacity;
    }

    @Override
    protected FluidVariant getBlankVariant() {
        return FluidVariant.blank();
    }

    @Override
    protected long getCapacity(FluidVariant variant) {
        return capacity;
    }

    public void toNbt(NbtCompound nbt, @Nullable String key) {
        NbtCompound tank = new NbtCompound();

        tank.put(FLUID_VARIANT_KEY, getResource());
        tank.put(AMOUNT_KEY, getAmount());

        nbt.put(key != null ? key : DEFAULT_KEY, tank);
    }

    public void fromNbt(NbtCompound nbt, @Nullable String key) {
        NbtCompound tank = nbt.getCompound(key != null ? key : DEFAULT_KEY);

        variant = tank.get(FLUID_VARIANT_KEY);
        amount = tank.get(AMOUNT_KEY);
    }
}
