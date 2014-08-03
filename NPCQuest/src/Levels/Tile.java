package Levels;

import java.awt.Graphics2D;
import java.awt.Image;

import entities.GameObject;

public class Tile extends GameObject{
	public static final int WIDTH = 16;
	public static final int HEIGHT = 16;
	public static final int GHOST = 0;
	public static final int SOLID = 1;
	
	public int collision;
	public int tileset_x;
	public int tileset_y;
	
	public Tile(float x, float y, int collision, int tx, int ty){
		super(x, y, 0, 0, WIDTH, HEIGHT);
		type = "Tile";
		this.collision = collision;
		tileset_x = tx;
		tileset_y = ty;
	}
	
	public void Render(Graphics2D g2d, Camera camera, Image image){
		int row = tileset_y;
		int column = tileset_x;
		int dx = (int)Math.round(this.x-camera.x+camera.screen_offset_x);
		int dy = (int)Math.round(this.y-camera.y+camera.screen_offset_y);
		int sx = WIDTH*column;
		int sy = HEIGHT*row;
		
		g2d.drawImage(image,
			//DESTINATION
			dx, dy, dx+WIDTH, dy+HEIGHT,
			//SOURCE
			sx, sy, sx + WIDTH, sy + HEIGHT,
			//OBSERVER
			null); 
	}
}
