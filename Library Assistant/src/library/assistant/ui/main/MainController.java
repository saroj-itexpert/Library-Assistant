package library.assistant.ui.main;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.effects.JFXDepthManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.database.DatabaseHandler;

public class MainController implements Initializable {
	
	@FXML
    private HBox book_info;

    @FXML
    private TextField bookIDInput;
   
    @FXML
    private TextField memberIDInput;

    @FXML
    private Text bookName;
    
    @FXML
    private Text memberName;

    @FXML
    private Text bookAuthor;

    @FXML
    private Text memberContact;
    
    @FXML
    private Text bookStatus;

    @FXML
    private HBox member_info;
    
    DatabaseHandler databaseHandler;

	@FXML
	void loadAddBook(ActionEvent event) {
		loadWindow("/library/assistant/ui/addbook/add_book.fxml", "Add Book");
	}

	@FXML
	void loadAddMember(ActionEvent event) {
		loadWindow("/library/assistant/ui/addmember/member_add.fxml", "Add Member");

	}

	@FXML
	void loadBookTable(ActionEvent event) {
		loadWindow("/library/assistant/ui/listbook/book_list.fxml", "Book List");

	}

	@FXML
	void loadMemberTable(ActionEvent event) {
		loadWindow("/library/assistant/ui/listmember/member_list.fxml", "Member List");

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
	
	 @FXML
	    void loadBookInfo(ActionEvent event) {
		 	clearBookCache();
		 	String id = bookIDInput.getText();
		 	String sql = "SELECT * FROM BOOK WHERE id = '"+id+"'";
		 	ResultSet rs = databaseHandler.execQuery(sql);
		 	Boolean flag = false;
		 	try {
				while(rs.next()) {
					String bName = rs.getString("title");
					String bAuthor = rs.getString("author");
					Boolean bStatus = rs.getBoolean("isAvail");
					
					bookName.setText(bName);
					bookAuthor.setText(bAuthor);
					String status = (bStatus)?"Available":"Not Available";
					bookStatus.setText(status);
					
					flag = true;
				}
				if(!flag) {
					bookName.setText("No Such Book Available!");
				}
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		 	
	 }
	 
	 void clearBookCache() {
		 bookName.setText("");
		 bookAuthor.setText("");
		 bookStatus.setText("");
	 }
	 
	 void clearMemberCache() {
		 memberName.setText("");
		 memberContact.setText("");
	 }
	 

	 @FXML
	    void loadMemberInfo(ActionEvent event) {
		 	clearMemberCache();
			String id = memberIDInput.getText();
		 	String sql = "SELECT * FROM MEMBER WHERE id = '"+id+"'";
		 	ResultSet rs = databaseHandler.execQuery(sql);
		 	Boolean flag = false;
		 	try {
				while(rs.next()) {
					String mName = rs.getString("name");
					String mMobile = rs.getString("mobile");
					
					memberName.setText(mName);
					memberContact.setText(mMobile);
					
					flag = true;
				}
				if(!flag) {
					memberName.setText("No Such Member Available!");
				}
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		 	
	 }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		JFXDepthManager.setDepth(book_info, 1);
		JFXDepthManager.setDepth(member_info, 1);
		databaseHandler = DatabaseHandler.getInstance();
	}

}
