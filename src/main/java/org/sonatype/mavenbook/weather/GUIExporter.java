package org.sonatype.mavenbook.weather;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Integer;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;


//import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;


public class GUIExporter implements Weather.Exporter {
	private static GUIExporter instance;
	
	private static Main.Controller app;
	static Display display;
	static Shell shell;
	static Shell stopingShell;
	static Label stopingLabel;
	static Table table;
	static Menu menubar;
	static ArrayList <String> arr;
	static Hashtable <String, TableItem> row;
	static ArrayList <Integer> colsize;
	
	final int STATUS_COL = 7;
	
	private GUIExporter (Main.Controller controller) {
		this.app = controller;
		//display = new Display ();
	//	if (!display.isDisposed ()){
		//	display.getDefault().asyncExec(new Runnable () {
			//	public void run () {
				//	appRun ();
				//}
		//	});
		//});
	}
	
	public static synchronized GUIExporter getDefault (Main.Controller controller) {
		if (instance == null) {
			instance = new GUIExporter (controller);
			Thread UIthread = new Thread (new Runnable () {
				public void run () {
					System.out.println ("GUIExporter start.");
					/*
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					*/
					init ();
					appRun ();
				}
			});
			UIthread.start();
		}
		return instance;
	}
	
	public static void init () {
			display = new Display();
			shell = new Shell(display);
			shell.setLayout(new FillLayout());
			shell.setText("SWT Table Test");
			shell.setSize(800, 500);
			shell.addListener(SWT.Close, new Listener () {
				public void handleEvent (Event e) {
					app.stop ();
				}
			});
			/* 
			 * create menu bar
			 */
			menubar = new Menu (shell, SWT.BAR | SWT.LEFT_TO_RIGHT); 
			shell.setMenuBar(menubar);
			
			MenuItem file = new MenuItem (menubar, SWT.CASCADE);
			file.setText ("&File");
			Menu filesub = new Menu (shell, SWT.DROP_DOWN);			
			file.setMenu (filesub);
			MenuItem newWeather = new MenuItem (filesub, SWT.CASCADE);
			newWeather.setText ("&New weather");
			newWeather.addListener(SWT.Selection, new Listener () {
				public void handleEvent (Event e) {
					InputDialog dlg = new InputDialog(shell);
					dlg.open ();
				}
			});
			MenuItem close = new MenuItem (filesub, SWT.CASCADE);
			close.setText ("&Close");
			close.addListener (SWT.Selection, new Listener () {
				public void handleEvent (Event event) {
					app.stop ();
				}
			});
			/*
			Listener tableListener = new Listener() {
				public void handleEvent(Event event) {
					if (event.type == SWT.DefaultSelection) {
						System.out.println("[Table-Default] event : " + event);
					} else if (event.type == SWT.Selection) {
						System.out.println("[Table-Selection] event : " + event);
						if (event.detail == SWT.CHECK) {
							System.out.println("[Table-Selection] " + event.item
									+ " Checked");
						} else {
							System.out.println("[Table-Selection] " + event.item
									+ " Selected");
						}
					} else if (event.type == SWT.Resize) {
						System.out.println("[Column-Resize] event : " + event);
					} else if (event.type == SWT.Selection) {
						System.out.println("[Column-Selection] event : " + event);
					}
				}
			};
			*/
			table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
			table.setHeaderVisible(true);
			table.setLinesVisible(true);

			//this.table.addListener(SWT.Selection, tableListener);
			//this.table.addListener(SWT.DefaultSelection, tableListener);
			
			row = new Hashtable <String, TableItem> ();
			
			arr = new ArrayList <String> ();
			arr.add("City"); arr.add("Region"); arr.add ("Country");
			arr.add("Condition"); arr.add ("Temparature"); arr.add("Chill");
			arr.add("Humidity"); arr.add("Status");
			// create base columns
			colsize = new ArrayList ();
			colsize.add (160); colsize.add (70); colsize.add (80); 
			colsize.add(110); colsize.add(120); colsize.add(70); colsize.add (70);
			colsize.add (100);
			Iterator iter = arr.iterator();
			
			while (iter.hasNext()) {
				String str = (String) iter.next ();
				
				TableColumn col = new TableColumn (table, SWT.LEFT);
				col.setText (str);
				
			}
			
			int i = 0;
		    for (iter = colsize.iterator(); iter.hasNext ();) {
		      TableColumn col = table.getColumn(i++);
		      col.pack ();
		      col.setWidth((Integer) iter.next ());
		    }
					
		    //shell.pack ();
			
			
	}
	
	public static void appRun () {
		//System.out.println ("Dockdooooooooooooooooooooooooooooooooooooo.");
		//init ();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		app.stop ();
		display.dispose();
	}
	
	public void startExport () {
	
	}
	
	public void stop () {
		if (stopingShell != null) {
			if (!stopingShell.isDisposed()) stopingShell.dispose();
		}
		if (!shell.isDisposed())	shell.dispose ();
	}
	
