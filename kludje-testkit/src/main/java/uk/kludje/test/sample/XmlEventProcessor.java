package uk.kludje.test.sample;

import uk.kludje.Res;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import java.util.function.UnaryOperator;

import static uk.kludje.Res.res;

public class XmlEventProcessor {

  private final XMLInputFactory inputFactory;
  private final XMLOutputFactory outputFactory;

  public XmlEventProcessor(XMLInputFactory inputFactory, XMLOutputFactory outputFactory) {
    this.inputFactory = inputFactory;
    this.outputFactory = outputFactory;
  }

  public void consumeEvents(Source source, Result result, UnaryOperator<XMLEvent> eventProcessor) {
    try (Res<XMLEventReader> reader = res(XMLEventReader::close, inputFactory.createXMLEventReader(source));
         Res<XMLEventWriter> writer = res(XMLEventWriter::close, outputFactory.createXMLEventWriter(result))) {

      while (reader.unwrap().hasNext()) {
        XMLEvent event = reader.unwrap().nextEvent();
        event = eventProcessor.apply(event);
        writer.unwrap().add(event);
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
