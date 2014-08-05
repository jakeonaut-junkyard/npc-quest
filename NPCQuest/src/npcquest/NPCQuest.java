package npcquest;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import Levels.World;
import Managers.KeyManager;

@SuppressWarnings("serial")
public class NPCQuest extends JFrame{
	public static final int GAME_WIDTH = 160;
	public static final int GAME_HEIGHT = 144;
	public static final int VIEW_SCALE = 3;
	
	public static Color[][] PALETTES;
	public static Color COLOR_ZERO;
	public static Color COLOR_ONE;
	public static Color COLOR_TWO;
	public static Color COLOR_THREE;
	
	public static boolean level_edit = true;
	
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
		if (level_edit){
			this.addMouseListener(new MouseListener(){
				@Override
				public void mouseClicked(MouseEvent e) {
					World.MouseClicked(e);
				}
				@Override
				public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseReleased(MouseEvent e) {}
			});
		}
		
		PALETTES = new Color[][]{
			{new Color(245,224,199), new Color(247,128,152), new Color(218,83,2), new Color(123,58,74)}, 
			{new Color(234,216,230), new Color(148,190,194), new Color(96,71,167), new Color(0,64,70)}, 
			{new Color(10, 31, 56), new Color(8,127,103), new Color(67,183,253), new Color(203,197,104)}, 
			{new Color(21,39,25), new Color(107,65,61), new Color(159,184,88), new Color(255,191,185)} 
		};
		
		COLOR_ZERO = PALETTES[1][0];
		COLOR_ONE = PALETTES[1][1];
		COLOR_TWO = PALETTES[1][2];
		COLOR_THREE = PALETTES[1][3];
		add(new GameWindow());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(GAME_WIDTH * VIEW_SCALE + 6, GAME_HEIGHT * VIEW_SCALE + 29);
		setLocationRelativeTo(null);
		setTitle("NPC Quest");
		setResizable(false);
		setVisible(true);
	}
	
	public static void main(String[] args){
		new NPCQuest();
	}
}
