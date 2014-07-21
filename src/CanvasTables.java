import java.awt.*;
import java.awt.image.BufferedImage;

public class CanvasTables extends Canvas {

	private static final long serialVersionUID = 8679099469723042884L;
	Dimension size;
	public static BufferedImage buffer = null;

	CanvasTables(Dimension size) {
		this.size = size;
		setBounds(0, 0, this.size.width, this.size.height);

		 buffer = new BufferedImage(size.width, size.height,
					BufferedImage.TYPE_INT_RGB);	
	}

	public void repaint() {
		this.paint(this.getGraphics());
	}

	public void paint(Graphics g) {
		// clear entire screen
	
		paintWorld();
		
		g.drawImage(buffer, 0, 0, this);
		
	}

	private void paintWorld() {
		Graphics2D g2 = (Graphics2D) buffer.getGraphics();

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// clear background
		//g2.setColor(Main.background);
		g2.setBackground(Main.background);
		g2.clearRect(0, 0, size.width, size.height);
		//g2.fillRect(0, 0, Main.renderWidth, Main.renderHeight);

		// output console
		Font courier = new Font("Courier New", Font.PLAIN, 14); // fixed console
																// font size
		g2.setFont(courier);
		g2.setColor(Main.foreground);
		String[] lines = Console.getText().split("\n");
		for (int i = 0; i < lines.length; i++) {
			g2.drawString(lines[i], 50, 50 + i * 15);
		}

		g2.translate(Main.offsetx, Main.offsety);
		g2.scale(Main.zoom, Main.zoom);

		// paint actual output
		WorldPainter.paint(g2);

	}

	public Dimension getMinimumSize() {
		return size;
	}

	public Dimension getPreferredSize() {
		return size;
	}

	public Dimension getMaximumSize() {
		return size;
	}

}
