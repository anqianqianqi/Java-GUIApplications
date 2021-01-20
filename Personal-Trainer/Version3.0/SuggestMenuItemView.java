//@Author: Anqi Luo
package hw3;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SuggestMenuItemView{
	Stage suggestStage;
	TextField timeTextField = new TextField("0");
	TextField caloriesTextField = new TextField("0");
	Label inputIgnore = new Label("0 input will be ignored");
	Label timeLabel = new Label("Enter time in minutes");
	Label caloriesLabel = new Label("Enter calories to burn");
	Button suggestButton = new Button("Suggest");
	Button cancelButton = new Button("Cancel");
	Workout workout = new Workout();
	
	
	void SuggestMenutItenView() {
		suggestStage = new Stage();
		suggestStage.setTitle("Workout Suggest Generator");
		GridPane suggestRoot = setUpSuggestRoot();
		Scene suggestScene = new Scene(suggestRoot,310,200);
		suggestStage.setScene(suggestScene);
		suggestStage.show();
	}
	
	GridPane setUpSuggestRoot() {
		GridPane suggestRoot = new GridPane();
		suggestRoot.setVgap(20);
		suggestRoot.setHgap(10);
		suggestRoot.add(timeTextField,2,1);
		suggestRoot.add(caloriesTextField,2,2);
		suggestRoot.add(timeLabel,1,1);
		suggestRoot.add(caloriesLabel,1,2);
		suggestRoot.add(inputIgnore, 1, 3,2,1);
		suggestRoot.add(suggestButton,1,4);
		suggestRoot.add(cancelButton, 2,4);
		suggestRoot.setAlignment(Pos.CENTER);
		GridPane.setHalignment(inputIgnore, HPos.CENTER);
		GridPane.setHalignment(suggestButton, HPos.CENTER);
		GridPane.setHalignment(cancelButton, HPos.CENTER);
		return suggestRoot;
	}
	
	void clear() {
		caloriesTextField.setText("0");
		timeTextField.setText("0");
		inputIgnore.setText("0 input will be ignored");
	}
	
}


