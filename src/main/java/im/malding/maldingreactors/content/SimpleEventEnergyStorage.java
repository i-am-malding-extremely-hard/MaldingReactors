package im.malding.maldingreactors.content;

import team.reborn.energy.api.base.SimpleEnergyStorage;

public class SimpleEventEnergyStorage extends SimpleEnergyStorage {

    private final Runnable onCommitAction;

    public SimpleEventEnergyStorage(long capacity, long maxInsert, long maxExtract, Runnable onCommitAction) {
        super(capacity, maxInsert, maxExtract);

        this.onCommitAction = onCommitAction;
    }

    @Override
    protected void onFinalCommit() {
        this.onCommitAction.run();
    }
}
