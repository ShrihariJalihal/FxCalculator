package com.shj.calculators.fxcalculator.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FxConversionRates {
	public static Map<String, Double> inputValuesOfCurrencyConversion() {
		Map<String, Double> conversionValues = new ConcurrentHashMap<>();
		conversionValues.put("AUDUSD", Double.valueOf(0.8371d));
		conversionValues.put("CADUSD", Double.valueOf(0.8711d));
		conversionValues.put("USDCNY", Double.valueOf(6.1715d));
		conversionValues.put("EURUSD", Double.valueOf(1.2315d));
		conversionValues.put("GBPUSD", Double.valueOf(1.5683d));
		conversionValues.put("NZDUSD", Double.valueOf(0.7750d));
		conversionValues.put("USDJPY", Double.valueOf(119.95d));
		conversionValues.put("EURCZK", Double.valueOf(27.6028d));
		conversionValues.put("EURDKK", Double.valueOf(7.4405d));
		conversionValues.put("EURNOK", Double.valueOf(8.6651d));
		return conversionValues;
	}

}
