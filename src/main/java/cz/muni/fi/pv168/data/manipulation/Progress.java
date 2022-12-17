package cz.muni.fi.pv168.data.manipulation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Progress tracking class that provides access to decision tracking and
 * notifications for various progress consumers on data updates.
 *
 * @author Jan Martinek, Radim Stejskal
 */
public class Progress {

    private final List<BiConsumer<Progress, Operation>> consumers = new ArrayList<>();

    private boolean doReplace = false;
    private boolean doIgnore = false;
    private boolean isDone = false;

    private int workload = 0;
    private int processed = 0;

    private int insert = 0;
    private int update = 0;
    private int remove = 0;
    private int ignore = 0;

    /* GETTERS */
    public int getWorkload() { return workload; }

    public int getProcessed() { return processed; }

    public int getPercentageDone() { return (int) Math.round((double) processed / workload * 100); }

    public int getInsert() { return insert; }

    public int getUpdate() { return update; }

    public int getRemove() { return remove; }

    public int getIgnore() { return ignore; }

    public boolean hasChanges() { return insert > 0 || update > 0 || remove > 0; }

    public boolean ignoreDecided() { return doReplace || doIgnore; }

    /* SETTERS */
    public void setIsDone() { isDone = true; }

    public void setWorkload(int workload) { this.workload = workload; }

    public void setReplace() {
        doReplace = true;
        doIgnore = false;
    }

    public void setIgnore() {
        doReplace = false;
        doIgnore = true;
    }

    /* VERIFIERS */
    public boolean isDone() { return isDone; }

    public boolean isReplace() { return doReplace; }

    public boolean isIgnore() { return doIgnore; }

    public synchronized void submit(Operation item) {
        switch (item) {
            case INSERT:
                insert++;
                break;
            case UPDATE:
                update++;
                break;
            case REMOVE:
                remove++;
                break;
            case IGNORE:
                ignore++;
                break;
            case PROCESS:
                processed++;
                break;
            default:
                break;
        }
        consumers.forEach(c -> c.accept(this, item));
    }

    public void resetCount() {
        insert = 0;
        update = 0;
        remove = 0;
        ignore = 0;
        processed = 0;
    }

    public void resetSettings() {
        workload = 0;
        doReplace = false;
        doIgnore = false;
        isDone = false;
    }

    public void addProgressConsumer(BiConsumer<Progress, Operation> consumer) {
        this.consumers.add(consumer);
    }

    public void removeProgressConsumers() {
        this.consumers.clear();
    }
}
