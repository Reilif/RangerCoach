package de.nondes.rangercoach;

import java.util.ArrayList;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;

public class Gameplan extends ArrayList<Spielzug> implements ObservableObjectValue<Gameplan>{
	public static final int MAX_PLAYS = 40;
	private String name;
	private int id;
	
	private ArrayList<ChangeListener<? super Gameplan>> listener = new ArrayList<>();
	
	@Override
	public boolean add(Spielzug e) {
		boolean add = super.add(e);
		fireChange();
		return add;
	}
	
	@Override
	public boolean remove(Object o) {
		boolean remove = super.remove(o);
		fireChange();
		return remove;
	}
	
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

	@Override
	public void addListener(ChangeListener<? super Gameplan> arg0) {
		listener.add(arg0);
	}

	@Override
	public Gameplan getValue() {
		return this;
	}

	@Override
	public void removeListener(ChangeListener<? super Gameplan> listener) {
		this.listener.remove(listener);
	}

	@Override
	public void addListener(InvalidationListener listener) {
	}

	@Override
	public void removeListener(InvalidationListener listener) {
	}

	@Override
	public Gameplan get() {
		return this;
	}
	
	public void fireChange(){
		for (ChangeListener<? super Gameplan> changeListener : listener) {
			changeListener.changed(null, this, this);
		}
	}
	
}
