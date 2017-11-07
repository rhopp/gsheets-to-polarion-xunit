package org.jboss.polarion.importer;

import org.apache.commons.lang3.StringEscapeUtils;

public class TestCaseVerdict {
	
	private String id;
	private TestCaseVerdictEnum verdict;
	private String escapedComment;
	
	
	public TestCaseVerdict(String id, TestCaseVerdictEnum verdict) {
		this.id = id;
		this.verdict = verdict;
	}
	
	public void setComment(String comment) {
		this.escapedComment = StringEscapeUtils.escapeXml(comment);
	}
	
	public String getId() {
		return id;
	}
	public String getComment() {
		if (escapedComment == null) {
			return "";
		}else {
			return escapedComment;
		}
	}
	
	public TestCaseVerdictEnum getVerdict() {
		return verdict;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if (escapedComment == null) {
			return id + ", " + verdict;
		}else {
			return id + ", " + verdict + ", " + escapedComment;
		}
		
	}
}
