import java.awt.Graphics2D;
import java.util.Iterator;

public class WorldPainter {

	public static void deselectAll() {
		{
			Iterator<DrawTable> it = Main.draw.iterator();
			while (it.hasNext()) {
				DrawTable t = it.next();
				t.selected = false;
			}
		}
	}

	public static void paint(Graphics2D g2) {

		// draw frame
		g2.drawRect(0, 0, Main.renderWidth - 1, Main.renderHeight - 1);

		Iterator<DrawTable> itd = null;
		DrawTable td = null;
		itd = Main.draw.iterator();
		while (itd.hasNext()) {
			td = itd.next();
			td.draw(g2);
		}

	}

}
