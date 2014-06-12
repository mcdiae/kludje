KλudJe
======

> __kludge__   _n. Slang_
> 
> A system, especially a computer system, that is constituted of poorly matched elements or of elements originally intended for other applications.

KλudJe is a Java lambda API.


License
=======

[Apache 2.0](https://github.com/mcdiae/kludje/blob/master/LICENSE)


Download
========

You can consume the library from the [Central Repository](http://search.maven.org/) using the following dependency:

```xml
<dependency>
  <groupId>uk.kludje</groupId>
  <artifactId>kludje-core</artifactId>
  <version>0.1</version>
</dependency>
```


Usage
=====

Consider this interface contract that takes a collection of files and returns the number of lines in them:

```java
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
```

Here is a traditional imperative implementation:

```java
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Line counter - imperative approach. */
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
```

Let's refactor to a parallel streaming implementation:

```java
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toConcurrentMap;

/** Line counter - stream approach. */
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
```

The above code is unsatisfactory. In order to obey the interface contract _IOException_ must be wrapped and unwrapped using _UncheckedIOException_.

This problem can be mitigated using a cast to KλudJe's _UFunction_ to transparently pass the exception to the calling method:

```java
import uk.kludje.fn.function.UFunction;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toConcurrentMap;

/** Line counter - stream approach with checked exception handling. */
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
```

Alternatively, a method can be used for type inference:

```java
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toConcurrentMap;
import static uk.kludje.fn.function.UFunction.asUFunction;

/** Line counter - stream approach with checked exception handling. */
public class LineCounter4 implements LineCounter {

  @Override
  public Map<Path, Long> countLines(Collection<? extends Path> paths) throws IOException {
    return paths.stream()
        .parallel()
        .collect(toConcurrentMap(p -> p, asUFunction(this::linesIn)));
  }

  private long linesIn(Path path) throws IOException {
    try (Stream<String> lines = Files.lines(path, UTF_8)) {
      return lines.count();
    }
  }
}
```


Build Environment
=================

Software:

 - [JDK 8](https://jdk8.java.net/)
 - [Gradle](http://www.gradle.org/) (1.12 used)

To build:

```
  gradle build
```

