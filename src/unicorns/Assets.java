package unicorns;

import java.awt.Image;
import java.net.URLClassLoader;

import javax.swing.ImageIcon;

import unicorns.hypervox.Animation;

public class Assets {
	
	public static final Image BOARD=new ImageIcon("assets/board.png").getImage();
	public static final Image BOARD_S=new ImageIcon("assets/boardS.png").getImage();
	public static final Image BOARD_1D=new ImageIcon("assets/1Dboard.png").getImage();
	public static final Image BOARD_2D=new ImageIcon("assets/2Dboard.png").getImage();
	public static final Image BOARD_2DSM=new ImageIcon("assets/2Dboardsm.png").getImage();
	public static final Image BOARD_3D=new ImageIcon("assets/3Dboard.png").getImage();
	public static final Image BOARD_2DFLAT=new ImageIcon("assets/2Dboardflat.png").getImage();
	public static final Image HOTKEYS=new ImageIcon("assets/hotkeys.png").getImage();
	
	public static final Image GHOST_W=new ImageIcon("assets/GhostW.png").getImage();
	public static final Image PAWN_W=new ImageIcon("assets/PawnW.png").getImage();
	public static final Image KNIGHT_W=new ImageIcon("assets/KnightW.png").getImage();
	public static final Image KING_W=new ImageIcon("assets/KingW.png").getImage();
	public static final Image ROOK_W=new ImageIcon("assets/RookW.png").getImage();
	public static final Image BISHOP_W=new ImageIcon("assets/BishopW.png").getImage();
	public static final Image UNICORN_W=new ImageIcon("assets/UnicornW.png").getImage();
	public static final Image DRAGON_W=new ImageIcon("assets/DragonW.png").getImage();
	public static final Image QUEEN_W=new ImageIcon("assets/QueenW.png").getImage();

	public static final Image GHOST_B=new ImageIcon("assets/GhostB.png").getImage();
	public static final Image PAWN_B=new ImageIcon("assets/PawnB.png").getImage();
	public static final Image KNIGHT_B=new ImageIcon("assets/KnightB.png").getImage();
	public static final Image KING_B=new ImageIcon("assets/KingB.png").getImage();
	public static final Image ROOK_B=new ImageIcon("assets/RookB.png").getImage();
	public static final Image BISHOP_B=new ImageIcon("assets/BishopB.png").getImage();
	public static final Image UNICORN_B=new ImageIcon("assets/UnicornB.png").getImage();
	public static final Image DRAGON_B=new ImageIcon("assets/DragonB.png").getImage();
	public static final Image QUEEN_B=new ImageIcon("assets/QueenB.png").getImage();
	
	
	
	//button images
	public static final Image PLAY_BUTTON=new ImageIcon("assets/buttons/play.png").getImage();
	public static final Image EXIT_BUTTON=new ImageIcon("assets/buttons/exit.png").getImage();
	public static final Image SAVE_BUTTON=new ImageIcon("assets/buttons/save.png").getImage();
	public static final Image LOAD_BUTTON=new ImageIcon("assets/buttons/load.png").getImage();
	public static final Image CLOCKS_OFF_BUTTON=new ImageIcon("assets/buttons/clocksOff.png").getImage();
	public static final Image CLOCKS_ON_BUTTON=new ImageIcon("assets/buttons/clocksOn.png").getImage();
	public static final Image HUMAN_BUTTON=new ImageIcon("assets/buttons/human.png").getImage();
	public static final Image AI_BUTTON=new ImageIcon("assets/buttons/ai.png").getImage();
	public static final Image MAP_BUTTON=new ImageIcon("assets/buttons/map.png").getImage();
	public static final Image NEW_BUTTON=new ImageIcon("assets/buttons/new.png").getImage();
	public static final Image ONLINE_BUTTON=new ImageIcon("assets/buttons/online.png").getImage();
	public static final Image TUTORIAL_BUTTON=new ImageIcon("assets/buttons/tutorial.png").getImage();
	public static final Image MENU_BUTTON=new ImageIcon("assets/buttons/menu_button.png").getImage();
	public static final Image CREATE_BUTTON=new ImageIcon("assets/buttons/create.png").getImage();
	public static final Image JOIN_BUTTON=new ImageIcon("assets/buttons/join.png").getImage();	
	public static final Image ANALYZE_BUTTON=new ImageIcon("assets/buttons/analyze.png").getImage();	
	public static final Image COPY_BUTTON=new ImageIcon("assets/buttons/copy.png").getImage();	
	
