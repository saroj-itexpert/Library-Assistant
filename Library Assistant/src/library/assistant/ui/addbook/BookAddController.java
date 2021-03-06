package library.assistant.ui.addbook;

import javafx.event.ActionEvent;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.listbook.BookListController;

public class BookAddController implements Initializable {

	
	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private AnchorPane rootPane;
	
	@FXML
	private JFXTextField title;
	
	@FXML
	private JFXTextField id;
	
	@FXML
	private JFXTextField author;
	
	@FXML
	private JFXTextField publisher;
	
	@FXML
    private JFXButton saveButton;

    @FXML
    private JFXButton cancelButton;
    
    public Boolean isInEditMode = Boolean.FALSE;
    
    DatabaseHandler databaseHandler;

    @FXML
    void addBook(ActionEvent event) {
    	String bookID = id.getText();
    	String bookName = title.getText();
    	String bookAuthor = author.getText();
    	String bookPublisher = publisher.getText();

    	if(bookID.isEmpty() || bookAuthor.isEmpty()|| bookName.isEmpty()||bookPublisher.isEmpty()) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setHeaderText(null);
    		alert.setContentText("Please Enter in all fields");
    		alert.showAndWait();
    		return;
    	}
    	
    	if(isInEditMode) {
    		handleEditOption();
    		return;
    	}
    	 
    		String qu = "INSERT INTO BOOK VALUES ("+
    				"'"+bookID +"',"+
    				"'"+bookName +"',"+
    				"'"+bookAuthor +"',"+
    				"'"+bookPublisher +"',"+
    				""+ "true" +""+
    				")";
    		System.out.println(qu);
    		boolean success = databaseHandler.execAction(qu);
    			if(success) {
    				Alert alert = new Alert(Alert.AlertType.INFORMATION);
    	    		alert.setHeaderText(null);
    	    		alert.setContentText("SUCCESS");
    	    		alert.showAndWait();
    			}else {
    				Alert alert = new Alert(Alert.AlertType.ERROR);
    	    		alert.setHeaderText(null);
    	    		alert.setContentText("FAILED");
    	    		alert.showAndWait();
    			}
    	
    	
    	clearAddBookCache();
    
    }

    private void handleEditOption() {
    	BookListController.Book book = new BookListController.Book(title.getText(), id.getText(), author.getText(), publisher.getText(), true);
    	if(databaseHandler.updateBook(book)) {
    		AlertMaker.showSimpleAlert("Success", "Book Updated Successfully!");
    	}else {
    		AlertMaker.showErrorMessage("Error", "Sorry! Book wasn't updated!");
    	}
	}

	private void clearAddBookCache() {
    	id.setText("");
    	author.setText("");
    	title.setText("");
    	publisher.setText("");
	}

	@FXML
    void cancel(ActionEvent event) {
    	Stage stage = (Stage)rootPane.getScene().getWindow();
    	stage.close();
    }

	private void checkData() {
		String qu = "SELECT title FROM BOOK";
		ResultSet rs = databaseHandler.execQuery(qu);
		try{
			while(rs.next()) {
			String titlex = rs.getString("title");
			System.out.println(titlex);
				
			}
			
		}catch(SQLException ex) {
			Logger.getLogger(BookAddController.class.getName()).log(Level.SEVERE, null, ex);
			
		}
	}
	
	public void infalteUI(BookListController.Book book) {
		
		title.setText(book.getTitle());
		id.setText(book.getId());
		author.setText(book.getAuthor());
		publisher.setText(book.getPublisher());
		id.setEditable(false);
		isInEditMode= Boolean.TRUE;
		
	}
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		databaseHandler =  DatabaseHandler.getInstance();
		checkData();
	}
	
	
}
