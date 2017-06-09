package org.jboss.polarion.importer;

import java.io.IOException;
import java.util.List;

public class PolarionImporter {
	
	private static final String PROJECT_ID = "JBDS";
	private static final String TEST_RUN_ID = "JBDS_11_0_AM1_SPRINT_132";
	
	private static String SHEET_ID;
	private static final String SHEET_ID_KEY = "sheet.id";
	
    

    public static void main(String[] args) throws IOException {
    	populateParameters();
    	
        List<TestCaseVerdict> verdicts = GoogleSheetsDataRetriever.getVerdicts(SHEET_ID);
        
        System.out.println(XUnitXMLGenerator.generateXML(verdicts, PROJECT_ID, TEST_RUN_ID));
        
    }

    private static void populateParameters() {
		SHEET_ID = System.getProperty(SHEET_ID_KEY);
		if (SHEET_ID == null) {
			throw new RuntimeException(String.format("System property %s must be set", SHEET_ID_KEY));
		}
	}

}