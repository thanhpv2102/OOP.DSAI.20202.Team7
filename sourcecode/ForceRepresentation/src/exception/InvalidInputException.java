package exception;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.paint.Paint;

public class InvalidInputException extends Exception {

	public InvalidInputException(String message) {
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle("Exception");
		
		DialogPane pane = dialog.getDialogPane();
		pane.getStylesheets().add(getClass().getResource("../main/application.css").toExternalForm());
		pane.getStyleClass().add("inputDialog");
		
		Label error = new Label(message);
		error.setTextFill(Paint.valueOf("#8be9fd"));

		pane.setContent(error);
		         
		ButtonType btnConfirm = new ButtonType("Ok", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(btnConfirm);
	
		dialog.showAndWait();
	}

}
