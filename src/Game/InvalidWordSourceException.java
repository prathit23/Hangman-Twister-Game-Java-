/**
 * Prathit Pannase: ppannase
 */
package Game;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("serial")
public class InvalidWordSourceException extends RuntimeException{
	String message;
		
	InvalidWordSourceException(String message) {
		this.message = message;
	}
	// Custom Alert Class
	void showAlert() {
		// Pops us Alert
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("Word Source Format Error" );
		alert.setTitle("WordNerd 3.0");
		alert.setContentText(message );
		alert.showAndWait();
	}
}
