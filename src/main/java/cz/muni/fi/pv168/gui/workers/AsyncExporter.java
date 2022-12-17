package cz.muni.fi.pv168.gui.workers;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import javax.swing.*;

import cz.muni.fi.pv168.data.manipulation.Exporter;
import cz.muni.fi.pv168.data.manipulation.Operation;
import cz.muni.fi.pv168.data.manipulation.Progress;

/**
 * Asynchronous exporter for use with any exporter conforming
 * to the exporter interface. Aside from creating the copy of
 * current data it should be unblocking to gui.
 *
 * @author Jan Martinek, Radim Stejskal
 */
public class AsyncExporter extends Progressed implements Exporter {

    private final Exporter exporter;
    private final Runnable onError;
    private final Consumer<Integer> onFinish;

    public AsyncExporter(
        Exporter exporter,
        Runnable onError,
        Consumer<Integer> onFinish
    ) {
        this.exporter = Objects.requireNonNull(exporter, "exporter cannot be null");
        this.onError = Objects.requireNonNull(onError, "onError cannot be null");
        this.onFinish = Objects.requireNonNull(onFinish, "onFinish cannot be null");
    }

    @Override
    public Progress getProgress() {
        return exporter.getProgress();
    }

    @Override
    public void createDataCopy(List<?> source) {
        exporter.createDataCopy(source);
    }

    @Override
    public int exportData(String filePath, int... indexes) {
        var asyncWorker = new SwingWorker<Integer, Void>()
        {
            @Override
            protected Integer doInBackground() throws Exception {
                setProgress(0);
                exporter.getProgress().addProgressConsumer(this::setProgress);
                exporter.getProgress().addProgressConsumer((p, o) -> progressListener(p, o, this));
                return exporter.exportData(filePath, indexes);
            }

            @Override
            protected void done() {
                progressMonitor.close();
                exporter.getProgress().removeProgressConsumers();

                if (isCancelled()) return;
                try {
                    onFinish.accept(get());
                } catch (InterruptedException|ExecutionException e) {
                    onError.run();
                }
            }

            private void setProgress(Progress progress, Operation operation) {
                if (operation == Operation.PROCESS) this.setProgress(progress.getProcessed());
            }
        };

        showProgressBar("Running export");
        asyncWorker.execute();

        return 0;
    }
}
