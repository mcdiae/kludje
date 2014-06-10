package co.uk.kludje.test.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Line counter - imperative approach.
 */
public class LineCounter1 implements LineCounter {

  @Override
  public Map<Path, Long> countLines(Collection<? extends Path> paths) throws IOException {
    Map<Path, Long> result = new HashMap<>();
    for (Path path : paths) {
      result.put(path, linesIn(path));
    }
    return result;
  }

  private long linesIn(Path path) throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
      long lines = 0L;
      while (reader.readLine() != null) {
        lines++;
      }
      return lines;
    }
  }
}
