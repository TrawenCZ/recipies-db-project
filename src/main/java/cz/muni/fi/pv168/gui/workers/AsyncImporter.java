package cz.muni.fi.pv168.gui.workers;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.*;

import cz.muni.fi.pv168.data.manipulation.Importer;
import cz.muni.fi.pv168.data.manipulation.Operation;
import cz.muni.fi.pv168.data.manipulation.Progress;

/**
 * Asynchronous importer for use with any importer conforming
 * to the importer interface.
 *
 * @author Jan Martinek, Radim Stejskal
 */
public class AsyncImporter extends Progressed implements Importer {

    private final Importer importer;
    private final Runnable onStart;
    private final BiConsumer<Throwable, String> onError;
    private final Consumer<Progress> onFinish;

    public AsyncImporter(
        Importer importer,
        Runnable onStart,
        BiConsumer<Throwable, String> onError,
        Consumer<Progress> onFinish
    ) {
        this.importer = Objects.requireNonNull(importer, "importer cannot be null");
        this.onStart = Objects.requireNonNull(onStart, "onStart cannot be null");
        this.onError = Objects.requireNonNull(onError, "onError cannot be null");
        this.onFinish = Objects.requireNonNull(onFinish, "onFinish cannot be null");
    }

    @Override
    public Progress getProgress() {
        return importer.getProgress();
    }

    @Override
    public void importData(String filePath) {
        onStart.run();

        var asyncWorker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception {
                setProgress(0);
                importer.getProgress().addProgressConsumer(this::setProgress);
                importer.getProgress().addProgressConsumer((p, o) -> progressListener(p, o, this));
                importer.importData(filePath);
                return null;
            }

            @Override
            protected void done() {
                progressMonitor.close();
                importer.getProgress().removeProgressConsumers();

                if (isCancelled()) return;
                try {
                    get(); // error check
                    onFinish.accept(importer.getProgress());
                } catch (InterruptedException|ExecutionException ex) {
                    onError.accept(ex, filePath);
                }
            }

            private void setProgress(Progress progress, Operation operation) {
                if (operation == Operation.PROCESS) this.setProgress(progress.getProcessed());
            }
        };

        showProgressBar("Running import");
        asyncWorker.execute();
    }
}
