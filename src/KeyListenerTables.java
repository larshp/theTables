import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyListenerTables implements KeyListener {

	private double delta = 1.0;
	
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_LEFT) {
			Main.offsetx = Main.offsetx - (int)delta;
			delta = delta * 1.1;
			Main.ca.repaint();
		} else if(arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
			Main.offsetx = Main.offsetx + (int)delta;
			delta = delta * 1.1;
			Main.ca.repaint();
		} else if(arg0.getKeyCode() == KeyEvent.VK_UP) {
			Main.offsety = Main.offsety - (int)delta;
			delta = delta * 1.1;
			Main.ca.repaint();
		} else if(arg0.getKeyCode() == KeyEvent.VK_DOWN) {
			Main.offsety = Main.offsety + (int)delta;
			delta = delta * 1.1;
			Main.ca.repaint();
		}
	}

	public void keyReleased(KeyEvent arg0) {
		delta = 1.0;
	}

	public void keyTyped(KeyEvent arg0) {
		// System.out.println(arg0.getKeyChar() + 0);
		
		if(arg0.getKeyChar() == 10) { // enter
			Console.parseCommand();
		} else if(arg0.getKeyChar() == 8) { // backspace
			Console.removeLastCharacter();
		} else if(arg0.getKeyChar() == 127) { // delete
			Commands.delete();
		} else if(arg0.getKeyChar() == 45) { // minus
			if(Main.zoom - 0.2 > 0.1) {
				Main.zoom = Main.zoom - 0.2;
			}
		} else if(arg0.getKeyChar() == 43) { // plus
			Main.zoom = Main.zoom + 0.2;
		} else {
		    // System.out.println("pressed: " + (arg0.getKeyChar() + 0));
			Console.addChar(arg0.getKeyChar());
		}
		
		Main.ca.repaint();
	}
}
