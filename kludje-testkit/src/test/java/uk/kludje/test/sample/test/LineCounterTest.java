package uk.kludje.test.sample.test;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import uk.kludje.test.sample.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.google.common.jimfs.Jimfs;

@RunWith(Parameterized.class)
public class LineCounterTest {

  private LineCounter counter;
  private FileSystem fs;

  public LineCounterTest(LineCounter counter) {
    this.counter = counter;
  }

  @Parameterized.Parameters
  public static Collection<Object[]> params() {
    Object[][] params = {
        {new LineCounter1()},
        {new LineCounter2()},
        {new LineCounter3()},
        {new LineCounter4()}
    };
    return Arrays.asList(params);
  }

  @Before
  public void init() {
    fs = Jimfs.newFileSystem();
  }

  private Map<Path, Long> makeFiles() throws IOException {
    Map<Path, Long> paths = new HashMap<>();
    for(long i =0; i<100000; i+= 1000) {
      Path p = fs.getPath("foo" + i);
      paths.put(p, i);
      write(p, "hello", i);
    }
    return paths;
  }

  @Test
  public void testCounter() throws IOException {
    Map<Path, Long> expected = makeFiles();
    Map<Path, Long> result = counter.countLines(expected.keySet());

    Assert.assertEquals(expected, result);
  }

  @Test(expected = IOException.class)
  public void testDoesNotExist() throws IOException {
    Map<Path, Long> map = makeFiles();
    map.put(fs.getPath("does not exist.txt"), Long.MAX_VALUE);
    counter.countLines(map.keySet());
  }

  private void write(Path p, String data, long times) throws IOException {
    Files.write(p, list(data, times), StandardCharsets.UTF_8);
  }

  private List<String> list(String data, long times) {
    class Repeater extends AbstractList<String> {

      @Override
      public String get(int index) {
        return data;
      }

      @Override
      public int size() {
        return (int) times;
      }
    }

    return new Repeater();
  }

}
