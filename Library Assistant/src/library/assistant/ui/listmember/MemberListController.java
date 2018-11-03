package library.assistant.ui.listmember;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.addbook.BookAddController;
import library.assistant.ui.addmember.MemberAddController;
import library.assistant.ui.listbook.BookListController;

public class MemberListController implements Initializable {

	ObservableList<Member> list = FXCollections.observableArrayList();
	
	@FXML
	private TableView<Member> tableView;

	@FXML
	private TableColumn<Member, String> nameCol;

	@FXML
	private TableColumn<Member, String> idCol;

	@FXML
	private TableColumn<Member, String> mobileCol;

	@FXML
	private TableColumn<Member, String> emailCol;
	
	@FXML
    void handleDeleteMember(ActionEvent event) {

    }

    @FXML
    void handleEditMember(ActionEvent event) {
    	Member selectedforEdit = tableView.getSelectionModel().getSelectedItem();
    	if(selectedforEdit == null ) {
    		 AlertMaker.showErrorMessage("No Member selected", "Please select a member to edit.");
	         return;
    	}
    	
        try {
        	
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/assistant/ui/addmember/member_add.fxml"));
			Parent parent = loader.load();
			
			MemberAddController controller = (MemberAddController)loader.getController();
			controller.infalteUI(selectedforEdit);
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Edit Member");
			stage.setScene(new Scene(parent));
			stage.show();
			
			stage.setOnCloseRequest((e)->{
				handleRefresh(new ActionEvent());
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
        
    }

    @FXML
    void handleRefresh(ActionEvent event) {
		loadData();
		
    }
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initCol();
		loadData();
	}

	
	private void loadData() {
		list.clear();
		DatabaseHandler handler = DatabaseHandler.getInstance();
		String qu = "SELECT * FROM MEMBER";
		ResultSet rs = handler.execQuery(qu);
		try {
			while(rs.next()) {
				String name = rs.getString("name");
				String id = rs.getString("id");
				String mobile = rs.getString("mobile");
				String email = rs.getString("email");
				//add datas to the list
			
				list.add(new Member(name, id, mobile, email));
				
			}
			
		}catch(SQLException ex) {
			Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, ex);	
		}
		//associate list with tableview
		tableView.setItems(list);

	}

	private void initCol() {
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		
	}

	public static class Member {
		private final SimpleStringProperty name;
		private final SimpleStringProperty id;
		private final SimpleStringProperty mobile;
		private final SimpleStringProperty email;

		public Member(String name, String id, String mobile, String email) {
			super();
			this.name = new SimpleStringProperty(name);
			this.id = new SimpleStringProperty(id);
			this.mobile = new SimpleStringProperty(mobile);
			this.email = new SimpleStringProperty(email);
		}

		public String getName() {
			return name.get();
		}

		public String getId() {
			return id.get();
		}

		public String getMobile() {
			return mobile.get();
		}

		public String getEmail() {
			return email.get();
		}

	}

}
