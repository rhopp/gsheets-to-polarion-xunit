package org.jboss.polarion.importer;

import java.util.List;

public class XUnitXMLGenerator {

	public static String generateXML(List<TestCaseVerdict> verdicts, String projectId, String testRunId) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(getHeader(verdicts.size(), projectId, testRunId));
    	sb.append(getBody(verdicts));    	
    	sb.append(getFooter());
    	return sb.toString();
    	
    }
    
	private static String getBody(List<TestCaseVerdict> verdicts) {
		StringBuilder sb = new StringBuilder();
		for (TestCaseVerdict verdict : verdicts) {
			switch(verdict.getVerdict()) {
			case PASSED:
				sb.append(getPassedVerdict(verdict));
				break;
			case FAILED:
				sb.append(getFailedVerdict(verdict));
				break;
			case BLOCKED:
				sb.append(getBlockedVerdict(verdict));
				break;
			}
		}
		return sb.toString();
	}

	private static String getBlockedVerdict(TestCaseVerdict verdict) {
		String testCaseString = "\t<testcase name=\""+verdict.getId()+"\">\n";
		testCaseString+="\t\t<error message=\""+verdict.getComment()+"\" type=\"error\"/>\n";
		testCaseString+="\t\t<properties>\n"
				+ "\t\t\t<property name=\"polarion-testcase-id\" value=\""+verdict.getId()+"\" />\n"
				+ "\t\t</properties>\n";
		testCaseString+="\t</testcase>\n";
		return testCaseString;
	}

	private static String getFailedVerdict(TestCaseVerdict verdict) {
		String testCaseString = "\t<testcase name=\""+verdict.getId()+"\">\n";
		testCaseString+="\t\t<failure message=\""+verdict.getComment()+"\" type=\"failure\"/>\n";
		testCaseString+="\t\t<properties>\n"
				+ "\t\t\t<property name=\"polarion-testcase-id\" value=\""+verdict.getId()+"\" />\n"
				+ "\t\t</properties>\n";
		testCaseString+="\t</testcase>\n";
		return testCaseString;
	}

	private static String getPassedVerdict(TestCaseVerdict verdict) {
		String testCaseString = "\t<testcase name=\""+verdict.getId()+"\">\n";
		testCaseString+="\t\t<properties>\n"
				+ "\t\t\t<property name=\"polarion-testcase-id\" value=\""+verdict.getId()+"\" />\n"
				+ "\t\t</properties>\n";
		testCaseString+="\t</testcase>\n";
		return testCaseString;
	}

	private static String getHeader(int verdictsCount, String projectId, String testRunId) {
		return "<testsuites>\n"+
				"<properties>\n"+
				"\t<property name=\"polarion-project-id\" value=\""+projectId+"\" />\n"+
				"\t<property name=\"polarion-testrun-id\" value=\""+testRunId+"\" />\n"+
				"\t<property name=\"polarion-testrun-status-id\" value=\"inprogress\" />\n"+
				"</properties>\n"+
				"<testsuite name=\"test\" tests=\""+verdictsCount+"\">\n";
	}
	
	private static String getFooter() {
		return "</testsuite>\n</testsuites>";
	}
	
}
