package entities;

import java.awt.geom.Point2D;

import Levels.Room;
import Levels.Tile;

public class GameMover extends GameSprite{
	public final static int LEFT = 10;
	public final static int RIGHT = 11;
	public final static int UP = 12;
	public final static int DOWN = 13;
	
	public final static int STANDING = 0;
	public final static int RUNNING = 1;
	
	public Point2D prev;
	public float max_run_vel = 2.0f;
	public float run_acc = 0.67f;
	public float run_dec = 0.67f;
	public boolean horizontal_input = false;
	public boolean vertical_input = false;
	
	public boolean horizontal_collision = false;
	public boolean vertical_collision = false;
	
	public Point2D vel = new Point2D.Float();	
	public int move_state = STANDING;
	public int prev_move_state = move_state;
	public int facing = DOWN;
	public int original_facing = facing;
	
	public GameMover(float x, float y, int lb, int tb, int rb, int bb, String img_name){
		super(x, y, lb, tb, rb, bb, img_name);
		type = "GameMover";
		prev = new Point2D.Float(x, y);
	}
	
	@Override
	public void ResetPosition(){
		super.ResetPosition();
		facing = original_facing;
	}
	
	@Override
	public void Update(Room room){
		UpdateMoveState();
		UpdateAnimationFromState();
		ApplyPhysics(room);
		prev.setLocation(x, y);
		
		super.Update(room);
	}
	
	public void ApplyPhysics(Room room){
		Point2D prev_pos = new Point2D.Float(x, y);
		
		if (!horizontal_input) HorizontalMoveStop();
		if (!vertical_input) VerticalMoveStop();
		HandleCollisionsAndMove(room);
		horizontal_input = false;
		vertical_input = false;
		
		if (x == prev_pos.getX()) vel.setLocation(0, vel.getY());
		if (y == prev_pos.getY()) vel.setLocation(vel.getX(), 0);
	}
	
	public void HandleCollisionsAndMove(Room room){
		int left_tile = (int)Math.floor((x + lb + vel.getX() - 1) / Tile.WIDTH);
		int right_tile = (int)Math.ceil((x + rb + vel.getX() + 1) / Tile.WIDTH);
		int top_tile = (int)Math.floor((y + tb + vel.getY() - 1) / Tile.HEIGHT);
		int bottom_tile = (int)Math.ceil((y + bb + vel.getY() + 1) / Tile.HEIGHT);
		
		int q_horz = 3;
		int q_vert = 3;
		HandleHorizontalCollision(room, left_tile, right_tile, top_tile, bottom_tile, q_horz);
		x += vel.getX();
		HandleVerticalCollision(room, left_tile, right_tile, top_tile, bottom_tile, q_vert);
		y += vel.getY();
	}
	
	public void HandleHorizontalCollision(Room room, int left_tile, int right_tile, int top_tile, int bottom_tile, int q){
		horizontal_collision = false;
		for (int i = top_tile; i <= bottom_tile; i++){
			for (int j = left_tile; j <= right_tile; j++){
				if (!room.isValidTile(i, j)) continue;
				Tile tile = room.tiles.get(i).get(j);
				if (tile.collision != Tile.SOLID) continue;
				
				HorizontalCollision((GameObject)tile, q);
			}
		}
		
		for (int i = 0; i < room.entities.size(); i++){
			if (room.entities.get(i).solid && room.entities.get(i) != this){
				HorizontalCollision(room.entities.get(i), q);
			}
		}
	}
	
	public void HorizontalCollision(GameObject obj, int q){
		//Check for left collisions
		if (vel.getX() < 0 && IsRectColliding(obj, 
				(int)Math.round(x+lb+vel.getX()+1), (int)Math.round(y+tb+q), 
				(int)Math.round(x+lb), (int)Math.round(y+bb-q))){
			vel.setLocation(0, vel.getY());
			horizontal_collision = true;
			x = obj.x + obj.rb - lb;
		}
		
		//Check for right collisions
		if (vel.getX() > 0 && IsRectColliding(obj,
				(int)Math.round(x+rb), (int)Math.round(y+tb+q),
				(int)Math.round(x+rb+vel.getX()+1), (int)Math.round(y+bb-q))){
			vel.setLocation(0, vel.getY());
			horizontal_collision = true;
			x = obj.x + obj.lb - rb;
		}
	}
	
	public void HandleVerticalCollision(Room room, int left_tile, int right_tile, int top_tile, int bottom_tile, int q){
		vertical_collision = false;
		for (int i = top_tile; i <= bottom_tile; i++){
			for (int j = left_tile; j <= right_tile; j++){
				if (!room.isValidTile(i, j)) continue;
				Tile tile = room.tiles.get(i).get(j);
				if (tile.collision != Tile.SOLID) continue;
				
				VerticalCollision((GameObject) tile, q);
			}
		}
		
		for (int i = 0; i < room.entities.size(); i++){
			if (room.entities.get(i).solid && room.entities.get(i) != this){
				VerticalCollision(room.entities.get(i), q);
			}
		}
	}
	
