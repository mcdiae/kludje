/*
Copyright 2014 McDowell

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package uk.kludje.annotation.processor;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class Generator {
  private final List<Token> tokens;

  public Generator(String template) {
    tokens = tokenize(template);
  }

  private static List<Token> tokenize(String str) {
    List<Token> result = new ArrayList<>();
    Matcher matcher = Pattern.compile("\\$\\{[a-zA-Z]+\\}")
        .matcher(str);
    int last = 0;
    while (matcher.find()) {
      if (last < matcher.start()) {
        result.add(new Literal(str.substring(last, matcher.start())));
      }
      result.add(new Param(str.substring(matcher.start() + 2, matcher.end() - 1)));
      last = matcher.end();
    }
    if (last < str.length()) {
      result.add(new Literal(str.substring(last)));
    }
    return result;
  }

  public void write(Writer writer, Map<String, String> params) throws IOException {
    tokens.forEach((UConsumer<Token>) t -> t.write(writer, params));
  }

  private static interface Token {
    void write(Writer writer, Map<String, String> params) throws IOException;
  }

  private static final class Literal implements Token {
    private final String token;

    public Literal(String token) {
      this.token = token;
    }

    @Override
    public void write(Writer writer, Map<String, String> params) throws IOException {
      writer.write(token);
    }
  }

  private static final class Param implements Token {
    private final String token;

    public Param(String token) {
      this.token = token;
    }

    @Override
    public void write(Writer writer, Map<String, String> params) throws IOException {
      String value = params.get(token);
      if (value == null) {
        throw new IOException("Expected param " + token);
      }
      writer.write(value);
    }
  }
}
