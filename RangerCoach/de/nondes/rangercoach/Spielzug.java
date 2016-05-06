package de.nondes.rangercoach;

import de.nondes.rangercoach.Spieler.Block;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

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

	public Spieler[] getPlayer() {

		Spieler center = new Spieler("", 0, 0);
		Spieler leftGuard = new Spieler("", -1, 0);
		Spieler leftTackle = new Spieler("", -2, 0);
		Spieler rightGuard = new Spieler("", 1, 0);
		Spieler rightTackle = new Spieler("", 2, 0);
		
		String blockSchema = getBlockSchema();
		if(blockSchema.toLowerCase().contains("base")){
			center.setBlock(Block.BASE);
			leftGuard.setBlock(Block.BASE);
			leftTackle.setBlock(Block.BASE);
			rightGuard.setBlock(Block.BASE);
			rightTackle.setBlock(Block.BASE);
		}

		Spieler qb = new Spieler(Color.RED, Color.WHITE, "QB");

		Spieler wr10 = new Spieler(Color.WHITE, Color.BLACK, "10");
		Spieler rb20 = new Spieler(Color.YELLOW, Color.BLACK, "20");
		Spieler rb30 = new Spieler(Color.GREEN, Color.WHITE, "30");
		Spieler te40 = new Spieler(Color.GREEN, Color.WHITE, "40");
		Spieler wr50 = new Spieler(Color.WHITE, Color.BLACK, "50");

		String[] split = aufstellung.get().split(" ");
		if (split[0].equalsIgnoreCase("T") || split[0].equalsIgnoreCase("Tight")) {
			getPosForTight(qb, wr10, rb20, rb30, te40, wr50, split);
		} else if (split[0].equalsIgnoreCase("W") || split[0].equalsIgnoreCase("Wing")) {
			getPosForWing(qb, wr10, rb20, rb30, te40, wr50, split);
		} else if (split[0].equalsIgnoreCase("S") || split[0].equalsIgnoreCase("Slot")) {
			getPosForSlot(qb, wr10, rb20, rb30, te40, wr50, split);
		} else if (split[0].equalsIgnoreCase("DW") || split[0].equalsIgnoreCase("DoubleWing")) {
			getPosForDoubleWing(qb, wr10, rb20, rb30, te40, wr50, split);
		}

		Spieler[] ret = { center, leftGuard, leftTackle, rightGuard, rightTackle, qb, wr10, rb20, rb30, te40, wr50 };
		return ret;

	}

	private void getPosForDoubleWing(Spieler qb, Spieler wr10, Spieler rb20, Spieler rb30, Spieler te40, Spieler wr50,
			String[] split) {
		// Direction
		boolean right = split[2].equalsIgnoreCase("R") || split[2].equalsIgnoreCase("Right");
		boolean left = split[2].equalsIgnoreCase("L") || split[2].equalsIgnoreCase("Left");

		// Strong /Weak
		boolean strong = split[1].equalsIgnoreCase("S") || split[1].equalsIgnoreCase("Strong");
		boolean weak = split[1].equalsIgnoreCase("W") || split[1].equalsIgnoreCase("Weak");

		// QB
		qb.setPosY(1);

		wr10.setPosX(-6);
		wr50.setPosX(6);

		int side = 0;
		if (right) {
			side = 1;
		} else if (left) {
			side = -1;
		}

		// 40
		te40.setPosX(side * 3);
		te40.setPosY(1);

		if (strong) {
			side = side;
			// 20
			rb20.setPosX(side * -3);
			rb20.setPosY(1);
			// 30
			rb30.setPosY(3);

		} else if (weak) {
			side = -side;
			// 20
			rb30.setPosX(side * -3);
			rb30.setPosY(1);
			// 30
			rb20.setPosY(3);

		}
	}

	private void getPosForSlot(Spieler qb, Spieler wr10, Spieler rb20, Spieler rb30, Spieler te40, Spieler wr50,
			String[] split) {
		// Direction
		boolean right = split[2].equalsIgnoreCase("R") || split[2].equalsIgnoreCase("Right");
		boolean left = split[2].equalsIgnoreCase("L") || split[2].equalsIgnoreCase("Left");

		// Strong /Weak
		boolean strong = split[1].equalsIgnoreCase("S") || split[1].equalsIgnoreCase("Strong");
		boolean weak = split[1].equalsIgnoreCase("W") || split[1].equalsIgnoreCase("Weak");

		// QB
		qb.setPosY(1);
		wr10.setPosX(-6);
		wr50.setPosX(6);
		rb20.setPosY(3);

		int side = 0;
		if (right) {
			side = 1;
		} else if (left) {
			side = -1;
		}

		// 40
		te40.setPosX(side * 4);
		te40.setPosY(1);

		if (strong) {
			side = side;

		} else if (weak) {
			side = -side;
		}

		rb30.setPosX(side);
		rb30.setPosY(2);

	}

	private void getPosForWing(Spieler qb, Spieler wr10, Spieler rb20, Spieler rb30, Spieler te40, Spieler wr50,
			String[] split) {
		// Direction
		boolean right = split[2].equalsIgnoreCase("R") || split[2].equalsIgnoreCase("Right");
		boolean left = split[2].equalsIgnoreCase("L") || split[2].equalsIgnoreCase("Left");

		// Strong / Weak
		boolean strong = split[1].equalsIgnoreCase("S") || split[1].equalsIgnoreCase("Strong");
		boolean weak = split[1].equalsIgnoreCase("W") || split[1].equalsIgnoreCase("Weak");

		// 20
		rb20.setPosY(3);

		// QB
		qb.setPosY(1);

		wr10.setPosX(-6);

		wr50.setPosX(6);

		int side = 0;
		if (right) {
			side = 1;
		} else if (left) {
			side = -1;
		}

		// 40
		te40.setPosX(side * 3);
		te40.setPosY(1);

		if (strong) {
			side = side;
		} else if (weak) {
			side = -side;
		}

		// 30
		rb30.setPosX(side);
		rb30.setPosY(2);
	}

	private void getPosForTight(Spieler qb, Spieler wr10, Spieler rb20, Spieler rb30, Spieler te40, Spieler wr50,
			String[] split) {
		// Direction
		boolean right = split[2].equalsIgnoreCase("R") || split[2].equalsIgnoreCase("Right");
		boolean left = split[2].equalsIgnoreCase("L") || split[2].equalsIgnoreCase("Left");

		// Strong /Weak
		boolean strong = split[1].equalsIgnoreCase("S") || split[1].equalsIgnoreCase("Strong");
		boolean weak = split[1].equalsIgnoreCase("W") || split[1].equalsIgnoreCase("Weak");

		// 20
		rb20.setPosY(3);

		// QB
		qb.setPosY(1);

		int side = 0;
		if (right) {
			side = 1;
		} else if (left) {
			side = -1;
		}

		// 40
		te40.setPosX(side * 3);

		// 10
		wr10.setPosX(side * -6);

		wr50.setPosX(side * -5);
		wr50.setPosY(1);

		if (strong) {
			side = side;
		} else if (weak) {
			side = -side;
		}

		// 30
		rb30.setPosX(side);
		rb30.setPosY(2);
	}

	@Override
	public String toString() {
		return getAufstellung() + " " + getRoutes();
	}
}
