package library.assistant.settings;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import library.assistant.alert.AlertMaker;

public class Preferences {

	public static final String CONFIG_FILE = "config.txt";

	int nDaysWithoutFine;
	float finePerDay;
	String username;
	String password;

	public Preferences() {
		nDaysWithoutFine = 14;
		finePerDay = 2;
		username = "admin";
		setPassword("admin");

	}

	public int getnDaysWithoutFine() {
		return nDaysWithoutFine;
	}

	public void setnDaysWithoutFine(int nDaysWithoutFine) {
		this.nDaysWithoutFine = nDaysWithoutFine;
	}

	public float getFinePerDay() {
		return finePerDay;
	}

	public void setFinePerDay(float finePerDay) {
		this.finePerDay = finePerDay;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (password.length() < 16) {

			this.password = DigestUtils.shaHex(password);

		} else {
			this.password = password;
		}
	}

	public static void initConfig() {
		Writer writer = null;
		try {
			Preferences preference = new Preferences();
			Gson gson = new Gson();
			writer = new FileWriter(CONFIG_FILE);
			gson.toJson(preference, writer);
		} catch (IOException ex) {
			System.out.println(ex);
		} finally {
			try {
				writer.close();
			} catch (IOException ex) {
				System.err.print(ex);
			}
		}

	}

	public static Preferences getPreferences() {
		Gson gson = new Gson();
		// initialise with default preference
		Preferences preferences = new Preferences();
		try {
			preferences = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// if not found any, then create one
			initConfig();
			e.printStackTrace();
		}
		return preferences;
	}

	public static void writePreferenceToFIle(Preferences preference) {
		Writer writer = null;
		try {
			Gson gson = new Gson();
			writer = new FileWriter(CONFIG_FILE);
			gson.toJson(preference, writer);

			AlertMaker.showSimpleAlert("Success", "Settings Updated!");

		} catch (IOException ex) {
			System.out.println(ex);
			AlertMaker.showErrorMessage("Failed", "Can't update settings");

		} finally {
			try {
				writer.close();
			} catch (IOException ex) {
				System.err.print(ex);
			}
		}
	}

}
