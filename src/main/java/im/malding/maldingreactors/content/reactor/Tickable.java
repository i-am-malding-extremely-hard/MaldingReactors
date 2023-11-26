package im.malding.maldingreactors.content.reactor;

public interface Tickable {

    default void clientTick(){}

    default void serverTick(){}
}
