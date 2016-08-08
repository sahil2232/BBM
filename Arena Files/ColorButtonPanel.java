import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

class ColorButtonPanel extends JPanel{
	public static JList<String> ArenaComponentsList;	
	public JLabel savedStatus;
	
	ColorButtonPanel(){
		setBounds(1050,200,300,300);
		//setBackground(Color.RED);
		String ArenaComponents[] = 
		{
			"EMPTY",
			"BRICK",
			"METAL",
		};
		ArenaComponentsList = new JList<String>(ArenaComponents);
		ArenaComponentsList.setFont(new Font("Century",Font.PLAIN,20));
		add(ArenaComponentsList);
		ArenaComponentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JButton saveArena = new JButton("SAVE");
		saveArena.addMouseListener( new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e){
			try{
				FileOutputStream f = new FileOutputStream("Arena.txt");
				f.write('{');
				f.write('\n');
				for(int i=0;i<Constants.ARENA_ROWS;i++){
					f.write('{');
					for(int j=0;j<Constants.ARENA_COLS;j++){
						f.write(Arena.arenaArray[i][j]+'0');
						if(j!=Constants.ARENA_COLS-1)
							f.write(',');
					}
					f.write('}');
					f.write(',');
					f.write('\n');
				}
				f.write('}');
				f.close();
				savedStatus.setText("Saved --> Arena.txt");
			}catch(Exception ex){savedStatus.setText("Failed");}
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
		add(saveArena);
		savedStatus = new JLabel("");
		add(savedStatus);
		setVisible(true);
	}
}