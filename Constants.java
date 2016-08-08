import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.image.*;
import java.net.*;
import java.io.*;

public final class Constants{
	
	final static int ARENA_ROWS = 19;
	final static int ARENA_COLS = 28;
	final static int ARENA_BLOCK_SIZE = 35;
	final static int ARENA_EMPTY = 0;//Green Color
	final static int ARENA_BRICK = 1;//Red Color
	final static int ARENA_METAL = 2;//Light Gray Color
	final static int ARENA_PLAYER = 3;//Black Color
	final static int ARENA_BOMB = 4;//Yellow Color
	final static int ARENA_PLAYER_BOMB = 5;//Blue Color
	final static int ARENA_BOMB_EXPLOSION = 6;//Orange Color

	final static int ARENA_EXPLOSION_RIGHT = 1;
	final static int ARENA_EXPLOSION_LEFT = 0;
	final static int ARENA_EXPLOSION_UP = 1;
	final static int ARENA_EXPLOSION_DOWN = 0;
}