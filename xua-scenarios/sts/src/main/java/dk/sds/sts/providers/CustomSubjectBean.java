package dk.sds.sts.providers;

import org.apache.wss4j.common.saml.bean.SubjectBean;

public class CustomSubjectBean extends SubjectBean {
	private String subjectConfirmationNameIDFormat;
	private String subjectConfirmationNameIDValue;

	public CustomSubjectBean(String subjectName, String string, String confirmationMethod) {
		super(subjectName, string, confirmationMethod);
	}

	public String getSubjectConfirmationNameIDValue() {
		return subjectConfirmationNameIDValue;
	}
	
	public void setSubjectConfirmationNameIDValue(String subjectConfirmationNameIDValue) {
		this.subjectConfirmationNameIDValue = subjectConfirmationNameIDValue;
	}
	
	public String getSubjectConfirmationNameIDFormat() {
		return subjectConfirmationNameIDFormat;
	}
	
	public void setSubjectConfirmationNameIDFormat(String subjectConfirmationNameIDFormat) {
		this.subjectConfirmationNameIDFormat = subjectConfirmationNameIDFormat;
	}
}
