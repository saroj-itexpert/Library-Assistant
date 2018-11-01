package library.assistant.ui.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			AnchorPane root =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("library/assistant/ui/main/main.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("Dashboard");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
