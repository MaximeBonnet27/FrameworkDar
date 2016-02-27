package com.wasp;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.IOException;
import java.io.InputStream;

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

    //TODO XML to T, Class<T> doit est conforme pour jaxb
}
