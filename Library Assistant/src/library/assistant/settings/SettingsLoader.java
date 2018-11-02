package library.assistant.settings;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;

public class SettingsLoader extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			AnchorPane root =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("library/assistant/settings/settings.fxml"));
			Scene scene = new Scene(root);
		//	scene.getStylesheets().addAll(SettingsLoader.class.getResource("common.css").toExternalForm());
			primaryStage.setTitle("Settings");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					DatabaseHandler.getInstance();
				}
			}).start();
			
			//Preferences.initConfig();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
