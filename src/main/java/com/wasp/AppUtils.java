package com.wasp;

import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.bind.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class AppUtils {

    private static Logger logger= Logger.getLogger(AppUtils.class);

    public <T> T loadXML(InputStream xml, Class<T> clazz) throws JAXBException, ParserConfigurationException, SAXException {
        JAXBContext jc = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        SAXParserFactory saxParser = SAXParserFactory.newInstance();
        saxParser.setNamespaceAware(true);
        XMLReader xmlReader = saxParser.newSAXParser().getXMLReader();
        SAXSource source = new SAXSource(xmlReader, new InputSource(xml));
        final JAXBElement<? extends T> unmarshal = unmarshaller.unmarshal(source,clazz);

        return unmarshal.getValue();
    }

    //Class<T> doit est conforme pour jaxb
    public <T> String toXml(T obj) throws JAXBException {
        StringBuilderWriter writer = new StringBuilderWriter();
        JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(obj, writer);
        return writer.toString();
    }

    public String toJSON(Object o){
        String ressult="{}";

        ObjectMapper mapper = new ObjectMapper();
        try {
            ressult=mapper.writeValueAsString(o);
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
        return ressult;
    }

    public <T> T fromJSON(String json,Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json.getBytes(),clazz);
    }

    public <T> T fromJSON(File json,Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json,clazz);
    }

    public <T> T fromJSON(URL json,Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json,clazz);
    }


}
