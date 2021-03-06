package library.assistant.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import library.assistant.ui.listbook.BookListController.Book;
import library.assistant.ui.listmember.MemberListController.Member;

public class DatabaseHandler {
	private static DatabaseHandler handler;
	//Fields for database connection
	private static final String DB_URL = "jdbc:derby:database/library2;create=true";
	private static Connection conn = null;
	private static Statement stmt = null;
	//Constructor for starting program to create Connection
	private DatabaseHandler() {
		createConnection();
		setupBookTable();
		setupMemberTable();
		setupIssueTable();
	}
	
	public static DatabaseHandler getInstance() {
		
		if(handler == null) {
			handler = new DatabaseHandler();
		}
		return handler;
	}
	
	private void setupMemberTable() {
		String TABLE_NAME = "MEMBER";
		String sql = "CREATE TABLE "+ TABLE_NAME+ "("
				+" id varchar(200) primary key,\n"
				+" name varchar(200),\n"
				+" mobile varchar(20),\n"
				+" email varchar(100)"
				+" )";
				
		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
			if(tables.next()) {
				System.out.println("Table "+ TABLE_NAME + " already exists.You're good to go!");
			}else {
				stmt.execute(sql);
				
			}
		}catch (SQLException e) {
			System.err.println(e.getMessage()+"---setupDatabase");
		}finally {
			
		}
		
	}
	//code to create derby database connection
	void createConnection() {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			conn = DriverManager.getConnection(DB_URL);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//create a table of name BOOK
	void setupBookTable() {
		String TABLE_NAME = "BOOK";
		String sql = "CREATE TABLE "+ TABLE_NAME+ "("
				+" id varchar(200) primary key,\n"
				+" title varchar(200),\n"
				+" author varchar(200),\n"
				+" publisher varchar(100),\n"
				+" isAvail boolean default true"
				+" )";
				
		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
			if(tables.next()) {
				System.out.println("Table "+ TABLE_NAME + " already exists. You're good to go!");
			}else {
				stmt.execute(sql);
				
			}
		}catch (SQLException e) {
			System.err.println(e.getMessage()+"---setupDatabase");
		}finally {
			
		}
	}
	
	private void setupIssueTable() {
		String TABLE_NAME = "ISSUE";
		String sql = "CREATE TABLE "+ TABLE_NAME+ "("
				+" bookID varchar(200) primary key,\n"
				+" memberID varchar(200),\n"
				+" issueTime timestamp default CURRENT_TIMESTAMP,\n"
				+" renew_count integer default 0,\n"
				+" FOREIGN KEY (bookID) REFERENCES BOOK(id),\n"
				+" FOREIGN KEY (memberID) REFERENCES MEMBER(id)"
				+" )";
				
		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
			if(tables.next()) {
				System.out.println("Table "+ TABLE_NAME + " already exists.You're good to go!");
			}else {
				stmt.execute(sql);
				
			}
		}catch (SQLException e) {
			System.err.println(e.getMessage()+"---setupDatabase");
		}finally {
			
		}
		
	}
	
	
	public ResultSet execQuery(String query) {
		ResultSet result;
		try {
			stmt = conn.createStatement();
			result= stmt.executeQuery(query);
			
		}catch(SQLException ex) {
			System.out.println("Exception at execQuery:dataHandler "+ ex.getLocalizedMessage());
			return null;
		}
		finally {
			
		}
		return result;
	}
	
	public boolean execAction(String qu) {
		try {
			stmt = conn.createStatement();
			stmt.execute(qu);
			return true;
			
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error:" +ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
			System.out.println("Exception at execQuery:dataHandeler"+ex.getLocalizedMessage());
			return false;
		}finally {
			
		}
	}
	
	public boolean deleteBook(Book book) {
		String deleteStatement = "DELETE FROM BOOK WHERE ID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(deleteStatement);
			pstmt.setString(1, book.getId());
			int result = pstmt.executeUpdate();
			System.out.println(result);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deleteMember(Member member) {
		String deleteStatement = "DELETE FROM MEMBER WHERE ID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(deleteStatement);
			pstmt.setString(1, member.getId());
			int result = pstmt.executeUpdate();
			System.out.println(result);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean isBookAlreadyIssued(Book book) {
		String checkstmt = "SELECT COUNT(*) FROM ISSUE WHERE bookid = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(checkstmt);
			pstmt.setString(1, book.getId());
			ResultSet rs = stmt.executeQuery(checkstmt);
			if(rs.next()) {
				int count = rs.getInt(1);
				System.out.println(count);
				if(count>0) {
					return true;
				}else {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateBook(Book book) {
    	String update = "UPDATE BOOK SET TITLE =?, AUTHOR =?, PUBLISHER=? WHERE ID = ?";
    	PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(update);
	    	
	    	pstmt.setString(1, book.getTitle());
	    	pstmt.setString(2, book.getAuthor());
	    	pstmt.setString(3, book.getPublisher());
	    	pstmt.setString(4, book.getId());
	    	
	    	int result = pstmt.executeUpdate();
	    	return (result>0);
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false; 	
	}
	
	public boolean updateMember(Member member) {
    	String update = "UPDATE MEMBER SET NAME =?, MOBILE =?, EMAIL=? WHERE ID = ?";
    	PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(update);
	    	
	    	pstmt.setString(1, member.getName());
	    	pstmt.setString(2, member.getMobile());
	    	pstmt.setString(3, member.getEmail());
	    	pstmt.setString(4, member.getId());
	    	
	    	int result = pstmt.executeUpdate();
	    	return (result>0);
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false; 	
	}
}
