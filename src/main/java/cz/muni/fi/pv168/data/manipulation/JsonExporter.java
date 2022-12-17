package cz.muni.fi.pv168.data.manipulation;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.tinylog.Logger;

/**
 * @author Jan Martinek, Radim Stejskal
 */
public class JsonExporter implements Exporter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<?> dataCopy = null;
    private Progress progress = new Progress();

    @Override
    public Progress getProgress() {
        return progress;
    }

    @Override
    public void createDataCopy(List<?> source) {
        synchronized (this) {
            dataCopy = Collections.unmodifiableList(source);
        }
    }

    @Override
    public int exportData(String filePath, int... indexes) throws IOException {
        synchronized (this) {
            progress.resetCount();
            var data = prepareData(indexes);
            dataCopy = null;

            objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(new File(filePath), data);

            for (int i = progress.getProcessed(); i < progress.getWorkload(); i++) {
                progress.submit(Operation.PROCESS);
            }

            progress.setIsDone();
            return data.size();
        }
    }

    private Collection<?> prepareData(int... indexes) {
        if (progress.isDone()) progress.resetSettings();

        if (indexes == null || indexes.length == 0) {
            progress.setWorkload(2);
            progress.submit(Operation.PROCESS);
            return dataCopy;
        }

        progress.setWorkload(2 * indexes.length);

        Collection<Object> data = new ArrayList<>(indexes.length);
        for (int index : indexes) {
            if (index < 0 || index >= dataCopy.size()) {
                Logger.warn("Could not find object with index: %d [maxIndex: %d]".formatted(index, dataCopy.size()));
                continue;
            }
            data.add(dataCopy.get(index));
            progress.submit(Operation.PROCESS);
        }

        return data;
    }
}
