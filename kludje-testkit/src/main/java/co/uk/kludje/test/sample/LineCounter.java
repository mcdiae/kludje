package co.uk.kludje.test.sample;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

public interface LineCounter {
  public Map<Path, Long> countLines(Collection<? extends Path> paths) throws IOException;
}
