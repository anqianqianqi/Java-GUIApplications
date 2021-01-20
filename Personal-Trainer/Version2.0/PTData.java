//AndrewID: anqiluo
//Name: Anqi Luo
package hw2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PTData {

	ObservableList<Exercise> masterObservables = FXCollections.observableArrayList(); //data loaded from the file opened by the user
	ObservableList<Exercise> selectedObservables = FXCollections.observableArrayList(); //data displayed in the table-view in selection grid  

	/** loadData method checks if the file has extension .xml. If so, it creates an object of 
	 * XMLFiler class. Else it assumes it to be of CSV type and creates an object of CSVFiler class. 
	 * It then invokes readData() method and passes the File object of filename provided as the argument for this method.
	 * 
	 * The list returned by readData method is used to initialize masterObservables and is also returned by this method.
	 * Notice that the data files must reside in the DEFAULT_PATH as defined in PersonalTrainer class.
	 * @param filename
	 * @return
	 */
	ObservableList<Exercise> loadData(String filename) {
		String extension = filename.substring(filename.length()-3,filename.length());
		DataFiler data = null;
		switch (extension) {
		case "xml":
			data = new XMLFiler();
			break;
		case "csv":
			data = new CSVFiler();
			break;
		}
		masterObservables = data.readData(filename);
		selectedObservables = data.readData(filename);
		return masterObservables;
	}
}