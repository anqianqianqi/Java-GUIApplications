//@Author: Anqi Luo
package hw3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//AndrewID: anqiluo
public class Workout {
	int timeSpent, caloriesBurned; //as computed by the buildWorkoutPlan() method

	/**buildWorkoutPlan() method starts with the allExercises array and based on
	 * timeInput and caloriesInput values, assigns Exercise objects to a new suggestedExercises.
	 * The logic is that you make sequential pass on allExercises array and keep adding its 
	 * Exercises to suggestedExercises. If the timeInput <= 13 and caloriesInput <= 100, it will require only 
	 * one full or partial pass through allExercises. If more time or calories are needed, then continue to make more passes and 
	 * add to the Exercise objects' repTime, repCount, and calories. 
	 * Return the final suggestedExercises built by the method.
	 * @return
	 */
	public ObservableList<Exercise> buildWorkoutPlan(ObservableList<Exercise> allExercises, int timeInput, int caloriesInput) {
		timeSpent = caloriesBurned = 0;
		ObservableList<Exercise> selectExercise = FXCollections.observableArrayList();
		int[] count = new int[allExercises.size()];
		while (timeSpent < timeInput || caloriesBurned < caloriesInput) {
			for (int i = 0; i < allExercises.size(); i++) {
				if (timeSpent < timeInput || caloriesBurned < caloriesInput) {
				timeSpent += allExercises.get(i).getRepTime();
				caloriesBurned += allExercises.get(i).getCalories();
				count[i]++;
				}else{
					break;
				}
			}
		}
		for (int i = 0; i < allExercises.size(); i++) {
			Exercise record = allExercises.get(i);
			int time = count[i];
			if (time >= 1) {
			Exercise item = new Exercise(record.getName(),record.getLevel(),record.getRepTime()*time,record.getRepCount()*time,record.getCalories()*time,record.getImageFile(),record.getExerciseNotes());
			selectExercise.add(item);}
		}
		return selectExercise;
	}
}
