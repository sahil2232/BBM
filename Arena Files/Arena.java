import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


class Arena extends JPanel{
	
	static int[][] arenaArray;
	Arena(){
		setBounds(0,0,20+Constants.ARENA_BLOCK_SIZE*Constants.ARENA_COLS,20+Constants.ARENA_BLOCK_SIZE*Constants.ARENA_ROWS);
		arenaArray = new int[Constants.ARENA_ROWS][Constants.ARENA_COLS];
		//setBackground(Color.WHITE);	
		
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				//System.out.println(ColorButtonPanel.ArenaComponentsList.getSelectedIndex());
				int selectedIndex = ColorButtonPanel.ArenaComponentsList.getSelectedIndex();
				if(selectedIndex == 0)
					arenaArray[e.getY()/Constants.ARENA_BLOCK_SIZE][e.getX()/Constants.ARENA_BLOCK_SIZE] = Constants.ARENA_EMPTY;
				else if(selectedIndex == 1)
					arenaArray[e.getY()/Constants.ARENA_BLOCK_SIZE][e.getX()/Constants.ARENA_BLOCK_SIZE] = Constants.ARENA_BRICK;
				else
					arenaArray[e.getY()/Constants.ARENA_BLOCK_SIZE][e.getX()/Constants.ARENA_BLOCK_SIZE] = Constants.ARENA_METAL;
				repaint();
			}
		}
		);
		
	}
	
	@Override 
	public void paintComponent(Graphics g){
		for(int i=0;i<Constants.ARENA_ROWS;i++){
			for(int j=0;j<Constants.ARENA_COLS;j++){
				if(arenaArray[i][j] == Constants.ARENA_EMPTY)
					g.setColor(Color.GREEN);
				else if(arenaArray[i][j] == Constants.ARENA_BRICK)
					g.setColor(Color.RED);
				else if(arenaArray[i][j] == Constants.ARENA_METAL)
					g.setColor(Color.LIGHT_GRAY);
				else if(arenaArray[i][j] == Constants.ARENA_PLAYER)
					g.setColor(Color.BLACK);
				else if(arenaArray[i][j] == Constants.ARENA_BOMB)
					g.setColor(Color.YELLOW);
				else if(arenaArray[i][j] == Constants.ARENA_PLAYER_BOMB)
					g.setColor(Color.BLUE);
				else if(arenaArray[i][j] == Constants.ARENA_BOMB_EXPLOSION)
					g.setColor(Color.ORANGE);
				g.fillRect(Constants.ARENA_BLOCK_SIZE*j,Constants.ARENA_BLOCK_SIZE*i,Constants.ARENA_BLOCK_SIZE,Constants.ARENA_BLOCK_SIZE);
			}
		}
		
		g.setColor(Color.BLACK);
		for(int i=0;i<=Constants.ARENA_ROWS;i++){
			g.drawLine(0,i*Constants.ARENA_BLOCK_SIZE,Constants.ARENA_COLS*Constants.ARENA_BLOCK_SIZE,i*Constants.ARENA_BLOCK_SIZE);
		}
		for(int j=0;j<=Constants.ARENA_COLS;j++){
			g.drawLine(j*Constants.ARENA_BLOCK_SIZE,0,j*Constants.ARENA_BLOCK_SIZE,Constants.ARENA_ROWS*Constants.ARENA_BLOCK_SIZE);
		}
	}
}