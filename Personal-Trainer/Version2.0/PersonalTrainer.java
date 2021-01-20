//@Author: Anqi Luo
package hw2;

import java.io.File;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class PersonalTrainer extends Application {
	
	PTViewer ptView = new PTViewer();  //will perform all view-related operations that do not need data-components
	PTData ptData = new PTData(); //will perform all data-related operations that do not need view-components
	Stage mainStage; //to be used for FileChooser in OpenHandler
	GridPane workoutGrid; //will hold the central grid populated with GUI components and will be attached to root in New and Open handlers
	DataFiler dataFiler; //will hold CSVFiler or XMLFiler
	Exercise currentExercise; //this points to whichever exercise is selected in exerciseComboBox or in exerciseTableView 
	
	static final String DEFAULT_PATH = "./data"; //relative path for all data files to reside 
	static final String PT_IMAGE = "personaltrainer.jpg";  //the personal trainer image
	static final int PT_WIDTH = 875;
	static final int PT_HEIGHT = 500;
	static final int PT_IMAGE_WIDTH = 300;
	
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
		Background b = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
		ptView.root.setBackground(b);
		Scene scene = new Scene(ptView.root, PT_WIDTH, PT_HEIGHT);
		mainStage.setScene(scene);
		workoutGrid = ptView.setupScreen();  //populate the grid, but don't attach to the root yet
		setActions();
		mainStage.show();
	}
	
	/**showImage is a helper method to display the image. 
	 * This method is used every time user selects an exercise in the exerciseComboBox, 
	 * or changes selection in exerciseTableView. 
	 * @param filename
	 */
	void showImage(String filename) {
		if (filename == null) return;
		ptView.image = new Image(getClass().getClassLoader().getResource(filename).toString());
		ptView.imageView.setImage(ptView.image);
	}

	/** loadInputGrid() populates various components in the inputGrid. 
	 * It links exerciseComboBox with masterObservables
	 * It also attaches listeners to timeSlider and exerciseComboBox to change the labels for 
	 * timeValue, caloriesValue, and repsCountValue. 
	 */
	private void loadInputGrid() {
		//enter your code here
		ptView.exerciseComboBox.setItems(ptData.masterObservables);
		int totalTime = 0;
		int totalCalories = 0;
		for (Exercise e:ptData.selectedObservables) {
			totalTime += e.getRepTime();
			totalCalories += e.getCalories();
		}
		ptView.totalCaloriesValue.setText(String.valueOf(totalCalories));
		ptView.totalTimeValue.setText(String.valueOf(totalTime));
		ptView.timeSlider.setSnapToTicks(false);
		ptView.exerciseComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
				ptView.caloriesValue.textProperty().unbind();
				ptView.repsCountValue.textProperty().unbind();
				ptView.timeValue.textProperty().unbind();	
				if(!(newValue == null)) {
					currentExercise = new Exercise(newValue.getName(), newValue.getLevel(), newValue.getRepTime(), newValue.getRepCount(), newValue.getCalories(), newValue.getImageFile(), newValue.getExerciseNotes());
					ptView.timeSlider.setValue(newValue.getRepTime());
					currentExercise.repTimeProperty().bind(ptView.timeSlider.valueProperty());
					currentExercise.caloriesProperty().bind(currentExercise.repTimeProperty().divide((float)newValue.getRepTime()).multiply((float)newValue.getCalories()));
					currentExercise.repCountProperty().bind(currentExercise.repTimeProperty().divide((float)newValue.getRepTime()).multiply((float)newValue.getRepCount()));
					ptView.caloriesValue.textProperty().bind(Bindings.format("%.0f", currentExercise.repTimeProperty().divide((float)newValue.getRepTime()).multiply((float)newValue.getCalories())));
					ptView.repsCountValue.textProperty().bind(Bindings.format("%.0f", currentExercise.repTimeProperty().divide((float)newValue.getRepTime()).multiply((float)newValue.getRepCount())));
					ptView.timeValue.textProperty().bind(Bindings.format("%d", currentExercise.repTimeProperty()));	
					ptView.notesTextArea.setText(currentExercise.getExerciseNotes());
					ptView.imageView.setImage(new Image(getClass().getClassLoader().getResource( currentExercise.getImageFile()).toString()));
				}});
		
	}
	
	/**loadSelectionGrid() reads the data from data file and populates the components in selectionGrid.  
	 * It links exerciseTableView with selectedObservables list.
	 * This method first clears the table-view from past-data, if any, and then
	 * loads data from selectedExercises list to various components.   
	 * @param selectedExercises
	 */
	void loadSelectionGrid(ObservableList<Exercise> selectedObservables) {
		//enter your code here	
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
		//enter your code here 
		ptView.addButton.setOnAction(new AddButtomHandler());
		ptView.removeButton.setOnAction(new RemoveButtomHandler());	
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
			ptView.root.setBottom(null);
			ptView.root.setCenter(workoutGrid);
			ptView.clearScreen();
			Image image = new Image(getClass().getClassLoader().getResource(PT_IMAGE).toString());
			ptView.imageView.setImage(image);
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select File");
			fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
			fileChooser.getExtensionFilters().addAll(
					new ExtensionFilter("PT files", "*.xml","*.csv")
					);
			File file = null;
			if ((file = fileChooser.showOpenDialog(mainStage))!=null) {
				loadSelectionGrid(ptData.loadData(file.getAbsolutePath()));
				ptView.workoutNameValue.setText(file.getName());
				loadInputGrid();
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
			// TODO Auto-generated method stub
			if (!(currentExercise == null)) {
				Exercise exerciseAdd = new Exercise(currentExercise.getName(), currentExercise.getLevel(), currentExercise.getRepTime(), currentExercise.getRepCount(), currentExercise.getCalories(), currentExercise.getImageFile(), currentExercise.getExerciseNotes());
				ptData.selectedObservables.add(exerciseAdd);
				ptView.totalCaloriesValue.setText(String.valueOf(Integer.valueOf(ptView.totalCaloriesValue.getText()) + currentExercise.getCalories()));
				ptView.totalTimeValue.setText(String.valueOf(Integer.valueOf(ptView.totalTimeValue.getText()) + currentExercise.getRepTime()));
				ptView.exerciseTableView.getSelectionModel().selectLast();
			}	
		}
		
	}
	private class RemoveButtomHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			int selectIndex = (int)ptView.exerciseTableView.getSelectionModel().getSelectedIndex();
			if (!(selectIndex == -1)) {
				Exercise selectExercise = ptData.selectedObservables.get(selectIndex);
				ptView.totalCaloriesValue.setText(String.valueOf(Integer.valueOf(ptView.totalCaloriesValue.getText()) - selectExercise.getCalories()));
				ptView.totalTimeValue.setText(String.valueOf(Integer.valueOf(ptView.totalTimeValue.getText()) - selectExercise.getRepTime()));
				ptData.selectedObservables.remove(selectIndex);
				Exercise displayExercise = ptView.exerciseTableView.getSelectionModel().getSelectedItem();
				if (!(displayExercise == null)) {
				String imagePath = displayExercise.getImageFile();
				ptView.imageView.setImage(new Image(getClass().getClassLoader().getResource(imagePath).toString()));
				ptView.notesTextArea.setText(displayExercise.getExerciseNotes());
				}else {
					ptView.notesTextArea.setText("");
					ptView.imageView.setImage(new Image(getClass().getClassLoader().getResource(PT_IMAGE).toString()));
				}
			}
		}
	}
	
	private class SelectTableRowHandler implements EventHandler<Event>{

		@Override
		public void handle(Event event) {
			// TODO Auto-generated method stub
			Exercise displayExercise = ptView.exerciseTableView.getSelectionModel().getSelectedItem();
			if (displayExercise != null) {
				String imagePath = displayExercise.getImageFile();
				ptView.imageView.setImage(new Image(getClass().getClassLoader().getResource(imagePath).toString()));
				ptView.notesTextArea.setText(displayExercise.getExerciseNotes());
			}	
		}
	}

}



