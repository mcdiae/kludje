package uk.kludje.test.sample;

import uk.kludje.fn.function.UFunction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toConcurrentMap;

/**
 * Line counter - stream approach with checked exception handling.
 */
public class LineCounter3 implements LineCounter {

  @Override
  public Map<Path, Long> countLines(Collection<? extends Path> paths) throws IOException {
    return paths.stream()
        .parallel()
        .collect(toConcurrentMap(p -> p, (UFunction<Path, Long>) this::linesIn));
  }

  private long linesIn(Path path) throws IOException {
    try (Stream<String> lines = Files.lines(path, UTF_8)) {
      return lines.count();
    }
  }
}
