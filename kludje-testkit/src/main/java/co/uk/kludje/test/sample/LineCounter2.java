package co.uk.kludje.test.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Line counter - stream approach.
 */
public class LineCounter2 implements LineCounter {

  @Override
  public Map<Path, Long> countLines(Collection<? extends Path> paths) throws IOException {
    try {
      return paths.stream()
          .collect(Collectors.toMap(p -> p, this::lineCount));
    } catch (RuntimeIOException e) {
      throw (IOException) e.getCause();
    }
  }

  private long lineCount(Path path) {
    try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
      return reader.lines().count();
    } catch (IOException e) {
      throw new RuntimeIOException(e);
    }
  }

  private static class RuntimeIOException extends RuntimeException {
    RuntimeIOException(IOException e) {
      super(e);
    }
  }
}