	public static final Image MENU=new ImageIcon("assets/buttons/menu.png").getImage();
	
	public static final int BUTTON_WIDTH=PLAY_BUTTON.getWidth(null);
	public static final int BUTTON_HEIGHT=PLAY_BUTTON.getHeight(null);
	
	public static final Image TEXT_BOX=new ImageIcon("assets/textbox.png").getImage();
	public static final Image LABLE=new ImageIcon("assets/LableDR.png").getImage();
	public static final Image LABLE_CONTINUE=new ImageIcon("assets/LableContinue.png").getImage();
	
	public static final Image PORTRAIT=new ImageIcon("assets/portraitbg.png").getImage();
	public static final Image HEART=new ImageIcon("assets/heart.png").getImage();
	
	//For Dee stuff (the unicorn above is also used in For Dee)
	public static final Image BOARD_5=new ImageIcon("assets/board5.png").getImage();
	public static final Image CRYSTAL=new ImageIcon("assets/crystal.png").getImage();
	public static final Image VOX=new ImageIcon("assets/floor.png").getImage();
	public static final Image EXIT=new ImageIcon("assets/exit.png").getImage();
	public static final Image PARAVOX=new ImageIcon("assets/paravox.png").getImage();
	public static final Image WALL=new ImageIcon("assets/vox.png").getImage();
	public static final Image HYPERCRATE=new ImageIcon("assets/hypercrate.png").getImage();
	public static final Image WIRE_OFF=new ImageIcon("assets/wireOff.png").getImage();
	public static final Image WIRE_ON_1=new ImageIcon("assets/wireOn1.png").getImage();
	public static final Image WIRE_ON_2=new ImageIcon("assets/wireOn2.png").getImage();
	public static final Image H_GEN_OFF=new ImageIcon("assets/hgenOff.png").getImage();
	public static final Image H_GEN_ON_1=new ImageIcon("assets/hgenOn_0000.png").getImage();
	public static final Image H_GEN_ON_2=new ImageIcon("assets/hgenOn_0001.png").getImage();
	public static final Image H_GEN_ON_3=new ImageIcon("assets/hgenOn_0002.png").getImage();
	public static final Image H_GEN_ON_4=new ImageIcon("assets/hgenOn_0003.png").getImage();
	public static final Image H_GEN_ON_5=new ImageIcon("assets/hgenOn_0004.png").getImage();
	public static final Image H_GEN_ON_6=new ImageIcon("assets/hgenOn_0005.png").getImage();
	public static final Image H_GEN_ON_7=new ImageIcon("assets/hgenOn_0006.png").getImage();
	public static final Image H_GEN_ON_8=new ImageIcon("assets/hgenOn_0007.png").getImage();
	public static final Image H_GEN_ON_9=new ImageIcon("assets/hgenOn_0008.png").getImage();
	public static final Image H_GEN_ON_10=new ImageIcon("assets/hgenOn_0009.png").getImage();
	public static final Image BREAK = new ImageIcon("assets/break.png").getImage();
	public static final Animation WIRE_ON=new Animation(new Image[] {WIRE_ON_1,WIRE_ON_2},500);
	public static final Animation H_GEN_ON=new Animation(new Image[] {
			H_GEN_ON_1,
			H_GEN_ON_2,
			H_GEN_ON_3,
			H_GEN_ON_4,
			H_GEN_ON_5,
			H_GEN_ON_6,
			H_GEN_ON_7,
			H_GEN_ON_8,
			H_GEN_ON_9,
			H_GEN_ON_10,});
	
}
