import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ListenerClass implements Runnable{

	public Game game;
	public BufferedReader br;
	ListenerClass(Game game,BufferedReader br){
		this.game = game;
		this.br = br;
	}
	
	@Override
	public void run(){
			System.out.println("Receiving...In ListenClass");
		while(true){
			String out;
			try{
			while((out = br.readLine())!=null){
					String[] receivedString = out.split(" ");
					game.receive(Integer.parseInt(receivedString[0]),Integer.parseInt(receivedString[1]),Integer.parseInt(receivedString[2]));
				}
			//break;
			}catch(Exception e){e.printStackTrace();break;}
		}
	}
}