	public void VerticalCollision(GameObject obj, int q){
		//Check for top collisions
		if (vel.getY() < 0 && IsRectColliding(obj,
				(int)Math.round(x+lb+q), (int)Math.round(y+tb+vel.getY()-1),
				(int)Math.round(x+rb-q), (int)Math.round(y+tb))){
			vel.setLocation(vel.getX(), 0);
			vertical_collision = true;
			y = obj.y + obj.bb - tb;
		}
		
		//Check for bottom collisions
		if (vel.getY() > 0 && IsRectColliding(obj,
				(int)Math.round(x+lb+q), (int)Math.round(y+bb),
				(int)Math.round(x+rb-q), (int)Math.round(y+bb+vel.getY()+1))){
			vel.setLocation(vel.getX(), 0);
			vertical_collision = true;
			y = obj.y + obj.tb - bb;
		}
	}
	
	public void UpdateMoveState(){
		if (horizontal_input || vertical_input) 
			move_state = RUNNING;
		else move_state = STANDING;
	}
	
	public void UpdateAnimationFromState(){
		if (vertical_input || horizontal_input){
			animation.frame_delay = 8;
		}else animation.frame_delay = 16;
		
		switch (facing){
			case LEFT:
				animation.Change(2, 1, 2);
				break;
			case RIGHT:
				animation.Change(0, 1, 2);
				break;
			case UP:
				animation.Change(2, 0, 2);
				break;
			case DOWN: default:
				animation.Change(0, 0, 2);
				break;
		}
		prev_move_state = move_state;
	}
	
	public void HorizontalMoveStop(){
		if (vel.getX() > 0){
			vel.setLocation(vel.getX() - run_dec, vel.getY());
			if (vel.getX() < 0)
				vel.setLocation(0, vel.getY());
		}else if (vel.getX() < 0){
			vel.setLocation(vel.getX() + run_dec, vel.getY());
			if (vel.getX() > 0)
				vel.setLocation(0, vel.getY());
		}
	}
	
	public void VerticalMoveStop(){
		if (vel.getY() > 0){
			vel.setLocation(vel.getX(), vel.getY() - run_dec);
			if (vel.getY() < 0)
				vel.setLocation(vel.getX(), 0);
		}else if (vel.getY() < 0){
			vel.setLocation(vel.getX(), vel.getY() + run_dec);
			if (vel.getY() > 0)
				vel.setLocation(vel.getX(), 0);
		}
	}
	
	/*****************FUNCTIONS FOR MOVEMENT INPUT*****************/
	public void MoveLeft(){
		facing = LEFT;
		MoveHorizontal(-1);
	}
	
	public void MoveRight(){
		facing = RIGHT;
		MoveHorizontal(1);
	}
	
	public void MoveUp(){
		facing = UP;
		MoveVertical(-1);
	}
	
	public void MoveDown(){
		facing = DOWN;
		MoveVertical(1);
	}
	
	public void MoveHorizontal(int mult){
		horizontal_input = true;
		ResetHorizontalVelocity(mult);
		if (Math.abs(vel.getX()) < max_run_vel){
			vel.setLocation(vel.getX() + (run_acc*mult), vel.getY());
			CorrectHorizontalVelocity(mult);
		}else if (Math.abs(vel.getX()) > max_run_vel){
			vel.setLocation(vel.getX() - (run_acc*mult), vel.getY());
		}else if (Math.abs(vel.getX()) == max_run_vel && vel.getX() != max_run_vel * mult){
			vel.setLocation(vel.getX() + (run_acc*mult), vel.getY());
		}
	}
	
	public void MoveVertical(int mult){
		vertical_input = true;
		ResetVerticalVelocity(mult);
		if (Math.abs(vel.getY()) < max_run_vel){
			vel.setLocation(vel.getX(), vel.getY() + (run_acc*mult));
			CorrectVerticalVelocity(mult);
		}else if (Math.abs(vel.getY()) > max_run_vel){
			vel.setLocation(vel.getX(), vel.getY() - (run_acc*mult));
		}else if (Math.abs(vel.getY()) == max_run_vel && vel.getY() != max_run_vel * mult){
			vel.setLocation(vel.getX(), vel.getY() + (run_acc*mult));
		}
	}
	
	public void ResetHorizontalVelocity(int mult){
		if (vel.getX() * mult < 0) vel.setLocation(0, vel.getY());
	}
	
	public void ResetVerticalVelocity(int mult){
		if (vel.getY() * mult < 0) vel.setLocation(vel.getX(), 0);
	}
	
	public void CorrectHorizontalVelocity(int mult){
		if (Math.abs(vel.getX()) > max_run_vel){
			vel.setLocation(max_run_vel * mult, vel.getY());
		}
	}
	
	public void CorrectVerticalVelocity(int mult){
		if (Math.abs(vel.getY()) > max_run_vel){
			vel.setLocation(vel.getX(), max_run_vel * mult);
		}
	}
}
