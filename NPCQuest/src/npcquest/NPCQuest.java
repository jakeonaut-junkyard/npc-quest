package npcquest;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import Managers.KeyManager;

@SuppressWarnings("serial")
public class NPCQuest extends JFrame{
	public static final int GAME_WIDTH = 160;
	public static final int GAME_HEIGHT = 144;
	public static final int VIEW_SCALE = 4;
	public static Color COLOR_ZERO;
	public static Color COLOR_ONE;
	public static Color COLOR_TWO;
	public static Color COLOR_THREE;
	
	public NPCQuest(){
		this.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent e) {
				KeyManager.KeyPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				KeyManager.KeyReleased(e);
			}

			@Override
			public void keyTyped(KeyEvent e) {
				KeyManager.KeyTyped(e);
			}
		});
		add(new GameWindow());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(GAME_WIDTH * VIEW_SCALE, GAME_HEIGHT * VIEW_SCALE);
		setLocationRelativeTo(null);
		setTitle("NPC Quest");
		setResizable(false);
		setVisible(true);
		
		COLOR_ZERO = new Color(234, 216, 230);
		COLOR_ONE = new Color(148, 190, 194);
		COLOR_TWO = new Color(96, 71, 167);
		COLOR_THREE = new Color(0, 64, 70);
	}
	
	public static void main(String[] args){
		new NPCQuest();
	}
}
