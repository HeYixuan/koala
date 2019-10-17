package org.igetwell.common.uitls;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class XmlUtils {
    private XmlUtils() {
    }

    public static Document parse(String xmlStr) {
        DocumentBuilderFactory dbf = getDocumentBuilderFactory();
        StringReader sr = null;

        Document var6;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            sr = new StringReader(xmlStr);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);
            var6 = document;
        } catch (ParserConfigurationException var12) {
            throw new RuntimeException(var12);
        } catch (SAXException var13) {
            throw new RuntimeException(var13);
        } catch (IOException var14) {
            throw new RuntimeException(var14);
        } finally {
            if (sr != null) {
                sr.close();
            }
        }

        return var6;
    }

    public static String elementText(Element element, String name) {
        NodeList node = element.getElementsByTagName(name);
        return node.getLength() == 0 ? null : node.item(0).getTextContent();
    }

    public static String documentText(Document doc, String name) {
        NodeList node = doc.getElementsByTagName(name);
        return node.getLength() == 0 ? null : node.item(0).getTextContent();
    }

    public static Document element(Element element, String name) {
        NodeList list = element.getElementsByTagName(name);
        return list.getLength() == 0 ? null : list.item(0).getOwnerDocument();
    }

    private static DocumentBuilderFactory getDocumentBuilderFactory() {
        return XmlUtils.DocumentBuilderFactoryHolder.INSTANCE;
    }

    private static class DocumentBuilderFactoryHolder {
        private static DocumentBuilderFactory INSTANCE = DocumentBuilderFactory.newInstance();

        private DocumentBuilderFactoryHolder() {
        }
    }
}
