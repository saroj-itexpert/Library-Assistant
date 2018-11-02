package library.assistant.ui.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import java.util.ResourceBundle;

//import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.effects.JFXDepthManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.database.DatabaseHandler;

public class MainController implements Initializable {

	@FXML
	private ListView<String> issueDataList;

	@FXML
	private HBox book_info;

	@FXML
	private TextField bookIDInput;

	@FXML
	private TextField bookID;

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

	Boolean isReadyForSubmission = false;

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
		String sql = "SELECT * FROM BOOK WHERE id = '" + id + "'";
		ResultSet rs = databaseHandler.execQuery(sql);
		Boolean flag = false;
		try {
			while (rs.next()) {
				String bName = rs.getString("title");
				String bAuthor = rs.getString("author");
				Boolean bStatus = rs.getBoolean("isAvail");

				bookName.setText(bName);
				bookAuthor.setText(bAuthor);
				String status = (bStatus) ? "Available" : "Not Available";
				bookStatus.setText(status);

				flag = true;
			}
			if (!flag) {
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
		String sql = "SELECT * FROM MEMBER WHERE id = '" + id + "'";
		ResultSet rs = databaseHandler.execQuery(sql);
		Boolean flag = false;
		try {
			while (rs.next()) {
				String mName = rs.getString("name");
				String mMobile = rs.getString("mobile");

				memberName.setText(mName);
				memberContact.setText(mMobile);

				flag = true;
			}
			if (!flag) {
				memberName.setText("No Such Member Available!");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void loadIssueOperation(ActionEvent event) {
		String memberID = memberIDInput.getText();
		String bookID = bookIDInput.getText();

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirm Issue Operation");
		alert.setHeaderText(null);
		alert.setContentText(
				"Are you sure want to issue the book " + bookName.getText() + "\n to " + memberName.getText() + " ?");

		Optional<ButtonType> response = alert.showAndWait();
		if (response.get() == ButtonType.OK) {
			String str = "INSERT INTO ISSUE(memberID,bookID) VALUES (" + "'" + memberID + "'," + "'" + bookID + "')";
			String str2 = "UPDATE BOOK SET isAvail = false WHERE id='" + bookID + "'";

			if (databaseHandler.execAction(str) && databaseHandler.execAction(str2)) {
				Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText(null);
				alert.setContentText("Book Issue Complete!");
				alert.showAndWait();

			} else {
				Alert alert1 = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Failed");
				alert.setHeaderText(null);
				alert.setContentText("Issue Operation Failed!");
				alert.showAndWait();

			}

		} else {
			Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Cancelled");
			alert.setHeaderText(null);
			alert.setContentText("Issue Operation Cancelled!");
			alert.showAndWait();
		}

	}

	@FXML
	private void loadBookInfo2(ActionEvent event) {

		ObservableList<String> issueData = FXCollections.observableArrayList();
		isReadyForSubmission = false;

		String id = bookID.getText();
		String qu = "SELECT * FROM ISSUE WHERE bookID='" + id + "'";
		ResultSet rs = databaseHandler.execQuery(qu);
		try {
			while (rs.next()) {
				String mBookId = id;
				String mMemberId = rs.getString("memberID");
				Timestamp mIssueTime = rs.getTimestamp("issueTime");
				int mRenewCount = rs.getInt("renew_count");

				issueData.add("Issue Date and Time: " + mIssueTime.toGMTString());
				issueData.add("Renew Count: " + mRenewCount);

				issueData.add("Book Information: ");
				qu = "SELECT * FROM BOOK WHERE ID = '" + mBookId + "'";
				ResultSet r1 = databaseHandler.execQuery(qu);
				while (r1.next()) {
					issueData.add("\tBook Name: " + r1.getString("title"));
					issueData.add("\tBook ID: " + r1.getString("id"));
					issueData.add("\tBook Author: " + r1.getString("author"));
					issueData.add("\tBook Publisher: " + r1.getString("publisher"));
				}
				qu = "SELECT * FROM MEMBER WHERE ID = '" + mMemberId + "'";
				r1 = databaseHandler.execQuery(qu);
				issueData.add("Member Information:");
				while (r1.next()) {
					issueData.add("\tName: " + r1.getString("name"));
					issueData.add("\tMobile: " + r1.getString("mobile"));
					issueData.add("\tEmail: " + r1.getString("email"));

				}
				isReadyForSubmission = true;

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		issueDataList.getItems().setAll(issueData);

	}

	@FXML
	void loadSubmissionOp(ActionEvent event) {
		if (!isReadyForSubmission) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Failed");
			alert.setHeaderText(null);
			alert.setContentText("Please select a book to Submit");
			alert.showAndWait();
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirm Issue Operation");
		alert.setHeaderText(null);
		alert.setContentText(
				"Are you sure want to return the book ?");

		Optional<ButtonType> response = alert.showAndWait();
		if (response.get() == ButtonType.OK) {
			String id = bookID.getText();
			String ac1 = "DELETE FROM ISSUE WHERE BOOKID = '" + id + "'";
			String ac2 = "UPDATE BOOK SET ISAVAIL = TRUE WHERE ID = '" + id + "'";

			if (databaseHandler.execAction(ac1) && databaseHandler.execAction(ac2)) {
				Alert alert1 = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText(null);
				alert.setContentText("Book has been submited");
				alert.showAndWait();
			} else {
				Alert alert1 = new Alert(AlertType.ERROR);
				alert.setTitle("Failed");
				alert.setHeaderText(null);
				alert.setContentText("Book Submission Failed");
				alert.showAndWait();
			}
		}else {
			Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Cancelled");
			alert.setHeaderText(null);
			alert.setContentText("Submission Operation Cancelled!");
			alert.showAndWait();
		}
	}
	
    @FXML
    void loadRenewOp(ActionEvent event) {
    	
    	if (!isReadyForSubmission) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Failed");
			alert.setHeaderText(null);
			alert.setContentText("Please select a book to Renew");
			alert.showAndWait();
			return;
		}
    	
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirm Renew Operation");
		alert.setHeaderText(null);
		alert.setContentText(
				"Are you sure want to renew the book ?");

		Optional<ButtonType> response = alert.showAndWait();
		if (response.get() == ButtonType.OK) {
			String ac = "UPDATE ISSUE SET issueTime = CURRENT_TIMESTAMP, renew_count = renew_count + 1 WHERE BOOKID = '"+bookID.getText()+"'";
			System.out.println(ac);
			if(databaseHandler.execAction(ac)) {
				Alert alert1 = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText(null);
				alert.setContentText("Book has been Renewed");
				alert.showAndWait();
			}else {
				Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Cancelled");
				alert.setHeaderText(null);
				alert.setContentText("Renew Operation Cancelled!");
				alert.showAndWait();
			}
		}else {
			Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Cancelled");
			alert.setHeaderText(null);
			alert.setContentText("Renew Operation Cancelled!");
			alert.showAndWait();
		}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		JFXDepthManager.setDepth(book_info, 1);
		JFXDepthManager.setDepth(member_info, 1);
		databaseHandler = DatabaseHandler.getInstance();
	}

}
