package library.assistant.ui.addmember;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;

public class MemberAddController implements Initializable {

	DatabaseHandler handler;
	
	@FXML
	private AnchorPane rootPane;
	
	@FXML
	private JFXTextField name;

	@FXML
	private JFXTextField id;

	@FXML
	private JFXTextField mobile;

	@FXML
	private JFXTextField email;

	@FXML
	private JFXButton saveButton;

	@FXML
	private JFXButton cancelButton;

	@FXML
	void addMember(ActionEvent event) {

		String mName = name.getText();
		String mID = id.getText();
		String mMobile = mobile.getText();
		String mEmail = email.getText();

		Boolean flag = mName.isEmpty() || mID.isEmpty() || mMobile.isEmpty() || mEmail.isEmpty();
		if (flag) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Please Enter in all fields");
			alert.showAndWait();
			return;
		}
		String st = "INSERT INTO MEMBER VALUES (" +
					"'"+mID+"',"+
					"'"+mName+"',"+
					"'"+mMobile+"',"+
					"'"+mEmail+"'"+
					")";
		System.out.println(st);
		boolean success= handler.execAction(st);
		if(success) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Saved");
			alert.showAndWait();
		}else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Error Occured");
			alert.showAndWait();
		}
	}

	@FXML
	void cancel(ActionEvent event) {
		Stage stage = (Stage)rootPane.getScene().getWindow();
    	stage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//initiate DatabaseHandler
		handler = new DatabaseHandler();
	
	}

}