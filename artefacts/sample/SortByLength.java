package uk.kludje.test.sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static uk.kludje.fn.util.UComparator.asUComparator;

public class SortByLength {

  public <P extends Path, L extends List<P>> L sortByLen(L list) throws IOException {
    Collections.sort(list, asUComparator((Path a, Path b) -> {
      return ((Long) Files.size(a)).compareTo(Files.size(b));
    }));
    return list;
  }
}
