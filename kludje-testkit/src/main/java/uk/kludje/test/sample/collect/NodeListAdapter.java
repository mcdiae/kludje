/*
 * Copyright 2015 McDowell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.kludje.test.sample.collect;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import uk.kludje.experimental.collect.LambdaLists;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.List;

public class NodeListAdapter {

  public static void main(String[] args) throws Exception {
    String xml = "<foo><bar /><bar /></foo>";
    Document doc = DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .parse(new InputSource(new StringReader(xml)));
    long barCount = asList(doc.getElementsByTagName("bar")).stream()
        .count();
    System.out.println(barCount);
  }

  public static List<Node> asList(NodeList nodeList) {
    return LambdaLists.list(nodeList::item, nodeList::getLength);
  }
}
