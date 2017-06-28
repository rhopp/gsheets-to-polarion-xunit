package org.jboss.polarion.importer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

public class GoogleSheetsDataRetriever {

	/** Application name. */
	private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/sheets.googleapis.com-java-quickstart");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/sheets.googleapis.com-java-quickstart
	 */
	private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public static Credential authorize() throws IOException {
		// Load client secrets.
		InputStream in = PolarionImporter.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	/**
	 * Build and return an authorized Sheets API client service.
	 * 
	 * @return an authorized Sheets API client service
	 * @throws IOException
	 */
	public static Sheets getSheetsService() throws IOException {
		Credential credential = authorize();
		return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
				.build();
	}

	public static List<TestCaseVerdict> getVerdicts(String spreadsheetId, int idColumn, int verdictColumn,
			int commentColumn) throws IOException {
		// Build a new authorized API client service.
		Sheets service = getSheetsService();

		List<TestCaseVerdict> verdicts = new ArrayList<TestCaseVerdict>();

		// Prints the names and majors of students in a sample spreadsheet:
		// https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
		// String spreadsheetId = "1ChqrLRZR8FZYwXR6Ehj3u5WwKzigBxDz49v1sn5TNfE";
		String range = "Basic Report!A2:M";
		ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
		List<List<Object>> values = response.getValues();
		processData(verdicts, values, idColumn, verdictColumn, commentColumn);
		return verdicts;
	}

	private static void processData(List<TestCaseVerdict> verdicts, List<List<Object>> values, int idColumn,
			int verdictColumn, int commentColumn) {
		if (values == null || values.size() == 0) {
			System.out.println("No data found.");
		} else {
			for (List<Object> row : values) {
				if (isInBounds(row, idColumn)) {
					if (isInBounds(row, verdictColumn)) {
						TestCaseVerdict testCaseVerdict = new TestCaseVerdict((String) row.get(idColumn),
								TestCaseVerdictEnum.lookup((String) row.get(verdictColumn)));
						if (isInBounds(row, commentColumn)) {
							testCaseVerdict.setComment((String) row.get(commentColumn));
						} else {
							System.out.println(
									String.format("Unable to find comment for test case ID: %s", row.get(idColumn)));
						}
						verdicts.add(testCaseVerdict);
					} else {
						System.out.println(String.format("Unable to find verdict for test case ID %s. Skipping.",
								row.get(idColumn)));
					}
				} else {
					throw new RuntimeException(String.format("Column %s is out of bounds", idColumn));
				}
			}
		}
	}

	private static boolean isInBounds(List<Object> array, int position) {
		return (position >= 0) && (position < array.size());
	}
}
