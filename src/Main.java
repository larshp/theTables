import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;

import javax.swing.*;

import com.itextpdf.text.PageSize;

import java.util.Properties;
import java.util.concurrent.CancellationException;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DestinationDataProvider;

// extra features:
// snap to grid

public class Main {

	// dictionary
	public static LinkedList<Table> tables = new LinkedList<Table>();

	// things to draw
	public static LinkedList<DrawTable> draw = new LinkedList<DrawTable>();

	// canvas + settings
	public static CanvasTables ca;
	public static int size = 10;
	public static boolean hide_mandt = true;
	public static Color background = Color.WHITE;
	public static Color foreground = Color.BLACK;
	public static Color selected = Color.RED;

	public static int offsetx = 0;
	public static int offsety = 0;
	public static double zoom = 1.0;

	// A3
	public static String renderSize = "A3";
	public static int renderWidth = (int) PageSize.A3.getHeight();
	public static int renderHeight = (int) PageSize.A3.getWidth();

	// ////////////////////////////////////////

	private static JCoDestination destination = null;

	// ////////////////////////////////////////

	public static void main(String[] args) {
		Main m = new Main();
		m.run();
	}

	public static JCoDestination getDest() throws JCoException, IOException {
		final String filename = "jco.ini";

		if (destination == null) {
			Properties prop = new Properties();
			
			// load settings from file
			FileInputStream in;
			try {
				in = new FileInputStream(filename);
				prop.load(in);
				in.close();
			} catch (Exception e) {
				// ignore if not exists or wrong format or something else happens
			}
			
			JTextField hostname = new JTextField();
			JTextField sysnr = new JTextField();
			JTextField client = new JTextField();
			JTextField username = new JTextField();
			JTextField password = new JPasswordField();

			hostname.setText(prop.getProperty(DestinationDataProvider.JCO_ASHOST));
			sysnr.setText(prop.getProperty(DestinationDataProvider.JCO_SYSNR));
			client.setText(prop.getProperty(DestinationDataProvider.JCO_CLIENT));
			username.setText(prop.getProperty(DestinationDataProvider.JCO_USER));
			
			Object[] message = { "Hostname", hostname, "Instance Number",
					sysnr, "Client", client, "Username", username, "Password",
					password };
			int result = JOptionPane.showConfirmDialog(null, message,
					"Connection Settings", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.CANCEL_OPTION) {
				throw new CancellationException();
			}

			prop.setProperty(DestinationDataProvider.JCO_ASHOST,
					hostname.getText());
			prop.setProperty(DestinationDataProvider.JCO_SYSNR, sysnr.getText());
			prop.setProperty(DestinationDataProvider.JCO_CLIENT,
					client.getText());
			prop.setProperty(DestinationDataProvider.JCO_USER,
					username.getText());
			
			// save settings to file, without password
			FileOutputStream out = new FileOutputStream(filename);
			prop.store(out, "---JCO settings---");
			out.close();
			
			prop.setProperty(DestinationDataProvider.JCO_PASSWD,
					password.getText());
			prop.setProperty(DestinationDataProvider.JCO_LANG, "en");

			MyDestinationDataProvider myProvider = new MyDestinationDataProvider();
			com.sap.conn.jco.ext.Environment
					.registerDestinationDataProvider(myProvider);
			myProvider.changePropertiesForABAP_AS(prop);

			destination = JCoDestinationManager.getDestination("SAP_SERVER");
			System.out.println(destination.getAttributes());
		}

		return destination;
	}

	public void run() {

		// ////////////////////////////////////
		// Read tables

		System.out.println("Reading Tables");

		Table t;

		t = new Table("VBAK");
		t.setDescription("Sales Document: Header Data");
		t.addField(new Field("VBELN", 1, "Domain", "Sales Document"));
		t.addField(new Field("ERDAT", 2, "Domain",
				"Date on which the record was created"));
		t.addField(new Field("ERZET", 3, "Domain", "Entry time"));
		t.addField(new Field("ERNAM", 4, "Domain",
				"Name of Person who Created the Object"));
		t.addField(new Field("ANGDT", 5, "Domain",
				"Quotation/Inquiry is valid from"));
		t.addField(new Field("BNDDT", 6, "Domain",
				"Date until which bid/quotation is binding (valid-to date)"));
		t.addField(new Field("AUDAT", 7, "Domain",
				"Document Date (Date Received/Sent)"));
		tables.add(t);

		t = new Table("VBAP");
		t.setDescription("Sales Document: Item Data");
		t.addField(new Field("VBELN", 1, "Domain", "Sales Document"));
		t.addField(new Field("POSNR", 2, "Domain", "Sales Document Item"));
		t.addField(new Field("MATNR", 3, "Domain", "Material Number"));
		t.addField(new Field("MATWA", 4, "Domain", "Material entered"));
		t.addField(new Field("PMATN", 5, "Domain", "Pricing reference material"));
		t.addField(new Field("CHARG", 6, "Domain", "Batch Number"));
		t.addField(new Field("MATKL", 7, "Domain", "Material group"));
		t.addField(new Field("ARKTX", 8, "Domain",
				"Short text for sales order item"));
		t.addField(new Field("PSTYV", 9, "Domain",
				"Sales document item category"));
		t.addField(new Field("POSAR", 10, "Domain", "Item type"));
		tables.add(t);

		t = new Table("VBPA");
		t.setDescription("Sales Document: Partner");
		t.addField(new Field("VBELN", 1, "Domain",
				"Sales and Distribution Document Number"));
		t.addField(new Field("POSNR", 2, "Domain",
				"Item number of the SD document"));
		t.addField(new Field("PARVW", 3, "Domain", "Partner function"));
		t.addField(new Field("KUNNR", 4, "Domain", "Customer Number 1"));
		t.addField(new Field("LIFNR", 5, "Domain",
				"Account Number of Vendor or Creditor"));
		t.addField(new Field("PERNR", 6, "Domain", "Personnel Number"));
		t.addField(new Field("PARNR", 7, "Domain", "Number of contact person"));
		tables.add(t);

		t = new Table("VBEP");
		t.setDescription("Sales Document: Schedule Line Data");
		t.addField(new Field("VBELN", 1, "Domain", "Sales Document"));
		t.addField(new Field("POSNR", 1, "Domain", "Sales Document Item"));
		t.addField(new Field("ETENR", 1, "Domain", "Schedule line"));
		t.addField(new Field("ETTYP", 1, "Domain", "Schedule line category"));
		t.addField(new Field("LFREL", 1, "Domain",
				"Item is relevant for delivery"));
		t.addField(new Field("EDATU", 1, "Domain", "Schedule line date"));
		tables.add(t);

		System.out.println("Done");

		// ////////////////////////////////////
		// Initialize canvas to be size of screen

		GraphicsEnvironment env = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Dimension size = env.getMaximumWindowBounds().getSize();

		ca = new CanvasTables(size);
		JFrame frame = new JFrame("Tables");

		JPanel panel = (JPanel) frame.getContentPane();

		panel.setPreferredSize(size);
		panel.setLayout(null);
		panel.add(ca);

		// finally make the window visible
		frame.pack();
		frame.setResizable(true);
		frame.setSize(size);

		frame.setVisible(true);

		// add listeners
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		ca.addMouseListener(new MouseListenerTables());
		ca.addMouseMotionListener(new MouseListenerTables());
		ca.addKeyListener(new KeyListenerTables());

		ca.requestFocus();
	}
}
