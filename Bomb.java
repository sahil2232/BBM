import java.util.*;

public class Bomb{
	public int bombX,bombY;
	public boolean explode;
	public boolean explodeTime;
	public boolean used;
	Bomb(int bombX,int bombY){
		this.bombX = bombX;
		this.bombY = bombY;
		explode = false;
		explodeTime = false;
		used = false;
	}	
}