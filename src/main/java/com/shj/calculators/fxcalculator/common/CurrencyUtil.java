package com.shj.calculators.fxcalculator.common;

public class CurrencyUtil {

	public static String formatCurrency(Double amountToFormat, Integer roundingDigit) {
		return String.format("%." + roundingDigit + "f", amountToFormat);
	}

}
