package Managers;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class ResourceManager {
	public static Map<String, Image> images;
	
	public static void init(){		
		images = new HashMap<String, Image>();
		String[] image_names = {
			"player_sheet",
			"npc_sheet",
			"tilesheet"
		};
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		for (int i = 0; i < image_names.length; i++){
			ImageIcon ii = new ImageIcon(
					loader.getResource("resources/"+image_names[i]+".png"));
			images.put(image_names[i], ii.getImage());
		}
	}
}
