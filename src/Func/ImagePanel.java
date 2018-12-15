package Func;

import java.awt.Image;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Dimension;

public class ImagePanel extends JPanel {

	private Image image;

	public ImagePanel(String url) {
		image = Toolkit.getDefaultToolkit().getImage(url);
	}

	public ImagePanel(Image image) {
		this.image = image;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
	}
}
