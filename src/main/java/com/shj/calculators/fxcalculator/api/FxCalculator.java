package com.shj.calculators.fxcalculator.api;

import com.shj.calculators.fxcalculator.common.FxCurrency;

public interface FxCalculator {
	Double calculateForexValue(FxCurrency fromCurrency, Double amountToConvert, FxCurrency toCurrency);
}
