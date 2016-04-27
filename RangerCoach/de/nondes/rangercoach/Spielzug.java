package de.nondes.rangercoach;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Spielzug {
		
	private final StringProperty aufstellung = new SimpleStringProperty();
	private String motion;
	private final StringProperty type = new SimpleStringProperty();
	private final StringProperty blockSchema = new SimpleStringProperty();
	private final StringProperty routes = new SimpleStringProperty();
	private int id;
	
	public Spielzug() {
	}

	public String getAufstellung() {
		return aufstellung.get();
	}

	public void setAufstellung(String aufstellung) {
		this.aufstellung.set(aufstellung);
	}

	public String getMotion() {
		return motion;
	}

	public void setMotion(String motion) {
		this.motion = motion;
	}

	public String getType() {
		return type.get();
	}

	public void setType(String type) {
		this.type.set(type);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBlockSchema() {
		return blockSchema.get();
	}

	public void setBlockSchema(String blockSchema) {
		this.blockSchema.set(blockSchema);
	}

	public String getRoutes() {
		return routes.get();
	}
	
	public void setRoutes(String routes) {
		this.routes.set(routes);
	}
	
	@Override
	public String toString() {
		return getAufstellung()+ " "+ getRoutes();
	}
}
