package library.assistant.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DatabaseHandler {
	private static DatabaseHandler handler;
	//Fields for database connection
	private static final String DB_URL = "jdbc:derby:database/library2;create=true";
	private static Connection conn = null;
	private static Statement stmt = null;
	//Constructor for starting program to create Connection
	public DatabaseHandler() {
		createConnection();
		setupBookTable();
		setupMemberTable();
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
				System.out.println("Table "+ TABLE_NAME + " already exists");
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
				System.out.println("Table "+ TABLE_NAME + " already exists");
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

}
