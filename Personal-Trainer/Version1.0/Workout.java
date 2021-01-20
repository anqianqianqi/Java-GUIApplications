//@Author: Anqi Luo
package hw1;

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
	public Exercise[] buildWorkoutPlan(Exercise[] allExercises, int timeInput, int caloriesInput) {
		Exercise[] selectExercise = new Exercise[allExercises.length];
		int[] count = new int[allExercises.length];
		while (timeSpent < timeInput || caloriesBurned < caloriesInput) {
			for (int i = 0; i < allExercises.length; i++) {
				if (timeSpent < timeInput || caloriesBurned < caloriesInput) {
				timeSpent += allExercises[i].repTime;
				caloriesBurned += allExercises[i].calories;
				count[i]++;
				}else{
					break;
				}
			}
		}
		for (int i = 0; i < allExercises.length; i++) {
			Exercise record = allExercises[i];
			int time = count[i];
			Exercise item = new Exercise(record.name,record.level,record.repTime*time,record.repCount*time,record.calories*time,record.exerciseNotes);
			selectExercise[i] = item;
		}
		return selectExercise;
	}
}
