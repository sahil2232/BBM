import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.image.*;
import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Game extends JPanel implements KeyListener{

		public boolean communicationEnabled = false;
		public boolean checkServer=false; //checks if current program is server or client
		public int playerY = 7,playerX = 7,player2Y = 7,player2X = 7; // player position
		private Grid grid;
		private boolean enabled = true;  // gameover variable.. disables controls after game ends
		/*
			BombList includes instances of Bomb Class which contains 
			bombX,bombY
			explode(false if bomb has not exploded)
			explodeTime(time for which explosion should last)
		*/
		public ArrayList<Bomb> bombList; 
		/*
			Different Images
		*/
		public BufferedImage img;
		public BufferedImage IMAGE_BOMB;
		public BufferedImage IMAGE_PLAYER;
		public BufferedImage IMAGE_BRICK;
		public BufferedImage IMAGE_METAL;
		public BufferedImage IMAGE_PLAYER_EXPLOSION;
		public BufferedImage IMAGE_EXPLOSION;
		
		/*
			
			Sounds After a Certain Action Specified below 
		*/
		Thread Sound = null;//Bomb Exploding Sound
		Thread backgroundMusic = null;//Background Sound(FIGHT CLUB)
		
		/*
			Listens for other player's status
		*/
		Thread playerListener = null;
		
		/*
		Server Socket Declaration with different Reader and Writer Classes 
		Same Reader and Writer can be used for Client Side
		*/
		ServerSocket serversocket = null;
		Socket Server = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		
		Socket Client;
		
		/*
		Includes all the Logic 
		*/
	 	public Game(){
			backgroundMusic = new Thread(new BombSound("Sounds/backgroundMusic.wav",45),"MainMusic");
			//backgroundMusic.start();
			/*
			playerListener = new Thread(new ListenerClass(this,br));
			playerListener.start();
			*/
			
			/*
				Setting Arena Dimensions
			*/
			setBounds(0, 0, 20+Constants.ARENA_BLOCK_SIZE*Constants.ARENA_COLS, 20+Constants.ARENA_BLOCK_SIZE*Constants.ARENA_ROWS);
			setFocusable(true);
			requestFocus(true);
			addKeyListener(this);
			try{			
				IMAGE_BOMB = ImageIO.read(new File("Images/bombImage.png"));
				IMAGE_PLAYER = ImageIO.read(new File("Images/playerImage.png"));
				IMAGE_BRICK = ImageIO.read(new File("Images/brickImage.png"));
				IMAGE_METAL = ImageIO.read(new File("Images/metalImage.png"));
				IMAGE_PLAYER_EXPLOSION = ImageIO.read(new File("Images/playerExplosionImage.png"));
				IMAGE_EXPLOSION = ImageIO.read(new File("Images/explosionVerticalImage.png"));
				img = ImageIO.read(new File("bombImage.jpg"));
			}catch(Exception e){}
			/*
				Mouse Listener to make JPanel Focusable when Clicked
			*/
			addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e){
					requestFocus();
					super.mouseClicked(e);
				}
			}
			);
			//for testing Purposes
			addMouseMotionListener(new MouseAdapter(){
				@Override
				public void mouseMoved(MouseEvent e){
				}
			});
			
			
			/*
				Arena Array (aka statusArray)
				Will make it read from file(created by Arena Generator JAR File)
			*/
			int array[][] = new int[][]
			{
{0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
{0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
{0,0,1,1,0,1,0,0,0,0,0,1,0,1,1,0,0,0,0,0,1,0,0,2,2,2,2,2},
{2,2,1,1,2,2,0,1,0,1,0,0,0,1,1,1,0,1,0,0,1,1,1,1,1,1,1,1},
{0,0,1,1,0,1,0,0,0,0,1,2,0,1,1,0,0,0,1,0,1,1,1,1,1,1,1,1},
{1,1,1,1,0,0,1,0,0,0,1,1,2,1,1,0,0,1,1,0,0,0,1,1,0,0,0,0},
{0,0,1,1,0,0,0,0,0,0,0,2,2,1,1,0,0,1,0,1,1,2,0,0,0,1,1,1},
{0,0,1,1,0,1,0,0,1,0,0,0,2,1,1,1,0,0,0,1,0,2,0,1,0,0,1,1},
{0,0,1,1,0,2,2,2,1,1,0,0,2,1,1,0,2,2,2,2,2,2,2,1,0,1,0,0},
{1,0,1,1,0,2,2,2,2,1,0,0,2,1,1,0,0,0,0,0,0,2,0,0,0,0,0,0},
{0,0,1,1,0,1,0,0,1,0,0,0,2,1,1,0,1,0,1,0,1,0,1,1,1,0,0,1},
{0,0,1,1,0,0,0,1,0,1,0,0,1,0,1,0,0,0,0,0,0,0,0,1,1,0,0,0},
{0,1,1,1,0,0,0,1,1,0,0,0,0,1,0,0,0,0,1,0,0,1,0,1,1,2,2,0},
{0,0,1,1,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,1,0,0,0,1,1,2,1,1},
{0,0,1,1,0,0,0,1,0,1,1,1,0,0,0,0,0,1,1,0,0,0,0,1,1,2,1,1},
{1,0,1,1,0,0,0,0,0,0,1,1,0,0,1,0,0,1,1,0,1,1,0,1,1,2,1,1},
{0,0,1,1,0,0,0,0,0,0,1,1,1,0,0,1,0,1,1,0,0,0,0,1,1,2,1,1},
{0,1,0,1,0,0,0,0,0,1,1,1,2,2,1,0,2,1,1,0,1,1,0,1,1,2,1,1},
{0,0,0,0,0,1,1,1,1,1,1,1,1,2,2,1,1,1,1,0,0,1,0,1,1,2,1,1},
};
		grid = new Grid(array);// Pass Array to the Grid Class (AIWAI)
		bombList = new ArrayList<Bomb>(10);//BombList (AS OF NOW ONLY 10 BOMBS ALLOWED PER PLAYER)
		}
		/*
			Paint the Arena According to the values in statusArray
		*/
		@Override
		public void paintComponent(Graphics g){
			setBomb();//First Set the bomb Location and their Explosion time

			/*
				Start Filling Cells of ARENA 
			*/
			for(int i=0;i<Constants.ARENA_ROWS;i++){
				for(int j=0;j<Constants.ARENA_COLS;j++){
					int status = grid.statusArray[i][j];
					try{
					if(status == Constants.ARENA_EMPTY)
						g.setColor(Color.GREEN);
					else if(status == Constants.ARENA_BRICK)
						g.setColor(Color.RED);
					else if(status == Constants.ARENA_METAL)
						//g.drawImage(IMAGE_METAL,j*Constants.ARENA_BLOCK_SIZE,i*Constants.ARENA_BLOCK_SIZE,Constants.ARENA_BLOCK_SIZE,Constants.ARENA_BLOCK_SIZE,null);
						g.setColor(Color.LIGHT_GRAY);
					else if(status == Constants.ARENA_BOMB)
						//g.setColor(Color.YELLOW);
						g.drawImage(IMAGE_BOMB,j*Constants.ARENA_BLOCK_SIZE,i*Constants.ARENA_BLOCK_SIZE,Constants.ARENA_BLOCK_SIZE,Constants.ARENA_BLOCK_SIZE,null);
					else if(status == Constants.ARENA_PLAYER)
						//g.setColor(Color.BLACK);
						g.drawImage(IMAGE_PLAYER,j*Constants.ARENA_BLOCK_SIZE,i*Constants.ARENA_BLOCK_SIZE,Constants.ARENA_BLOCK_SIZE,Constants.ARENA_BLOCK_SIZE,null);
					else if(status == Constants.ARENA_BOMB_EXPLOSION)
						//g.setColor(Color.ORANGE);
						g.drawImage(IMAGE_EXPLOSION,j*Constants.ARENA_BLOCK_SIZE,i*Constants.ARENA_BLOCK_SIZE,Constants.ARENA_BLOCK_SIZE,Constants.ARENA_BLOCK_SIZE,null);
					else // status == Constants.ARENA_PLAYER_BOMB{
					{	
						g.setColor(Color.BLUE);
						System.out.println(status);
					}
					
					if(status == Constants.ARENA_BOMB_EXPLOSION && playerY == i && playerX == j){
						g.drawImage(IMAGE_PLAYER_EXPLOSION,j*Constants.ARENA_BLOCK_SIZE,i*Constants.ARENA_BLOCK_SIZE,Constants.ARENA_BLOCK_SIZE,Constants.ARENA_BLOCK_SIZE,null);
						enabled = false;
						playerListener.stop();
						pw.close();
						br.close();
						Server.close();
						serversocket.close();
						Client.close();
						backgroundMusic.stop();
					}
					if(status == Constants.ARENA_EMPTY || status == Constants.ARENA_BRICK || status == Constants.ARENA_METAL)
						g.fillRect(0+Constants.ARENA_BLOCK_SIZE*j,0+Constants.ARENA_BLOCK_SIZE*i,Constants.ARENA_BLOCK_SIZE,Constants.ARENA_BLOCK_SIZE);
					}catch(Exception e){}
				}
			}
			/*Stop Array Cell Filling */
			
			/*
				make Lines for the ARENA
			*/
			g.setColor(Color.DARK_GRAY);
			for(int i=0;i<=Constants.ARENA_COLS;i++){
				g.drawLine(0+i*Constants.ARENA_BLOCK_SIZE,0,0+i*Constants.ARENA_BLOCK_SIZE,0+Constants.ARENA_ROWS*Constants.ARENA_BLOCK_SIZE);
			}
			for(int j=0;j<=Constants.ARENA_ROWS;j++){
				g.drawLine(0,0+j*Constants.ARENA_BLOCK_SIZE,0+Constants.ARENA_COLS*Constants.ARENA_BLOCK_SIZE,0+j*Constants.ARENA_BLOCK_SIZE);
			}
			/*
				Stop making Lines 
			*/
			repaint();
		}
		
		
		/*
			Set the explosion in horizontal Direction
		*/
		public void explodeHorizontal(int bombX,int bombY,int explosionDirection){
			for(int i=1;i<=2;i++){
				int status=-1,direction=-1;
				if(explosionDirection == Constants.ARENA_EXPLOSION_RIGHT){
					direction = i;
					if(bombY+direction<Constants.ARENA_COLS)
						status = grid.statusArray[bombX][bombY+direction];
					else
						break;
				}
				else if(explosionDirection == Constants.ARENA_EXPLOSION_LEFT){
					direction = (-1)*i;
					if(bombY+direction>=0)
						status = grid.statusArray[bombX][bombY+direction];
					else
						break;
				}
				if(status == Constants.ARENA_METAL || status == Constants.ARENA_BOMB_EXPLOSION){
					break;
				}
				else if(status == Constants.ARENA_BRICK){
					grid.statusArray[bombX][bombY+direction] = Constants.ARENA_BOMB_EXPLOSION;
					break;
				}
				else{
					grid.statusArray[bombX][bombY+direction] = Constants.ARENA_BOMB_EXPLOSION;
				}
			}
		}
		
		/*
			Set the explosion in vertical Direction
		*/
		public void explodeVertical(int bombX,int bombY,int explosionDirection){
			for(int i=1;i<=2;i++){
				int status=-1,direction=-1;
				if(explosionDirection == Constants.ARENA_EXPLOSION_DOWN){
					direction = i;
					if(bombX+direction<Constants.ARENA_ROWS){
						status = grid.statusArray[bombX+direction][bombY];
					}
					else
						break;
				}
				else if(explosionDirection == Constants.ARENA_EXPLOSION_UP){
					direction = (-1)*i;
					if(bombX+direction>=0){
						status = grid.statusArray[bombX+direction][bombY];
					}
				}
				if(status == Constants.ARENA_METAL || status == Constants.ARENA_BOMB_EXPLOSION){
					break;
				}
				else if(status == Constants.ARENA_BRICK){
					grid.statusArray[bombX+direction][bombY] = Constants.ARENA_BOMB_EXPLOSION;
					break;
				}
				else{
					grid.statusArray[bombX+direction][bombY] = Constants.ARENA_BOMB_EXPLOSION;
				}
				
			}
		}
		
		/*
			Defuse Explosion in Horizontal Direction After explodeTime = false
		*/
		public void defuseHorizontal(int bombX,int bombY,int explosionDirection){
			for(int j=1;j<=2;j++){
				int status=-1,direction=-1;
				if(explosionDirection == Constants.ARENA_EXPLOSION_RIGHT){
					direction = j;
					if(bombY+direction<Constants.ARENA_COLS)
						status = grid.statusArray[bombX][bombY+direction];
					else
						break;
				}
				else if(explosionDirection == Constants.ARENA_EXPLOSION_LEFT){
					direction = (-1)*j;
					if(bombY+direction>=0)
						status = grid.statusArray[bombX][bombY+direction];
					else
						break;
				}
				if(status == Constants.ARENA_BOMB_EXPLOSION){
					grid.statusArray[bombX][bombY+direction] = Constants.ARENA_EMPTY;
				}
				else
					break;
			}
		}
		
		/*
			Defuse Explosion in Vertical Direction After explodeTime = false
		*/
		public void defuseVertical(int bombX,int bombY,int explosionDirection){
			for(int j=1;j<=2;j++){
				int status=-1,direction=-1;
				if(explosionDirection == Constants.ARENA_EXPLOSION_DOWN){
					direction = j;
					if(bombX+direction<Constants.ARENA_ROWS)
						status = grid.statusArray[bombX+direction][bombY];
					else
						break;
				}
				else if(explosionDirection == Constants.ARENA_EXPLOSION_UP){
					direction = (-1)*j;
					if(bombX+direction>=0)
						status = grid.statusArray[bombX+direction][bombY];
					else
						break;
				}
				if(status == Constants.ARENA_BOMB_EXPLOSION){
					grid.statusArray[bombX+direction][bombY] = Constants.ARENA_EMPTY;
				}
				else
					break;
			}
		}
		
		/*
			Sets Explosion and calls explodeHorizontal and explodeVetical Functions 
		*/
		public void setExplosion(int bombX,int bombY){
			grid.statusArray[bombX][bombY] = Constants.ARENA_BOMB_EXPLOSION;
			explodeHorizontal(bombX,bombY,Constants.ARENA_EXPLOSION_RIGHT);
			explodeHorizontal(bombX,bombY,Constants.ARENA_EXPLOSION_LEFT);
			explodeVertical(bombX,bombY,Constants.ARENA_EXPLOSION_UP);
			explodeVertical(bombX,bombY,Constants.ARENA_EXPLOSION_DOWN);
		}
		
		/*
			Unsets Explosion and calls defuseHorizontal and defuseVertical Functions
		*/
		public void defuseExplosion(int bombX,int bombY){
			grid.statusArray[bombX][bombY] = Constants.ARENA_EMPTY;
			defuseHorizontal(bombX,bombY,Constants.ARENA_EXPLOSION_RIGHT);
			defuseHorizontal(bombX,bombY,Constants.ARENA_EXPLOSION_LEFT);
			defuseVertical(bombX,bombY,Constants.ARENA_EXPLOSION_UP);
			defuseVertical(bombX,bombY,Constants.ARENA_EXPLOSION_DOWN);
		}
		
		/*
			Sets Bomb Position and deletes Bomb Instance from bombList when its
			variable viz used = true; 
			Also it calls setExposion and defuseExplosion depending upon 
			explode and explodeTime Values of different Bomb Instances
		*/
		public void setBomb(){
			for(int i=0;i<bombList.size();i++){
				if(bombList.get(i).used)
					continue;
				if(!bombList.get(i).explode){
						grid.statusArray[bombList.get(i).bombX][bombList.get(i).bombY] = Constants.ARENA_BOMB;
				}
				else{
					if(bombList.get(i).explodeTime)
						setExplosion(bombList.get(i).bombX,bombList.get(i).bombY);
					else{
						defuseExplosion(bombList.get(i).bombX,bombList.get(i).bombY);
						bombList.get(i).used = true;
						Bomb b = bombList.get(i);
						bombList.remove(b);
						b = null;
					}	
				}
			}
		}
		
		/*
			Controls player Movements
		*/
		public void movePlayer(KeyEvent e){
			int code = e.getKeyCode();
			/*
				Moves player up when UP KEY is pressed only when the cell above is 
				not a metal or a brick
				LOGIC(If above condition holds true): Make current cell as Empty and Above 
				cell as Player
			*/
			if(code == KeyEvent.VK_UP){
				if(playerY>0 && grid.statusArray[playerY-1][playerX]!= Constants.ARENA_METAL && grid.statusArray[playerY-1][playerX]!= Constants.ARENA_BRICK){
					grid.statusArray[playerY][playerX]=Constants.ARENA_EMPTY;
					playerY--;
					grid.statusArray[playerY][playerX] = Constants.ARENA_PLAYER;
					send(Constants.ARENA_PLAYER,playerY,playerX);
				}
			}
			/*
				Same as Above
			*/
			if(code == KeyEvent.VK_DOWN){
				if(playerY<Constants.ARENA_ROWS-1 && grid.statusArray[playerY+1][playerX]!= Constants.ARENA_METAL && grid.statusArray[playerY+1][playerX]!= Constants.ARENA_BRICK){
					grid.statusArray[playerY][playerX]=Constants.ARENA_EMPTY;
					playerY++;			
					grid.statusArray[playerY][playerX] = Constants.ARENA_PLAYER;					
					send(Constants.ARENA_PLAYER,playerY,playerX);
				}
			}
			/*
				Same as Above
			*/
			if(code == KeyEvent.VK_RIGHT){
				if(playerX<Constants.ARENA_COLS-1 && grid.statusArray[playerY][playerX+1]!= Constants.ARENA_METAL && grid.statusArray[playerY][playerX+1]!= Constants.ARENA_BRICK){
					grid.statusArray[playerY][playerX]=Constants.ARENA_EMPTY;
					playerX++;
					grid.statusArray[playerY][playerX] = Constants.ARENA_PLAYER;
					send(Constants.ARENA_PLAYER,playerY,playerX);
				}
			}
			/*
				Same as Above
			*/
			if(code == KeyEvent.VK_LEFT){
				if(playerX>0 && grid.statusArray[playerY][playerX-1]!= Constants.ARENA_METAL && grid.statusArray[playerY][playerX-1]!= Constants.ARENA_BRICK){
					grid.statusArray[playerY][playerX]=Constants.ARENA_EMPTY;
					playerX--;
					grid.statusArray[playerY][playerX] = Constants.ARENA_PLAYER;
					send(Constants.ARENA_PLAYER,playerY,playerX);
				}
			}
			/*
				Bomb is placed to the current cell when SPACE KEY is pressed
				It Calls placeBomb Function which makes Bomb Instance and 
				insert it into BombList
			*/
			if(code == KeyEvent.VK_SPACE){
				placeBomb(playerY,playerX);
				if(communicationEnabled)
				{	
					send(Constants.ARENA_BOMB,playerY,playerX);
				}	
			}
		
		repaint(); // Repaint after a key Stroke
		}

	/*
		Make a Bomb Instance and add it to BombList
		Also start a timer for Explode and ExplodeTime Variables and 
		subsequently call repaint
	*/		
	void placeBomb(int x,int y){
		final Bomb bomb = new Bomb(x,y);
		bombList.add(bomb);
		/*
			Timer for Explode Variable
		*/
		new java.util.Timer().schedule(
			new java.util.TimerTask(){
				@Override
				public void run(){
					bombList.get(bombList.indexOf(bomb)).explode = true;
					bombList.get(bombList.indexOf(bomb)).explodeTime = true;
					repaint();
					Sound = new Thread(new BombSound("Sounds/explosion.wav",2),"BombSound");
					//Sound.start();
				}	
			},
			2500
		);
		/*
			Timer for explodeTime Variable
		*/
		new java.util.Timer().schedule(
			new java.util.TimerTask(){
				@Override
				public void run(){
					bombList.get(bombList.indexOf(bomb)).explodeTime = false;
					repaint();
					Sound.stop();
				}
			},
			4500
		);
		setBomb();//set bomb Positions in statusArray
	}
	
	/*
		Functions as Server for Communication
	*/
	public void server(){
		try{
		communicationEnabled = true;
		serversocket = new ServerSocket(8989);
    Server = serversocket.accept();
    br = new BufferedReader(new InputStreamReader(Server.getInputStream()));
    pw = new PrintWriter(Server.getOutputStream(),true);
		System.out.println("SERVER");
		playerListener = new Thread(new ListenerClass(this,br));
		playerListener.start();
		}catch(Exception e){e.printStackTrace();}
	}
	/*
		Functions as Client for Communication
	*/
	public void join(String ip){
		System.out.println(ip);
		try{
		communicationEnabled = true;
		Client = new Socket(ip,8989);
		System.out.println("CLIENT");
    pw = new PrintWriter(Client.getOutputStream(),true);
    br = new BufferedReader(new InputStreamReader(Client.getInputStream()));
		playerListener = new Thread(new ListenerClass(this,br));
		playerListener.start();
		}catch(Exception e){}
	}
	
	/*
		Sends Player or Bomb Status to other Player
	*/
	public void send(int entity,int x,int y){
		String str = entity+" "+x+" "+y;
		try{
		pw.println(str);
		}catch(Exception e){e.printStackTrace();}
	}
	
	public void receive(int entity,int x,int y){
		//System.out.println(entity+" "+x+" "+y);
		if(entity == Constants.ARENA_BOMB){
			placeBomb(x,y);
			repaint();
		}
		else if(entity == Constants.ARENA_PLAYER){
			grid.statusArray[player2Y][player2X] = Constants.ARENA_EMPTY;
			grid.statusArray[x][y] = entity;
			player2Y = x;
			player2X = y;
			repaint();
		}
	}
	
	
	/*
		KeyBoard Listener which Calls movePlayer Function when keyEvent Occurs
	*/
	public void keyPressed(KeyEvent e) {
		String code = e.getKeyCode()+"";
		if(enabled)
			movePlayer(e);
	}
	/*
		Other keyListener Methods (NOT USED NOW)
	*/
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}