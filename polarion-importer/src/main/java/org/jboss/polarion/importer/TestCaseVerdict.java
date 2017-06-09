package org.jboss.polarion.importer;

public class TestCaseVerdict {
	
	private String id;
	private TestCaseVerdictEnum verdict;
	private String comment;
	
	
	public TestCaseVerdict(String id, TestCaseVerdictEnum verdict) {
		this.id = id;
		this.verdict = verdict;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getId() {
		return id;
	}
	public String getComment() {
		if (comment == null) {
			return "";
		}else {
			return comment;
		}
	}
	
	public TestCaseVerdictEnum getVerdict() {
		return verdict;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if (comment == null) {
			return id + ", " + verdict;
		}else {
			return id + ", " + verdict + ", " + comment;
		}
		
	}
}
