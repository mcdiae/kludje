package uk.kludje.test.sample;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

public interface LineCounter {
  /**
   * Counts the lines in files.
   *
   * @param paths the files to process
   * @return a map of their line counts
   * @throws IOException on error
   */
  public Map<Path, Long> countLines(Collection<? extends Path> paths) throws IOException;
}
