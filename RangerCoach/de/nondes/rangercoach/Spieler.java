package de.nondes.rangercoach;

import javafx.scene.paint.Color;

public class Spieler {

	public enum Block{
		KEIN, BASE, ZONE_L, ZONE_R, SPRINT_ZONE_L, SPRINT_ZONE_R, GO_21, BOB, PASS;
	}
	
	private Color bgColor = Color.BLACK;
	private Color fgColor = Color.WHITE;

	private String name = "";
	private int posX = 0;
	private int posY = 0;
	private String run = "";
	private Block block = Block.KEIN;
	
	
	public Spieler() {
	}
	
	
	
	public Spieler(Color bgColor, Color fgColor, String name) {
		super();
		this.bgColor = bgColor;
		this.fgColor = fgColor;
		this.name = name;
	}



	public Spieler(String name, int posX, int posY) {
		super();
		this.name = name;
		this.posX = posX;
		this.posY = posY;
	}



	public Color getBgColor() {
		return bgColor;
	}
	public Color getFgColor() {
		return fgColor;
	}
	public String getName() {
		return name;
	}
	public int getPosX() {
		return posX;
	}
	public int getPosY() {
		return posY;
	}
	public String getRun() {
		return run;
	}
	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}
	public void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
	public void setRun(String run) {
		this.run = run;
	}



	public Block getBlock() {
		return block;
	}



	public void setBlock(Block block) {
		this.block = block;
	}

	
	
}
