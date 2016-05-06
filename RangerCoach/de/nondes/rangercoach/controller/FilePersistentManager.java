package de.nondes.rangercoach.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.JsonWriter;

import de.nondes.rangercoach.Gameplan;
import de.nondes.rangercoach.Spielzug;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class FilePersistentManager implements PersistentManager {
	
	private static final String TYPE = "Type";
	private static final String PLAN_PLAYS = "PlanPlays";
	private static final String NAME = "Name";
	private static final String GAMEPLANS2 = "gameplans";
	private static final String PLAYS2 = "plays";
	private static final String AUFSTELLUNG = "Aufstellung";
	private static final String ROUTES = "Routen";
	private static final String BLOCK = "Block";
	private static final String ID = "Id";
	private ObservableMap<Integer, Spielzug> plays = FXCollections.observableHashMap();
	private ObservableList<Gameplan> gameplans = FXCollections.observableArrayList();
	private File file;
	private int maxId = 0;
	private int maxGpId = 0;
	
	/**
	 * Manager for files
	 */
	public FilePersistentManager() {
		Gameplan gameplan = new Gameplan();
		gameplan.setName("Alle Spielzüge");
		gameplan.setId(-1);
		gameplans.add(gameplan);
	}

	@Override
	public ObservableMap<Integer, Spielzug> getAllPlays() {
		return plays;
	}

	@Override
	public boolean loadPlays() {
		FileReader reader;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			return false;
		} 
		JsonReader createReader = Json.createReader(reader);
		JsonStructure jsonst;
		try {
			jsonst = createReader.read();
		} catch (Exception e) {
			return false;
		}
		
		if(jsonst.getValueType() != ValueType.OBJECT){
			return false;
		}
		
		JsonObject obj = (JsonObject) jsonst;
		
		JsonArray playsJSON = obj.getJsonArray(PLAYS2);
		
		maxId = 0;
		for (JsonValue jsonValue : playsJSON) {
			if(jsonValue.getValueType() == ValueType.OBJECT){
				Spielzug playFromJSON = getPlayFromJSON((JsonObject) jsonValue);
				if(playFromJSON.getId() > maxId){
					maxId =playFromJSON.getId(); 
				}
				plays.put(playFromJSON.getId(), playFromJSON);
			}
		}
		
		maxGpId = 0;
		JsonArray plansJSON = obj.getJsonArray(GAMEPLANS2);
		for (JsonValue jsonValue : plansJSON) {
			if(jsonValue.getValueType() == ValueType.OBJECT){
				Gameplan planFromJSON = getGameplanFromJSON((JsonObject) jsonValue);
				
				if(maxGpId < planFromJSON.getId()){
					maxGpId = planFromJSON.getId();
				}
				gameplans.add(planFromJSON);
			}
		}
		
		
		return true;
	}

	private Gameplan getGameplanFromJSON(JsonObject jsonGameplan) {
		Gameplan gameplan = new Gameplan();
		gameplan.setId(jsonGameplan.getInt(ID));
		gameplan.setName(jsonGameplan.getString(NAME));
		
		JsonArray jsonArray = jsonGameplan.getJsonArray(PLAN_PLAYS);
		for (JsonValue jsonID : jsonArray) {
			if(jsonID.getValueType() == ValueType.NUMBER){
				int id = ((JsonNumber) jsonID).intValue();
				Spielzug spielzug = plays.get(id);
				if(spielzug!=null){
					gameplan.add(spielzug);
				}
			}
		}
		return gameplan;
	}

	@Override
	public boolean savePlays() {
		FileWriter writer;
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		
		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		
		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
		
		Collection<Spielzug> values = this.plays.values();
		for (Spielzug spielzug : values) {
			JsonObject jsonObject = createJSONFromPlay(spielzug);
			jsonArrayBuilder.add(jsonObject);
		}
		
		
		JsonArray plays = jsonArrayBuilder.build();
		jsonObjectBuilder.add(PLAYS2, plays);
		
		JsonArrayBuilder jsonArrayBuilderGameplans = Json.createArrayBuilder();
				
		for (Gameplan gameplan : this.gameplans) {
			if(gameplan.getId() < 0){
				continue;
			}
			JsonObject jsonGameplay = createJSONFromGameplay(gameplan);
			jsonArrayBuilderGameplans.add(jsonGameplay);
		}
		
		JsonArray jsonArrayGameplans = jsonArrayBuilderGameplans.build();
		jsonObjectBuilder.add(GAMEPLANS2, jsonArrayGameplans);
		
		
		JsonObject obj = jsonObjectBuilder.build();
		
		JsonWriter jsonWriter = Json.createWriter(writer);
		jsonWriter.writeObject(obj);
		
		try {
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
		
	}

	private JsonObject createJSONFromGameplay(Gameplan gameplan) {
		
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		objectBuilder.add(ID, gameplan.getId());
		objectBuilder.add(NAME, gameplan.getName());
		
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		for (Spielzug spielzug : gameplan) {
			arrayBuilder.add(spielzug.getId());
		}
		
		JsonArray build = arrayBuilder.build();
		objectBuilder.add(PLAN_PLAYS, build);
		
		
		return objectBuilder.build();
		
	}
	
	private JsonObject createJSONFromPlay(Spielzug spielzug) {
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		
		objectBuilder.add(ID, spielzug.getId());
		objectBuilder.add(AUFSTELLUNG, spielzug.getAufstellung());
		objectBuilder.add(BLOCK, spielzug.getBlockSchema());
		objectBuilder.add(ROUTES, spielzug.getRoutes());
		objectBuilder.add(TYPE, spielzug.getType());
		

		JsonObject jsonObject = objectBuilder.build();
		
		return jsonObject;
		
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public ObservableList<Gameplan> getAllGameplans() {
		return gameplans;
	}
	
	private Spielzug getPlayFromJSON(JsonObject obj){
		Spielzug spielzug = new Spielzug();
		spielzug.setId(obj.getInt(ID));
		spielzug.setAufstellung(obj.getString(AUFSTELLUNG));
		spielzug.setBlockSchema(obj.getString(BLOCK));
		spielzug.setRoutes(obj.getString(ROUTES));
		spielzug.setType(obj.getString(TYPE));
		
		return spielzug;
	}

	@Override
	public Spielzug getNewSpielzug() {
		Spielzug spielzug = new Spielzug();
		spielzug.setId(++maxId);
		
		plays.put(spielzug.getId(), spielzug);
		return spielzug;
	}

	@Override
	public Gameplan getNewGameplan() {
		Gameplan gameplan = new Gameplan();
		gameplan.setId(++maxGpId);
		gameplans.add(gameplan);
		return gameplan;
	}

	@Override
	public void removeSpielzug(Spielzug spielzug) {
		plays.remove(spielzug.getId());
		for (Gameplan gameplan : gameplans) {
			gameplan.remove(spielzug);
		}
	}

	@Override
	public void removeGameplan(Gameplan gameplan) {
		gameplans.remove(gameplan);
	}

}
