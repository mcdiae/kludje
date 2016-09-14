package uk.kludje.test.sample;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import java.util.function.UnaryOperator;

public class XmlEventProcessorVerbose {

  private final XMLInputFactory inputFactory;
  private final XMLOutputFactory outputFactory;

  public XmlEventProcessorVerbose(XMLInputFactory inputFactory, XMLOutputFactory outputFactory) {
    this.inputFactory = inputFactory;
    this.outputFactory = outputFactory;
  }

  public void consumeEvents(Source source, Result result, UnaryOperator<XMLEvent> consumer) {
    try {
      XMLEventReader reader = inputFactory.createXMLEventReader(source);
      try {
        XMLEventWriter writer = outputFactory.createXMLEventWriter(result);
        try {

          while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            event = consumer.apply(event);
            writer.add(event);
          }
        } finally {
          writer.close();
        }
      } finally {
        reader.close();
      }
    } catch (XMLStreamException e) {
      throw new UncheckedXMLStreamException(e);
    }
  }

  public static class UncheckedXMLStreamException extends RuntimeException {
    public UncheckedXMLStreamException(XMLStreamException e) {
      super(e);
    }
  }
}
