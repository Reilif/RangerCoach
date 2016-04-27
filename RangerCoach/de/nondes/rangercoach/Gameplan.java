package de.nondes.rangercoach;

import java.util.ArrayList;

public class Gameplan extends ArrayList<Spielzug>{
	public static final int MAX_PLAYS = 40;
	private String name;
	private int id;
	
	public Gameplan() {
		super(MAX_PLAYS);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
