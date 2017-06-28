package org.jboss.polarion.importer;

import java.io.IOException;
import java.util.List;

public class PolarionImporter {

	private static String PROJECT_ID = "JBDS";

	private static String TEST_RUN_ID;
	private static final String TEST_RUN_ID_KEY = "testrun.id";

	private static String SHEET_ID;
	private static final String SHEET_ID_KEY = "sheet.id";

	private static Character ID_COLUMN_CHAR;
	private static final String ID_COLUMN_CHAR_KEY = "id.column.char";

	private static Character VERDICT_COLUMN_CHAR;
	private static final String VERDICT_COLUMN_CHAR_KEY = "verdict.column.char";

	private static Character COMMENT_COLUMN_CHAR;
	private static final String COMMENT_COLUMN_CHAR_KEY = "comment.column.char";

	public static void main(String[] args) throws IOException {
		populateParameters();

		List<TestCaseVerdict> verdicts = GoogleSheetsDataRetriever.getVerdicts(SHEET_ID,
				charToIntAlphabetically(ID_COLUMN_CHAR), charToIntAlphabetically(VERDICT_COLUMN_CHAR),
				charToIntAlphabetically(COMMENT_COLUMN_CHAR));

		System.out.println(XUnitXMLGenerator.generateXML(verdicts, PROJECT_ID, TEST_RUN_ID));

	}

	private static void populateParameters() {
		SHEET_ID = System.getProperty(SHEET_ID_KEY);
		if (SHEET_ID == null) {
			throw new RuntimeException(String.format("System property %s must be set", SHEET_ID_KEY));
		}
		TEST_RUN_ID = System.getProperty(TEST_RUN_ID_KEY);
		if (TEST_RUN_ID == null) {
			throw new RuntimeException(String.format("System property %s must be set", TEST_RUN_ID_KEY));
		}
		ID_COLUMN_CHAR = parameterShouldBeChar(System.getProperty(ID_COLUMN_CHAR_KEY));
		if (ID_COLUMN_CHAR == null) {
			throw new RuntimeException(String.format("System property %s must be set", ID_COLUMN_CHAR_KEY));
		}
		VERDICT_COLUMN_CHAR = parameterShouldBeChar(System.getProperty(VERDICT_COLUMN_CHAR_KEY));
		if (VERDICT_COLUMN_CHAR == null) {
			throw new RuntimeException(String.format("System property %s must be set", VERDICT_COLUMN_CHAR_KEY));
		}
		COMMENT_COLUMN_CHAR = parameterShouldBeChar(System.getProperty(COMMENT_COLUMN_CHAR_KEY));
		if (COMMENT_COLUMN_CHAR == null) {
			throw new RuntimeException(String.format("System property %s must be set", ID_COLUMN_CHAR_KEY));
		}
	}

	private static Character parameterShouldBeChar(String value) {
		if (value.length() != 1) {
			return null;
		}
		Character c = value.charAt(0);
		if (!Character.isLetter(c)) {
			return null;
		}
		return c;
	}

	private static int charToIntAlphabetically(Character c) {
		c = Character.toLowerCase(c);
		return c.charValue() - 97; // a=0
	}

}