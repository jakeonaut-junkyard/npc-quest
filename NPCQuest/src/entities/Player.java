package entities;

import entities.helpers.Txt;
import Levels.Room;
import Levels.World;
import Managers.ResourceManager;

public class Player extends NPC{
	private boolean try_interact = false;
	private boolean try_cancel = false;
	
	public Player(float x, float y){
		super(x, y, 0, "", "player_sheet");
		type = "Player";
		facing = UP;
		whatdidisay = Txt.ELLIPSE;
	}
	
	@Override
	public void Update(Room room){
		max_run_vel = 1.4f;
		
		if (try_interact || try_cancel){
			for (int i = 0; i < room.entities.size(); i++){
				if (room.entities.get(i).type.equals("NPC")){
					if (FacingToward((NPC)room.entities.get(i))){
						NPC npc = (NPC)room.entities.get(i);
						if (!npc.visible || npc.fade_away) break;
						if (npc.speaking){
							if (World.num_skulls >= 1){
								if (try_interact){
									ResourceManager.playSound("learn");
									whatdidisay = npc.whatdidisay;
									voice = npc.voice;
									//ChangeNPC_ID(npc.npc_id);
								}else if (try_cancel){
									ResourceManager.playSound("forget");
								}
							}
							
							npc.StopSpeaking(room);
							npc.Event(room);
						}else if (speaking){
							StopSpeaking(room);
							npc.Speak(room, whatdidisay);
						}else if (try_interact){
							Speak(room);
						}
	
						try_cancel = false;
						try_interact = false;
						break;
					}
				}
			}
			
			if (try_interact || try_cancel){
				if (speaking){
					StopSpeaking(room);
				}else if (try_interact){
					Speak(room);
				}
				try_interact = false;
				try_cancel = false;
			}
		}
		
		super.Update(room);
	}
	
	public boolean FacingToward(NPC npc){
		int q = 8;
		if (facing == UP)
			return (IsRectColliding(npc,
				(int)Math.round(x+lb), (int)Math.round(y+tb-q), 
				(int)Math.round(x+rb), (int)Math.round(y+tb)));
		if (facing == DOWN)
			return (IsRectColliding(npc,
				(int)Math.round(x+lb), (int)Math.round(y+bb), 
				(int)Math.round(x+rb), (int)Math.round(y+bb+q)));
		if (facing == LEFT)
			return (IsRectColliding(npc,
				(int)Math.round(x+lb-q), (int)Math.round(y+tb), 
				(int)Math.round(x+lb), (int)Math.round(y+bb)));
		if (facing == RIGHT)
			return (IsRectColliding(npc,
				(int)Math.round(x+rb), (int)Math.round(y+tb), 
				(int)Math.round(x+rb+q), (int)Math.round(y+bb)));
		return false;
	}
	
	public void Speak(Room room){
		speaking = true;
		room.Speak(whatdidisay, npc_id, true);

		ResourceManager.playSound(voice);
	}
	
	public void Interact(){
		try_interact = true;
	}
	
	public void Cancel(){
		try_cancel = true;
		/*if (!whatdidisay.equals(Txt.ELLIPSE))
			ResourceManager.playSound("forget");
		whatdidisay = Txt.ELLIPSE;
		voice = "";
		ChangeNPC_ID(0);*/
	}
}
