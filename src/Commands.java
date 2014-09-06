import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.CancellationException;

import javax.imageio.ImageIO;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.thoughtworks.xstream.XStream;

public class Commands {

	public static void delete() {
		int size = Main.draw.size();
		for (int i = 0; i < size; i++) {
			if (Main.draw.get(i).selected == true) {
				Main.draw.remove(i);
				break;
			}
		}
	}

	public static void getsize() {
		Console.addLine("Size: " + Main.renderSize + " (" + Main.renderWidth
				+ "x" + Main.renderHeight + ")");
	}

	public static void setsize(String size) {
		String sz = "";
		Rectangle r = null;

		size = size.trim();
		if (size.length() > 2) {
			sz = size.substring(size.length() - 2);
		}

		if (sz.compareTo("A4") == 0) {
			r = PageSize.A4;
		} else if (sz.compareTo("A3") == 0) {
			r = PageSize.A3;
		} else if (sz.compareTo("A2") == 0) {
			r = PageSize.A2;
		} else if (sz.compareTo("A1") == 0) {
			r = PageSize.A1;
		} else if (sz.compareTo("A0") == 0) {
			r = PageSize.A0;
		} else if (sz.compareTo("PP") == 0) {
			r = new Rectangle(17000, 4250);
		} else {
			Console.addLine("Unknown Size \"" + sz + "\"");
			return;
		}

		Main.renderWidth = (int) r.getWidth();
		Main.renderHeight = (int) r.getHeight();
		Main.renderSize = sz;
		Console.addLine("Setting Size: " + sz);
	}

	private static String path() {
		String current = null;
		try {
			current = new java.io.File(".").getCanonicalPath();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return current + "/";
	}

	public static void save() {

		XStream xstream = new XStream();
		xstream.alias("table", Table.class);
		xstream.alias("field", Field.class);

		try {

			FileOutputStream fout = new FileOutputStream(path() + "save.xml");
			ObjectOutputStream out = xstream.createObjectOutputStream(fout);

			out.writeObject(Main.size);
			out.writeObject(Main.hide_mandt);
			out.writeObject(Main.background);
			out.writeObject(Main.foreground);
			out.writeObject(Main.selected);
			out.writeObject(Main.renderWidth);
			out.writeObject(Main.renderHeight);
			out.writeObject(Main.draw);

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Console.addLine("Save: Done, save.xml");
	}

	@SuppressWarnings("unchecked")
	public static void load() {
		XStream xstream = new XStream();
		xstream.alias("table", Table.class);
		xstream.alias("field", Field.class);

		try {
			FileInputStream fin = new FileInputStream(path() + "save.xml");
			ObjectInputStream in = xstream.createObjectInputStream(fin);

			Main.size = in.readInt();
			Main.hide_mandt = in.readBoolean();
			Main.background = (Color) in.readObject();
			Main.foreground = (Color) in.readObject();
			Main.selected = (Color) in.readObject();
			Main.renderWidth = in.readInt();
			Main.renderHeight = in.readInt();
			Main.draw = (LinkedList<DrawTable>) in.readObject();

			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Console.addLine("Load: Done");
	}

	public static void jCO(String parameter) {
		try {
			String table = parameter.toUpperCase();
			table = table.subSequence(4, parameter.length()).toString()
					.toUpperCase();

			// check if it already exists
			Iterator<DrawTable> itd = null;
			DrawTable td = null;
			itd = Main.draw.iterator();
			while (itd.hasNext()) {
				td = itd.next();
				if (td.t.getName().compareTo(table) == 0) {
					Console.addLine("Already exists");
					return;
				}
			}

			Main.draw.add(new DrawTable(new SAPTable(table)));
			Console.addLine("Adding \"" + table + "\"");
		} catch (CancellationException e) {
			Console.addLine("Cancelled");
		} catch (NotFoundException e) {
			Console.addLine("Table not found");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void add(String parameters) {
		if (parameters.length() > 4) {
			String name = parameters.subSequence(4, parameters.length())
					.toString().toUpperCase();
			Console.addLine("Adding table: " + name);

			Iterator<DrawTable> itd = null;
			Iterator<Table> it = null;
			DrawTable td = null;
			Table t = null;

			// check that it is not already there
			itd = Main.draw.iterator();
			t = null;
			while (itd.hasNext()) {
				td = itd.next();
				if (td.getTable().getName().compareTo(name) == 0) {
					Console.addLine("Already there");
					return;
				}
			}

			// find in tables list and insert in draw list
			it = Main.tables.iterator();
			t = null;
			while (it.hasNext()) {
				t = it.next();
				if (t.getName().compareTo(name) == 0) {
					Main.draw.add(new DrawTable(t));
					Console.addLine("Added");
					return;
				}
			}
			Console.addLine("Table not found");
		} else {
			Console.addLine("Error parsing command");
		}
	}

	// clear console
	public static void clear() {
		Console.clear();
	}

	public static void savepng() {
		String loc = path() + "out.png";

		Console.clear();

		WorldPainter.deselectAll();
		Main.ca.repaint();
		try {
			ImageIO.write(CanvasTables.buffer, "png", new File(loc));
			Console.addLine("Saved: " + loc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void savepdf() {
		String loc = path() + "test.pdf";

		Document document = new Document(new Rectangle(
				(float) Main.renderWidth, (float) Main.renderHeight), 50, 50,
				50, 50);
		WorldPainter.deselectAll();
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(loc));
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			java.awt.Graphics2D g2 = cb.createGraphicsShapes(document
					.getPageSize().getWidth(), document.getPageSize()
					.getHeight());
			// paint the world
			WorldPainter.paint(g2);
			g2.dispose();
			document.close();
		} catch (Exception de) {
			de.printStackTrace();
		}
		Console.addLine("Saved: " + loc);
	}

	public static void help() {
		Console.addLine("Commands:");
		Console.addLine("  add <table>");
		Console.addLine("  clear");
		Console.addLine("  help");
		Console.addLine("  quit");
		Console.addLine("  exit");
		Console.addLine("  getsize");
		Console.addLine("  setsize <size>");
		Console.addLine("  savepng");
		Console.addLine("  savepdf");
		Console.addLine("  incfont");
		Console.addLine("  decfont");
		Console.addLine("  jco <table>");
		Console.addLine("  save");
	}

	public static void quit() {
		System.exit(0);
	}

	public static void incFont() {
		Main.size++;
		Console.addLine("Done, " + Main.size);
	}

	public static void decFont() {
		if (Main.size > 0) {
			Main.size--;
		}
		Console.addLine("Done, " + Main.size);
	}
}
