package application;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import de.nondes.rangercoach.Gameplan;
import de.nondes.rangercoach.Spielzug;
import de.nondes.rangercoach.controller.FilePersistentManager;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;

public class MainFrameController {

	@FXML
	ComboBox<Gameplan> cbGameplan;

	@FXML
	TableColumn<Spielzug, String> clBlock;

	@FXML
	TableColumn<Spielzug, String> clLineup;

	@FXML
	TableColumn<Spielzug, String> clRoutes;

	@FXML
	TableColumn<Spielzug, String> clType;

	private FilePersistentManager persistentManager;

	@FXML
	Parent root;

	private String sep;

	@FXML
	TableView<Spielzug> tablePlays;

	public MainFrameController() {
		persistentManager = new FilePersistentManager();
		
		String property = System.getProperty("user.home");
		sep = System.getProperty("file.separator");
		File defaultFile = new File(property+sep+".default.rcf");
		if(!defaultFile.exists()){
			try {
				defaultFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		persistentManager.setFile(defaultFile);
		persistentManager.loadPlays();
	}

	@FXML
    void addPlayToGameplan(ActionEvent event) {
		
    	
    	Spielzug spielzug = tablePlays.getSelectionModel().getSelectedItem();

    	if(spielzug == null){
			return;
		}

		Dialog<Gameplan> dialog = new Dialog<>();
		dialog.setTitle("Gameplan bearbeiten");
		dialog.setHeaderText("Spielzug zum Gameplan hinzufügen");

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Speichern", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		ComboBox<Gameplan> cbGameplan = new ComboBox<>();
		cbGameplan.setCellFactory(new Callback<ListView<Gameplan>, ListCell<Gameplan>>() {
		     @Override public ListCell<Gameplan> call(ListView<Gameplan> p) {
		         return new ListCell<Gameplan>() {
		             
		             @Override protected void updateItem(Gameplan item, boolean empty) {
		                 super.updateItem(item, empty);
		                 
		                 if (item == null || empty) {
		                     setGraphic(null);
		                 } else {
		                    setText(item.getName() + "("+item.size()+"/"+item.MAX_PLAYS+")");
		                 }
		            }
		       };
		   }
		});
		
		
		ObservableList<Gameplan> allGameplans = persistentManager.getAllGameplans();
		for (Gameplan gameplan2 : allGameplans) {
			if(gameplan2.getId() >=0){
				if(gameplan2.size() < gameplan2.MAX_PLAYS){
					cbGameplan.getItems().add(gameplan2);
				}
			}
		}
		
		grid.add(new Label("Gameplan:"), 0, 0);
		grid.add(cbGameplan, 1, 0);

		// Enable/Disable login button depending on whether a username was
		// entered.
		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> cbGameplan.requestFocus());

		// Convert the result to a username-password-pair when the login button
		// is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				Gameplan value = cbGameplan.getValue();
				if(value != null){
					value.add(spielzug);
				}

				return value;
			}
			return null;
		});

		Optional<Gameplan> result = dialog.showAndWait();

		result.ifPresent(play -> {
			System.out.println("Name: " + play.getName());
			loadGameplan();
		});
	}

	@FXML
    void checkGameplan(ActionEvent event) {
		Gameplan selectedItem = cbGameplan.getSelectionModel().getSelectedItem();

		if(selectedItem == null || selectedItem.getId() < 0){
			if(event.getSource() instanceof Node){
				Node node = (Node) event.getSource();
				node.setDisable(true);
			}
		}else{
			if(event.getSource() instanceof Node){
				Node node = (Node) event.getSource();
				node.setDisable(false);
			}

		}
    }

	@FXML
    void checkNoGameplan(ActionEvent event) {
    	Gameplan selectedItem = cbGameplan.getSelectionModel().getSelectedItem();
    	
		if(selectedItem == null || selectedItem.getId() < 0){
			if(event.getSource() instanceof Node){
				Node node = (Node) event.getSource();
				node.setDisable(false);
			}
		}else{
			if(event.getSource() instanceof Node){
				Node node = (Node) event.getSource();
				node.setDisable(true);
			}

		}
    }
	
	@FXML
	void copyPlan(ActionEvent event) {

		Dialog<Gameplan> dialog = new Dialog<>();
		dialog.setTitle("Neuer Gameplan");
		dialog.setHeaderText("Gameplan kopieren");

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Speichern", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField tfName = new TextField();
		tfName.setPromptText("Name");

		ComboBox<Gameplan> cbGameplan = new ComboBox<>();
		cbGameplan.setCellFactory(new Callback<ListView<Gameplan>, ListCell<Gameplan>>() {
		     @Override public ListCell<Gameplan> call(ListView<Gameplan> p) {
		         return new ListCell<Gameplan>() {
		             
		             @Override protected void updateItem(Gameplan item, boolean empty) {
		                 super.updateItem(item, empty);
		                 
		                 if (item == null || empty) {
		                     setGraphic(null);
		                 } else {
		                    setText(item.getName() + "("+item.size()+"/"+item.MAX_PLAYS+")");
		                 }
		            }
		       };
		   }
		});
		
		
		ObservableList<Gameplan> allGameplans = persistentManager.getAllGameplans();
		for (Gameplan gameplan2 : allGameplans) {
			if(gameplan2.getId() >=0){
				if(gameplan2.size() < gameplan2.MAX_PLAYS){
					cbGameplan.getItems().add(gameplan2);
				}
			}
		}
		
		grid.add(new Label("Name:"), 0, 0);
		grid.add(tfName, 1, 0);
		grid.add(new Label("Gameplan:"), 0, 1);
		grid.add(cbGameplan, 1, 1);
		
		

		// Enable/Disable login button depending on whether a username was
		// entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		tfName.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> tfName.requestFocus());

		// Convert the result to a username-password-pair when the login button
		// is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				Gameplan newGameplan = persistentManager.getNewGameplan();
				newGameplan.setName(tfName.getText());
				
				Gameplan value = cbGameplan.getValue();
				if(value != null){
					newGameplan.addAll(value);
				}

				return newGameplan;
			}
			return null;
		});

		Optional<Gameplan> result = dialog.showAndWait();

		result.ifPresent(play -> {
			System.out.println("Name: " + play.getName());
			loadGameplans();
			loadGameplan(play);
		});
	}

	@FXML
	void editPlan(ActionEvent event) {
		
		Gameplan gameplan = cbGameplan.getSelectionModel().getSelectedItem();
		if(gameplan == null ||gameplan.getId() < 0){
			return;
		}

		Dialog<Gameplan> dialog = new Dialog<>();
		dialog.setTitle("Gameplan bearbeiten");
		dialog.setHeaderText("Gameplan");

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Speichern", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField tfName = new TextField();
		tfName.setPromptText("Name");

		grid.add(new Label("Name:"), 0, 0);
		grid.add(tfName, 1, 0);

		// Enable/Disable login button depending on whether a username was
		// entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		tfName.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> tfName.requestFocus());

		// Convert the result to a username-password-pair when the login button
		// is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				gameplan.setName(tfName.getText());

				return gameplan;
			}
			return null;
		});

		Optional<Gameplan> result = dialog.showAndWait();

		result.ifPresent(play -> {
			System.out.println("Name: " + play.getName());
			loadGameplans();
		});
	}
	
	@FXML
	void editPlay(ActionEvent event) {
		
		Spielzug spielzug = tablePlays.getSelectionModel().getSelectedItem();

		if(spielzug == null){
			return;
		}
		
		Dialog<Spielzug> dialog = new Dialog<>();
		dialog.setTitle("Spielzug bearbeiten");
		dialog.setHeaderText("Spielzug");

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Speichern", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField tfLineup = new TextField();
		tfLineup.setText(spielzug.getAufstellung());

		TextField tfRoutes = new TextField();
		tfRoutes.setText(spielzug.getRoutes());

		TextField tfBlock = new TextField();
		tfBlock.setText(spielzug.getBlockSchema());;
		
		TextField tfType = new TextField();
		tfType.setText(spielzug.getType());

		grid.add(new Label("Aufstellung:"), 0, 0);
		grid.add(tfLineup, 1, 0);
		grid.add(new Label("Routen:"), 0, 1);
		grid.add(tfRoutes, 1, 1);
		grid.add(new Label("Blockschema:"), 0, 2);
		grid.add(tfBlock, 1, 2);
		grid.add(new Label("Type:"), 0, 3);
		grid.add(tfType, 1, 3);

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> tfLineup.requestFocus());

		// Convert the result to a username-password-pair when the login button
		// is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				
				
				spielzug.setAufstellung(tfLineup.getText());
				spielzug.setBlockSchema(tfBlock.getText());
				spielzug.setRoutes(tfRoutes.getText());
				spielzug.setType(tfType.getText());

				return spielzug;
			}
			return null;
		});

		Optional<Spielzug> result = dialog.showAndWait();
		result.ifPresent(play -> {
			System.out.println("Aufstellung=" + play.getAufstellung() + ", Block=" + play.getBlockSchema());
			loadGameplan();
		});
	}


    @FXML
    void exportWristcoach(ActionEvent event) {
    	Gameplan value = cbGameplan.getValue();
    	if(value == null || value.getId() < 0){
    		return;
    	}
    	
    	Canvas canvas = new Canvas( 115*5, 65*5);
    	GraphicsContext gc = canvas.getGraphicsContext2D();
    	
    	double heightStep = canvas.getHeight()/14;
    	
    	gc.setFill(Color.RED);
    	gc.setStroke(Color.BLACK);
    	gc.fillRect(0, 0, canvas.getWidth(), heightStep);
    	
    	gc.fillRect(0, heightStep, heightStep, heightStep);
    	gc.fillRect(canvas.getWidth()-heightStep, heightStep, heightStep, heightStep);
    	gc.fillRect((canvas.getWidth()-heightStep)/2, heightStep, heightStep, heightStep);
    	
    	gc.fillRect(0, canvas.getHeight() - heightStep, canvas.getWidth(), heightStep);
    	
    	gc.fillRect(0, 7*heightStep, heightStep, heightStep);
    	gc.fillRect(canvas.getWidth()-heightStep, 7*heightStep, heightStep, heightStep);
    	gc.fillRect((canvas.getWidth()-heightStep)/2, 7*heightStep, heightStep, heightStep);
    	gc.setTextAlign(TextAlignment.CENTER);
    	gc.setTextBaseline(VPos.CENTER);
    	gc.setFont(Font.font(heightStep*0.8));
    	
    	gc.setFill(Color.BLACK);
    	gc.fillText("Kopf", canvas.getWidth()/4, 1.5*heightStep, (canvas.getWidth() - 3 * heightStep)/2);
    	gc.fillText("Arm", 3*canvas.getWidth()/4, 1.5*heightStep, (canvas.getWidth() - 3 * heightStep)/2);
    	gc.fillText("Brust", canvas.getWidth()/4, 7.5*heightStep, (canvas.getWidth() - 3 * heightStep)/2);
    	gc.fillText("Bein", 3*canvas.getWidth()/4, 7.5*heightStep, (canvas.getWidth() - 3 * heightStep)/2);
    	
    	for (int i = 1; i < 6; i++) {
    		gc.fillText(String.valueOf(i), heightStep/2, (1.5+i)*heightStep);
		}
    	
    	for (int i = 1; i < 6; i++) {
    		gc.fillText(String.valueOf(i), canvas.getWidth()/2, (1.5+i)*heightStep);
		}
    	
    	for (int i = 1; i < 6; i++) {
    		gc.fillText(String.valueOf(i), heightStep/2, (7.5+i)*heightStep);
		}
    	
    	for (int i = 1; i < 6; i++) {
    		gc.fillText(String.valueOf(i), canvas.getWidth()/2, (7.5+i)*heightStep);
		}
    	
    	for (int i = 2; i < 23; i++) {
    		gc.strokeLine(0, (i)*heightStep, canvas.getWidth(), (i)*heightStep);
		}
    	
    	gc.strokeLine(heightStep, heightStep, heightStep, canvas.getHeight()-heightStep);
    	gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(1, 1, canvas.getWidth()-2, canvas.getHeight()-2);
        
    	
    	Dialog<Spielzug> dialog = new Dialog<>();
		dialog.setTitle("Wristcoach exportieren");
		dialog.setHeaderText("Wristcoach");

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Exportieren", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		dialog.getDialogPane().setContent(canvas);


		dialog.showAndWait();
    	
    }
	
	@FXML
	public void initialize() {

		clBlock.setCellValueFactory(new PropertyValueFactory<Spielzug, String>("blockSchema"));
		clLineup.setCellValueFactory(new PropertyValueFactory<Spielzug, String>("aufstellung"));
		clRoutes.setCellValueFactory(new PropertyValueFactory<Spielzug, String>("routes"));
		clType.setCellValueFactory(new PropertyValueFactory<Spielzug, String>("type"));


		cbGameplan.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Gameplan>() {

			@Override
			public void changed(ObservableValue<? extends Gameplan> observable, Gameplan oldValue, Gameplan newValue) {
				loadGameplan();
			}
		});
		
		cbGameplan.setCellFactory(new Callback<ListView<Gameplan>, ListCell<Gameplan>>() {
		     @Override public ListCell<Gameplan> call(ListView<Gameplan> p) {
		         return new ListCell<Gameplan>() {
		             
		             @Override protected void updateItem(Gameplan item, boolean empty) {
		                 super.updateItem(item, empty);
		                 
		                 if (item == null || empty) {
		                     setText(null);
		                 } else {
		                	 
		                	 if(item.getId() < 0){
		                		 setText(item.getName() + "("+persistentManager.getAllPlays().size()+")");
		                	 }else{
		                		 
								setText(item.getName() + "(" + item.size() + "/" + item.MAX_PLAYS + ")");
		                	 }
		                	 
		                 }
		            }
		       };
		   }
		});
		
		
		

		loadGameplans();

		ImageView imageView = new ImageView("logo.JPG");
		imageView.setFitWidth(200);
		imageView.setFitHeight(200);
		tablePlays.setPlaceholder(imageView);
	}

	@FXML
	void loadFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Coach Datei laden");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Coach Dateien (.rcf)", "*.rcf"));
		File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
		if (selectedFile != null) {
			persistentManager.setFile(selectedFile);
			persistentManager.loadPlays();
		}
	}
	
	protected void loadGameplan() {
		Gameplan selectedItem = cbGameplan.getSelectionModel().getSelectedItem();

		if(selectedItem != null){
			loadGameplan(selectedItem);
		}else{
			tablePlays.getItems().clear();
		}
	}
	
	@FXML
	void loadGameplan(ActionEvent event) {
		loadGameplan();
	}


	protected void loadGameplan(Gameplan plan) {
		if (plan.getId() < 0) {
			tablePlays.getItems().clear();
			tablePlays.getItems().addAll(persistentManager.getAllPlays().values());
		} else {
			tablePlays.getItems().clear();
			if(!plan.isEmpty()){
				tablePlays.getItems().addAll(plan);
			}
		}
	}

	protected void loadGameplans() {
		ObservableList<Gameplan> allGameplans = persistentManager.getAllGameplans();
		cbGameplan.setItems(allGameplans);
	}

	@FXML
	void newPlan(ActionEvent event) {

		Dialog<Gameplan> dialog = new Dialog<>();
		dialog.setTitle("Neuer Gameplan");
		dialog.setHeaderText("Gameplan anlegen");

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Speichern", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField tfName = new TextField();
		tfName.setPromptText("Name");

		grid.add(new Label("Name:"), 0, 0);
		grid.add(tfName, 1, 0);

		// Enable/Disable login button depending on whether a username was
		// entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		tfName.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> tfName.requestFocus());

		// Convert the result to a username-password-pair when the login button
		// is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				Gameplan newGameplan = persistentManager.getNewGameplan();
				newGameplan.setName(tfName.getText());

				return newGameplan;
			}
			return null;
		});

		Optional<Gameplan> result = dialog.showAndWait();

		result.ifPresent(play -> {
			System.out.println("Name: " + play.getName());
			loadGameplans();
		});
	}
	
    @FXML
	void newPlay(ActionEvent event) {



		System.out.println("Neuer Spielzug");
		Dialog<Spielzug> dialog = new Dialog<>();
		dialog.setTitle("Neuer Spielzug");
		dialog.setHeaderText("Spielzug");

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Speichern", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField tfLineup = new TextField();
		tfLineup.setPromptText("Aufstellung");

		TextField tfRoutes = new TextField();
		tfRoutes.setPromptText("Routen");

		TextField tfBlock = new TextField();
		tfBlock.setPromptText("Blockschema");
		
		TextField tfType = new TextField();
		tfType.setPromptText("Type");

		grid.add(new Label("Aufstellung:"), 0, 0);
		grid.add(tfLineup, 1, 0);
		grid.add(new Label("Routen:"), 0, 1);
		grid.add(tfRoutes, 1, 1);
		grid.add(new Label("Blockschema:"), 0, 2);
		grid.add(tfBlock, 1, 2);
		grid.add(new Label("Type:"), 0, 3);
		grid.add(tfType, 1, 3);

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> tfLineup.requestFocus());

		// Convert the result to a username-password-pair when the login button
		// is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				
				Spielzug newSpielzug = persistentManager.getNewSpielzug();
				newSpielzug.setAufstellung(tfLineup.getText());
				newSpielzug.setBlockSchema(tfBlock.getText());
				newSpielzug.setRoutes(tfRoutes.getText());
				newSpielzug.setType(tfType.getText());

				return newSpielzug;
			}
			return null;
		});

		Optional<Spielzug> result = dialog.showAndWait();

		result.ifPresent(play -> {
			System.out.println("Aufstellung=" + play.getAufstellung() + ", Block=" + play.getBlockSchema());
			loadGameplan();
		});
	}
    
    @FXML
	void removePlan(ActionEvent event) {
		Gameplan gameplan = cbGameplan.getSelectionModel().getSelectedItem();
		if (gameplan.getId() < 0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Hinweis");
			alert.setHeaderText(null);
			alert.setContentText("System Gameplans lassen sich nicht löschen.");

			alert.showAndWait();
			return;
		}

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Bestätigung");
		alert.setHeaderText("Gameplan löschen");
		alert.setContentText("Willst du den Gameplan " + gameplan.toString() + " wirklich löschen?");

		Optional<ButtonType> showAndWait = alert.showAndWait();
		if (showAndWait.get() == ButtonType.OK) {
			persistentManager.removeGameplan(gameplan);
		}
	}

    @FXML
	void removePlay(ActionEvent event) {
		Spielzug spielzug = tablePlays.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Bestätigung");
		alert.setHeaderText("Spielzug löschen");
		alert.setContentText("Willst du den Spielzug " + spielzug.toString() + " wirklich löschen?");

		Optional<ButtonType> showAndWait = alert.showAndWait();
		if (showAndWait.get() == ButtonType.OK) {
			persistentManager.removeSpielzug(spielzug);
			loadGameplan();
		}
	}

    @FXML
    void removePlayFromGameplan(ActionEvent event) {
    	Spielzug spielzug = tablePlays.getSelectionModel().getSelectedItem();
    	Gameplan gameplan = cbGameplan.getValue();
    	
    	if(gameplan == null || spielzug == null || gameplan.getId() < 0){
    		return;
    	}
		
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Bestätigung");
		alert.setHeaderText("Spielzug aus Gameplan entfernen");
		alert.setContentText("Willst du den Spielzug " + spielzug.toString() + " wirklich aus "+gameplan.toString() +" entfernen?");

		Optional<ButtonType> showAndWait = alert.showAndWait();
		if (showAndWait.get() == ButtonType.OK) {
			gameplan.remove(spielzug);
			loadGameplan();
		}

    }

	
	@FXML
	void saveFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Coach Datei speichern");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Coach Dateien (.rcf)", "*.rcf"));
		File selectedFile = fileChooser.showSaveDialog(root.getScene().getWindow());
		if (selectedFile != null) {
			persistentManager.setFile(selectedFile);
			persistentManager.savePlays();
		}
	}
	
	@FXML
	void saveFileShort(ActionEvent event) {
		persistentManager.savePlays();
	}

}
