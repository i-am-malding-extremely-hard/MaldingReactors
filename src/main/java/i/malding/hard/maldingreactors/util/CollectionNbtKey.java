package i.malding.hard.maldingreactors.util;

import i.malding.hard.maldingreactors.mixins.NbtKey$TypeAccessor;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class CollectionNbtKey<T, C extends Collection<T>> extends NbtKey<NbtList> {

    private final Type<T> elementType;

    private final IntFunction<C> collectionBuilder;

    private final Function<NbtElement, T> getter;
    private final Function<T, NbtElement> setter;

    private boolean alwaysUseNewFake = false;
    private final FakeNbtCompound cachedFake = new FakeNbtCompound(null);

    public CollectionNbtKey(String key, Type<T> elementType, IntFunction<C> collectionBuilder) {
        super(key, null);

        this.elementType = elementType;
        this.collectionBuilder = collectionBuilder;

        var getter = ((NbtKey$TypeAccessor<T>) (Object) elementType).malding$getGetter();
        var setter = ((NbtKey$TypeAccessor<T>) (Object) elementType).malding$getSetter();

        this.getter = element -> getter.apply(getFakeCompound(element), "");
        this.setter = value -> {
            FakeNbtCompound fake = getFakeCompound(null);

            setter.accept(fake, "", value);

            return fake.get();
        };
    }

    public CollectionNbtKey<T, C> alwaysUseNewFake(){
        this.alwaysUseNewFake = true;

        return this;
    }

    private FakeNbtCompound getFakeCompound(@Nullable NbtElement element){
        return this.alwaysUseNewFake ? new FakeNbtCompound(element) : cachedFake;
    }

    //------

    public C getCollection(@NotNull NbtCompound nbt) {
        NbtList nbtList = get(nbt);

        C collection = collectionBuilder.apply(nbtList.size());

        for (NbtElement element : nbtList) collection.add(getter.apply(element));

        return collection;
    }

    public void putCollection(@NotNull NbtCompound nbt, C values) {
        NbtList nbtList = new NbtList();

        for(T value : values) nbtList.add(setter.apply(value));

        put(nbt, nbtList);
    }

    public Iterator<T> iterator(NbtCompound nbt){
        return new NbtListIterator<>(get(nbt), getter);
    }

    //------

    @Override
    public NbtList get(@NotNull NbtCompound nbt) {
        return nbt.getList(this.key, ((NbtKey$TypeAccessor<T>) (Object) elementType).malding$getNbtEquivalent());
    }

    @Override
    public void put(@NotNull NbtCompound nbt, NbtList value) {
        nbt.put(this.key, value);
    }

    @Override
    public boolean isIn(@NotNull NbtCompound nbt) {
        return nbt.contains(this.key, NbtElement.LIST_TYPE);
    }

    //------

    public static class NbtListIterator<T, E extends NbtElement> implements Iterator<T> {

        private final Iterator<NbtElement> listIterator;
        private final Function<E, T> getter;

        public NbtListIterator(List<NbtElement> listIterator, Function<E, T> getter){
            this.listIterator = listIterator.iterator();

            this.getter = getter;
        }

        @Override
        public boolean hasNext() {
            return listIterator.hasNext();
        }

        @Override
        public T next() {
            return this.getter.apply((E) listIterator.next());
        }
    }

    private static class FakeNbtCompound extends NbtCompound implements Supplier<NbtElement> {
        private static final Map<String, NbtElement> EMPTY = Map.of();

        public NbtElement element;

        public FakeNbtCompound(NbtElement element){
            super(EMPTY);

            this.element = element;
        }

        @Nullable
        @Override
        public NbtElement get(String key) {
            return element;
        }

        @Nullable
        @Override
        public NbtElement put(String key, NbtElement element) {
            this.element = element;

            return null;
        }

        @Override
        public NbtElement get() {
            return this.element;
        }
    }
}
