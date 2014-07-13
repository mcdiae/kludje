package uk.kludje.test.sample;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toConcurrentMap;

/**
 * Line counter - stream approach.
 */
public class LineCounter2 implements LineCounter {

  @Override
  public Map<Path, Long> countLines(Collection<? extends Path> paths) throws IOException {
    try {
      return paths.stream()
          .parallel()
          .collect(toConcurrentMap(p -> p, this::linesIn));
    } catch (UncheckedIOException e) {
      throw e.getCause();
    }
  }

  private long linesIn(Path path) {
    try (Stream<String> lines = Files.lines(path, UTF_8)) {
      return lines.count();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
