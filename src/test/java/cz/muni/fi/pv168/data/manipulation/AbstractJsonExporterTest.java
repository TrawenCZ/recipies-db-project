package cz.muni.fi.pv168.data.manipulation;

import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class  AbstractJsonExporterTest<T>{
    @TempDir
    private static Path testDir;
    private final Path exportFilePath = testDir.resolve(Instant.now().toString().replace(':', '_'));
    private final JsonExporterImpl exporter = new JsonExporterImpl();

    protected void assertExportedContent(String expectedContent) throws IOException {
        assertThat(Files.readString(exportFilePath))
                .isEqualToIgnoringWhitespace(expectedContent);
    }

    protected void testDirSave(List<T> values) throws IOException {
        exporter.saveEntities(exportFilePath.toString(), values);
    }
}
