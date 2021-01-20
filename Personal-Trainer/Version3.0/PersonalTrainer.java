//AndrewID: anqiluo
//Name: Anqi Luo
package hw3;

import java.io.File;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;


public class PersonalTrainer extends Application {

	PTViewer ptView = new PTViewer();  //will perform all view-related operations that do not need data-components
	PTData ptData = new PTData(); //will perform all data-related operations that do not need view-components
	Stage mainStage; //to be used for FileChooser in OpenHandler
	GridPane workoutGrid; //will hold the central grid populated with GUI components and will be attached to root in New and Open handlers
	DataFiler dataFiler; //will hold CSVFiler or XMLFiler
	Exercise currentExercise; //this points to whichever exercise is selected in exerciseComboBox or in exerciseTableView 
	Exercise displayTextExerciseBefore,displayTextExerciseAfter; //hold the exercise displayed before and after the user choose a new one
	boolean ifFromTableView = false; //check whether the display exercise is chosen from the table view or combo box

	static final String DEFAULT_PATH = "./data"; //relative path for all data files to reside 
	static final String PT_IMAGE = "personaltrainer.jpg";  //the personal trainer image
	static final int PT_WIDTH = 875;
	static final int PT_HEIGHT = 500;
	static final int PT_IMAGE_WIDTH = 300;

	static MediaPlayer videoPlayer;  //to be used for viewing videos

	public static void main(String[] args) {
		launch(args);
	}

