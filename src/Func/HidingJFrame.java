package Func;

import javax.swing.JFrame;
import java.awt.event.*;

// A class can implement this interface to hide whenever another window is created
public class HidingJFrame extends JFrame implements WindowListener {

	boolean isChildOpen = false;

	public HidingJFrame(String str) {
		super(str);
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		this.setVisible(isChildOpen);
		isChildOpen = !isChildOpen;
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

}
