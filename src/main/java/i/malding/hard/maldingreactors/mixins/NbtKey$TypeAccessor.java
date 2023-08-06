package i.malding.hard.maldingreactors.mixins;

import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.nbt.NbtCompound;
import org.apache.logging.log4j.util.TriConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.BiFunction;

@Mixin(NbtKey.Type.class)
public interface NbtKey$TypeAccessor<T> {

    @Accessor("nbtEquivalent") byte malding$getNbtEquivalent();
    @Accessor("getter") BiFunction<NbtCompound, String, T> malding$getGetter();
    @Accessor("setter") TriConsumer<NbtCompound, String, T> malding$getSetter();
}
