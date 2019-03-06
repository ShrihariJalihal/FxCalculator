package com.shj.calculators.fxcalculator.exception;

public class FxCurrencyConversionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8614249623941841398L;
	private String message;

	public FxCurrencyConversionException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
