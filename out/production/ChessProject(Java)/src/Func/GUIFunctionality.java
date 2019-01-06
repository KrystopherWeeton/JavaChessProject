package Func;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Point;
import java.awt.Component;

public class GUIFunctionality {

	/*
	Returns a dimension with the center of the screen
	 */
	public static Location getScreenCenter() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		return new Location(dimension.width / 2, dimension.height / 2);
	}

	/*
	Returns a location for the
	 */
	public static Point getLocationToCenter(Dimension d) {
		Location center = getScreenCenter();
		return new Point(center.getX() - d.width / 2, center.getY() - d.height / 2);
	}


	/*
	Makes a button with the desired traits
	 */
	public static JButton makeButton(String text, Dimension size, ActionListener actionListener) {
		JButton button = new JButton(text);
		button.setSize(size);
		button.addActionListener(actionListener);
		return button;
	}

	/*
	Sends a warning pop up to the screen as a JDialogBox
	 */
	public static void sendWarning(String message, Component parent) {
		JOptionPane.showMessageDialog(parent, message);
	}
}