package co.uk.kludje.test.sample;

import co.uk.kludje.fn.function.UFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Line counter - stream approach with checked exception handling.
 */
public class LineCounter3 implements LineCounter {

  @Override
  public Map<Path, Long> countLines(Collection<? extends Path> paths) throws IOException {
    return paths.stream()
        .collect(toMap(p -> p, (UFunction<Path, Long>) this::lineCount));
  }

  private long lineCount(Path path) throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
      return reader.lines().count();
    }
  }
}
