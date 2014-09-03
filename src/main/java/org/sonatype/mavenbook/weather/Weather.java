package org.sonatype.mavenbook.weather;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Weather {

	
	public interface Importer {
		public void startImport (String zip);
		public Hashtable loadData ();
		public void endImport ();
	}
	
	public interface Exporter {
		public void startExport ();
		public void export (Hashtable data);
		public void endExport ();
		public void loadingData (String city);
		public void stoping (String message);
	}
	
	private Hashtable data;
	private String zip;
	private ArrayList <Exporter> observer;
	private Importer importer;
	private boolean loading = true;
	private boolean loopflag = true; 
	private Thread loopThread;
	private static Hashtable <String, Weather> friends; 
	
	/*
	 
	private String city;
	private String region;
	private String country;
	private String condition;
	private String temp;
	private String chill;
	private String humadity;

	*/
	
	private Weather (String zip, Importer importer) {
		this.data = new Hashtable ();
		this.zip = zip;
		this.observer = new ArrayList <Exporter> ();
		this.importer = importer;
	}

	public static synchronized Weather getWeather (String zip, Importer importer) {
		if (friends == null) friends = new Hashtable <String, Weather> ();
		Weather weather = friends.get(zip);
		if (weather == null) {
			System.out.println ("creating : " + zip);
			weather = new Weather (zip, importer);
			friends.put(zip, weather);
		}
		return weather;
	}
	
	public void resisgerImporter (Importer importer) {
		this.importer = importer;
	}
	
	public void registExporter (Exporter exporter) {
		observer.add(exporter);
	}
	
	public void removeExporter (Exporter exporter) {
		observer.remove (exporter);
	}
	
	public void updateloop () {
		loopflag = true;
		if (loopThread == null) {
			loopThread = new Thread (new Runnable () {
				public void run () {
					while (loopflag) {
						try {
							update ();
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace ();
						}
					}
				}
			});
			loopThread.start ();
		}
	}
	
	public void stoploop () {
		loopflag = false;
	}
	
	public synchronized static void stopAll () {
		if (friends != null) {
		Enumeration <String> en = friends.keys ();
		while (en.hasMoreElements()) {
			Weather item = friends.get(en.nextElement());
			try {
				Iterator iter = item.observer.iterator();
				while (iter.hasNext ()) {
					Exporter exporter = (Exporter) iter.next();
					exporter.stoping ("Disconnecting (" + (String) item.data.get("city") + ") ...");
				}
				item.loopflag = false;
				item.loopThread.join ();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		}
	}
	
	public void update () {
		if (loading) {
			updateExporter ();
			if (updateImporter ()) update ();
		} else {
			updateExporter ();
			loading = true;
		}
	
		/*
		if (updateImporter ()) {
			updateExporter ();
		}
		*/
	}
	
	public class ExportRunnable implements Runnable {
		protected Exporter exporter;
		
		public ExportRunnable (Exporter exporter) {
			this.exporter = exporter;
		}
		
		public void run () {
			exportData(exporter);
		}
	}
	
	public class LoadingRunnable extends ExportRunnable {
		public LoadingRunnable (Exporter exporter) {
			super (exporter);
		}
		public void run () {
			loadingData (exporter);
		}
	}
	
	public void loadingData (Exporter exporter) {
		String city = (String) data.get("city");
		if (city != null) {
			exporter.loadingData (city);
		}
	}
	public void updateExporter () {
		Iterator <Exporter> iter;
		Thread thread;
		if (!this.observer.isEmpty()) {
				iter = this.observer.iterator ();
				for (;iter.hasNext ();) {
					if (!loading) {
						thread = new Thread (new ExportRunnable (iter.next ())); 
						thread.start ();
					} else {
						thread = new Thread (new LoadingRunnable (iter.next ())); 
						thread.start ();
					}
				}
		} else {
			System.out.println ("There's no export object. please register your export objects!!");
		}
	}
	
	public boolean updateImporter () {
		loading = !importData (this.importer);
		return !loading;
	}
	
	public boolean importData (Importer importer) {
		importer.startImport (zip);
		Hashtable lodata = importer.loadData ();
		importer.endImport ();
		
		if (lodata.get("city") != "" && lodata.get("region") != null &&
			lodata.get ("condition") != null && lodata.get ("temparature") != null && 
			lodata.get("chill") != "" && lodata.get("humidity") != null) {
			//System.out.println ("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + lodata.get ("city"));
			this.data = lodata;
			//System.out.println (data.get("city"));
			return true;
			
		} else {
			
			return false;
			
		}
	}
	
	public void exportData (Exporter exporter) {
		exporter.startExport();
		exporter.export ((Hashtable) this.data);
		exporter.endExport ();
		
	}
	
}
