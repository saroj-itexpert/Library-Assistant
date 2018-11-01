package library.assistant.ui.main;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController implements Initializable {
	
	@FXML
	void loadAddBook(ActionEvent event) {
		loadWindow("/library/assistant/ui/addmember/member_add.fxml", "Add Member");
	}

	@FXML
	void loadAddMember(ActionEvent event) {

	}

	@FXML
	void loadBookTable(ActionEvent event) {

	}

	@FXML
	void loadMemberTable(ActionEvent event) {

	}
	
	void loadWindow(String location, String title) {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource(location));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle(title);
			stage.setScene(new Scene(parent));
			stage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

}
