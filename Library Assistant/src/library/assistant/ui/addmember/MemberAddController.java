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
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.listbook.BookListController;
import library.assistant.ui.listmember.MemberListController;
import library.assistant.ui.listmember.MemberListController.Member;

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
	
    public Boolean isInEditMode = Boolean.FALSE;
    DatabaseHandler databaseHandler;


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
		
		if(isInEditMode) {
			handleEditMember();
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
		
		clearAddMemberCache();
	}
	@FXML
	private void handleEditMember() {
		Member member = new MemberListController.Member( name.getText(), id.getText(), mobile.getText(), email.getText());
    	if(databaseHandler.updateMember(member)) {
    		AlertMaker.showSimpleAlert("Success", "Member Updated Successfully!");
    	}else {
    		AlertMaker.showErrorMessage("Error", "Sorry! Member wasn't updated!");
    	}
		
	}

	private void clearAddMemberCache() {
		id.setText("");
		name.setText("");
		mobile.setText("");
		email.setText("");
		
	}

	@FXML
	void cancel(ActionEvent event) {
		Stage stage = (Stage)rootPane.getScene().getWindow();
    	stage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//initiate DatabaseHandler
		handler = DatabaseHandler.getInstance();
	
	}

	public void infalteUI(MemberListController.Member member) {
		name.setText(member.getName());
		id.setText(member.getId());
		mobile.setText(member.getMobile());
		email.setText(member.getEmail());
		id.setEditable(false);
		isInEditMode = Boolean.TRUE;
		
		
	}

}
