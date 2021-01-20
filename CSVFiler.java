//AndrewID: anqiluo
//Name: Anqi Luo
package hw3;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CSVFiler extends DataFiler{

	@SuppressWarnings("resource")
	@Override
	public ObservableList<Exercise> readData(String filename) {
		StringBuilder sb = new StringBuilder();
		Scanner input = null;
		try {
			 input = new Scanner(new File(filename)).useDelimiter("\n");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (input.hasNextLine()) {
			sb.append(input.nextLine()+'\n');
		}
		String[] masterExercise  = sb.toString().split("\n");
		
		ObservableList<Exercise> masterExercises = FXCollections.observableArrayList();
		for (int i = 0; i < masterExercise.length;i++) {
			String[] itemSplit = masterExercise[i].split(",");
			Exercise item = new Exercise(itemSplit[0], itemSplit[1], Integer.parseInt(itemSplit[2]), Integer.parseInt(itemSplit[3]), Integer.parseInt(itemSplit[4]), itemSplit[5], itemSplit[6]);
			masterExercises.add(item);
		}	
		input.close();
		return masterExercises;
	}

	@Override
	public void writeData(ObservableList<Exercise> selectedExercises, File file) {
		StringBuilder sb = new StringBuilder();
		for (Exercise e:selectedExercises) {
			sb.append(String.format("%s,%s,%s,%s,%s,%s,%s \n",e.getName(),e.getLevel(),e.getRepTime(),e.getRepCount(),e.getCalories(),e.getImageFile(),e.getExerciseNotes()));	
		}
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(sb.toString());
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
