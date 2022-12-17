package cz.muni.fi.pv168.gui.workers;

import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import org.tinylog.Logger;

import cz.muni.fi.pv168.data.manipulation.Operation;
import cz.muni.fi.pv168.data.manipulation.Progress;
import cz.muni.fi.pv168.gui.frames.MainWindow;

public abstract class Progressed {

    protected ProgressMonitor progressMonitor;

    protected void showProgressBar(String message) {
        MainWindow.getContentPane().setOpaque(true);
        progressMonitor = new ProgressMonitor(
            MainWindow.getContentPane(),
            message,
            "", 0, 100
        );

        progressMonitor.setProgress(0);
        progressMonitor.setMillisToDecideToPopup(0);
        progressMonitor.setMillisToPopup(0);
    }

    private void setProgressElseCancel(int progress, SwingWorker<?, ?> worker) {
        if (progressMonitor == null) return;

        if (progressMonitor.isCanceled() && !worker.isDone() && !worker.isCancelled()) {
            worker.cancel(true);
            Logger.debug("progressBar & worker canceled");
        }

        if (worker.isDone() || worker.isCancelled()) {
            progressMonitor.close();
            Logger.debug("progressBar closed");
        } else {
            progressMonitor.setProgress(progress);
            Logger.debug("progressBar set: " + progress);
        }
    }

    protected void progressListener(Progress progress, Operation operation, SwingWorker<?, ?> worker) {
        if (operation == Operation.PROCESS) setProgressElseCancel(progress.getPercentageDone(), worker);
    }
}
