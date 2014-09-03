package org.sonatype.mavenbook.weather;

import java.io.InputStreamReader;
import java.util.Hashtable;
import java.io.Reader;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class WeatherFormatter implements Weather.Exporter {
	private static Logger log = Logger.getLogger (WeatherFormatter.class);
	Reader reader;
	VelocityContext context;
	
	public String format (Hashtable data) throws Exception {
		log.info ("날씨 데이터 정형화");
		if (reader == null) reader = new InputStreamReader (getClass ().getClassLoader ().getResourceAsStream ("output.vm"));

		if (context == null) context = new VelocityContext ();
		/*
		context.put ("city", data.g);
		context.put ("temp", data);
		context.put ("region", data);
		context.put ("humidity", data);
		context.put ("chill", data);
		context.put ("condition", data);
		context.put ("country", data);
			*/
	
		context.put("data", data);
		StringWriter writer = new StringWriter ();
		Velocity.evaluate (context, writer, "", reader);
		return writer.toString ();
	}
	
	public void startExport () {
		
	}
	
	public synchronized void  export (Hashtable data) {
		try {
			System.out.print (new WeatherFormatter ().format (data));
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public void endExport () {
		
	}
	
	public void loadingData (String city) {
		log.info("Start to load the weather data of " + city + "...");
	}
	
	public void stoping (String message) {
		System.out.println (message);
	}
}
