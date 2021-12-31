package make_database;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MyParser {

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
        	factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING,false);
            SAXParser parser = factory.newSAXParser();
            SAXParserHandler handler = new SAXParserHandler();
            parser.parse("data/dblp.xml", handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long end = System.currentTimeMillis();
        System.out.println("Used: " + (end - start) / 1000 + " seconds");
    }
}  
