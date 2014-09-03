package org.sonatype.mavenbook.weather;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import java.util.Hashtable;

public class YahooRetriever implements Weather.Importer {
	private static Logger log = Logger.getLogger (YahooRetriever.class);
	private InputStream dataIn;
	private URLConnection conn;
	
	public InputStream retreive (String zipcode) throws Exception {
		log.info ("Retreiving Weather Date");
		String url = "http://weather.yahooapis.com/forecastrss?p=" + zipcode;
		conn = new URL (url).openConnection ();
		return conn.getInputStream ();
	}
	
	public void startImport (String zip) {
		try {
			dataIn = new YahooRetriever().retreive(zip);
		} catch (Exception e) { e.printStackTrace (); }
	}
	
	public Hashtable loadData () {
		Hashtable data = null;
		try {
			data = new YahooParser ().parse (dataIn);
			if (data == null) throw new Exception ();
		} catch (Exception e) { e.printStackTrace(); 
		} finally {
			return data;
		}
	}
	
	public void endImport () {
		
	}
}
