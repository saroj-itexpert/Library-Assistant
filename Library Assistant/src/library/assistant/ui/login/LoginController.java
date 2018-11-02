package library.assistant.ui.login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.codec.digest.DigestUtils;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.alert.AlertMaker;
import library.assistant.settings.Preferences;
import library.assistant.ui.main.Main;

public class LoginController implements Initializable {

	Preferences preference;
	@FXML
	private Label titleText;
	@FXML
	private JFXTextField username;

	@FXML
	private JFXPasswordField password;

	@FXML
	void handleCancelButtonAction(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void handleLoginButtonAction(ActionEvent event) {
		titleText.setText("Library Assistant Login");
		titleText.setStyle("-fx-background-color:#4059a9;-fx-text-fill:white");
		
		String uname = username.getText();
		String pword = DigestUtils.shaHex(password.getText());
		
		if(uname.equals(preference.getUsername())&& pword.equals(preference.getPassword())) {
			//login
			closeStage();
			loadMain();
			
		}
		else {
			titleText.setText("Invalid Credentials!");
			titleText.setStyle("-fx-background-color:#d32f2f; -fx-text-fill:white");
		}
		
		
	}

	private void closeStage() {
		((Stage)username.getScene().getWindow()).close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		preference = Preferences.getPreferences();
	}
	
	void loadMain() {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/library/assistant/ui/main/main.fxml"));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Dashboard");
			stage.setScene(new Scene(parent));
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
