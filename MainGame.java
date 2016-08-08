import javax.swing.*;
import java.awt.*;

class MainGame{
	public static void main(String[] args){
		JFrame GameFrame = new JFrame("BBM");
		GameFrame.setLayout(null);
		Game game = new Game();
		GameFrame.add(game);
		GameFrame.add(new ControlPanel(game));
		GameFrame.setExtendedState(GameFrame.getExtendedState() | GameFrame.MAXIMIZED_BOTH);
		GameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GameFrame.setVisible(true);
	}
}