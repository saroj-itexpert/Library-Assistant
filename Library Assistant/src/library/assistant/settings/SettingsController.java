package library.assistant.settings;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SettingsController implements Initializable{


    @FXML
    private AnchorPane settingsPane;
    @FXML
    private JFXTextField nDaysWithoutFine;

    @FXML
    private JFXTextField finePerDay;

    @FXML
    private JFXTextField username;
    
    @FXML
    private JFXPasswordField password;

   

    @FXML
    void handleCancelButtonAction(ActionEvent event) {
    	Stage stage = (Stage)settingsPane.getScene().getWindow();
    	stage.close();
    
    }

    @FXML
    void handleSaveButtonAction(ActionEvent event) {
    	int ndays = (Integer.parseInt(nDaysWithoutFine.getText()));
    	float fine = Float.parseFloat(finePerDay.getText());
    	String uname = username.getText();
    	String pass = password.getText();
    	
    	
    	Preferences preferences = Preferences.getPreferences();
    	preferences.setnDaysWithoutFine(ndays);
    	preferences.setFinePerDay(fine);
    	preferences.setUsername(uname);
    	preferences.setPassword(pass);
    	
    	Preferences.writePreferenceToFIle(preferences);
    	
    }

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initDefaultValues();
	}

	private void initDefaultValues() {
		
		Preferences preferences = Preferences.getPreferences();
		nDaysWithoutFine.setText(String.valueOf(preferences.getnDaysWithoutFine()));
		finePerDay.setText(String.valueOf(preferences.getFinePerDay()));
		username.setText(String.valueOf(preferences.getUsername()));
		password.setText(String.valueOf(preferences.getPassword()));

	
	}
	
	

}
