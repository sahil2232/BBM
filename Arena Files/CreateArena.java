import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class CreateArena{
//19x28 size:35
	public static void main(String[] args){
		JFrame mainFrame = new JFrame("ARENA CONSTRUCTION GUI");
		mainFrame.setLayout(null);
		mainFrame.add(new Arena());
		mainFrame.add(new ColorButtonPanel());
		mainFrame.setExtendedState(mainFrame.getExtendedState() | mainFrame.MAXIMIZED_BOTH);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
	}
}