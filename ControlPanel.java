import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ControlPanel extends JPanel{
	static JLabel status;
	JTextField joinIpAdd;
	public Game game;
	ControlPanel(Game game){
		this.game = game;
		setBounds(1000,100,300,300);
		JButton createGame = new JButton("Create");
		JButton joinGame = new JButton("Join");
		final JButton submitIp = new JButton("Enter");
		joinIpAdd = new JTextField(20);
		joinIpAdd.setBounds(20,50,80,25);
		joinIpAdd.setVisible(false);
		submitIp.setVisible(false);
		submitIp.setBounds(50,70,50,50);
		
		createGame.addMouseListener( new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e){
				System.out.println("Clicked");
				createServer();
		}
	
	@Override
	public void mousePressed(MouseEvent e){}
	
	@Override
	public void mouseExited(MouseEvent e){}
	
	@Override
	public void mouseEntered(MouseEvent e){}
	
	@Override
	public void mouseReleased(MouseEvent e){}
		});
		
		joinGame.addMouseListener( new MouseListener()
		{
			@Override
	public void mouseClicked(MouseEvent e){joinIpAdd.setVisible(true);submitIp.setVisible(true);
		}
	
	@Override
	public void mousePressed(MouseEvent e){}
	
	@Override
	public void mouseExited(MouseEvent e){}
	
	@Override
	public void mouseEntered(MouseEvent e){}
	
	@Override
	public void mouseReleased(MouseEvent e){}
		});
		
		
		submitIp.addMouseListener( new MouseListener()
		{
			@Override
	public void mouseClicked(MouseEvent e){String ipAdd = joinIpAdd.getText();joinGame(ipAdd);}
	
	@Override
	public void mousePressed(MouseEvent e){}
	
	@Override
	public void mouseExited(MouseEvent e){}
	
	@Override
	public void mouseEntered(MouseEvent e){}
	
	@Override
	public void mouseReleased(MouseEvent e){}
		});
		status = new JLabel("Status");
		add(createGame);
		add(joinGame);
		add(joinIpAdd);
		add(status);
		add(submitIp);
	}
	public void createServer(){
			//System.out.println("GAME CREATED");
			game.server();
		}
	public void joinGame(String ip){
		game.join(ip);
	}
}