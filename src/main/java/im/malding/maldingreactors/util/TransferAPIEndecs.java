package im.malding.maldingreactors.util;

import io.wispforest.owo.serialization.BuiltInEndecs;
import io.wispforest.owo.serialization.Endec;
import io.wispforest.owo.serialization.impl.AttributeEndecBuilder;
import io.wispforest.owo.serialization.impl.SerializationAttribute;
import io.wispforest.owo.serialization.impl.nbt.NbtEndec;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;

public class TransferAPIEndecs {
    public static final Endec<FluidVariant> FLUID_VARIANT_ENDEC = new AttributeEndecBuilder<>(
            NbtEndec.COMPOUND.xmap(FluidVariant::fromNbt, TransferVariant::toNbt), SerializationAttribute.SELF_DESCRIBING
    ).orElse(
            BuiltInEndecs.PACKET_BYTE_BUF.xmap(
                    FluidVariant::fromPacket,
                    fluidVariant -> {
                        var byteBuf = PacketByteBufs.create();

                        fluidVariant.toPacket(byteBuf);

                        return byteBuf;
                    }
            )
    );

}
