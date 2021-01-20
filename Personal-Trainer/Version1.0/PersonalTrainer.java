package hw1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//AndrewID: anqiluo
public class PersonalTrainer {
	
	static final String EXERCISE_DATA_FILE = "data/MasterExercises1.csv";
	
	Exercise[] masterExercises;		//loads all exercises from the master data file
	Exercise[] selectedExercises;	//has the proposed exercise plan based on user input
	int timeInput, caloriesInput;  	//stores user inputs
	Scanner input;  				//use this for file and keyboard I/O
	
	//Do not change this method
	public static void main(String[] args) {
		
		PersonalTrainer personalTrainer = new PersonalTrainer();
		personalTrainer.loadExercises();
		personalTrainer.getUserInputs();
		Workout workout = new Workout();
		personalTrainer.selectedExercises = workout.buildWorkoutPlan(personalTrainer.masterExercises, personalTrainer.timeInput, personalTrainer.caloriesInput);
		personalTrainer.printWorkoutPlan();
	}
	
	/** loadExercises() method reads EXERCISE_DATA_FILE and loads allExercises array
	 * with Exercise objects where each object stores data in one row of
	 * EXERCISE_DATA_FILE
	 */
	void loadExercises() {
		StringBuilder sb = new StringBuilder();
		Scanner input = null;
		try {
			 input = new Scanner(new File(EXERCISE_DATA_FILE)).useDelimiter("\n");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (input.hasNextLine()) {
			sb.append(input.nextLine()+'\n');
		}
		String[] masterExercise  = sb.toString().split("\n");
		
		masterExercises = new Exercise[masterExercise.length];
		for (int i = 0; i < masterExercise.length;i++) {
			String[] itemSplit = masterExercise[i].split(",");
			Exercise item = new Exercise(itemSplit[0].trim(),itemSplit[1].trim(),Integer.parseInt(itemSplit[2].trim()),
					Integer.parseInt(itemSplit[3].trim()),Integer.parseInt(itemSplit[4].trim()),itemSplit[5].trim());	
			masterExercises[i] = item;
		}	
		
		input.close();
	
	}
	
	/** getUserInputs() method asks user for two inputs: timeInput and caloriesInput. 
	 * It initializes the member variables with the values input by the user
	 */
	private void getUserInputs() {
		input = new Scanner(System.in);
		System.out.println("How much time (in minutes)do you have for today's workout?");
		timeInput = input.nextInt();
		System.out.println("Target calories to burn?");
		caloriesInput = input.nextInt();
	}
	
	/** printWorkoutPlan() method takes the selectedExercises initialized
	 * in the main method and prints the output as shown in the HW1 handout. 
	 */
	private void printWorkoutPlan() {
		if (timeInput ==0 && caloriesInput ==0) {
			System.out.println("Sorry! No workout plan can be suggested!");
		}else {
			System.out.println("-----------------------------------------------------------------------------------------------------");
			System.out.println("Exercises\tLevel\t\tTime(min)\tReps\t\tCalories\tNotes");
			System.out.println("-----------------------------------------------------------------------------------------------------");
			int time = 0,cal = 0; 
			for (Exercise item:selectedExercises) {
				System.out.printf("%-16s%-18s%-14d%-16d%-16d%-17s%n",item.name,item.level,item.repTime,item.repCount,item.calories,item.exerciseNotes);	
				time += item.repTime;
				cal += item.calories;
			}
			System.out.println("-----------------------------------------------------------------------------------------------------");
			System.out.println(String.format("This workour will take about %d minutes and wll burn approximately %d calories", time,cal));
			System.out.println("****************************** Enjoy your workout!!****************************** ");
		}
	}
}
