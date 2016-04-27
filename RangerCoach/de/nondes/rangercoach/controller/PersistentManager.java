package de.nondes.rangercoach.controller;

import java.util.List;

import de.nondes.rangercoach.Gameplan;
import de.nondes.rangercoach.Spielzug;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public interface PersistentManager {
	
	/**
	 * Returns a list of all saved plays
	 * @return
	 */
	public ObservableMap<Integer, Spielzug> getAllPlays();
	
	/**
	 * Returns a list of all saved gameplans
	 * @return
	 */
	public ObservableList<Gameplan> getAllGameplans();
	
	/**
	 * Load plays from the persistent memory
	 * @return
	 */
	public boolean loadPlays();
	
	/**
	 * Saves all plays to the persistent memory
	 * @return
	 */
	public boolean savePlays();
	
	/**
	 * Creates a new Play with a defined ID
	 * @return
	 */
	public Spielzug getNewSpielzug();
	
	/**
	 * Creates a new Play with a defined ID
	 * @return
	 */
	public Gameplan getNewGameplan();

	/**
	 * Creates a new Play with a defined ID
	 * @return
	 */
	public void removeSpielzug(Spielzug spielzug);
	
	/**
	 * Creates a new Play with a defined ID
	 * @return
	 */
	public void removeGameplan(Gameplan gameplan);
	
}
