package com.shj.calculators.fxcalculator.common;

import java.util.HashMap;
import java.util.Map;

public enum FxCurrency {
	AUD(2), CAD(2), CNY(2), CZK(2), DKK(2), EUR(2), GBP(2), JPY(0), NOK(2), NZD(2), KRW(2), FJD(2), USD(2);

	private FxCurrency(Integer decimalPlace) {
		this.decimalPlace = decimalPlace;
	}

	private final Integer decimalPlace;
	private final Map<String, Double> conversionMap = new HashMap<>();

	public Map<String, Double> getConversionMap() {
		return conversionMap;
	}

	public Integer getDecimalPlace() {
		return this.decimalPlace;
	}

}