	public void stoping (String message) {
		
				if (stopingShell == null) {
					stopingShell = new Shell (shell);
					stopingShell.setText("SWT Table Test");
					
					stopingLabel = new Label (stopingShell, SWT.LEFT);
					//stopingLabel.setBounds(0, 0, 100, 20);
					//stopingLabel.setText("CHILD");
					//Point p = stopingLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			        //stopingLabel.setBounds(5, 5, p.x+5, p.y+5);
			        //stopingShell.pack ();
					stopingLabel.setText("child");
					stopingLabel.setBounds(10, 0, 300, 50);
					stopingShell.setSize(350, 50);
			        //stopingShell.setLocation(300, 300);
			        //stopingShell.setLayout(new FillLayout ());
			        //stopingShell.pack ();
					stopingShell.open ();
					
				}
				if (!stopingShell.isDisposed())	{
					stopingLabel.setText(message);
					//stopingShell.layout ();
					stopingShell.update();
					//stopingShell.layout();
				}
			}
		
		
	
	
	public class reqUI implements Runnable {
		private Hashtable data;
		public reqUI (Hashtable data) {
			this.data = data;
		}
		public void run () {
			if (!table.isDisposed ()) {
				//System.out.println ((String)data.get("city"));
				TableItem item = getRow ((String)data.get("city"));
				freshRow (item, data);
				refreshStatus (item, "Complete!");
			}
		}
	}
	
	public void export (Hashtable data) {
		if (!display.isDisposed()) {
			display.asyncExec (new reqUI (data));
		}
	}
	
	public void endExport () {
		
	}
	
	public void loadingData (String city) {
		if (!table.isDisposed ()) {
			//System.out.println ((String)data.get("city"));
			TableItem item = getRow (city);
			if (!display.isDisposed()) {
				display.asyncExec (new RefreshRunnable (item, "Loading ..."));
			}
			
		}
	}
	
	public TableItem getRow (String city) {
		TableItem item = row.get(city);
		if (item == null) {
			
			item = new TableItem (this.table, SWT.LEFT);
			//System.out.println (item.toString ());
			row.put (city, item);
		} 
		return item;
	}
	
	public void freshRow (TableItem item, Hashtable data) {
		int i = 0;
		for (Iterator iter = arr.iterator(); iter.hasNext(); ) {
			String field = (String) iter.next ();
			//System.out.println (field.toLowerCase());
			if (!field.equals("Status"))
				item.setText(i++, (String) data.get (field.toLowerCase()));
		}
	}
	
	public void refreshStatus (TableItem item, String status) {
		item.setText(STATUS_COL, status);
	}
	
	public class RefreshRunnable implements Runnable {
		private TableItem item;
		private String status;
		public RefreshRunnable (TableItem item, String status) {
			this.item = item;
			this.status = status;
		}
		public void run () {
			if (!table.isDisposed ()) {
				//System.out.println ((String)data.get("city"));
				refreshStatus (item, status);
			}
		}
	}

	public static class InputDialog extends Dialog {

		// dialog 결과값
		boolean result = false;

		private String fileName = "";
		private String title = "";
		private String description = "";

		private Text fileNameText;
		private Text titleText;
		private Text descriptionText;

		
		Shell shell;
		// /////////////////////////////////////////////////////

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		// /////////////////////////////////////////////////////

		public InputDialog(Shell parent) {
			this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		}

		public InputDialog(Shell parent, int style) {
			super(parent, style);
		}

		public boolean open() {
			shell = new Shell(getParent(), getStyle());
			shell.setText(getText());
			createContents(shell);
			shell.pack();
			shell.open();
			Display display = getParent().getDisplay();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			return result;
		}

		private void createContents(final Shell shell) {

			shell.setText("Input Box");
			shell.setLayout(new GridLayout(2, true));

			// //////////////////////////////
			// fileName
			Label fileNameLabel = new Label(shell, SWT.NONE);
			fileNameLabel.setText("Zip code : ");
			fileNameLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			fileNameText = new Text(shell, SWT.BORDER);
			fileNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			((GridData) fileNameText.getLayoutData()).widthHint = 100;
/*
			// //////////////////////////////
			// title
			Label titleLabel = new Label(shell, SWT.NONE);
			titleLabel.setText("Title");
			titleLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			titleText = new Text(shell, SWT.BORDER);
			titleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			// ////////////////////////////
			// Description
			Label descriptionLabel = new Label(shell, SWT.NONE);
			descriptionLabel.setText("Description");
			descriptionLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			descriptionText = new Text(shell, SWT.BORDER);
			descriptionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			// //////////////////////////////
*/
			Button ok = new Button(shell, SWT.PUSH);
			ok.setText("OK");
			ok.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			ok.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					result = true;
					fileName = fileNameText.getText();
					app.newWeather (fileName);
					shell.close();
				}
			});

			Button cancel = new Button(shell, SWT.PUSH);
			cancel.setText("Cancel");
			cancel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			cancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					result = false;

					shell.close();
				}
			});

			shell.setDefaultButton(ok);

		}



	}
}