	/** start() method creates the opening screen with menus. 
	 * It also creates the screenGrid by invoking setupScreen() method
	 * of PTViewer class but doesn't attach it to the root yet	 * 
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		mainStage = primaryStage;
		mainStage.setTitle("Personal Trainer");
		ptView.setupMenus();
		ptView.setupWelcomeScreen();
		ptView.closeWorkoutMenuItem.setDisable(true);
		ptView.saveWorkoutMenuItem.setDisable(true);
		ptView.suggestWorkoutMenuItem.setDisable(true);
		Background b = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
		ptView.root.setBackground(b);
		Scene scene = new Scene(ptView.root, PT_WIDTH, PT_HEIGHT);
		mainStage.setScene(scene);
		workoutGrid = ptView.setupScreen();  //populate the grid, but don't attach to the root yet
		setActions();
		mainStage.show();
	}


	/** loadInputGrid() populates various components in the inputGrid. 
	 * It links exerciseComboBox with masterObservables
	 * It also attaches listeners to timeSlider and exerciseComboBox to change the labels for 
	 * timeValue, caloriesValue, and repsCountValue. 
	 */
	private void loadInputGrid() { 
		ptView.exerciseComboBox.setItems(ptData.masterObservables);
		calculateTotalCalTime(); //this function calculates the total calories and time, which is coded in the end
		ptView.timeSlider.setSnapToTicks(false);
		ptView.exerciseComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			ptView.caloriesValue.textProperty().unbind(); //to bind the values with the newly selected exercise
			ptView.repsCountValue.textProperty().unbind();
			ptView.timeValue.textProperty().unbind();
			if(!(newValue == null)) {
				currentExercise = new Exercise(newValue.getName(), newValue.getLevel(), newValue.getRepTime(), newValue.getRepCount(), newValue.getCalories(), newValue.getImageFile(), newValue.getExerciseNotes());
				// catch the error when the media file is missing
				try {
					ptView.imageView.setImage(new Image(getClass().getClassLoader().getResource( currentExercise.getImageFile()).toString()));
					WorkoutViewer.createViewer(currentExercise.getImageFile()).view(ptView.imageStackPane);
				} catch (NullPointerException missingFileException) {
					mediaError(currentExercise);
					currentExercise.setImageFile(PT_IMAGE);
					for (Exercise e:ptData.selectedObservables) {
						if (e.getName().equalsIgnoreCase(currentExercise.getName()) && e.getLevel().equalsIgnoreCase(currentExercise.getLevel())) {
							e.setImageFile(PT_IMAGE);	
						}
					}
				}
				ptView.timeSlider.setValue(newValue.getRepTime());
				currentExercise.repTimeProperty().bind(ptView.timeSlider.valueProperty());
				currentExercise.caloriesProperty().bind(currentExercise.repTimeProperty().divide((float)newValue.getRepTime()).multiply((float)newValue.getCalories()));
				currentExercise.repCountProperty().bind(currentExercise.repTimeProperty().divide((float)newValue.getRepTime()).multiply((float)newValue.getRepCount()));
				ptView.caloriesValue.textProperty().bind(Bindings.format("%.0f", currentExercise.repTimeProperty().divide((float)newValue.getRepTime()).multiply((float)newValue.getCalories())));
				ptView.repsCountValue.textProperty().bind(Bindings.format("%.0f", currentExercise.repTimeProperty().divide((float)newValue.getRepTime()).multiply((float)newValue.getRepCount())));
				ptView.timeValue.textProperty().bind(Bindings.format("%d", currentExercise.repTimeProperty()));	
				ptView.notesTextArea.setText(currentExercise.getExerciseNotes());
			}
		});

	}

	/**loadSelectionGrid() reads the data from data file and populates the components in selectionGrid.  
	 * It links exerciseTableView with selectedObservables list.
	 * This method first clears the table-view from past-data, if any, and then
	 * loads data from selectedExercises list to various components.   
	 * @param selectedExercises
	 */
	void loadSelectionGrid(ObservableList<Exercise> selectedExercises) {
		ptView.exerciseTableView.getSelectionModel().selectAll();
		ptView.exerciseTableView.getSelectionModel().clearSelection();
		ptView.exerciseTableView.setItems(ptData.selectedObservables);
		ptView.exerciseTableView.setOnMouseClicked(new SelectTableRowHandler());
	}

	/** setActions() method attaches all action-handlers to their respective
	 * GUI components. All GUI has been defined in PTViewer.
	 */
	private void setActions() {
		ptView.openWorkoutMenuItem.setOnAction(new OpenWorkoutHandler());
		ptView.closeWorkoutMenuItem.setOnAction(new CloseWorkoutHandler());
		ptView.aboutHelpMenuItem.setOnAction(new AboutHandler());
		ptView.addButton.setOnAction(new AddButtomHandler());
		ptView.removeButton.setOnAction(new RemoveButtomHandler());	
		ptView.searchButton.setOnAction(new SearchButtonHandler());
		ptView.updateButton.setOnAction(new UpdateButtonHandler());
		ptView.saveWorkoutMenuItem.setOnAction(new SaveMenueItemHandler());
		ptView.suggestWorkoutMenuItem.setOnAction(new SuggestMenuItemHandler());
		ptView.exitWorkoutMenuItem.setOnAction(e -> System.exit(0));
		//change the value ifFromTableView to false when user use combo box to choose a new exercise to display 
		//and set the update button color to black, in case the user did not save the modified note
		ptView.exerciseComboBox.getSelectionModel().selectedIndexProperty().addListener((object,oldValue,newValue) -> {
			ifFromTableView = false;
			ptView.updateButton.setStyle("-fx-text-fill: Black");
		});
		//check whether the change in notesTextArea is initiated by user changing display exercise or modifying the notes
		//change colors of UpdateButton when it is in the case of modifying notes
		ptView.notesTextArea.textProperty().addListener((object,oldValue,newValue) -> {
			if (ifFromTableView){
				displayTextExerciseAfter = ptView.exerciseTableView.getSelectionModel().getSelectedItem();
			}else {
				displayTextExerciseAfter = ptView.exerciseComboBox.getSelectionModel().getSelectedItem();
			}
			if (displayTextExerciseAfter != null) {
				if (displayTextExerciseBefore ==null || !(displayTextExerciseBefore.getName().equalsIgnoreCase(displayTextExerciseAfter.getName()) && displayTextExerciseBefore.getLevel().equalsIgnoreCase(displayTextExerciseAfter.getLevel()))) {
					ptView.updateButton.setStyle("-fx-text-fill: red");
				}
			}
		});
		//change the color of searchTextField back to color black when the user clear the prior search
		ptView.searchTextField.textProperty().addListener((object,oldValue,newValue) -> {
			if (ptView.searchTextField.getText().isEmpty()) {
				ptView.searchTextField.setStyle("-fx-text-inner-color: black;");}

		});
	}


	/***********All event handles as inner classes below this line****************************************/

	/**OpenWorkoutHandler is used when user chooses Open option from the menu.
	 * It first clears data from previous interactions. It then takes user input for the file to be opened by using
	 * the FileChooser library class. It uses DEFAULT_PATH as the opening folder.  
	 * If the user chooses a file, then its absolute path is passed to ptData's loadData() method.  
	 * The data returned by loadData() method is used to populate the inpuGrid and selectionGrid components
	 * by invoking loadInputGrid() and loadSelectionGrid() methods.  
	 * 
	 * The code shown below is provided as a dummy to display the
	 * workoutGrid. The workoutGrid needs to be populated with data
	 * in this handler
	 */
	private class OpenWorkoutHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select File");
			fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
			fileChooser.getExtensionFilters().addAll(
					new ExtensionFilter("PT files", "*.xml","*.csv")
					);
			File file = null;
			if ((file = fileChooser.showOpenDialog(mainStage))!=null) {
				//catch error of file in wrong format 
				try {
					ptView.clearScreen();
					loadSelectionGrid(ptData.loadData(file.getAbsolutePath()));
					loadInputGrid();
					ptView.root.setBottom(null);
					ptView.root.setCenter(workoutGrid);
					ptView.workoutNameValue.setText(file.getName());
					Image image = new Image(getClass().getClassLoader().getResource(PT_IMAGE).toString());
					ptView.imageView.setImage(image);
					ptView.closeWorkoutMenuItem.setDisable(false);
					ptView.saveWorkoutMenuItem.setDisable(false);
					ptView.suggestWorkoutMenuItem.setDisable(false);
					WorkoutViewer.createViewer(PT_IMAGE).view(ptView.imageStackPane);
					ptView.openWorkoutMenuItem.setDisable(true);
				}catch (Exception wrongFileException) {
					//display an alert window when the file is in wrong format
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("File Format Error");
					alert.setHeaderText("The Personal Trainer");
					alert.setContentText(String.format("Invalid format in %s \nExpected CSV format: String, String,int ,int,int ,String, String",file.getName() ));
					alert.showAndWait();
				}
			}
		}
	}

	/**CloseWorkoutHandler clears workoutGrid as well as all data. 
	 * It then displays the Welcome screen
	 */
	private class CloseWorkoutHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			ptView.root.setCenter(null);
			ptView.clearScreen();
			ptView.setupWelcomeScreen();
			//disable close, save and suggest menu items before the user open a data file
			ptView.closeWorkoutMenuItem.setDisable(true);
			ptView.saveWorkoutMenuItem.setDisable(true);
			ptView.suggestWorkoutMenuItem.setDisable(true);
			ptView.openWorkoutMenuItem.setDisable(false);
			//clear the value of displayTextExerciseBefore and displayTextExerciseAfter 
			displayTextExerciseBefore = displayTextExerciseAfter = null;
		}
	}

	private class AboutHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("About");
			alert.setHeaderText("The Personal Trainer");
			alert.setContentText("Version 1.0 \nRelease 1.0\nCopyleft Java Nerds\nThis software is designed purely for educational purposes.\nNo commercial use intended");
			Image image = new Image(getClass().getClassLoader().getResource(PT_IMAGE).toString());
			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setFitWidth(PT_IMAGE_WIDTH);
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			alert.setGraphic(imageView);
			alert.showAndWait();
		}
	}
	private class AddButtomHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			if (!(currentExercise == null)) {
				Exercise exerciseAdd = new Exercise(currentExercise.getName(), currentExercise.getLevel(), currentExercise.getRepTime(), currentExercise.getRepCount(), currentExercise.getCalories(), currentExercise.getImageFile(), currentExercise.getExerciseNotes());
				ptData.selectedObservables.add(exerciseAdd);
				calculateTotalCalTime();
				ptView.exerciseTableView.getSelectionModel().selectLast();
			}
		}
	}
	private class RemoveButtomHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			int selectIndex = (int)ptView.exerciseTableView.getSelectionModel().getSelectedIndex();
			if (!(selectIndex == -1)) {
				Exercise selectExercise = ptData.selectedObservables.get(selectIndex);
				ptView.totalCaloriesValue.setText(String.valueOf(Integer.valueOf(ptView.totalCaloriesValue.getText()) - selectExercise.getCalories()));
				ptView.totalTimeValue.setText(String.valueOf(Integer.valueOf(ptView.totalTimeValue.getText()) - selectExercise.getRepTime()));
				ptData.selectedObservables.remove(selectIndex);
				Exercise displayExercise = ptView.exerciseTableView.getSelectionModel().getSelectedItem();
				if (!(displayExercise == null)) {
					String imagePath = displayExercise.getImageFile();
					// catch the error when the media file is missing
					try {
						ptView.imageView.setImage(new Image(getClass().getClassLoader().getResource(imagePath).toString()));
					} catch (NullPointerException missingFileException) {
						mediaError(displayExercise);
						displayExercise.setImageFile(PT_IMAGE);
						for (Exercise e:ptData.selectedObservables) {
							if (e.getName().equalsIgnoreCase(displayExercise.getName()) && e.getLevel().equalsIgnoreCase(displayExercise.getLevel())) {
								e.setImageFile(PT_IMAGE);	
							}
						}
					}
					ptView.notesTextArea.setText(displayExercise.getExerciseNotes());
				}else {
					ptView.notesTextArea.setText("");
					ptView.imageView.setImage(new Image(getClass().getClassLoader().getResource(PT_IMAGE).toString()));
				}
			}
			//clear the value of displayTextExerciseBefore and displayTextExerciseAfter after the user remove an exercise
			//Exercise notes area will change value when an exercise is remove and change the updateButton color to red, so set it back to black
			displayTextExerciseBefore = displayTextExerciseAfter = null;
			ptView.updateButton.setStyle("-fx-text-fill: Black");
		}
	}
	private class SelectTableRowHandler implements EventHandler<Event>{

		@Override
		public void handle(Event event) {
			ifFromTableView = true;
			Exercise displayExercise = ptView.exerciseTableView.getSelectionModel().getSelectedItem();
			//catch media file missing error and update masterObservable and selectedObservable
			if (displayExercise != null) {
				try {
					String imagePath = displayExercise.getImageFile();
					ptView.imageView.setImage(new Image(getClass().getClassLoader().getResource(imagePath).toString()));
					ptView.notesTextArea.setText(displayExercise.getExerciseNotes());
					WorkoutViewer.createViewer(imagePath).view(ptView.imageStackPane);
				} catch (NullPointerException missingMediaException) {
					mediaError(displayExercise);
					for (Exercise e:ptData.masterObservables) {
						if (e.getName().equalsIgnoreCase(displayExercise.getName()) && e.getLevel().equalsIgnoreCase(displayExercise.getLevel())) {
							e.setImageFile(PT_IMAGE);	
						}
					}
					for (Exercise e:ptData.selectedObservables) {
						if (e.getName().equalsIgnoreCase(displayExercise.getName()) && e.getLevel().equalsIgnoreCase(displayExercise.getLevel())) {
							e.setImageFile(PT_IMAGE);	
						}
					}
				}	
			}
			//Exercise notes area will change value when a new exercise is selected and change the updateButton color to red, so set it back to black
			ptView.updateButton.setStyle("-fx-text-fill: Black");
		}
	}

	private class SaveMenueItemHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save File");
			fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
			fileChooser.getExtensionFilters().addAll(
					new ExtensionFilter("xml", "*.xml"),new ExtensionFilter("csv", "*.csv")
					);
			File file = null;
			if ((file = fileChooser.showSaveDialog(mainStage))!=null) {
				ptData.writeData(ptData.selectedObservables,file);
			}
		}
	}

	private class SearchButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			ptView.searchTextField.setStyle("-fx-text-inner-color: black;");
			String searchString = ptView.searchTextField.getText().trim();
			boolean ifFound = false;
			ObservableList<Exercise> searchList = FXCollections.observableArrayList();
			//return all Exercise when there is no search word or space(s)
			if (searchString == null || searchString.equals("//s+")) {
				ptData.selectedObservables = ptData.masterObservables;
			}else {
				ptData.selectedObservables = FXCollections.observableArrayList();
				for (Exercise e:ptData.masterObservables) {
					if (e.getExerciseNotes()!=null) {
						if(e.getName().toLowerCase().contains(searchString.toLowerCase()) || e.getExerciseNotes().toLowerCase().contains(searchString.toLowerCase())) {
							ifFound = true;
							searchList.add(e);
						}
					}else {
						if(e.getName().toLowerCase().contains(searchString.toLowerCase())) {
							ifFound = true;
							searchList.add(e);
						}
					}
				}
			}
			if (ifFound) {
				ptView.searchTextField.setStyle("-fx-text-inner-color: black;");
				ptData.selectedObservables = searchList;
				ptView.exerciseTableView.setItems(ptData.selectedObservables);
				calculateTotalCalTime();
			}else {
				ptView.searchTextField.setStyle("-fx-text-inner-color: red;");
				ptView.searchTextField.setText(searchString+" not found");
			}
		}
	}

	private class UpdateButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			String newNote = ptView.notesTextArea.getText();
			Exercise updateExercise = null;
			if (ptView.exerciseTableView.getSelectionModel().getSelectedItem() !=null && ifFromTableView){
				updateExercise = ptView.exerciseTableView.getSelectionModel().getSelectedItem();
				//update notes to masterObservable
				for (Exercise e:ptData.masterObservables) {
					if (e.getName().equalsIgnoreCase(updateExercise.getName()) && e.getLevel().equalsIgnoreCase(updateExercise.getLevel())) {
						e.setExerciseNotes(newNote);
					}
				}
				ptView.exerciseComboBox.setItems(ptData.masterObservables);
				//synchronize the same exercise in the table view
				for (Exercise e:ptData.selectedObservables) {
					if (e.getName().equalsIgnoreCase(updateExercise.getName()) && e.getLevel().equalsIgnoreCase(updateExercise.getLevel())) {
						e.setExerciseNotes(newNote);
					}
				}
			}else { 
				if (ptView.exerciseComboBox.getSelectionModel().getSelectedItem()!=null) {
					updateExercise = ptView.exerciseComboBox.getSelectionModel().getSelectedItem(); 
					//synchronize the same exercise in the table view
					for (Exercise e:ptData.selectedObservables) {
						if (e.getName().equalsIgnoreCase(updateExercise.getName()) && e.getLevel().equalsIgnoreCase(updateExercise.getLevel())) {
							e.setExerciseNotes(newNote);
						}
					}
					updateExercise.setExerciseNotes(newNote);
				}
			}
			//clear value of displayTextExerciseBefore, displayTextExerciseAfter 
			displayTextExerciseBefore = displayTextExerciseAfter = null;
			ptView.updateButton.setStyle("-fx-text-fill: Black");
		}
	}

	private class SuggestMenuItemHandler implements EventHandler<ActionEvent>{
		Workout workout = new Workout();
		SuggestMenuItemView suggest = new SuggestMenuItemView();
		@Override
		public void handle(ActionEvent event) {
			//setup the suggest stage 
			suggest.SuggestMenutItenView();
			suggest.suggestButton.setOnAction(new SuggestButtonHandler());
			suggest.cancelButton.setOnAction(new CancelButtonHandler());
		}
		private class SuggestButtonHandler implements EventHandler<ActionEvent>{

			@Override
			public void handle(ActionEvent event) {
				//generate suggest work out plan with the inputs 
				//catch wrong input format error
				try {
					int caloriesInput = Integer.parseInt(suggest.caloriesTextField.getText());
					int timeInput = Integer.parseInt(suggest.timeTextField.getText());
					ptData.selectedObservables = workout.buildWorkoutPlan(ptData.masterObservables, timeInput, caloriesInput);
					ptView.exerciseTableView.setItems(ptData.selectedObservables);
					calculateTotalCalTime();
					suggest.suggestStage.close();
					suggest.clear();
				} catch (NumberFormatException wrongInput){
					suggest.inputIgnore.setText("Wrong input! Please enter number!");
				}
			}
		}

		private class CancelButtonHandler implements EventHandler<ActionEvent>{

			@Override
			public void handle(ActionEvent event) {
				suggest.suggestStage.close();
				suggest.clear();
			}
		}
	}

	//display alert window when there is missing media file
	void mediaError(Exercise e) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Media Error");
		alert.setHeaderText("The Personal Trainer");
		String mediaType = e.getImageFile().substring(e.getImageFile().length()-3,e.getImageFile().length());
		switch (mediaType) {
		case "mp4":
			mediaType = "vedio";
			break;
		case "jpg":
			mediaType = "picture";
			break;
		case "png":
			mediaType = "picture";
			break;
		default:
			mediaType = "media";
		}
		alert.setContentText(String.format("Unable to load %s %s",mediaType,e.getImageFile()));
		alert.showAndWait();
	}	

	//calculate for the total calories and total time label
	void calculateTotalCalTime() {
		int totalTime = 0;
		int totalCalories = 0;
		for (Exercise e:ptData.selectedObservables) {
			totalTime += e.getRepTime();
			totalCalories += e.getCalories();
		}
		ptView.totalCaloriesValue.setText(String.valueOf(totalCalories));
		ptView.totalTimeValue.setText(String.valueOf(totalTime));
	}
}
