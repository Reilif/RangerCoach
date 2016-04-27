package de.nondes.rangercoach;

public enum Seite {
	LEFT("Left"), RIGHT("Right");
	
	private String name;

	Seite(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public char getShort() {
		return name.charAt(0);
	}

}
