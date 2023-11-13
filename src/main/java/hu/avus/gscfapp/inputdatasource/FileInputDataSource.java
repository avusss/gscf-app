package hu.avus.gscfapp.inputdatasource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;

public class FileInputDataSource implements InputDataSource {

    private final Stream<String> lines;

    public FileInputDataSource(String inputFilePath) throws IOException {
        lines = Files.lines(Path.of(inputFilePath));
    }

    @Override
    public Iterator<String> iterator() {
        return lines.iterator();
    }
}
