package library.assistant.ui.addmember;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MemberAddLoader extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			AnchorPane root =(AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("library/assistant/ui/addmember/member_add.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("Add Member");
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
