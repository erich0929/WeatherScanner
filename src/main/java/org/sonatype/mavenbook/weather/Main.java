package org.sonatype.mavenbook.weather;

import java.io.InputStream;
import org.apache.log4j.PropertyConfigurator;

public class Main {	
	public interface Controller {
		public void start ();
		public void stop ();
		public void newWeather (String zip);
	}
	
	private String zip;
	private static Controller app;

	private Main (String zipcode) {
		this.zip = zipcode;
	}
	
	public static Controller getApp () {
		if (app == null) app = new App ();
		return app;
	}

	public static class App implements Controller {
		public App () {}
		
		public void start () {
			//데이터 추출
			try {
				/*
				Weather newyork = Weather.getWeather ("10002", new YahooRetriever ());
				Weather la = Weather.getWeather ("90001", new YahooRetriever ());
				if (newyork != null) {
					newyork.registExporter (GUIExporter.getDefault(this));
					newyork.registExporter (new WeatherFormatter ());
				}
				//la = Weather.getWeather ("90001", new YahooRetriever ());
				//System.out.println (la == newyork);
			
				la.registExporter (GUIExporter.getDefault(this));
				la.registExporter (new WeatherFormatter ());
				
					
				//Thread.sleep(3000);
				newyork.updateloop ();
				la.updateloop ();
			*/
				//Thread.sleep(10000);
				//newyork.stoploop ();
				//la.stoploop();
				//Weather.stopAll();
				//boolean flag = model.importData(new YahooRetriever ());
				//System.out.println (flag);
				//model.exportData(new WeatherFormatter ());

				//데이터 정형화 (출력)
				
				GUIExporter.getDefault(this);
			} catch (Exception e ) {e.printStackTrace ();}
		}
	
		public void stop () {
			Weather.stopAll();
			GUIExporter.getDefault(this).stop ();
		}
		
		public void newWeather (String zip) {
			Weather thingNew = Weather.getWeather(zip, new YahooRetriever ());
			thingNew.registExporter(GUIExporter.getDefault (this));
			thingNew.registExporter(new WeatherFormatter ());
			thingNew.updateloop();
		}
	}
	
	public static void main (String [] args) {
		//Log4j 설정
		PropertyConfigurator.configure (Main.class.getClassLoader ().getResource ("log4j.properties"));

		//Read thie Zip code from the Command-line (if none supplied, use 60202)
		String zipcode = "10002";
		try {
			zipcode = args [0];
		} catch (Exception e) {}

		getApp ().start ();
	}
}	
