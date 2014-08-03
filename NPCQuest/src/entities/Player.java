package entities;

import Levels.Room;

public class Player extends NPC{
	private boolean try_interact = false;
	
	public Player(float x, float y){
		super(x, y, 0, "player_sheet");
		type = "Player";
		facing = UP;
		whatdidisay = "...";
	}
	
	@Override
	public void Update(Room room){
		if (try_interact){
			int q = 16;
			for (int i = 0; i < room.entities.size(); i++){
				if (room.entities.get(i).type == "NPC" && IsRectColliding(room.entities.get(i),
						(int)Math.round(x+lb-q), (int)Math.round(y+tb-q), 
						(int)Math.round(x+rb+q), (int)Math.round(y+bb+q))){

					NPC npc = (NPC)room.entities.get(i);
					if (npc.speaking){
						whatdidisay = npc.whatdidisay;
						npc.StopSpeaking(room);
						//ChangeNPC_ID(npc.npc_id);
					}else if (speaking){
						StopSpeaking(room);
						npc.Speak(room, whatdidisay);
					}else{
						Speak(room);
					}
					break;
				}
			}
		}
		try_interact = false;
		
		super.Update(room);
	}
	
	public void Speak(Room room){
		speaking = true;
		room.Speak(whatdidisay, npc_id, (type == "Player"));
	}
	
	public void Interact(){
		try_interact = true;
	}
	
	public void Cancel(){
		whatdidisay = "...";
		ChangeNPC_ID(0);
	}
}
