package main.game.tetris.util;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Statistics {
	private final static String path = "/main/game/tetris"; 
	
	//main preference
	private static Preferences preference = Preferences.userRoot().node(path);
	
	//intended to keep track of lazy players
	private static Preferences lazyPlayer = Preferences.userRoot().node(path);
	
	public static void updateHighScore(String name, int score, int level, int lines, int[] tetriminoStat, int[] lineStat) {
		if(name.isBlank()) {
			int numOfLazyPlayer = lazyPlayer.getInt("LAZY_PLAYER", 0) + 1;
			lazyPlayer.putInt("LAZY_PLAYER", numOfLazyPlayer);
			name = "UNKNOWN_LAZY_PLAYER #" + numOfLazyPlayer;
		}
		
		preference.put("NAME", name);
		preference.putInt("SCORE", score);
		preference.putInt("LEVEL", level);
		preference.putInt("LINES", lines);
		
		//tetriminoes statistics
		preference.putInt("J-SHAPE", tetriminoStat[0]);
		preference.putInt("L-SHAPE", tetriminoStat[1]);
		preference.putInt("Z-SHAPE", tetriminoStat[2]);
		preference.putInt("S-SHAPE", tetriminoStat[3]);
		preference.putInt("I-SHAPE", tetriminoStat[4]);
		preference.putInt("T-SHAPE", tetriminoStat[5]);
		preference.putInt("O-SHAPE", tetriminoStat[6]);
		
		//line statistics
		preference.putInt("1-LINE", lineStat[0]);
		preference.putInt("2-LINES", lineStat[1]);
		preference.putInt("3-LINES", lineStat[2]);
		preference.putInt("TETRIS", lineStat[3]);
	}
	
	//always prompt a question to ensure that deletion of data has the user's approval
	public static void resetPreferences() {
		try {
			preference.clear();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	public static String getUserName() {	return preference.get("NAME", "---------");	}
	public static int getHighScore() {		return preference.getInt("SCORE", 0);	}
	public static int getLevel() {			return preference.getInt("LEVEL", 0);	}
	public static int getLines() {			return preference.getInt("LINES", 0);	}
	
	//tetriminoStat
	public static int getJShape() {			return preference.getInt("J-SHAPE", 0);	}
	public static int getLShape() {			return preference.getInt("L-SHAPE", 0);	}
	public static int getZShape() {			return preference.getInt("Z-SHAPE", 0);	}
	public static int getSShape() {			return preference.getInt("S-SHAPE", 0);	}
	public static int getIShape() {			return preference.getInt("I-SHAPE", 0);	}
	public static int getTShape() {			return preference.getInt("T-SHAPE", 0);	}
	public static int getOShape() {			return preference.getInt("O-SHAPE", 0);	}
	
	//lineStat
	public static int getOneLine() {		return preference.getInt("1-LINE", 0);	}
	public static int getTwoLines() {		return preference.getInt("2-LINES", 0);	}
	public static int getThreeLines() {		return preference.getInt("3-LINES", 0);	}
	public static int getTetris() {			return preference.getInt("TETRIS", 0);	}
}