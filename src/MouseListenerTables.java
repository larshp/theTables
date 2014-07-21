import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;

public class MouseListenerTables implements MouseListener, MouseMotionListener {
	boolean debug = false;
	static int lastX = 0;
	static int lastY = 0;

	private boolean selectTable(MouseEvent arg0) {
		Iterator<DrawTable> it = Main.draw.iterator();
		boolean found = false;

		while (it.hasNext()) {
			DrawTable t = it.next();
			t.selected = false;
			if (found == false
					&& t.clickCheck(new Point(
							 (arg0.getX() - Main.offsetx), 
							 (arg0.getY() - Main.offsety)
							))) {
				t.selected = true;
				found = true;
			}
		}

		Main.ca.repaint();
		return found;
	}

	private void deltaMove(int deltaX, int deltaY) {
		Iterator<DrawTable> it = Main.draw.iterator();
		while (it.hasNext()) {
			DrawTable t = it.next();
			if (t.selected) {
				t.move(deltaX, deltaY);
			}
		}

		Main.ca.repaint();
	}

	// /////////////////////////

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (debug) {
			System.out.println("click button: " + arg0.getButton() + ", x:"
					+ arg0.getX() + ", y:" + arg0.getY());
		}

		selectTable(arg0);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (debug) {
			System.out.println("Mouse pressed");
		}

		lastX = arg0.getX();
		lastY = arg0.getY();

		selectTable(arg0);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (debug) {
			System.out.println("Mouse released");
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (debug) {
			System.out.println("Mouse dragged");
		}

		int deltaX = arg0.getX() - lastX;
		int deltaY = arg0.getY() - lastY;
		lastX = arg0.getX();
		lastY = arg0.getY();

		if (debug) {
			System.out.println("deltax: " + deltaX + ", deltay: " + deltaY);
		}

		// find all selected tables and make delta move
		deltaMove(deltaX, deltaY);
	}

	// ////////////////////////////////

	@Override
	public void mouseMoved(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		if (debug) {
			System.out.println("Mouse entered");
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		if (debug) {
			System.out.println("Mouse exited");
		}
	}
}
