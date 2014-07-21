import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;

// TODO: why not extend table? probably because of the main variable?
public class DrawTable {

	Table t;
	Point placement;
	Point bounds;
	boolean selected = false;
	public static final int space = 2;

	DrawTable(Table t) {
		this.t = t;
		this.placement = new Point(300 - Main.offsetx, 100 - Main.offsety);
	}

	Table getTable() {
		return this.t;
	}

	public void draw(Graphics2D g2) {
		Rectangle2D r2 = null;
		boolean drawBounds = false;
		boolean rounded = true;
		int y = placement.y;
		int boundWidth = 0;

		// Font verdana = new Font("Verdana", Font.PLAIN, Main.size);
		Font verdanaBold = new Font("Verdana", Font.BOLD, Main.size + 4);
		Font verdanaSmall = new Font("Verdana", Font.PLAIN,
				(Main.size - 4 > 5 ? Main.size - 4 : Main.size));
		Font courier = new Font("Courier New", Font.PLAIN, Main.size);
		Font courierb = new Font("Courier New", Font.BOLD, Main.size);
		g2.setColor(Main.foreground);

		// table name
		g2.setFont(verdanaBold);
		r2 = g2.getFont().getStringBounds(t.getName(),
				g2.getFontRenderContext());
		if (drawBounds)
			g2.drawRect(placement.x, y, (int) r2.getWidth(),
					(int) r2.getHeight());
		g2.drawString(t.getName(), placement.x, y + (int) r2.getHeight());
		y = y + (int) r2.getHeight();
		if ((int) r2.getWidth() > boundWidth) {
			boundWidth = (int) r2.getWidth();
		}

		// table description
		g2.setFont(verdanaSmall);
		r2 = g2.getFont().getStringBounds("  " + t.getDescription(),
				g2.getFontRenderContext());
		if (drawBounds) {
			g2.drawRect(placement.x, y, (int) r2.getWidth(),
					(int) r2.getHeight());
		}
		g2.drawString("  " + t.getDescription(), placement.x,
				y + (int) r2.getHeight());
		y = y + (int) r2.getHeight();
		if ((int) r2.getWidth() > boundWidth) {
			boundWidth = (int) r2.getWidth();
		}

		// table fields
		LinkedList<Field> fields = t.getFields();
		Iterator<Field> it = fields.iterator();
		int maxFieldNameWidth = 0;
		int fieldNameHeight = 0;
		int fieldNameStart = y;
		while (it.hasNext()) {
			Field f = it.next();
			if (Main.hide_mandt && f.getName().compareTo("MANDT") == 0) {
				continue;
			}

			// fieldname
			if (f.getKey()) {
				g2.setFont(courierb);
			} else {
				g2.setFont(courier);
			}
			r2 = g2.getFont().getStringBounds(f.getFieldName(),
					g2.getFontRenderContext());
			if (drawBounds) {
				g2.drawRect(placement.x, y, (int) r2.getWidth(),
						(int) r2.getHeight());
			}
			g2.drawString(f.getFieldName(), placement.x,
					y + (int) r2.getHeight());
			y = y + (int) r2.getHeight();
			fieldNameHeight = (int) r2.getHeight();

			if ((int) r2.getWidth() > boundWidth) {
				boundWidth = (int) r2.getWidth();
			}
			// int fieldNameHeight = (int) r2.getHeight();
			int fieldNameWidth = (int) r2.getWidth();
			if (fieldNameWidth > maxFieldNameWidth) {
				maxFieldNameWidth = fieldNameWidth;
			}
		}

		// field descriptions
		it = fields.iterator();
		y = fieldNameStart;
		while (it.hasNext()) {
			Field f = it.next();
			if (Main.hide_mandt && f.getName().compareTo("MANDT") == 0) {
				continue;
			}
			y = y + fieldNameHeight;

			g2.setFont(verdanaSmall);
			String text = "  " + f.getDescription();
			r2 = g2.getFont().getStringBounds(text, g2.getFontRenderContext());
			if (drawBounds)
				g2.drawRect(placement.x + maxFieldNameWidth,
						y - (int) r2.getHeight(), (int) r2.getWidth(),
						(int) r2.getHeight());
			g2.drawString(text, placement.x + maxFieldNameWidth, y);
			if ((int) r2.getWidth() + maxFieldNameWidth > boundWidth) {
				boundWidth = (int) r2.getWidth() + maxFieldNameWidth;
			}
		}

		// draw bounding box
		if (selected == true) {
			g2.setColor(Main.selected);
		} else {
			g2.setColor(Main.foreground);
		}
		if (rounded) {
			g2.drawRoundRect(placement.x - space, placement.y - space,
					boundWidth + space * 2, y - placement.y + space * 2, 10, 10);
		} else {
			g2.drawRect(placement.x - space, placement.y - space, boundWidth
					+ space * 2, y - placement.y + space * 2);
		}
		g2.setColor(Main.foreground);

		bounds = new Point(placement.x + boundWidth, placement.y + y
				- placement.y);
	}

	// returns true if click is on drawed table
	public boolean clickCheck(Point p) {
		if (p.getX() <= bounds.x && p.getX() >= placement.x) {
			if (p.getY() <= bounds.y && p.getY() >= placement.y) {
				return true;
			}
		}
		return false;
	}

	// move
	public void move(int deltaX, int deltaY) {
		placement.translate(deltaX, deltaY);
	}
}
