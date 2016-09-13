package uk.kludje.test.sample;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class LineCounterVerbose {

  public Map<Path, Long> countLines(Collection<? extends Path> paths) throws IOException {
    try {
      ConcurrentMap<Path, Long> result = paths.stream()
          .parallel()
          .collect(Collectors.<Path, Path, Long>toConcurrentMap(p -> p, this::linesIn));
      return result;
    } catch (UncheckedIOException e) {
      throw e.getCause();
    }
  }

  private Long linesIn(Path path) {
    try (Stream<String> lines = Files.lines(path, UTF_8)) {
      return lines.count();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
