package net.objectof.model.corc.validation;

public class ValidationResult {

	private boolean valid;
	private String message;
	private int code;
	
	
	public ValidationResult(boolean valid, String message, int code) {
		this.valid = valid;
		this.message = message;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public String getMessage() {
		return message;
	}
	
	public int getCode() {
		return code;
	}
	
	
	
}
