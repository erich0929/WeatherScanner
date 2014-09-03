package org.sonatype.mavenbook.weather;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.io.SAXReader;

import java.util.Hashtable;

public class YahooParser {
	private static Logger log = Logger.getLogger (YahooParser.class);

	public Hashtable parse (InputStream inputStream) throws Exception {
	  //Weather weather = new Weather ();
		
	  Hashtable data = new Hashtable ();
	  log.info ("XML Reader 생성");
	  SAXReader xmlReader = createXmlReader ();
	 // BufferedReader str = new BufferedReader (new InputStreamReader (inputStream));
	 // System.out.println (str.readLine ());
	  Document doc = xmlReader.read (inputStream);
	  
	 
	  
	  log.info ("XML 응답 파싱");
	  data.put ("city", doc.valueOf ("/rss/channel/y:location/@city"));
	  data.put ("region", doc.valueOf ("/rss/channel/y:location/@region"));
	  data.put ("country", doc.valueOf ("/rss/channel/y:location/@country"));
	  data.put ("condition", doc.valueOf ("/rss/channel/item/y:condition/@text"));
	  data.put ("temparature", doc.valueOf ("/rss/channel/item/y:condition/@temp"));
	  data.put ("chill", doc.valueOf ("/rss/channel/y:wind/@chill"));
	  data.put ("humidity", doc.valueOf ("/rss/channel/y:atmosphere/@humidity"));

	  return data;
	}

	private SAXReader createXmlReader () {
		Map <String, String> uris = new HashMap <String, String> ();
	   	uris.put ("y", "http://xml.weather.yahoo.com/ns/rss/1.0");
   		DocumentFactory factory = new DocumentFactory ();
		factory.setXPathNamespaceURIs (uris);

		SAXReader xmlReader = new SAXReader ();
		xmlReader.setDocumentFactory (factory);
		return xmlReader;
	}	
}
