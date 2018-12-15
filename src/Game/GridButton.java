package Game;

import javax.swing.JButton;
import javax.swing.border.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

/*
An extension of the JButton used to display correctly
 */

public class GridButton extends JButton {

	public GridButton(Color color, Dimension size) {
		this.setBackground(color);
		this.setSize(size);
		this.setPreferredSize(size);
		this.setOpaque(false);
		this.setBorderPainted(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(this.getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		super.paintComponent(g);
	}
}